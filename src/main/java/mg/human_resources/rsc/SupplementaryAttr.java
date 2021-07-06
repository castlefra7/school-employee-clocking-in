/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.human_resources.rsc;

import java.sql.Timestamp;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author lacha
 */
public class SupplementaryAttr {

    private int id;
    private String code;
    private int max_hour_per_period;
    private String period_type;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date created_date;
    private double percentage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getMax_hour_per_period() {
        return max_hour_per_period;
    }

    public void setMax_hour_per_period(int max_hour_per_period) {
        this.max_hour_per_period = max_hour_per_period;
    }

    public String getPeriod_type() {
        return period_type;
    }

    public void setPeriod_type(String period_type) {
        this.period_type = period_type;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

}
