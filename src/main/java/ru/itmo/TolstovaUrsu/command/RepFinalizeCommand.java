package ru.itmo.TolstovaUrsu.command;

import ru.itmo.TolstovaUrsu.model.Report;
import ru.itmo.TolstovaUrsu.model.ReportStatus;
import ru.itmo.TolstovaUrsu.service.ExpCollectionManager;
import ru.itmo.TolstovaUrsu.service.ReportCollectionManager;
import ru.itmo.TolstovaUrsu.service.SampleCollectionManager;

import java.util.Optional;
import java.util.Scanner;

public class RepFinalizeCommand extends Command {

    public RepFinalizeCommand(ReportCollectionManager reportManager, SampleCollectionManager sampleManager, Scanner scanner) {
        ExpCollectionManager expManager = new ExpCollectionManager();
        super(reportManager, sampleManager, scanner);
    }

    public void execute(String[] args) {
        if (args.length < 1) {
            System.out.println("Ошибка: укажите id отчёта. Использование: rep_finalize <report_id>");
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

        if (report.getStatus() == ReportStatus.FINAL) {
            System.out.println("Ошибка: отчёт уже имеет статус FINAL");
            return;
        }

        if (report.getStatus() == ReportStatus.SIGNED) {
            System.out.println("Ошибка: отчёт уже подписан (SIGNED), изменение статуса невозможно");
            return;
        }

        reportManager.finalizeReport(reportId);
        System.out.println("OK report " + reportId + " FINAL");
    }

    @Override
    public String getDescription() {
        return "завершить отчет";
    }

    @Override
    public String getName() {
        return "finalize";
    }
}
