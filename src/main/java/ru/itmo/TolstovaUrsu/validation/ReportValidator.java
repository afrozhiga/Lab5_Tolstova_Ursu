package ru.itmo.TolstovaUrsu.validation;

public class ReportValidator {

    private static final int MAX_NAME_LENGTH = 128;
    private static final int MAX_UNIT_LENGTH = 16;

    public String validateName(String name) {
        if (name == null || name.isBlank()) {
            return "Ошибка: название отчёта не может быть пустым";
        }
        if (name.length() > MAX_NAME_LENGTH) {
            return "Ошибка: название слишком длинное (макс. " + MAX_NAME_LENGTH + ")";
        }
        return null;
    }

    public String validateUnit(String unit) {
        if (unit == null || unit.isBlank()) {
            return "Ошибка: единицы измерения не могут быть пустыми";
        }
        if (unit.length() > MAX_UNIT_LENGTH) {
            return "Ошибка: единицы слишком длинные (макс. " + MAX_UNIT_LENGTH + ")";
        }
        return null;
    }

    /* проверяет что строка является числом и возвращает значение
     */
    public double parseValue(String raw) throws ValidationException {
        try {
            double val = Double.parseDouble(raw);
            if (Double.isNaN(val) || Double.isInfinite(val)) {
                throw new ValidationException("Ошибка: значение должно быть обычным числом");
            }
            return val;
        } catch (NumberFormatException e) {
            throw new ValidationException("Ошибка: значение должно быть числом, получено: '" + raw + "'");
        }
    }
}
