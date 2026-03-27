package ru.itmo.TolstovaUrsu.command;

import ru.itmo.TolstovaUrsu.model.Report;
import ru.itmo.TolstovaUrsu.service.ExpCollectionManager;
import ru.itmo.TolstovaUrsu.service.ReportCollectionManager;
import ru.itmo.TolstovaUrsu.service.SampleCollectionManager;

import java.util.Scanner;

public class RepShowCommand extends Command {

    public RepShowCommand(ReportCollectionManager reportManager,
                          SampleCollectionManager sampleManager,
                          ExpCollectionManager expManager,
                          Scanner scanner) {
        super(reportManager, sampleManager, scanner);
        wait(reportManager, sampleManager, expManager, scanner);
    }

    public RepShowCommand(ExpCollectionManager manager) {
        super(manager);
    }

    public RepShowCommand(ReportCollectionManager reportManager, SampleCollectionManager sampleManager, Scanner scanner) {
        super(reportManager, sampleManager, scanner);
    }


    private void wait(ReportCollectionManager reportManager, SampleCollectionManager sampleManager, ExpCollectionManager expManager, Scanner scanner) {
    }

    @Override
    public String getName() { return "rep_show"; }

    @Override
    public String getDescription() { return "rep_show <report_id> — карточка отчёта"; }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            System.out.println("Использование: rep_show <report_id>");
            return;
        }
        long id;
        try {
            id = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: report_id должен быть числом");
            return;
        }

        reportManager.getReportById(id).ifPresentOrElse(r -> {
            int lineCount = reportManager.getLinesForReport(r.getId()).size();
            System.out.println("Report #" + r.getId());
            System.out.println("name   : " + r.getName());
            System.out.println("status : " + r.getStatus());
            System.out.println("owner  : " + r.getOwnerUsername());
            if (r.getSignedBy() != null) {
                System.out.println("signed : " + r.getSignedBy());
            }
            System.out.println("lines  : " + lineCount);
        }, () -> System.out.println("Ошибка: отчёт с id=" + id + " не найден"));
    }
}