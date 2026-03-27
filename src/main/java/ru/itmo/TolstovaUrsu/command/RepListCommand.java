package ru.itmo.TolstovaUrsu.command;

import ru.itmo.TolstovaUrsu.model.Report;
import ru.itmo.TolstovaUrsu.model.ReportStatus;
import ru.itmo.TolstovaUrsu.service.ExpCollectionManager;
import ru.itmo.TolstovaUrsu.service.ReportCollectionManager;
import ru.itmo.TolstovaUrsu.service.SampleCollectionManager;

import java.util.List;
import java.util.Scanner;

public class RepListCommand extends Command {

    public RepListCommand(ReportCollectionManager reportManager,
                          SampleCollectionManager sampleManager,
                          ExpCollectionManager expManager,
                          Scanner scanner) {
        super(reportManager, sampleManager, scanner);
    }

    public RepListCommand(ExpCollectionManager manager) {
        super(manager);
    }

    public RepListCommand(ReportCollectionManager reportManager, SampleCollectionManager sampleManager, Scanner scanner) {
        super(reportManager, sampleManager, scanner);
    }

    @Override
    public String getName() { return "rep_list"; }

    @Override
    public String getDescription() { return "rep_list [--status DRAFT|FINAL|SIGNED] — список отчётов"; }

    @Override
    public void execute(String[] args) {
        ReportStatus filterStatus = null;

        for (int i = 0; i < args.length; i++) {
            if ("--status".equals(args[i])) {
                if (i + 1 >= args.length) {
                    System.out.println("Ошибка: после --status нужно указать статус");
                    return;
                }
                try {
                    filterStatus = ReportStatus.valueOf(args[i + 1].toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Ошибка: неизвестный статус '" + args[i + 1]
                            + "'. Используйте DRAFT, FINAL или SIGNED");
                    return;
                }
                i++;
            }
        }

        ReportCollectionManager reportManager
                = new ReportCollectionManager();
        List<Report> list = (filterStatus == null)
                ? reportManager.getAllReports()
                : reportManager.getReportsByStatus(filterStatus);

        if (list.isEmpty()) {
            System.out.println("(нет отчётов)");
            return;
        }

        System.out.printf("%-5s %-30s %-8s%n", "ID", "Name", "Status");
        for (Report r : list) {
            System.out.printf("%-5d %-30s %-8s%n", r.getId(), r.getName(), r.getStatus());
        }
    }
}