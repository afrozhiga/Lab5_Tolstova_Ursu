package ru.itmo.TolstovaUrsu.command;

import ru.itmo.TolstovaUrsu.model.Report;
import ru.itmo.TolstovaUrsu.model.ReportLine;
import ru.itmo.TolstovaUrsu.model.ReportStatus;
import ru.itmo.TolstovaUrsu.service.ExpCollectionManager;
import ru.itmo.TolstovaUrsu.service.ReportCollectionManager;
import ru.itmo.TolstovaUrsu.service.SampleCollectionManager;

import java.util.Optional;
import java.util.Scanner;

public class RepDelLineCommand extends Command {

    public RepDelLineCommand(ReportCollectionManager reportManager, SampleCollectionManager sampleManager, Scanner scanner) {
        super(reportManager, sampleManager, scanner);

    }

    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Ошибка: укажите id строки. Использование: rep_delline <line_id>");
            return;
        }

        long lineId;
        try {
            lineId = Long.parseLong(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: line_id должен быть числом");
            return;
        }

        Optional<ReportLine> line = reportManager.getLineById(lineId);
        System.out.println("Ошибка: строка с id=" + lineId + " не найдена");
        return;

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
