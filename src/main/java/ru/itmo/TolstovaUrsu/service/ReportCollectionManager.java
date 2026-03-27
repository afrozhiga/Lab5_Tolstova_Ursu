package ru.itmo.TolstovaUrsu.service;

import ru.itmo.TolstovaUrsu.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReportCollectionManager {
    private final ArrayList<Report> reports = new ArrayList<>();
    private final ArrayList<ReportLine> reportLines = new ArrayList<>();
    private long reportIdCounter = 1;
    private long lineIdCounter = 1;

    public Report createReport(String name, long sampleId, long experimentId, String owner) {
        Report report = new Report(reportIdCounter++, name, sampleId, experimentId, owner);
        reports.add(report);
        return report;
    }

    public Report addReport(String name, long sampleId, ReportStatus status, Object owner) {
        return createReport(name, sampleId, 0L, String.valueOf(owner));
    }

    public List<Report> getAllReports() {
        return new ArrayList<>(reports);
    }

    public List<Report> getReportsByStatus(ReportStatus status) {
        return reports.stream()
                .filter(r -> r.getStatus() == status)
                .collect(Collectors.toList());
    }

    public Optional<Report> getReportById(long id) {
        return reports.stream()
                .filter(r -> r.getId() == id)
                .findFirst();
    }

    public void finalizeReport(long reportId) {
        Report report = requireReport(reportId);
        if (report.getStatus() == ReportStatus.FINAL) {
            throw new IllegalStateException("Ошибка: отчёт уже FINAL");
        }
        if (report.getStatus() == ReportStatus.SIGNED) {
            throw new IllegalStateException("Ошибка: нельзя изменить подписанный отчёт");
        }
        report.setStatus(ReportStatus.FINAL);
    }

    public void signReport(long reportId, String username) {
        Report report = requireReport(reportId);
        if (report.getStatus() == ReportStatus.DRAFT) {
            throw new IllegalStateException("Ошибка: сначала выполните finalize (rep_finalize)");
        }
        if (report.getStatus() == ReportStatus.SIGNED) {
            throw new IllegalStateException("Ошибка: отчёт уже подписан");
        }
        report.setStatus(ReportStatus.SIGNED);
        report.setSignedBy(username);
    }

    // ========================
    //   ReportLine operations
    // ========================

    public ReportLine addLine(long reportId, MeasurementParam param, double value, String unit) {
        Report report = requireReport(reportId);
        if (report.getStatus() != ReportStatus.DRAFT) {
            throw new IllegalStateException(
                    "Ошибка: нельзя добавлять строки в отчёт со статусом " + report.getStatus());
        }
        ReportLine line = new ReportLine(lineIdCounter++, reportId, param, value, unit);
        reportLines.add(line);
        return line;
    }

    /** Используется из RepAddLineCommand */
    public ReportLine addReportLine(long reportId, MeasurementParam param, double value, String unit) {
        return addLine(reportId, param, value, unit);
    }

    public List<ReportLine> getLinesForReport(long reportId) {
        requireReport(reportId);
        return reportLines.stream()
                .filter(l -> l.getReportId() == reportId)
                .collect(Collectors.toList());
    }

    public Optional<ReportLine> getLineById(long lineId) {
        return reportLines.stream()
                .filter(l -> l.getId() == lineId)
                .findFirst();
    }

    public void updateLine(long lineId, String field, String rawValue) {
        ReportLine line = requireLine(lineId);
        Report report = requireReport(line.getReportId());
        if (report.getStatus() != ReportStatus.DRAFT) {
            throw new IllegalStateException(
                    "Ошибка: нельзя изменять строки отчёта со статусом " + report.getStatus());
        }

        switch (field.toLowerCase()) {
            case "param" -> {
                try {
                    line.setParam(MeasurementParam.valueOf(rawValue.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Ошибка: неизвестный параметр '" + rawValue
                            + "'. Допустимые: " + java.util.Arrays.toString(MeasurementParam.values()));
                }
            }
            case "value" -> {
                try {
                    line.setValue(Double.parseDouble(rawValue));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Ошибка: значение должно быть числом");
                }
            }
            case "unit" -> {
                if (rawValue == null || rawValue.isBlank()) {
                    throw new IllegalArgumentException("Ошибка: unit не может быть пустым");
                }
                line.setUnit(rawValue);
            }
            default -> throw new IllegalArgumentException(
                    "Ошибка: нельзя менять поле '" + field + "'. Допустимые: param, value, unit");
        }
    }

    public void deleteLine(long lineId) {
        ReportLine line = requireLine(lineId);
        Report report = requireReport(line.getReportId());
        if (report.getStatus() != ReportStatus.DRAFT) {
            throw new IllegalStateException(
                    "Ошибка: нельзя удалять строки отчёта со статусом " + report.getStatus());
        }
        reportLines.removeIf(l -> l.getId() == lineId);
    }

    /** Используется из RepDelLineCommand */
    public void deleteReportLine(long lineId) {
        deleteLine(lineId);
    }

    public String exportReport(long reportId) {
        Report report = requireReport(reportId);
        List<ReportLine> lines = getLinesForReport(reportId);

        StringBuilder sb = new StringBuilder();
        sb.append("==================================================\n");
        sb.append("REPORT #").append(report.getId()).append("\n");
        sb.append("==================================================\n");
        sb.append(String.format("%-10s %s%n", "Name:", report.getName()));
        sb.append(String.format("%-10s %s%n", "Status:", report.getStatus()));
        sb.append(String.format("%-10s %s%n", "Owner:", report.getOwnerUsername()));
        if (report.getSignedBy() != null) {
            sb.append(String.format("%-10s %s%n", "Signed:", report.getSignedBy()));
        }
        sb.append(String.format("%-10s %s%n", "Created:", report.getCreatedAt()));
        sb.append("--------------------------------------------------\n");

        if (lines.isEmpty()) {
            sb.append("(нет строк)\n");
        } else {
            sb.append(String.format("%-6s %-15s %-12s %-8s%n", "ID", "Param", "Value", "Unit"));
            for (ReportLine l : lines) {
                sb.append(String.format("%-6d %-15s %-12.4f %-8s%n",
                        l.getId(), l.getParam(), l.getValue(), l.getUnit()));
            }
        }

        sb.append("==================================================\n");
        return sb.toString();
    }

    private Report requireReport(long id) {
        return getReportById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Ошибка: отчёт с id=" + id + " не найден"));
    }

    private ReportLine requireLine(long id) {
        return getLineById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Ошибка: строка с id=" + id + " не найдена"));
    }
}