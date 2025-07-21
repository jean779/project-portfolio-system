package br.com.portfolio.enums;

public enum ProjectStatus {
    UNDER_ANALYSIS,
    ANALYSIS_DONE,
    APPROVED,
    STARTED,
    PLANNED,
    IN_PROGRESS,
    FINISHED,
    CANCELED;

    public boolean isLocked() {
        return this == STARTED || this == IN_PROGRESS || this == FINISHED;
    }
}
