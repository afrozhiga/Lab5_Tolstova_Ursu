package ru.itmo.TolstovaUrsu.command;

import ru.itmo.TolstovaUrsu.model.ReportLine;
import ru.itmo.TolstovaUrsu.service.ExpCollectionManager;
import ru.itmo.TolstovaUrsu.service.ReportCollectionManager;
import ru.itmo.TolstovaUrsu.service.SampleCollectionManager;

import java.util.List;
import java.util.Scanner;

public class RepLinesCommand extends Command {

    public RepLinesCommand(ReportCollectionManager reportManager,
                           SampleCollectionManager sampleManager,
                           ExpCollectionManager expManager,
                           Scanner scanner) {
        super(reportManager, sampleManager, scanner);
        wait(reportManager, sampleManager, expManager, scanner);
    }

    public RepLinesCommand(ExpCollectionManager manager) {
        super(manager);
    }

    public RepLinesCommand(ReportCollectionManager reportManager, SampleCollectionManager sampleManager, Scanner scanner) {
        super(reportManager, sampleManager, scanner);
    }

    private void wait(ReportCollectionManager reportManager, SampleCollectionManager sampleManager, ExpCollectionManager expManager, Scanner scanner) {
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription() { return "rep_lines <report_id> — строки отчёта"; }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            System.out.println("Использование: rep_lines <report_id>");
            return;
        }
        long id;
        try {
            id = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: report_id должен быть числом");
            return;
        }

        if (reportManager.getReportById(id).isEmpty()) {
            System.out.println("Ошибка: отчёт с id=" + id + " не найден");
            return;
        }

        List<ReportLine> lines = reportManager.getLinesForReport(id);
        if (lines.isEmpty()) {
            System.out.println("(строк нет)");
            return;
        }

        System.out.printf("%-5s %-15s %-10s %-8s%n", "ID", "Param", "Value", "Unit");
        for (ReportLine l : lines) {
            System.out.printf("%-5d %-15s %-10.4f %-8s%n",
                    l.getId(), l.getParam(), l.getValue(), l.getUnit());
        }
    }
}