package com.blastradius.plugin;

import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.JBColor;
import com.intellij.ui.awt.RelativePoint;

import java.awt.event.MouseEvent;

/**
 * Renders detailed blast radius popups when the user interacts with gutter icons.
 */
public class BlastRadiusPopupRenderer {

    /**
     * Shows a detailed balloon popup with the blast radius analysis results.
     *
     * @param mouseEvent the triggering mouse event
     * @param result     the blast radius analysis result
     */
    public static void showPopup(MouseEvent mouseEvent, BlastRadiusResult result) {
        if (mouseEvent == null || result == null) {
            return;
        }

        String html = buildPopupHtml(result);

        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(html, null, JBColor.background(), null)
                .setFadeoutTime(8000)
                .createBalloon()
                .show(new RelativePoint(mouseEvent), Balloon.Position.above);
    }

    /**
     * Builds rich HTML content for the blast radius popup.
     */
    private static String buildPopupHtml(BlastRadiusResult result) {
        String severityColor = getSeverityColor(result.getSeverity());

        return String.format(
                "<html>" +
                "<div style='font-family: sans-serif; padding: 4px;'>" +
                "<h3 style='margin: 0; color: %s;'>⚡ Blast Radius: %s</h3>" +
                "<hr style='margin: 4px 0;'/>" +
                "<table style='border-collapse: collapse;'>" +
                "<tr><td style='padding: 2px 8px 2px 0;'><b>Class:</b></td><td>%s</td></tr>" +
                "<tr><td style='padding: 2px 8px 2px 0;'><b>Direct Dependents:</b></td><td>%d classes</td></tr>" +
                "<tr><td style='padding: 2px 8px 2px 0;'><b>Transitive Dependents:</b></td><td>%d classes</td></tr>" +
                "<tr><td style='padding: 2px 8px 2px 0;'><b>Change-Coupled Files:</b></td><td>%d files</td></tr>" +
                "</table>" +
                "<hr style='margin: 4px 0;'/>" +
                "<b style='color: %s;'>Total Impact: %d files affected</b>" +
                "</div>" +
                "</html>",
                severityColor,
                result.getSeverity(),
                result.getClassName(),
                result.getDirectDependents(),
                result.getTransitiveDependents(),
                result.getChangeCoupledFiles(),
                severityColor,
                result.getTotalImpact()
        );
    }

    /**
     * Returns a CSS color string for the given severity level.
     */
    private static String getSeverityColor(String severity) {
        switch (severity) {
            case "CRITICAL":
                return "#FF4444";
            case "HIGH":
                return "#FF8800";
            case "MEDIUM":
                return "#FFCC00";
            case "LOW":
            default:
                return "#44BB44";
        }
    }
}
