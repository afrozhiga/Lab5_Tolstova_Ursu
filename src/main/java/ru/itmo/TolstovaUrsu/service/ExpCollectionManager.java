package ru.itmo.TolstovaUrsu.service;

import ru.itmo.TolstovaUrsu.model.Experiment;
import ru.itmo.TolstovaUrsu.model.Report;
import ru.itmo.TolstovaUrsu.model.ReportLine;

import java.util.ArrayList;
import java.util.Optional;

public class ExpCollectionManager {
    private final ArrayList<Experiment> experiments = new ArrayList<>();
    private long idCounter = 1;

    public Experiment add(String name, String owner) {
        Experiment e = new Experiment(idCounter++, name, owner);
        experiments.add(e);
        return e;
    }

    public Optional<Experiment> getById(long id) {
        return experiments.stream().filter(e -> e.getId() == id).findFirst();
    }

    public ArrayList<Experiment> getAll() {
        return new ArrayList<>(experiments);
    }

    public void deleteReportLine(long lineId) {
    }

    public Report getReportById(long reportId) {
        return null;
    }

    public ReportLine getLineById(long lineId) {
        return null;
    }

    public void finalizeReport(long reportId) {

    }

    public void signReport(long reportId, String currentUser) {
    }

    public ArrayList<ReportLine> getLinesByReportId(long reportId) {

        return null;
    }
}
