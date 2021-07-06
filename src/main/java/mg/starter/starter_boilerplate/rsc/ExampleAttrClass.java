/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.starter.starter_boilerplate.rsc;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author lacha
 */
public class ExampleAttrClass {
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date date_birth;
    private String name;
    private double salary;

    public Date getDate_birth() {
        return date_birth;
    }

    public void setDate_birth(Date date_birth) {
        this.date_birth = date_birth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
    
}
