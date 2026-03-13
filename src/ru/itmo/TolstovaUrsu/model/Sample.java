package ru.itmo.TolstovaUrsu.model;

public final class Sample {
        // Уникальный номер образца. Программа назначает сама.
    private final long SampleId;

        // Название образца. Нельзя пустое. Желательно до 128 символов.
    private String SampleName;

    public Sample(long sampleId, String sampleName) {
        SampleId = sampleId;
        SampleName = sampleName;
    }

    public long getSampleId() {
        return SampleId;
    }

    public String getSampleName() {
        return SampleName;
    }

    public void setSampleName(String sampleName) {
        if (SampleName != null && !SampleName.isEmpty() && SampleName.length() <= 128) {
            this.SampleName = SampleName;
        } else {
            throw new IllegalArgumentException("Неверно введено название отчета:" + SampleName);
        }
    }
}
