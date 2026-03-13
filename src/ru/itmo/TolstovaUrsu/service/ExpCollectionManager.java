package ru.itmo.TolstovaUrsu.service;

import ru.itmo.TolstovaUrsu.model.Experiment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExpCollectionManager {
    private final ArrayList<Experiment> expCollection=new ArrayList<>();

    public void addExp(Experiment experiment){
        //проверка id у каждого элемента коллекции
        for(Experiment exp : expCollection) {
            if(exp.getExpId() == experiment.getExpId()) {
                throw new IllegalArgumentException("Эксперимент с id " + experiment.getExpId() + " уже существует");
            }
        }
        expCollection.add(experiment);
    }

    public List<Experiment> getAllExp(){
        return Collections.unmodifiableList(expCollection);
    }

    public long getExpNextId() {
        return System.currentTimeMillis() + expCollection.size();
    }

        public Experiment getById(long id) {
            for (Experiment experiment : expCollection) {
                if (experiment.getExpId() == id) {
                    return experiment;
                }
            }
            return null;
        }

        public Experiment update(long id, String newName) {
            Experiment experiment = getById(id);

            if (experiment == null) {
                throw new IllegalArgumentException(
                        "Эксперимент с id " + id + " не найден");
            }

            experiment.setExpName(newName);

            return experiment;
        }

        public boolean remove(long id) {
            return expCollection.removeIf(exp -> exp.getExpId() == id);
        }

        public int size() {
            return expCollection.size();
    }
}

