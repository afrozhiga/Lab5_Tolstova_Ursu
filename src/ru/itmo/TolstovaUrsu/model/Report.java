package ru.itmo.TolstovaUrsu.model;

import java.time.Instant;
import java.util.Objects;

public final class Report {
        // Уникальный номер отчёта. Программа назначает сама.
        private final long id;

        // Название отчёта. Нельзя пустое. До 128 символов.
        private String name;

        // На какой эксперимент относится (если это отчёт по эксперименту).Может быть 0, если отчёт по образцу.
        private long expId;

        // На какой образец относится (если это отчёт по образцу).Может быть 0, если отчёт по эксперименту.
        private long sampleId;

        // Статус отчёта: DRAFT, FINAL, SIGNED.
        private ReportStatus status;

        // Кто создал (логин). На ранних этапах можно "SYSTEM".
        private String ownerUsername;

        // Кто подписал (логин). Может быть null, если ещё не подписан.
        private String signedBy;

        // Когда создали. Программа ставит автоматически.
        private final Instant createdAt;

        // Когда обновляли. Программа обновляет автоматически.
        private Instant updatedAt;



    //полный отчет по образцу
    public Report(long id, String name, Sample sample,
                  ReportStatus status, String ownerUsername, String signedBy,
                  Instant createdAt, Instant updatedAt) {
        if (sample == null) {
            throw new IllegalArgumentException("Sample не может быть null");
        }

        this.id = id;
        this.setName(name);
        this.sampleId = sample.getSampleId();
        this.expId = 0;
        this.status = status;
        this.setOwnerUsername(ownerUsername);
        this.setSignedBy(signedBy);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    //полный отчет по эксперименту
    public Report(long id, String name, Experiment experiment,
                  ReportStatus status, String ownerUsername, String signedBy,
                  Instant createdAt, Instant updatedAt) {
        if (experiment == null) {
            throw new IllegalArgumentException("Experiment не может быть null");
        }

        this.id = id;
        this.setName(name);
        this.sampleId = 0;
        this.expId = experiment.getExpId();
        this.status = status;
        this.setOwnerUsername(ownerUsername);
        this.setSignedBy(signedBy);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }



    public String getName() {
        return name;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getSignedBy() {
        return signedBy;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        if (name != null && !name.isEmpty() && name.length() <= 128) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Неверно введено название отчета:" + name);
        }
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public void setOwnerUsername(String ownerUsername) {
        if (ownerUsername != null && !ownerUsername.isEmpty() && ownerUsername.length() <= 128) {
            this.ownerUsername = ownerUsername;
        }
        else {
            throw new IllegalArgumentException("Логин создателя не может быть пустым и длиннее 128 символов:" + ownerUsername);

        }
    }

    public void setSignedBy(String signedBy) {
        if (status == ReportStatus.SIGNED) {
            if (signedBy !=null && signedBy.length() <= 128) {
                this.signedBy = signedBy;
            } else {
                throw new IllegalArgumentException("Имя подписавшего не пустое и не длиннее 128 символов");
            }
        }
        else if (status == ReportStatus.DRAFT || status == ReportStatus.FINAL){
            if(signedBy==null){
                this.signedBy = signedBy;
            }
            else {
                throw new IllegalArgumentException("Документ еще не подписан");
            }
        }
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return id == report.id && expId == report.expId && sampleId == report.sampleId && Objects.equals(name, report.name) && status == report.status && Objects.equals(ownerUsername, report.ownerUsername) && Objects.equals(signedBy, report.signedBy) && Objects.equals(createdAt, report.createdAt) && Objects.equals(updatedAt, report.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, expId, sampleId, status, ownerUsername, signedBy, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", expId=" + expId +
                ", sampleId=" + sampleId +
                ", status=" + status +
                ", ownerUsername='" + ownerUsername + '\'' +
                ", signedBy='" + signedBy + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

