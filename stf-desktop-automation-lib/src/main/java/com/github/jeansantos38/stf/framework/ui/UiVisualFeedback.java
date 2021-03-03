package com.github.jeansantos38.stf.framework.desktop;


public class UiVisualFeedback {

    boolean enableHighlight;

    public boolean isEnableHighlight() {
        return enableHighlight;
    }

    public void setEnableHighlight(boolean enableHighlight) {
        this.enableHighlight = enableHighlight;
    }

    String masterHighlightColor;
    String relAreaHighlightColor;
    double masterHighlightTimeSec;
    double relAreaHighlightTimeSec;
    int relHighlightH;
    int relHighlightW;
    final int HIGHLIGHT_H_W_DEFAULT = 5;

    public UiVisualFeedback() {
        this(true, "yellow", "red", 0.5, 0.5, 0, 0);
    }

    public UiVisualFeedback(
            String masterHighlightColor,
            String relAreaHighlightColor) {
        this(true, masterHighlightColor, relAreaHighlightColor, 0.5, 0.5, 0, 0);

    }

    public UiVisualFeedback(
            String masterHighlightColor,
            String relAreaHighlightColor,
            double masterHighlightTimeSec,
            double relAreaHighlightTimeSec) {
        this(true, masterHighlightColor, relAreaHighlightColor, masterHighlightTimeSec, relAreaHighlightTimeSec, 0, 0);

    }

    public UiVisualFeedback(
            boolean enableHighlight,
            String masterHighlightColor,
            String relAreaHighlightColor,
            double masterHighlightTimeSec,
            double relAreaHighlightTimeSec,
            int relHighlightH,
            int relHighlightW) {
        this.enableHighlight = enableHighlight;
        this.masterHighlightColor = masterHighlightColor;
        this.relAreaHighlightColor = relAreaHighlightColor;
        this.masterHighlightTimeSec = masterHighlightTimeSec;
        this.relAreaHighlightTimeSec = relAreaHighlightTimeSec;
        this.relHighlightH = relHighlightH == 0 ? HIGHLIGHT_H_W_DEFAULT : relHighlightH;
        this.relHighlightW = relHighlightW == 0 ? HIGHLIGHT_H_W_DEFAULT : relHighlightW;
    }
}