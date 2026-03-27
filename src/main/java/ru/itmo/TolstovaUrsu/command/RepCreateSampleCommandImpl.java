package ru.itmo.TolstovaUrsu.command;

import ru.itmo.TolstovaUrsu.cli.RepCreateSampleCommand;
import ru.itmo.TolstovaUrsu.service.ExpCollectionManager;
import ru.itmo.TolstovaUrsu.service.ReportCollectionManager;
import ru.itmo.TolstovaUrsu.service.SampleCollectionManager;

import java.util.Scanner;

public class RepCreateSampleCommandImpl extends RepCreateSampleCommand {
    public RepCreateSampleCommandImpl(ReportCollectionManager reportManager, SampleCollectionManager sampleManager, ExpCollectionManager expManager, Scanner scanner) {
        super(reportManager, sampleManager, scanner);
    }
}
