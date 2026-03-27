package ru.itmo.TolstovaUrsu.command;

import ru.itmo.TolstovaUrsu.service.ExpCollectionManager;
import ru.itmo.TolstovaUrsu.service.ReportCollectionManager;
import ru.itmo.TolstovaUrsu.service.SampleCollectionManager;

import java.util.Scanner;

public abstract class Command {
    protected ReportCollectionManager reportManager;
    protected SampleCollectionManager sampleManager;
    protected Scanner scanner;

    protected Command(ReportCollectionManager reportManager,
                      SampleCollectionManager sampleManager,
                      Scanner scanner) {
        this.reportManager = reportManager;
        this.sampleManager = sampleManager;
        this.scanner = scanner;
    }

    public Command(ExpCollectionManager manager) {
    }

    public Command(ReportCollectionManager reportManager, SampleCollectionManager sampleManager, ExpCollectionManager manager, Scanner scanner) {
    }

    public <sampleCollectionManager> Command(sampleCollectionManager reportManager, sampleCollectionManager sampleManager, Scanner scanner, ExpCollectionManager manager) {
    }

    public abstract void execute(String[] args);
    public abstract String getDescription();
    public abstract String getName();
}