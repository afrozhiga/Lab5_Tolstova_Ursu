package ru.itmo.TolstovaUrsu;

import ru.itmo.TolstovaUrsu.cli.CommandInterpreter;
import ru.itmo.TolstovaUrsu.service.ReportService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ReportService service = new ReportService();
        Scanner scanner = new Scanner(System.in);
        CommandInterpreter interpreter = new CommandInterpreter(service, scanner);
        interpreter.run();
        scanner.close();
    }
}
