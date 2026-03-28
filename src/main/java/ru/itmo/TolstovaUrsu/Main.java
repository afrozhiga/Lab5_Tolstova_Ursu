package ru.itmo.TolstovaUrsu;

import ru.itmo.TolstovaUrsu.cli.CommandInterpreter;
import ru.itmo.TolstovaUrsu.service.ReportCollectionManager;
import ru.itmo.TolstovaUrsu.service.SampleCollectionManager;

public class Main {
    public static void main(String[] args) {
        ReportCollectionManager reportManager = new ReportCollectionManager();
        SampleCollectionManager sampleManager = new SampleCollectionManager();

        CommandInterpreter interpreter = new CommandInterpreter(reportManager, sampleManager);
        interpreter.start();
    }
}
