package ru.itmo.TolstovaUrsu.service;
import ru.itmo.TolstovaUrsu.model.Report;
import ru.itmo.TolstovaUrsu.model.ReportStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReportCollectionManager {
    private final ArrayList<Report> reportCollection=new ArrayList<>();

    public void addReport(Report report){
        //проверка id у каждого элемента коллекции
        for(Report r : reportCollection) {
            if(r.getId() == report.getId()) {
                throw new IllegalArgumentException("Отчет с id " + report.getId() + " уже существует");
            }
        }
        reportCollection.add(report);
    }

    public List<Report> getAllReports(){
        return Collections.unmodifiableList(reportCollection);
    }

    public long getReportNextId() {
        return  System.currentTimeMillis() + reportCollection.size();}

    public Report getById(long id) {
        for (Report report : reportCollection) {
            if (report.getId() == id) {
                return report;
            }
        }
        return null;
    }


    public Report update(long id, String name, ReportStatus status,
                String ownerUsername, String signedBy) {
            Report report = getById(id);

            if (report == null) {
                throw new IllegalArgumentException("Отчет с id " + id + " не найден");
            }

            report.setName(name);
            report.setOwnerUsername(ownerUsername);
            report.setStatus(status);
            report.setSignedBy(signedBy);
            report.setUpdatedAt(Instant.now());

            return report;
        }

        public boolean remove(long id) {
            return reportCollection.removeIf(report -> report.getId() == id);
    }
}

