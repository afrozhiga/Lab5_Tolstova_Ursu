package ru.itmo.TolstovaUrsu.cli;

import ru.itmo.TolstovaUrsu.domain.MeasurementParam;
import ru.itmo.TolstovaUrsu.domain.Report;
import ru.itmo.TolstovaUrsu.domain.ReportLine;
import ru.itmo.TolstovaUrsu.domain.ReportStatus;
import ru.itmo.TolstovaUrsu.service.ReportService;
import ru.itmo.TolstovaUrsu.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommandInterpreter {

    private final ReportService service;
    private final Scanner scanner;

    public CommandInterpreter(ReportService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    public void run() {
        System.out.println("Введите help для списка команд, exit для выхода.");
        System.out.println();

        while (true) {
            System.out.print("> ");
            String rawLine = scanner.nextLine().trim();

            if (rawLine.isBlank()) continue;

            String[] tokens = tokenize(rawLine);
            if (tokens.length == 0) continue;

            String command = tokens[0].toLowerCase();

            try {
                switch (command) {
                    case "help"              -> doHelp();
                    case "exit"              -> { System.out.println("До свидания!"); return; }
                    case "rep_create_sample" -> doRepCreateSample(tokens);
                    case "rep_addline"       -> doRepAddLine(tokens);
                    case "rep_list"          -> doRepList(tokens);
                    case "rep_show"          -> doRepShow(tokens);
                    case "rep_lines"         -> doRepLines(tokens);
                    case "rep_updateline"    -> doRepUpdateLine(tokens);
                    case "rep_delline"       -> doRepDelLine(tokens);
                    case "rep_finalize"      -> doRepFinalize(tokens);
                    case "rep_sign"          -> doRepSign(tokens);
                    case "rep_export"        -> doRepExport(tokens);
                    default -> System.out.println(
                            "Неизвестная команда: '" + command + "'. Введите 'help' для списка команд.");
                }
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: ожидалось числовое значение id.");
            }
        }
    }


    private void doHelp() {
        System.out.println("Доступные команды:");
        System.out.println("  rep_create_sample <sample_id>            — создать отчёт по образцу");
        System.out.println("  rep_addline <report_id>                  — добавить строку в отчёт");
        System.out.println("  rep_list [--status DRAFT|FINAL|SIGNED]   — список отчётов");
        System.out.println("  rep_show <report_id>                     — карточка отчёта");
        System.out.println("  rep_lines <report_id>                    — строки отчёта");
        System.out.println("  rep_updateline <line_id> field=value     — изменить строку (param, value, unit)");
        System.out.println("  rep_delline <line_id>                    — удалить строку");
        System.out.println("  rep_finalize <report_id>                 — перевести в FINAL");
        System.out.println("  rep_sign <report_id>                     — подписать отчёт (только FINAL)");
        System.out.println("  rep_export <report_id>                   — текстовый экспорт отчёта");
        System.out.println("  help                                     — эта справка");
        System.out.println("  exit                                     — выйти из программы");
    }

    private void doRepCreateSample(String[] tokens) throws ValidationException {
        if (tokens.length < 2) {
            System.out.println("Использование: rep_create_sample <sample_id>");
            return;
        }
        long sampleId = parseLongArg(tokens[1], "sample_id");

        String name = promptNonBlank("Название отчёта");

        Report report = service.createReportBySample(name, sampleId);
        System.out.println("OK report_id=" + report.getId());
    }

    private void doRepAddLine(String[] tokens) throws ValidationException {
        if (tokens.length < 2) {
            System.out.println("Использование: rep_addline <report_id>");
            return;
        }
        long reportId = parseLongArg(tokens[1], "report_id");

        MeasurementParam param = promptParam();

        double value = promptDouble("Значение");

        String unit = promptNonBlank("Единицы");

        ReportLine line = service.addLine(reportId, param, value, unit);
        System.out.println("OK line_id=" + line.getId());
    }

    private void doRepList(String[] tokens) throws ValidationException {
        ReportStatus statusFilter = null;

        for (int i = 1; i < tokens.length - 1; i++) {
            if ("--status".equalsIgnoreCase(tokens[i])) {
                statusFilter = ReportStatus.fromString(tokens[i + 1]);
                if (statusFilter == null) {
                    throw new ValidationException(
                            "Ошибка: неизвестный статус '" + tokens[i + 1]
                                    + "'. Используйте DRAFT, FINAL или SIGNED");
                }
            }
        }

        List<Report> list = service.listReports(statusFilter);
        if (list.isEmpty()) {
            System.out.println("Отчётов не найдено.");
            return;
        }

        printTableHeader("%-6s %-30s %-10s", "ID", "Name", "Status");
        printDivider(50);
        for (Report r : list) {
            System.out.printf("%-6d %-30s %-10s%n",
                    r.getId(), truncate(r.getName(), 30), r.getStatus());
        }
    }

    private void doRepShow(String[] tokens) throws ValidationException {
        if (tokens.length < 2) {
            System.out.println("Использование: rep_show <report_id>");
            return;
        }
        long reportId = parseLongArg(tokens[1], "report_id");
        Report report = service.getById(reportId)
                .orElseThrow(() -> new ValidationException(
                        "Ошибка: отчёт с id=" + reportId + " не найден"));

        long lineCount = service.countLinesForReport(reportId);
        System.out.println("Report #" + report.getId());
        System.out.println("  name:       " + report.getName());
        System.out.println("  status:     " + report.getStatus());
        if (report.getSampleId() > 0)     System.out.println("  sample_id:  " + report.getSampleId());
        if (report.getExperimentId() > 0) System.out.println("  exp_id:     " + report.getExperimentId());
        System.out.println("  owner:      " + report.getOwnerUsername());
        if (report.getSignedBy() != null)  System.out.println("  signed_by:  " + report.getSignedBy());
        System.out.println("  lines:      " + lineCount);
        System.out.println("  created:    " + report.getCreatedAt());
    }

    private void doRepLines(String[] tokens) throws ValidationException {
        if (tokens.length < 2) {
            System.out.println("Использование: rep_lines <report_id>");
            return;
        }
        long reportId = parseLongArg(tokens[1], "report_id");
        List<ReportLine> lines = service.getLinesForReport(reportId);

        if (lines.isEmpty()) {
            System.out.println("Строк в отчёте нет.");
            return;
        }

        printTableHeader("%-6s %-15s %-12s %-10s", "ID", "Param", "Value", "Unit");
        printDivider(47);
        for (ReportLine l : lines) {
            System.out.printf("%-6d %-15s %-12.4f %-10s%n",
                    l.getId(), l.getParam(), l.getValue(), l.getUnit());
        }
    }


    private void doRepUpdateLine(String[] tokens) throws ValidationException {
        if (tokens.length < 3) {
            System.out.println("Использование: rep_updateline <line_id> field=value [field=value ...]");
            System.out.println("Разрешённые поля: param, value, unit");
            return;
        }
        long lineId = parseLongArg(tokens[1], "line_id");

        for (int i = 2; i < tokens.length; i++) {
            String[] pair = tokens[i].split("=", 2);
            if (pair.length < 2) {
                System.out.println("Ошибка: неверный формат '" + tokens[i] + "', ожидается field=value");
                return;
            }
            service.updateLine(lineId, pair[0], pair[1]);
        }
        System.out.println("OK");
    }


    private void doRepDelLine(String[] tokens) throws ValidationException {
        if (tokens.length < 2) {
            System.out.println("Использование: rep_delline <line_id>");
            return;
        }
        long lineId = parseLongArg(tokens[1], "line_id");
        service.deleteLine(lineId);
        System.out.println("OK deleted");
    }

    private void doRepFinalize(String[] tokens) throws ValidationException {
        if (tokens.length < 2) {
            System.out.println("Использование: rep_finalize <report_id>");
            return;
        }
        long reportId = parseLongArg(tokens[1], "report_id");
        service.finalizeReport(reportId);
        System.out.println("OK report " + reportId + " FINAL");
    }


    private void doRepSign(String[] tokens) throws ValidationException {
        if (tokens.length < 2) {
            System.out.println("Использование: rep_sign <report_id>");
            return;
        }
        long reportId = parseLongArg(tokens[1], "report_id");

        String signer = promptNonBlank("Подпись (ваш логин)");

        service.signReport(reportId, signer);
        System.out.println("OK report " + reportId + " SIGNED by " + signer);
    }

    private void doRepExport(String[] tokens) throws ValidationException {
        if (tokens.length < 2) {
            System.out.println("Использование: rep_export <report_id>");
            return;
        }
        long reportId = parseLongArg(tokens[1], "report_id");
        Report report = service.getById(reportId)
                .orElseThrow(() -> new ValidationException(
                        "Ошибка: отчёт с id=" + reportId + " не найден"));
        List<ReportLine> lines = service.getLinesForReport(reportId);

        System.out.println("========================================");
        System.out.println("ОТЧЁТ ПО АНАЛИЗАМ");
        System.out.println("========================================");
        System.out.println("ID:       " + report.getId());
        System.out.println("Название: " + report.getName());
        System.out.println("Статус:   " + report.getStatus());
        if (report.getSampleId() > 0)     System.out.println("Образец:  " + report.getSampleId());
        if (report.getExperimentId() > 0) System.out.println("Эксп-т:   " + report.getExperimentId());
        System.out.println("Автор:    " + report.getOwnerUsername());
        if (report.getSignedBy() != null)  System.out.println("Подписан: " + report.getSignedBy());
        System.out.println("Создан:   " + report.getCreatedAt());
        System.out.println("----------------------------------------");
        System.out.printf("%-6s %-15s %-12s %-10s%n", "ID", "Параметр", "Значение", "Единицы");
        System.out.println("----------------------------------------");
        for (ReportLine l : lines) {
            System.out.printf("%-6d %-15s %-12.4f %-10s%n",
                    l.getId(), l.getParam(), l.getValue(), l.getUnit());
        }
        System.out.println("========================================");
    }


    /** Запрашивает строку и повторяет вопрос, пока ввод не будет непустым.*/
    private String promptNonBlank(String label) {
        while (true) {
            System.out.print(label + ": ");
            String value = scanner.nextLine().trim();
            if (!value.isBlank()) {
                return value;
            }
            System.out.println("Ошибка: поле '" + label + "' не может быть пустым. Попробуйте ещё раз.");
        }
    }

    private MeasurementParam promptParam() {
        while (true) {
            System.out.print("Параметр (PH/CONDUCTIVITY/TURBIDITY/NITRATE): ");
            String raw = scanner.nextLine().trim();
            MeasurementParam param = MeasurementParam.fromString(raw);
            if (param != null) {
                return param;
            }
            System.out.println("Ошибка: неизвестный параметр '" + raw
                    + "'. Доступны: PH, CONDUCTIVITY, TURBIDITY, NITRATE. Попробуйте ещё раз.");
        }
    }


    private double promptDouble(String label) {
        while (true) {
            System.out.print(label + ": ");
            String raw = scanner.nextLine().trim();
            try {
                double val = Double.parseDouble(raw);
                if (Double.isNaN(val) || Double.isInfinite(val)) {
                    System.out.println("Ошибка: значение должно быть обычным числом. Попробуйте ещё раз.");
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: '" + raw + "' не является числом. Попробуйте ещё раз.");
            }
        }
    }

    /** Разбивает строку на токены с поддержкой двойных кавычек. */
    private String[] tokenize(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ' ' && !inQuotes) {
                if (!current.isEmpty()) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
            } else {
                current.append(c);
            }
        }
        if (!current.isEmpty()) tokens.add(current.toString());
        return tokens.toArray(new String[0]);
    }

    private long parseLongArg(String raw, String argName) throws ValidationException {
        try {
            return Long.parseLong(raw);
        } catch (NumberFormatException e) {
            throw new ValidationException(
                    "Ошибка: " + argName + " должен быть целым числом, получено: '" + raw + "'");
        }
    }

    private void printTableHeader(String fmt, Object... args) {
        System.out.printf(fmt + "%n", args);
    }

    private void printDivider(int len) {
        System.out.println("-".repeat(len));
    }

    private String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max - 3) + "...";
    }
}
