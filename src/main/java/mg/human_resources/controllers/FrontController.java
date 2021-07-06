/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.human_resources.controllers;

import mg.human_resources.bl.Employee;
import mg.human_resources.bl.EmployeeCategory;
import mg.human_resources.bl.EmployeePaie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author lacha
 */
@Controller
public class FrontController {
Logger logger = LoggerFactory.getLogger(AdminController.class);
    private int count_per_page = 1;
    private int id_semaine = 1;

    @GetMapping("/employees-fiche-front/{id}")
    public String getEmployeesFiche(Model model, @PathVariable("id") int id) throws Exception {
        model.addAttribute("employee", new Employee().findById(id));
        model.addAttribute("categories", new EmployeeCategory().findAll());
        EmployeePaie empPaie = new Employee().calculatePaie(id, id_semaine);
        
        logger.info(String.valueOf(empPaie.getPaie().size()));

        model.addAttribute("paies", empPaie.getPaie());
        model.addAttribute("amounts", empPaie.getAmounts());
        return "employees-fiche-front";
    }

    @GetMapping("/employees-pointage-front/{id}")
    public String getEmployeesPointage(Model model, @PathVariable("id") int id) throws Exception {
        model.addAttribute("employee", new Employee().findById(id));
        model.addAttribute("categories", new EmployeeCategory().findAll());
        return "employees-pointage-front";
    }

    @GetMapping("/employees-front")
    public String getEmployees(Model model, @RequestParam(name = "page", required = false, defaultValue = "0") int page) throws Exception {
        model.addAttribute("employees", new Employee().findAllPage(null, page, count_per_page));
        model.addAttribute("numberpages", new Employee().numberPage(count_per_page));
        model.addAttribute("page", page);
        return "employees-list-front";
    }
}
