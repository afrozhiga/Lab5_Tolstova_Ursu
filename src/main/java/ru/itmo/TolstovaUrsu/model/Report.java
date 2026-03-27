package ru.itmo.TolstovaUrsu.model;

import java.time.Instant;

public final class Report {
    private final long id;
    private String name;
    private final long sampleId;
    private final long experimentId;
    private ReportStatus status;
    private final String ownerUsername;
    private String signedBy;
    private final Instant createdAt;
    private Instant updatedAt;

    public Report(long id, String name, long sampleId, long experimentId, String ownerUsername) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Ошибка: название отчёта не может быть пустым");
        }
        if (name.length() > 128) {
            throw new IllegalArgumentException("Ошибка: название слишком длинное (макс. 128)");
        }
        this.id = id;
        this.name = name;
        this.sampleId = sampleId;
        this.experimentId = experimentId;
        this.status = ReportStatus.DRAFT;
        this.ownerUsername = ownerUsername;
        this.signedBy = null;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public long getSampleId() { return sampleId; }
    public long getExperimentId() { return experimentId; }
    public ReportStatus getStatus() { return status; }
    public String getOwnerUsername() { return ownerUsername; }
    public String getSignedBy() { return signedBy; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Ошибка: название отчёта не может быть пустым");
        }
        if (name.length() > 128) {
            throw new IllegalArgumentException("Ошибка: название слишком длинное (макс. 128)");
        }
        this.name = name;
        this.updatedAt = Instant.now();
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
        this.updatedAt = Instant.now();
    }

    public void setSignedBy(String signedBy) {
        this.signedBy = signedBy;
        this.updatedAt = Instant.now();
    }
}