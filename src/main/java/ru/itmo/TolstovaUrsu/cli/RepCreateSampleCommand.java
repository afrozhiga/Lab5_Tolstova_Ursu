package ru.itmo.TolstovaUrsu.cli;

import ru.itmo.TolstovaUrsu.command.Command;
import ru.itmo.TolstovaUrsu.model.Report;
import ru.itmo.TolstovaUrsu.model.ReportStatus;
import ru.itmo.TolstovaUrsu.model.Sample;
import ru.itmo.TolstovaUrsu.service.ReportCollectionManager;
import ru.itmo.TolstovaUrsu.service.SampleCollectionManager;

import java.util.Optional;
import java.util.Scanner;

public class RepCreateSampleCommand extends Command {
    private static final Object CURRENT_USER = "user";
    private final SampleCollectionManager sampleManager;

    public RepCreateSampleCommand(ReportCollectionManager reportManager, SampleCollectionManager sampleManager, Scanner scanner) {
        super(reportManager, sampleManager, scanner);
        this.sampleManager = sampleManager;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            System.out.println("Ошибка: укажите sample_id");
            return;
        }

        long sampleId;
        try {
            sampleId = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: sample_id должен быть числом");
            return;
        }

        System.out.print("Название отчёта: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Ошибка: название не может быть пустым");
            return;
        }

        try {
            Report report = reportManager.addReport(name, sampleId, ReportStatus.DRAFT, CURRENT_USER);
            System.out.println("OK report_id=" + report.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getName() {
        return "";
    }
}
