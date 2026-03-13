package com.blastradius.plugin;

/**
 * Encapsulates the result of a blast radius analysis for a Java class.
 */
public class BlastRadiusResult {

    private final String className;
    private final int directDependents;
    private final int transitiveDependents;
    private final int changeCoupledFiles;
    private final int totalImpact;

    public BlastRadiusResult(String className, int directDependents,
                             int transitiveDependents, int changeCoupledFiles) {
        this.className = className;
        this.directDependents = directDependents;
        this.transitiveDependents = transitiveDependents;
        this.changeCoupledFiles = changeCoupledFiles;
        this.totalImpact = directDependents + transitiveDependents + changeCoupledFiles;
    }

    public String getClassName() {
        return className;
    }

    public int getDirectDependents() {
        return directDependents;
    }

    public int getTransitiveDependents() {
        return transitiveDependents;
    }

    public int getChangeCoupledFiles() {
        return changeCoupledFiles;
    }

    public int getTotalImpact() {
        return totalImpact;
    }

    /**
     * Returns a severity label based on total impact.
     */
    public String getSeverity() {
        if (totalImpact > 30) {
            return "CRITICAL";
        } else if (totalImpact > 15) {
            return "HIGH";
        } else if (totalImpact > 5) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }

    /**
     * Generates a formatted tooltip summarizing the blast radius.
     */
    public String getFormattedTooltip() {
        return String.format(
                "<html><b>Blast Radius: %s</b><br>" +
                "Class: %s<br>" +
                "Direct Dependents: %d<br>" +
                "Transitive Dependents: %d<br>" +
                "Change-Coupled Files: %d<br>" +
                "<b>Total Impact: %d files</b></html>",
                getSeverity(), className, directDependents,
                transitiveDependents, changeCoupledFiles, totalImpact
        );
    }
}
