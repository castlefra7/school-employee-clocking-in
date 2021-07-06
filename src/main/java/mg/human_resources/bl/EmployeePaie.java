    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.human_resources.bl;

import java.util.List;

/**
 *
 * @author lacha
 */
public class EmployeePaie {
    private List<EmployeeWeeklyHoursAndAmount> paie;
    private double[] amounts;

    public List<EmployeeWeeklyHoursAndAmount> getPaie() {
        return paie;
    }

    public void setPaie(List<EmployeeWeeklyHoursAndAmount> paie) {
        this.paie = paie;
    }

    public double[] getAmounts() {
        return amounts;
    }

    public void setAmounts(double[] amounts) {
        this.amounts = amounts;
    }
    
    
}
