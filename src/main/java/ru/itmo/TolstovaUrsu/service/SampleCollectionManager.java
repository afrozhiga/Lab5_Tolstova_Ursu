package ru.itmo.TolstovaUrsu.service;

import ru.itmo.TolstovaUrsu.model.Sample;

import java.util.ArrayList;
import java.util.Optional;

public class SampleCollectionManager {
    private final ArrayList<Sample> samples = new ArrayList<>();
    private long idCounter = 1;

    public Sample add(String name, String owner) {
        Sample s = new Sample(idCounter++, name, owner);
        samples.add(s);
        return s;
    }

    public Optional<Sample> getById(long id) {
        return samples.stream().filter(s -> s.getId() == id).findFirst();
    }

    public ArrayList<Sample> getAll() {
        return new ArrayList<>(samples);
    }

    public void addSample(String водаИзРеки) {
    }

    public Optional<Sample> getSampleById(long sampleId) {
        return Optional.empty();
    }
}
