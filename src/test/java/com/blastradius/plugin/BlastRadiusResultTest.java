package com.blastradius.plugin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BlastRadiusResultTest {

    @Test
    public void testSeverityLevels() {
        BlastRadiusResult low = new BlastRadiusResult("TestClass", 1, 1, 1);
        assertEquals("LOW", low.getSeverity());
        assertEquals(3, low.getTotalImpact());

        BlastRadiusResult medium = new BlastRadiusResult("TestClass", 2, 2, 2);
        assertEquals("MEDIUM", medium.getSeverity()); // 6 total
        
        BlastRadiusResult high = new BlastRadiusResult("TestClass", 5, 5, 6);
        assertEquals("HIGH", high.getSeverity()); // 16 total

        BlastRadiusResult critical = new BlastRadiusResult("TestClass", 10, 10, 15);
        assertEquals("CRITICAL", critical.getSeverity()); // 35 total
    }

    @Test
    public void testFormattedTooltip() {
        BlastRadiusResult result = new BlastRadiusResult("MyClass", 2, 3, 1);
        String tooltip = result.getFormattedTooltip();
        
        String expected = "<html><b>Blast Radius: MEDIUM</b><br>" +
                "Class: MyClass<br>" +
                "Direct Dependents: 2<br>" +
                "Transitive Dependents: 3<br>" +
                "Change-Coupled Files: 1<br>" +
                "<b>Total Impact: 6 files</b></html>";
        
        assertEquals(expected, tooltip);
    }
}
