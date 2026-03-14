package ru.itmo.TolstovaUrsu;

import ru.itmo.TolstovaUrsu.model.Experiment;
import ru.itmo.TolstovaUrsu.model.Report;
import ru.itmo.TolstovaUrsu.model.ReportStatus;
import ru.itmo.TolstovaUrsu.model.Sample;
import ru.itmo.TolstovaUrsu.service.ExpCollectionManager;
import ru.itmo.TolstovaUrsu.service.ReportCollectionManager;
import ru.itmo.TolstovaUrsu.service.SampleCollectionManager;

import java.time.Instant;
import java.util.List;

public class Main {
    public static void main(String[] args){
        //ввод эксперимента
        ExpCollectionManager expCollection = new ExpCollectionManager();
        long expNextId = expCollection.getExpNextId();
        Experiment experiment = new Experiment(expNextId, "титрование");

        //ввод образца
        SampleCollectionManager sampleCollection = new SampleCollectionManager();
        long sampleNextId = sampleCollection.getSampleNextId();
        Sample sample = new Sample(sampleNextId, "Нитрат калия");

        //ввод отчета 1
        ReportCollectionManager reportCollection = new ReportCollectionManager();
        long reportNextId = reportCollection.getReportNextId();
        Report report = new Report( reportNextId, "первый" , sample , ReportStatus.FINAL, "SYSTEM" , null, Instant.now(), null);
        reportCollection.addReport(report);

        long reportNextId2 = reportCollection.getReportNextId();
        Report report2 = new Report( reportNextId2, "второй" , experiment , ReportStatus.SIGNED, "SYSTEM" , "ментор" , Instant.now(), null);
        reportCollection.addReport(report2);

        List<Report> reports = reportCollection.getAllReports();
        reports.forEach(System.out::println);
        // reports.forEach(report -> System.out.println(report)); - полная форма

    }

}
