/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.starter.starter_boilerplate;

import mg.human_resources.bl.Employee;
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
        Employee emp = (Employee)new Employee().findById(1);
        //boxable.drawEmployeeInf(emp);
        boxable.drawTablePointings(null);    
        boxable.save();

    }

}
