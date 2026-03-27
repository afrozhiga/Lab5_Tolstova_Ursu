package ru.itmo.TolstovaUrsu.cli;

import ru.itmo.TolstovaUrsu.command.Command;
import ru.itmo.TolstovaUrsu.service.ReportCollectionManager;
import ru.itmo.TolstovaUrsu.service.SampleCollectionManager;

import java.util.Scanner;

public class RepUpdateLineCommand extends Command {
    public RepUpdateLineCommand(ReportCollectionManager reportManager, SampleCollectionManager sampleManager, Scanner scanner) {
        super(reportManager, sampleManager, scanner);
    }

    @Override
    public void execute(String[] args) {

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
