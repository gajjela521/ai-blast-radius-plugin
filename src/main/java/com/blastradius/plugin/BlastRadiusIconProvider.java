package com.blastradius.plugin;

import com.intellij.ui.JBColor;
import com.intellij.util.ui.ColorsIcon;

import javax.swing.*;
import java.awt.*;

/**
 * Provides color-coded gutter icons based on the blast radius impact level.
 * <p>
 * Color thresholds:
 * <ul>
 *     <li>Impact > 30 → Red (Critical)</li>
 *     <li>Impact > 15 → Orange (High)</li>
 *     <li>Impact > 5 → Yellow (Medium)</li>
 *     <li>Impact ≤ 5 → Green (Low)</li>
 * </ul>
 */
public class BlastRadiusIconProvider {

    private static final Color YELLOW = new JBColor(new Color(255, 200, 0), new Color(255, 200, 0));

    /**
     * Returns a color-coded icon based on the total blast radius impact.
     *
     * @param totalImpact the total number of affected files/components
     * @return a 12px colored circle icon
     */
    public static Icon getIcon(int totalImpact) {
        if (totalImpact > 30) {
            return new ColorsIcon(12, new Color[]{JBColor.RED});
        } else if (totalImpact > 15) {
            return new ColorsIcon(12, new Color[]{JBColor.ORANGE});
        } else if (totalImpact > 5) {
            return new ColorsIcon(12, new Color[]{YELLOW});
        } else {
            return new ColorsIcon(12, new Color[]{JBColor.GREEN});
        }
    }
}
