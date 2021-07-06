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
public final class EmployeeWeeklyHours {

    private String code;
    private int hours;
    
    public EmployeeWeeklyHours() {
        
    }
    
    public EmployeeWeeklyHours(String _code, int _hours) {
        this.setCode(_code);
        this.setHours(_hours);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

}
