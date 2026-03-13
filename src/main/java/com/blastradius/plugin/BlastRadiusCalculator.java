package com.blastradius.plugin;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.Query;
import git4idea.history.GitHistoryUtils;
import git4idea.repo.GitRepository;
import git4idea.repo.GitRepositoryManager;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Calculates the blast radius of a Java class by analyzing:
 * <ol>
 *     <li><b>Direct dependents</b> — classes that directly reference this class (via PSI)</li>
 *     <li><b>Transitive dependents</b> — classes reached through dependency chains (up to 3 levels deep)</li>
 *     <li><b>Change-coupled files</b> — files that frequently change together in Git commits</li>
 * </ol>
 */
public class BlastRadiusCalculator {

    private static final Logger LOG = Logger.getInstance(BlastRadiusCalculator.class);

    /** Maximum depth for transitive dependency traversal */
    private static final int MAX_TRANSITIVE_DEPTH = 3;

    /** Number of months to look back for Git change coupling */
    private static final int HISTORY_MONTHS = 6;

    /** Minimum co-change frequency to consider files as change-coupled */
    private static final int MIN_CO_CHANGE_COUNT = 2;

    /**
     * Performs a complete blast radius analysis for the given class.
     *
     * @param psiClass the Java class to analyze
     * @return analysis result, or null if the class has no containing file
     */
    public static BlastRadiusResult analyze(PsiClass psiClass) {
        Project project = psiClass.getProject();
        VirtualFile virtualFile = psiClass.getContainingFile().getVirtualFile();

        if (virtualFile == null) {
            return null;
        }

        String className = psiClass.getName() != null ? psiClass.getName() : "Unknown";

        // 1. Find direct dependents
        Set<PsiClass> directDependents = findDirectDependents(psiClass, project);

        // 2. Find transitive dependents (up to MAX_TRANSITIVE_DEPTH levels)
        Set<PsiClass> transitiveDependents = findTransitiveDependents(directDependents, project);
        transitiveDependents.removeAll(directDependents); // Don't double-count

        // 3. Find change-coupled files from Git history
        int changeCoupledFiles = findChangeCoupledFiles(virtualFile, project);

        return new BlastRadiusResult(
                className,
                directDependents.size(),
                transitiveDependents.size(),
                changeCoupledFiles
        );
    }

    /**
     * Finds all classes that directly reference the given class.
     */
    private static Set<PsiClass> findDirectDependents(PsiClass psiClass, Project project) {
        Set<PsiClass> dependents = new HashSet<>();
        try {
            Query<PsiReference> references = ReferencesSearch.search(
                    psiClass, GlobalSearchScope.projectScope(project));

            for (PsiReference ref : references.findAll()) {
                PsiElement refElement = ref.getElement();
                PsiClass containingClass = findContainingClass(refElement);
                if (containingClass != null && !containingClass.equals(psiClass)) {
                    dependents.add(containingClass);
                }
            }
        } catch (Exception e) {
            LOG.warn("Failed to find direct dependents for " + psiClass.getName(), e);
        }
        return dependents;
    }

    /**
     * Walks up the PSI tree to find the containing PsiClass.
     */
    private static PsiClass findContainingClass(PsiElement element) {
        PsiElement current = element;
        while (current != null) {
            if (current instanceof PsiClass) {
                return (PsiClass) current;
            }
            current = current.getParent();
        }
        return null;
    }

    /**
     * Finds transitive dependents by iteratively searching references of direct dependents.
     * Limited to {@link #MAX_TRANSITIVE_DEPTH} levels to avoid excessive computation.
     */
    private static Set<PsiClass> findTransitiveDependents(Set<PsiClass> directDependents, Project project) {
        Set<PsiClass> allTransitive = new HashSet<>();
        Set<PsiClass> currentLevel = new HashSet<>(directDependents);

        for (int depth = 1; depth < MAX_TRANSITIVE_DEPTH; depth++) {
            Set<PsiClass> nextLevel = new HashSet<>();
            for (PsiClass cls : currentLevel) {
                try {
                    Query<PsiReference> refs = ReferencesSearch.search(
                            cls, GlobalSearchScope.projectScope(project));
                    for (PsiReference ref : refs.findAll()) {
                        PsiClass containingClass = findContainingClass(ref.getElement());
                        if (containingClass != null
                                && !directDependents.contains(containingClass)
                                && !allTransitive.contains(containingClass)) {
                            nextLevel.add(containingClass);
                        }
                    }
                } catch (Exception e) {
                    LOG.warn("Error finding transitive dependents at depth " + depth, e);
                }
            }
            allTransitive.addAll(nextLevel);
            currentLevel = nextLevel;

            if (currentLevel.isEmpty()) {
                break;
            }
        }

        return allTransitive;
    }

    /**
     * Analyzes Git history to find files that frequently change alongside the target file.
     * Looks at commits from the last {@link #HISTORY_MONTHS} months.
     */
    private static int findChangeCoupledFiles(VirtualFile virtualFile, Project project) {
        try {
            GitRepositoryManager repoManager = GitRepositoryManager.getInstance(project);
            GitRepository repo = (GitRepository) repoManager.getRepositoryForFileQuick(virtualFile);
            if (repo == null) {
                return 0;
            }

            // Get commits that touched this file in the last N months
            var commits = GitHistoryUtils.history(project, repo.getRoot(),
                    "--since=" + HISTORY_MONTHS + ".months",
                    "--name-only",
                    "--", virtualFile.getPath());

            if (commits.isEmpty()) {
                return 0;
            }

            // Count unique files that appeared in the same commits
            // Each commit that touches this file likely touches co-changed files
            // We approximate by counting distinct commits as a proxy for coupling
            Map<String, Integer> coChangeCount = new HashMap<>();

            for (var commit : commits) {
                // Each commit's changed paths indicate files that changed together
                var changedPaths = commit.getChangedPaths();
                if (changedPaths != null) {
                    for (var path : changedPaths) {
                        String filePath = path.getPath();
                        if (!filePath.equals(virtualFile.getPath())) {
                            coChangeCount.merge(filePath, 1, Integer::sum);
                        }
                    }
                }
            }

            // Count files that co-changed at least MIN_CO_CHANGE_COUNT times
            return (int) coChangeCount.values().stream()
                    .filter(count -> count >= MIN_CO_CHANGE_COUNT)
                    .count();

        } catch (Exception e) {
            LOG.warn("Error analyzing change coupling for " + virtualFile.getPath(), e);
            return 0;
        }
    }
}
