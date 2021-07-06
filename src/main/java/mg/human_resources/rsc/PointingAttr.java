/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.human_resources.rsc;

import java.util.List;

/**
 *
 * @author lacha
 */
public class PointingAttr {

    private List<PointingDailyAttr> pointings;
    private EmployeeAttr employee;
    private int semaine;

    public int getSemaine() {
        return semaine;
    }

    public void setSemaine(int semaine) {
        this.semaine = semaine;
    }

    public EmployeeAttr getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeAttr employee) {
        this.employee = employee;
    }

    public List<PointingDailyAttr> getPointings() {
        return pointings;
    }

    public void setPointings(List<PointingDailyAttr> pointings) {
        this.pointings = pointings;
    }

}
