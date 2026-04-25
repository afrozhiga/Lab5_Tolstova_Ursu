package ru.itmo.TolstovaUrsu.service;

import ru.itmo.TolstovaUrsu.domain.MeasurementParam;
import ru.itmo.TolstovaUrsu.domain.Report;
import ru.itmo.TolstovaUrsu.domain.ReportLine;
import ru.itmo.TolstovaUrsu.domain.ReportStatus;
import ru.itmo.TolstovaUrsu.validation.ReportValidator;
import ru.itmo.TolstovaUrsu.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReportService {

    private final ArrayList<Report> reports = new ArrayList<>();
    private final ArrayList<ReportLine> reportLines = new ArrayList<>();

    private final ReportValidator validator = new ReportValidator();

    private long nextReportId = 1;
    private long nextLineId = 1;

    public Report createReportBySample(String name, long sampleId) throws ValidationException {
        String nameError = validator.validateName(name);
        if (nameError != null) throw new ValidationException(nameError);

        Report report = new Report(nextReportId++, name, sampleId, 0, "SYSTEM");
        reports.add(report);
        return report;
    }

    public Report createReportByExperiment(String name, long experimentId) throws ValidationException {
        String nameError = validator.validateName(name);
        if (nameError != null) throw new ValidationException(nameError);

        Report report = new Report(nextReportId++, name, 0, experimentId, "SYSTEM");
        reports.add(report);
        return report;
    }

    public Optional<Report> getById(long id) {
        return reports.stream().filter(r -> r.getId() == id).findFirst();
    }

    public List<Report> listReports(ReportStatus statusFilter) {
        if (statusFilter == null) {
            return new ArrayList<>(reports);
        }
        return reports.stream()
                .filter(r -> r.getStatus() == statusFilter)
                .collect(Collectors.toList());
    }

    public void finalizeReport(long reportId) throws ValidationException {
        Report report = requireReport(reportId);
        if (report.getStatus() == ReportStatus.FINAL) {
            throw new ValidationException("Ошибка: отчёт уже FINAL");
        }
        if (report.getStatus() == ReportStatus.SIGNED) {
            throw new ValidationException("Ошибка: нельзя изменить подписанный отчёт");
        }
        report.setStatus(ReportStatus.FINAL);
    }

    public void signReport(long reportId, String signerUsername) throws ValidationException {
        Report report = requireReport(reportId);
        if (report.getStatus() == ReportStatus.DRAFT) {
            throw new ValidationException("Ошибка: сначала выполните finalize отчёта");
        }
        if (report.getStatus() == ReportStatus.SIGNED) {
            throw new ValidationException("Ошибка: отчёт уже подписан");
        }
        report.setStatus(ReportStatus.SIGNED);
        report.setSignedBy(signerUsername);
    }


    public ReportLine addLine(long reportId, MeasurementParam param,
                              double value, String unit) throws ValidationException {
        Report report = requireReport(reportId);

        if (report.getStatus() != ReportStatus.DRAFT) {
            throw new ValidationException(
                    "Ошибка: нельзя добавлять строки в отчёт со статусом " + report.getStatus());
        }

        String unitError = validator.validateUnit(unit);
        if (unitError != null) throw new ValidationException(unitError);

        ReportLine line = new ReportLine(nextLineId++, reportId, param, value, unit);
        reportLines.add(line);
        return line;
    }

    public Optional<ReportLine> getLineById(long lineId) {
        return reportLines.stream().filter(l -> l.getId() == lineId).findFirst();
    }


    public List<ReportLine> getLinesForReport(long reportId) throws ValidationException {
        requireReport(reportId);
        return reportLines.stream()
                .filter(l -> l.getReportId() == reportId)
                .collect(Collectors.toList());
    }

    public void updateLine(long lineId, String field, String rawValue) throws ValidationException {
        ReportLine line = requireLine(lineId);

        Report report = requireReport(line.getReportId());
        if (report.getStatus() != ReportStatus.DRAFT) {
            throw new ValidationException(
                    "Ошибка: нельзя изменять строки отчёта со статусом " + report.getStatus());
        }

        switch (field.toLowerCase()) {
            case "param" -> {
                MeasurementParam param = MeasurementParam.fromString(rawValue);
                if (param == null) {
                    throw new ValidationException(
                            "Ошибка: неизвестный параметр '" + rawValue
                                    + "'. Доступны: PH, CONDUCTIVITY, TURBIDITY, NITRATE");
                }
                line.setParam(param);
            }
            case "value" -> {
                double val = validator.parseValue(rawValue);
                line.setValue(val);
            }
            case "unit" -> {
                String unitError = validator.validateUnit(rawValue);
                if (unitError != null) throw new ValidationException(unitError);
                line.setUnit(rawValue);
            }
            default -> throw new ValidationException(
                    "Ошибка: нельзя менять поле '" + field + "'. Разрешены: param, value, unit");
        }
    }

    public void deleteLine(long lineId) throws ValidationException {
        ReportLine line = requireLine(lineId);

        Report report = requireReport(line.getReportId());
        if (report.getStatus() != ReportStatus.DRAFT) {
            throw new ValidationException(
                    "Ошибка: нельзя удалять строки отчёта со статусом " + report.getStatus());
        }

        reportLines.remove(line);
    }

    public long countLinesForReport(long reportId) {
        return reportLines.stream().filter(l -> l.getReportId() == reportId).count();
    }

    public ReportValidator getValidator() {
        return validator;
    }

    private Report requireReport(long reportId) throws ValidationException {
        return getById(reportId)
                .orElseThrow(() -> new ValidationException(
                        "Ошибка: отчёт с id=" + reportId + " не найден"));
    }

    private ReportLine requireLine(long lineId) throws ValidationException {
        return getLineById(lineId)
                .orElseThrow(() -> new ValidationException(
                        "Ошибка: строка с id=" + lineId + " не найдена"));
    }
}
