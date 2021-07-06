/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.human_resources.controllers;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import mg.human_resources.bl.Employee;

import mg.human_resources.bl.EmployeeCategory;
import mg.human_resources.bl.Majorer;
import mg.human_resources.bl.MaxSupplConfig;
import mg.human_resources.bl.Supplementary;
import mg.human_resources.rsc.EmployeeAttr;
import mg.human_resources.rsc.MajorerAttr;
import mg.human_resources.rsc.SupplementaryAttr;
import mg.human_resources.security.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author lacha
 */
@Controller
public class AdminController {

    Logger logger = LoggerFactory.getLogger(AdminController.class);

    private int count_per_page = 5;

    /* STATS */
    @GetMapping("/stats")
    public String getStats(Model model, @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "id-semaine", required = false, defaultValue = "1") int id_semaine) throws Exception {
        Employee emp = new Employee();
        emp.setId_semaine(id_semaine);
        emp.setIsStat(true);
        model.addAttribute("id_semaine", id_semaine);
        model.addAttribute("employees", emp.findAllPage(null, page, count_per_page));
        model.addAttribute("statsHours", emp.sumHoursAndAmount());
        model.addAttribute("numberpages", emp.numberPage(count_per_page));
        model.addAttribute("page", page);
        return "employees-stats";
    }

    /* END STATS */

 /* CRUD SUPPL MAX  */
    @GetMapping("/supplmax/{id}")
    public String getSupplMaxDetail(@PathVariable("id") int id, Model model) throws Exception {
        model.addAttribute("supplAttr", new MaxSupplConfig().findById(id));
        model.addAttribute("supplmaxes", new MaxSupplConfig().findAll());
        model.addAttribute("isupdate", true);
        return "supplmax-form-back";
    }

    @GetMapping("/supplmax-form")
    public String getSupplMaxForm(Model model) throws Exception {
        MaxSupplConfig maj = new MaxSupplConfig();

        model.addAttribute("supplAttr", maj);
        model.addAttribute("supplmaxes", new MaxSupplConfig().findAll());
        return "supplmax-form-back";
    }

    @PostMapping("/supplmax-update")
    public String updateSupplMax(@ModelAttribute MaxSupplConfig attr, Model model, RedirectAttributes redirect) throws Exception {
        try {
            attr.update();
            return "redirect:/supplmax";
        } catch (Exception ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
            return "redirect:/supplmax/" + attr.getId();
        }

    }

    @PostMapping("/supplmax")
    public String postSupplMax(@ModelAttribute MaxSupplConfig attr, Model model, RedirectAttributes redirect) throws Exception {
        try {
            attr.insert();
            return "redirect:/supplmax";
        } catch (Exception ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
            return "redirect:/supplmax-form";
        }
    }

    @GetMapping("/supplmax")
    public String getSupplMax(Model model, @RequestParam(name = "page", required = false, defaultValue = "0") int page) throws Exception {
        model.addAttribute("supplmaxes", new MaxSupplConfig().findAllPage(null, page, count_per_page));
        model.addAttribute("numberpages", new MaxSupplConfig().numberPage(count_per_page));
        model.addAttribute("page", page);
        return "supplmax-list-back";
    }

    /* END CRUD SUPPL MAX  */

 /* CRUD MAJORER  */
    @GetMapping("/majorees/{id}")
    public String getMajorerDetail(@PathVariable("id") int id, Model model) throws Exception {
        model.addAttribute("majAttr", new Majorer().findById(id));
        model.addAttribute("majorees", new Majorer().findAll());
        model.addAttribute("isupdate", true);
        return "majorees-form-back";
    }

    @GetMapping("/majorees-form")
    public String getMajorerForm(Model model) throws Exception {
        MajorerAttr maj = new MajorerAttr();
        maj.setCode("hm60");
        maj.setMajorer_type("nuit");
        maj.setPercentage(0.6);

        model.addAttribute("majAttr", maj);
        model.addAttribute("majorees", new Majorer().findAll());
        return "majorees-form-back";
    }

    @PostMapping("/majorees-update")
    public String updateMajorees(@ModelAttribute MajorerAttr attr, Model model, RedirectAttributes redirect) throws Exception {
        try {
            Majorer maj = new Majorer(attr);
            maj.setId(attr.getId());
            maj.update();
            return "redirect:/majorees";
        } catch (Exception ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
            return "redirect:/majorees/" + attr.getId();
        }

    }

    @PostMapping("/majorees")
    public String postMajorees(@ModelAttribute MajorerAttr attr, Model model, RedirectAttributes redirect) throws Exception {
        try {
            Majorer maj = new Majorer(attr);
            maj.insert();
            return "redirect:/majorees";
        } catch (Exception ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
            return "redirect:/majorees-form";
        }
    }

    @GetMapping("/majorees")
    public String getMajorees(Model model, @RequestParam(name = "page", required = false, defaultValue = "0") int page) throws Exception {
        model.addAttribute("majorees", new Majorer().findAllPage(null, page, count_per_page));
        model.addAttribute("numberpages", new Majorer().numberPage(count_per_page));
        model.addAttribute("page", page);
        return "majorees-list-back";
    }

    /* END CRUD MAJORER */

 /* CRUD SUPPLEMENTARY */
    @GetMapping("/supplementaries/{id}")
    public String getSupplementaryDetail(@PathVariable("id") int id, Model model) throws Exception {
        model.addAttribute("supplAttr", new Supplementary().findById(id));
        model.addAttribute("supplementaries", new Supplementary().findAll());
        model.addAttribute("isupdate", true);
        return "supplementary-form-back";
    }

    @GetMapping("/supplementaries-form")
    public String getSupplementaryForm(Model model) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdf.parse("01/07/1995");

        SupplementaryAttr suppl = new SupplementaryAttr();
        suppl.setCode("HS15");
        suppl.setMax_hour_per_period(2);
        suppl.setPercentage(0.2);
        suppl.setPeriod_type("semaine");
        model.addAttribute("supplAttr", suppl);
        model.addAttribute("supplementaries", new EmployeeCategory().findAll());
        return "supplementary-form-back";
    }

    @PostMapping("/supplementaries-update")
    public String updateSupplementaries(@ModelAttribute SupplementaryAttr attr, Model model, RedirectAttributes redirect) throws Exception {
        try {
            Supplementary suppl = new Supplementary(attr);
            logger.info(attr.getPeriod_type());
            suppl.setId(attr.getId());
            suppl.update();
            return "redirect:/supplementaries";
        } catch (Exception ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
            return "redirect:/supplementaries/" + attr.getId();
        }

    }

    @PostMapping("/supplementaries")
    public String postSupplementaries(@ModelAttribute SupplementaryAttr attr, Model model, RedirectAttributes redirect) throws Exception {
        try {
            Supplementary suppl = new Supplementary(attr);
            suppl.insert();
            return "redirect:/supplementaries";
        } catch (Exception ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
            return "redirect:/supplementaries-form";
        }
    }

    @GetMapping("/supplementaries")
    public String getSupplementaries(Model model, @RequestParam(name = "page", required = false, defaultValue = "0") int page) throws Exception {
        model.addAttribute("supplementaries", new Supplementary().findAllPage(null, page, count_per_page));
        model.addAttribute("numberpages", new Supplementary().numberPage(count_per_page));
        model.addAttribute("page", page);
        return "supplementary-list-back";
    }

    /* END CRUD */

 /* CRUD EMPLOYEES */
    @GetMapping("/employees/{id}")
    public String getEmployeeDetail(@PathVariable("id") int id, Model model) throws Exception {
        model.addAttribute("employeeAttr", new Employee().findById(id));
        model.addAttribute("categories", new EmployeeCategory().findAll());
        model.addAttribute("isupdate", true);
        return "employees-form-back";
    }

    @GetMapping("/employees-form")
    public String getEmployeesForm(Model model) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdf.parse("01/07/1995");

        EmployeeAttr emp = new EmployeeAttr();
        emp.setFirst_name("lala");
        emp.setLast_name("ramaro");
        emp.setId_category(1);
        emp.setDate_birth(date);
        date = sdf.parse("02/01/2021");
        emp.setDate_begin_employment(date);

        model.addAttribute("employeeAttr", emp);
        model.addAttribute("categories", new EmployeeCategory().findAll());
        return "employees-form-back";
    }

    @PostMapping("/employees-update")
    public String updateEmployees(@ModelAttribute EmployeeAttr attr, Model model, RedirectAttributes redirect) throws Exception {
        try {
            Employee emp = new Employee(attr);
            emp.setId(attr.getId());
            emp.update();
            return "redirect:/employees";
        } catch (Exception ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
            return "redirect:/employees/" + attr.getId();
        }

    }

    @PostMapping("/employees")
    public String postEmployees(@ModelAttribute EmployeeAttr attr, Model model, RedirectAttributes redirect) throws Exception {
        try {
            Employee emp = new Employee(attr);
            emp.insert();
            return "redirect:/employees";
        } catch (Exception ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
            return "redirect:/employees-form";
        }
    }

    @GetMapping("/employees")
    public String getEmployees(Model model, @RequestParam(name = "page", required = false, defaultValue = "0") int page) throws Exception {
        model.addAttribute("employees", new Employee().findAllPage(null, page, count_per_page));
        model.addAttribute("numberpages", new Employee().numberPage(count_per_page));
        model.addAttribute("page", page);
        return "employees-list-back";
    }

    /* END CRUD */

 /* CRUD CATEGORIES */
    @GetMapping("/categories/{id}")
    public String getCategoryDetail(@PathVariable("id") int id, Model model) throws Exception {
        model.addAttribute("categoryAttr", new EmployeeCategory().findById(id));
        model.addAttribute("isupdate", true);
        return "categories-form-back";
    }

    @GetMapping("/categories-form")
    public String getCategoriesForm(Model model) throws Exception {
        EmployeeCategory categ = new EmployeeCategory();
        categ.setName("chauffeur");
        categ.setStandard_hour_per_day(8);
        categ.setDay_week_start(1);
        categ.setDay_week_end(6);
        categ.setStandard_salary(125000);
        model.addAttribute("categoryAttr", categ);
        return "categories-form-back";
    }

    @PostMapping("/categories-update")
    public String updateCategories(@ModelAttribute EmployeeCategory categ, Model model, RedirectAttributes redirect) throws Exception {
        try {
            categ.update();
            return "redirect:/categories";
        } catch (Exception ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
            return "redirect:/categories";
        }

    }

    @PostMapping("/categories")
    public String postCategories(@ModelAttribute EmployeeCategory categ, Model model, RedirectAttributes redirect) throws Exception {
        try {
            categ.insert();
            return "redirect:/categories";
        } catch (Exception ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
            return "redirect:/categories-form";
        }
    }

    @GetMapping("/categories")
    public String getCategories(Model model, @RequestParam(name = "page", required = false, defaultValue = "0") int page) throws Exception {
        model.addAttribute("categories", new EmployeeCategory().findAllPage(null, page, count_per_page));
        model.addAttribute("numberpages", new EmployeeCategory().numberPage(count_per_page));
        model.addAttribute("page", page);
        return "categories-list-back";
    }

    /* END CRUD */
    @PostMapping("/users-update")
    public String updateUser(@ModelAttribute User user, RedirectAttributes redirect) {
        try {
            user.update();
            return "redirect:/users";
        } catch (Exception ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
            return "redirect:/users/" + user.getId();
        }
    }

    @GetMapping("/users/{id}")
    public String updateUserForm(@PathVariable("id") int id, Model model) throws Exception {
        model.addAttribute("userAttr", new User().findById(id));
        model.addAttribute("isupdate", true);
        return "user-form";
    }

    @PostMapping("/users")
    public String postUser(@ModelAttribute User user, RedirectAttributes redirect) {
        try {
            user.insert();
            return "redirect:/users";
        } catch (Exception ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
            return "redirect:/users-form";
        }

    }

    @GetMapping("/users-form")
    public String getUsersForm(Model model) {
        model.addAttribute("userAttr", new User());
        return "user-form";
    }

    @GetMapping("/users")
    public String getUsers(Model model) throws Exception {
        model.addAttribute("users", new User().findAll());
        return "user-list";
    }

}
