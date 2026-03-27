package ru.itmo.TolstovaUrsu.command;

import ru.itmo.TolstovaUrsu.model.MeasurementParam;
import ru.itmo.TolstovaUrsu.model.Report;
import ru.itmo.TolstovaUrsu.model.ReportLine;
import ru.itmo.TolstovaUrsu.service.ExpCollectionManager;
import ru.itmo.TolstovaUrsu.service.ReportCollectionManager;
import ru.itmo.TolstovaUrsu.service.SampleCollectionManager;

import java.util.Optional;
import java.util.Scanner;

public class RepAddLineCommand extends Command {

    public RepAddLineCommand(ReportCollectionManager reportManager,
                             SampleCollectionManager sampleManager,
                             Scanner scanner) {
        super(reportManager, sampleManager, scanner);
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            System.out.println("Ошибка: укажите report_id");
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

        System.out.print("Параметр (PH/CONDUCTIVITY/TURBIDITY/NITRATE): ");
        String paramStr = scanner.nextLine().trim().toUpperCase();

        MeasurementParam param;
        try {
            param = MeasurementParam.valueOf(paramStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: неизвестный параметр '" + paramStr + "'");
            return;
        }

        System.out.print("Значение: ");
        double value;
        try {
            value = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: значение должно быть числом");
            return;
        }

        System.out.print("Единицы: ");
        String unit = scanner.nextLine().trim();

        try {
            ReportLine line = reportManager.addReportLine(reportId, param, value, unit);
            System.out.println("OK line_id=" + line.getId());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "rep_addline <report_id> - добавить строку в отчёт";
    }

    @Override
    public String getName() {
        return "rep_addline";
    }
}
