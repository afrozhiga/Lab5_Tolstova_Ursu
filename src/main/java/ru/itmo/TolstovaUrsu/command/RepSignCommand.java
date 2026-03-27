package ru.itmo.TolstovaUrsu.command;

import ru.itmo.TolstovaUrsu.model.Report;
import ru.itmo.TolstovaUrsu.model.ReportStatus;
import ru.itmo.TolstovaUrsu.service.ExpCollectionManager;
import ru.itmo.TolstovaUrsu.service.ReportCollectionManager;
import ru.itmo.TolstovaUrsu.service.SampleCollectionManager;

import java.util.Optional;
import java.util.Scanner;

public class RepSignCommand extends Command {
    private static final String CURRENT_USER = "SYSTEM";

    public RepSignCommand(ReportCollectionManager reportManager, SampleCollectionManager sampleManager, Scanner scanner) {
        ExpCollectionManager expManager = new ExpCollectionManager();
        super(reportManager, sampleManager, scanner);
    }

    public void execute(String[] args) {
        if (args.length < 1) {
            System.out.println("Ошибка: укажите id отчёта. Использование: rep_sign <report_id>");
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

        if (report.getStatus() == ReportStatus.DRAFT) {
            System.out.println("Ошибка: сначала finalize. Нельзя подписать отчёт в статусе DRAFT");
            return;
        }

        if (report.getStatus() == ReportStatus.SIGNED) {
            System.out.println("Ошибка: отчёт уже подписан пользователем " + report.getSignedBy());
            return;
        }

        reportManager.signReport(reportId, CURRENT_USER);
        System.out.println("OK report " + reportId + " SIGNED by " + CURRENT_USER);
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "";
    }
}
