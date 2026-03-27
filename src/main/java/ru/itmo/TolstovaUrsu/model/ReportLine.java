package ru.itmo.TolstovaUrsu.model;

import java.time.Instant;

public final class ReportLine {
    private final long id;
    private final long reportId;
    private MeasurementParam param;
    private double value;
    private String unit;
    private final Instant createdAt;
    private Instant updatedAt;

    public ReportLine(long id, long reportId, MeasurementParam param, double value, String unit) {
        if (param == null) {
            throw new IllegalArgumentException("Ошибка: параметр не может быть пустым");
        }
        if (unit == null || unit.isBlank()) {
            throw new IllegalArgumentException("Ошибка: единицы не могут быть пустыми");
        }
        if (unit.length() > 16) {
            throw new IllegalArgumentException("Ошибка: единицы слишком длинные (макс. 16)");
        }
        this.id = id;
        this.reportId = reportId;
        this.param = param;
        this.value = value;
        this.unit = unit;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public long getId() { return id; }
    public long getReportId() { return reportId; }
    public MeasurementParam getParam() { return param; }
    public double getValue() { return value; }
    public String getUnit() { return unit; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void setParam(MeasurementParam param) {
        if (param == null) {
            throw new IllegalArgumentException("Ошибка: параметр не может быть пустым");
        }
        this.param = param;
        this.updatedAt = Instant.now();
    }

    public void setValue(double value) {
        this.value = value;
        this.updatedAt = Instant.now();
    }

    public void setUnit(String unit) {
        if (unit == null || unit.isBlank()) {
            throw new IllegalArgumentException("Ошибка: единицы не могут быть пустыми");
        }
        if (unit.length() > 16) {
            throw new IllegalArgumentException("Ошибка: единицы слишком длинные (макс. 16)");
        }
        this.unit = unit;
        this.updatedAt = Instant.now();
    }
}