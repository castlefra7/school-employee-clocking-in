/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.human_resources.rsc;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author lacha
 */
public class EmployeeAttr {

    private int id;

    private String first_name;
    private String last_name;
    private int id_category;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date date_birth;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date date_begin_employment;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date date_end_employment;

    private int registration_number;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public int getId_category() {
        return id_category;
    }

    public void setId_category(int id_category) {
        this.id_category = id_category;
    }

    public Date getDate_birth() {
        return date_birth;
    }

    public void setDate_birth(Date date_birth) {
        this.date_birth = date_birth;
    }

    public Date getDate_begin_employment() {
        return date_begin_employment;
    }

    public void setDate_begin_employment(Date date_begin_employment) {
        this.date_begin_employment = date_begin_employment;
    }

    public Date getDate_end_employment() {
        return date_end_employment;
    }

    public void setDate_end_employment(Date date_end_employment) {
        this.date_end_employment = date_end_employment;
    }

    public int getRegistration_number() {
        return registration_number;
    }

    public void setRegistration_number(int registration_number) {
        this.registration_number = registration_number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
