package com.blastradius.plugin.actions;

import com.blastradius.plugin.BlastRadiusCalculator;
import com.blastradius.plugin.BlastRadiusPopupRenderer;
import com.blastradius.plugin.BlastRadiusResult;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Action accessible from the editor right-click context menu.
 * Analyzes the blast radius of the class at the current cursor position.
 */
public class AnalyzeBlastRadiusAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = event.getData(CommonDataKeys.PSI_FILE);

        if (editor == null || psiFile == null) {
            return;
        }

        int offset = editor.getCaretModel().getOffset();
        PsiElement elementAtCaret = psiFile.findElementAt(offset);

        if (elementAtCaret == null) {
            Messages.showInfoMessage("No element found at cursor position.", "Blast Radius");
            return;
        }

        // Find the enclosing class
        PsiClass psiClass = PsiTreeUtil.getParentOfType(elementAtCaret, PsiClass.class);

        if (psiClass == null) {
            Messages.showInfoMessage("No Java class found at cursor position.", "Blast Radius");
            return;
        }

        BlastRadiusResult result = BlastRadiusCalculator.analyze(psiClass);

        if (result == null) {
            Messages.showInfoMessage("Could not analyze blast radius for this class.", "Blast Radius");
            return;
        }

        // Show result in a message dialog for the action (more persistent than balloon)
        Messages.showInfoMessage(
                String.format(
                        "Blast Radius Analysis: %s\n\n" +
                        "Severity: %s\n" +
                        "Direct Dependents: %d classes\n" +
                        "Transitive Dependents: %d classes\n" +
                        "Change-Coupled Files: %d files\n\n" +
                        "Total Impact: %d files affected",
                        result.getClassName(),
                        result.getSeverity(),
                        result.getDirectDependents(),
                        result.getTransitiveDependents(),
                        result.getChangeCoupledFiles(),
                        result.getTotalImpact()
                ),
                "⚡ Blast Radius: " + result.getSeverity()
        );
    }

    @Override
    public void update(@NotNull AnActionEvent event) {
        // Only enable in Java files
        PsiFile psiFile = event.getData(CommonDataKeys.PSI_FILE);
        event.getPresentation().setEnabledAndVisible(psiFile instanceof PsiJavaFile);
    }
}
