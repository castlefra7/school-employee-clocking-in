/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.human_resources.controllers;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import mg.human_resources.bl.Employee;
import mg.human_resources.bl.EmployeeCategory;
import mg.human_resources.bl.EmployeePaie;
import mg.human_resources.gen.PDFBoxable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    private int count_per_page = 5;

    String uploadDir = "D:\\upload-dir\\fiche_paie.pdf";
    
    @GetMapping("/download-pdf/{filename:.+}")
    public ResponseEntity downloadPdf(@RequestParam(name = "id-emp", required = false, defaultValue = "0") int id_emp,
            @RequestParam(name = "id-semaine", required = false, defaultValue = "0") int id_semaine) throws Exception {
        new Employee().savePDF(uploadDir, id_emp);
        
        Path path = Paths.get(uploadDir);
        Resource resource;
        resource = new UrlResource(path.toUri());
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream")).header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename:\"" + resource.getFilename() + "\"").body(resource);
    }
    
    
    @GetMapping("/employees-fiche-front/{id}")
    public String getEmployeesFiche(Model model, @PathVariable("id") int id, 
            @RequestParam(name = "id-semaine", required = false, defaultValue = "1") int id_semaine) throws Exception {
        model.addAttribute("employee", new Employee().findById(id));
        model.addAttribute("categories", new EmployeeCategory().findAll());
        model.addAttribute("id_emp", id);
        model.addAttribute("id_semaine", id_semaine);
        
        EmployeePaie empPaie = new Employee().calculatePaie(id, id_semaine);
        
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
