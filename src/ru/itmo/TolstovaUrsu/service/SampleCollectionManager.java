package ru.itmo.TolstovaUrsu.service;

import ru.itmo.TolstovaUrsu.model.Sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SampleCollectionManager {
    private final ArrayList<Sample> sampleCollection=new ArrayList<>();

    public  void addSample(Sample sample){

        for(Sample s : sampleCollection) {
            if(s.getSampleId() == sample.getSampleId()) {
                throw new IllegalArgumentException("Образец с id " + sample.getSampleId() + " уже существует");

            }
        }
        sampleCollection.add(sample);
    }

    public List<Sample> getAllSample(){
        return Collections.unmodifiableList(sampleCollection);
    }

    public long getSampleNextId() {
        return System.currentTimeMillis() + sampleCollection.size();
    }

    public Sample getById(long id) {
        for (Sample sample : sampleCollection) {
            if (sample.getSampleId() == id) {
                return sample;
            }
        }
        return null;
    }

    public Sample update(long id, String name) {
        Sample sample = getById(id);

        if (sample == null) {
            throw new IllegalArgumentException(
                    "Образец с id " + id + " не найден");
        }

        sample.setSampleName(name);

        return sample;
    }

    public boolean remove(long id) {
        return sampleCollection.removeIf(sample -> sample.getSampleId() == id);
    }

    public int size() {
        return sampleCollection.size();
    }
}
