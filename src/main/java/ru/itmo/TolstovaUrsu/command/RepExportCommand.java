package ru.itmo.TolstovaUrsu.command;

import ru.itmo.TolstovaUrsu.model.Report;
import ru.itmo.TolstovaUrsu.model.ReportLine;
import ru.itmo.TolstovaUrsu.service.ReportCollectionManager;
import ru.itmo.TolstovaUrsu.service.SampleCollectionManager;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class RepExportCommand extends Command {

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.systemDefault());

    public RepExportCommand(ReportCollectionManager reportManager,
                            SampleCollectionManager sampleManager,
                            Scanner scanner) {
        super(reportManager, sampleManager, scanner);
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            System.out.println("Ошибка: укажите id отчёта. Использование: rep_export <report_id>");
            return;
        }

        long reportId;
        try {
            reportId = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: report_id должен быть числом");
            return;
        }

        Optional<Report> reportOpt = reportManager.getReportById(reportId);
        if (reportOpt.isEmpty()) {
            System.out.println("Ошибка: отчёт с id=" + reportId + " не найден");
            return;
        }

        Report report = reportOpt.get();
        List<ReportLine> lines = reportManager.getLinesForReport(reportId);

        printSeparator();
        System.out.println("REPORT #" + report.getId());
        printSeparator();
        System.out.println("Name   : " + report.getName());
        System.out.println("Status : " + report.getStatus());
        System.out.println("Owner  : " + report.getOwnerUsername());
        System.out.println("Created: " + DATE_FMT.format(report.getCreatedAt()));

        if (report.getSampleId() != 0) {
            System.out.println("Sample : #" + report.getSampleId());
        }
        if (report.getExperimentId() != 0) {
            System.out.println("Exp    : #" + report.getExperimentId());
        }
        if (report.getSignedBy() != null) {
            System.out.println("Signed : " + report.getSignedBy());
        }

        System.out.println();
        System.out.printf("%-6s %-15s %-12s %-8s%n", "ID", "Param", "Value", "Unit");
        printSeparator();

        if (lines.isEmpty()) {
            System.out.println("(нет строк)");
        } else {
            for (ReportLine line : lines) {
                System.out.printf("%-6d %-15s %-12.4f %-8s%n",
                        line.getId(),
                        line.getParam().name(),
                        line.getValue(),
                        line.getUnit());
            }
        }

        printSeparator();
        System.out.println("Report exported (text)");
    }

    @Override
    public String getDescription() { return "Текстовый экспорт отчёта"; }

    @Override
    public String getName() { return "rep_export"; }

    private void printSeparator() {
        System.out.println("--------------------------------------------------");
    }
}