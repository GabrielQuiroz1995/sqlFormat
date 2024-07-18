package org.example.sqlformat.util;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class TooltipConfigurer {

    public static void configure(Button button, String tooltipText) {
        Tooltip tooltip = new Tooltip(tooltipText);
        tooltip.setShowDelay(Duration.ZERO);
        button.setTooltip(tooltip);
    }
}
