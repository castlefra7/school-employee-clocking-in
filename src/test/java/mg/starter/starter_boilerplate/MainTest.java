/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.starter.starter_boilerplate;

import mg.human_resources.bl.Employee;
import mg.human_resources.bl.EmployeePaie;
import mg.human_resources.gen.PDFBoxable;

/**
 *
 * @author lacha
 */
public class MainTest {

    public static void main(String[] args) throws Exception {

        // PDFBoxable
        PDFBoxable boxable = new PDFBoxable();
        boxable.drawPageTitle();
        int _id_emp = 1;
        int _id_semaine = 1;
        boxable.id_semaine = _id_semaine;
        Employee emp = (Employee) new Employee().findById(_id_emp);
        boxable.drawEmployeeInf(emp);
        
        EmployeePaie empPaie = new Employee().calculatePaie(_id_emp, _id_semaine);
        boxable.drawTablePaie(empPaie);
        boxable.save();

    }

}
