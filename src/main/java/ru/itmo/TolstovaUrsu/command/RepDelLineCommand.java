package ru.itmo.TolstovaUrsu.command;

import ru.itmo.TolstovaUrsu.model.ReportLine;
import ru.itmo.TolstovaUrsu.service.ReportCollectionManager;
import ru.itmo.TolstovaUrsu.service.SampleCollectionManager;

import java.util.Optional;
import java.util.Scanner;

public class RepDelLineCommand extends Command {

    public RepDelLineCommand(ReportCollectionManager reportManager,
                             SampleCollectionManager sampleManager,
                             Scanner scanner) {
        super(reportManager, sampleManager, scanner);
    }

    @Override
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
        if (line.isEmpty()) {
            System.out.println("Ошибка: строка с id=" + lineId + " не найдена");
            return;
        }

        reportManager.deleteLine(lineId);
        System.out.println("Строка " + lineId + " успешно удалена");
    }

    @Override
    public String getDescription() {
        return "удалить строку отчета";
    }

    @Override
    public String getName() {
        return "rep_delline";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RepDelLineCommand;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "RepDelLineCommand{}";
    }
}

@Override
public String toString() {
    return "RepUpdateLineCommand{lineId=" + lineId + "}";
}
