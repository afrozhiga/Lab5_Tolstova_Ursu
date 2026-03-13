package ru.itmo.TolstovaUrsu.model;

import java.time.Instant;
import java.util.Objects;

public final class ReportLine {
    // Уникальный номер строки отчёта. Программа назначает сама.
    private long repLineId;

    // К какому отчёту относится строка (id отчёта).
    // Должен ссылаться на реально существующий Report.
   private long reportId;

    // Параметр (PH/CONDUCTIVITY/...). Выбирается из списка MeasurementParam.
    private MeasurementParam param;

    // Значение (число).
    private double value;

    // Единицы (например "pH"). Нельзя пустое. До 16 символов.
    private String unit;

    // Когда строку добавили/обновили. Программа ставит автоматически.
    private Instant updatedAt;

    // Когда строка создана. Программа ставит автоматически.
    private Instant createdAt;

    public ReportLine(long repLineId, Report report, MeasurementParam param, double value, String unit, Instant updatedAt, Instant createdAt) {
        this.repLineId = repLineId;
        this.reportId = report.getId();
        this.param = param;
        this.value = value;
        this.unit = unit;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public long getRepLineId() {
        return repLineId;
    }

    public void setRepLineId(long repLineId) {
        this.repLineId = this.repLineId;
    }

    public long getReportId() {
        return reportId;
    }

    public void setReportId(long reportId) {
        this.reportId = reportId;
    }

    public MeasurementParam getParam() {
        return param;
    }

    public void setParam(MeasurementParam param) {
        this.param = param;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        if(param==MeasurementParam.PH) {
            if (value >= 0 && value <= 14) {
                this.value = value;
            } else {
                throw new IllegalArgumentException("Значение pH варьируется от 0 до 14:" + value);
            }
        }
        else if(param==MeasurementParam.CONDUCTIVITY){
            if(value>0 && value<=20000){
            this.value = value;
            }
            else {
                throw new IllegalArgumentException("Неверно введено значение электропроводности:" + value);
            }
        }
        else if (param==MeasurementParam.SOLUBILITY) {
            if(value>0){
                this.value = value;
            }
            else {
                throw new IllegalArgumentException("Растворимость больше 0:" + value);
            }
        }

    }


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        if (param == MeasurementParam.PH) {
            if (unit == "pH") {
                this.unit = unit;
            } else {
                throw new IllegalArgumentException("величина pH–это pH:" + unit);
            }
        } else if (param == MeasurementParam.SOLUBILITY) {
            if (unit == "г/100г") {
                this.unit = unit;
            } else {
                throw new IllegalArgumentException("растворимость измеряется в 'г/100г':" + unit);
            }
        }
            else if (param == MeasurementParam.CONDUCTIVITY) {
                if (unit=="TDS" || unit=="См/м") {
                    this.unit = unit;
                }
                else{
                    throw new IllegalArgumentException("электропроводность измеряется в 'TDS' или 'См/м':" + unit);
                }
            }
    }


    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ReportLine that = (ReportLine) o;
        return repLineId == that.repLineId && reportId == that.reportId && Double.compare(value, that.value) == 0 && param == that.param && Objects.equals(unit, that.unit) && Objects.equals(updatedAt, that.updatedAt) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(repLineId, reportId, param, value, unit, updatedAt, createdAt);
    }

    @Override
    public String toString() {
        return "ReportLine{" +
                "repLineId=" + repLineId +
                ", reportId=" + reportId +
                ", param=" + param +
                ", value=" + value +
                ", unit='" + unit + '\'' +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                '}';
    }
}
