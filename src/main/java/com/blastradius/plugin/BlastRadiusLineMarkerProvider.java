package com.blastradius.plugin;

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import com.intellij.util.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Provides gutter icons for Java classes showing their blast radius impact.
 * <p>
 * The blast radius measures how many downstream components are affected when
 * this class is modified. It considers:
 * <ul>
 *     <li>Direct dependents — classes that directly reference this class</li>
 *     <li>Transitive dependents — classes reached through dependency chains (up to 3 levels)</li>
 *     <li>Recent change coupling — files that frequently change together in Git</li>
 * </ul>
 */
public class BlastRadiusLineMarkerProvider implements LineMarkerProvider {

    @Nullable
    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        if (!(element instanceof PsiIdentifier) || !(element.getParent() instanceof PsiClass)) {
            return null;
        }

        PsiClass psiClass = (PsiClass) element.getParent();
        BlastRadiusResult result = BlastRadiusCalculator.analyze(psiClass);

        if (result == null) {
            return null;
        }

        String tooltip = result.getFormattedTooltip();

        return new LineMarkerInfo<>(
                element,
                element.getTextRange(),
                BlastRadiusIconProvider.getIcon(result.getTotalImpact()),
                (Function<PsiElement, String>) e -> tooltip,
                (GutterIconNavigationHandler<PsiElement>) (mouseEvent, elt) ->
                        BlastRadiusPopupRenderer.showPopup(mouseEvent, result),
                GutterIconRenderer.Alignment.LEFT
        );
    }
}
