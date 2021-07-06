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
public class EmployeeWeeklyHoursAndAmount extends EmployeeWeeklyHours {

    private double hourlyRate;
    private double totalAmount;
    
    
    public EmployeeWeeklyHoursAndAmount(String _code, float _hours, double _percent) {
        this.setCode(_code);
        this.setHours(_hours);
        this.setPercentage(_percent);
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

}
