package ru.itmo.TolstovaUrsu.cli;

import ru.itmo.TolstovaUrsu.command.*;
import ru.itmo.TolstovaUrsu.service.ExpCollectionManager;
import ru.itmo.TolstovaUrsu.service.ReportCollectionManager;
import ru.itmo.TolstovaUrsu.service.SampleCollectionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CommandInterpreter {
    private Map<String, Command> commands = Map.of();
    private final Scanner scanner;
    private boolean running;

    public CommandInterpreter(ReportCollectionManager reportManager,
                              SampleCollectionManager sampleManager) {
        this.scanner = new Scanner(System.in);
        this.commands = new HashMap<>();
        this.running = true;

        // Создаём все команды с обоими менеджерами
        ExpCollectionManager expManager = new ExpCollectionManager();
        commands.put("rep_create_sample",
                new RepCreateSampleCommand(reportManager, sampleManager, scanner));
        commands.put("rep_addline",
                new RepAddLineCommand(reportManager, sampleManager, scanner));
        commands.put("rep_list",
                new RepListCommand(reportManager, sampleManager, scanner));
        commands.put("rep_show",
                new RepShowCommand(reportManager, sampleManager, scanner));
        commands.put("rep_lines",
                new RepLinesCommand(reportManager, sampleManager, scanner));
        commands.put("rep_updateline",
                new RepUpdateLineCommand(reportManager, sampleManager, scanner));
        commands.put("rep_delline",
                new RepDelLineCommand(reportManager, sampleManager, scanner));
        commands.put("rep_finalize",
                new RepFinalizeCommand(reportManager, sampleManager, scanner));
        Command repSign = commands.put("rep_sign", new RepSignCommand(reportManager, sampleManager, scanner));
        commands.put("rep_export",
                new RepExportCommand(reportManager, sampleManager, scanner));
    }

    public void start() {
        System.out.println("=== Система управления отчётами по анализам ===");
        System.out.println("Введите 'help' для списка команд\n");

        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            executeCommand(input);
        }
    }

    private void executeCommand(String input) {
        String[] parts = input.split("\\s+", 2);
        String commandName = parts[0].toLowerCase();
        String[] args = parts.length > 1 ? parts[1].split("\\s+") : new String[0];

        if ("help".equals(commandName)) {
            printHelp();
        } else if ("exit".equals(commandName)) {
            running = false;
        } else {
            Command command = commands.get(commandName);
            if (command != null) {
                command.execute(args);
            } else {
                System.out.println("Ошибка: неизвестная команда '" + commandName + "'. Введите 'help'.");
            }
        }
    }

    private void printHelp() {
        System.out.println("""
                Доступные команды:
                  rep_create_sample <sample_id>           — создать отчёт по образцу (интерактивно)
                  rep_addline <report_id>                  — добавить строку в отчёт (интерактивно)
                  rep_list [--status DRAFT|FINAL|SIGNED]   — список отчётов
                  rep_show <report_id>                     — карточка отчёта
                  rep_lines <report_id>                    — строки отчёта
                  rep_delline <line_id>                    — удалить строку (только DRAFT)
                  rep_finalize <report_id>                 — перевести отчёт в FINAL
                  rep_sign <report_id>                     — подписать отчёт (только FINAL)
                  rep_export <report_id>                   — текстовый экспорт отчёта
                  help                                     — эта справка
                  exit                                     — выход
                """);
    }

    public void run() {
    }
}