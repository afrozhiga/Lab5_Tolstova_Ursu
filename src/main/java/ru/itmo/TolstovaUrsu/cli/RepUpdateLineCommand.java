package ru.itmo.TolstovaUrsu.cli;

import ru.itmo.TolstovaUrsu.command.Command;
import ru.itmo.TolstovaUrsu.service.ReportCollectionManager;
import ru.itmo.TolstovaUrsu.service.SampleCollectionManager;

import java.util.Scanner;

public class RepUpdateLineCommand extends Command {

    public RepUpdateLineCommand(ReportCollectionManager reportManager,
                                SampleCollectionManager sampleManager,
                                Scanner scanner) {
        super(reportManager, sampleManager, scanner);
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 4) {
            System.out.println("Использование: rep_updateline <line_id> <field> <value>");
            return;
        }

        long lineId;
        try {
            lineId = Long.parseLong(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: line_id должен быть числом");
            return;
        }

        String field = args[2];
        String value = args[3];

        try {
            reportManager.updateLine(lineId, field, value);
            System.out.println("Строка " + lineId + " обновлена: " + field + " = " + value);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "обновить поле строки отчёта";
    }

    @Override
    public String getName() {
        return "rep_updateline";
    }
}
