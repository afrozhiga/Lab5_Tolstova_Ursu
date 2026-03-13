package ru.itmo.TolstovaUrsu.model;

public final class Experiment {
        // Уникальный номер эксперимента. Программа назначает сама.
    private final long expId;

        // Название эксперимента. Нельзя пустое. До 128 символов.
    private String expName;

    public Experiment(long expId, String expName) {
        this.expId = expId;
        this.expName = expName;
    }

    public long getExpId() {
        return expId;
    }

    public String getExpName() {
        return expName;
    }

    public void setExpName(String expName) {
        if (expName != null && !expName.isEmpty() && expName.length() <= 128) {
            this.expName = expName;
        } else {
            throw new IllegalArgumentException("Неверно введено название отчета:" + expName);
        }
    }


}
