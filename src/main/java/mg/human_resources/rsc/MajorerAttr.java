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
public class MajorerAttr {

    private int id;
    private String majorer_type;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date created_date;
    private String code;
    private double percentage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMajorer_type() {
        return majorer_type;
    }

    public void setMajorer_type(String majorer_type) {
        this.majorer_type = majorer_type;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

}
