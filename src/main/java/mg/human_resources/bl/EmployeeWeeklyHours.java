/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.human_resources.bl;

/**
 *
 * @author lacha
 */
public class EmployeeWeeklyHours {

    private String code;
    private float hours;
    private double percentage;

    public EmployeeWeeklyHours() {

    }

    public EmployeeWeeklyHours(String _code, float _hours, double _percent) {
        this.setCode(_code);
        this.setHours(_hours);
        this.setPercentage(_percent);
    }

    public String getCode() {
        return code;
    }

    public final void setCode(String code) {
        this.code = code;
    }

    public float getHours() {
        return hours;
    }

    public final void setHours(float hours) {
        this.hours = hours;
    }

    public double getPercentage() {
        return percentage;
    }

    public final void setPercentage(double percentage) {
        this.percentage = percentage;
    }

}
