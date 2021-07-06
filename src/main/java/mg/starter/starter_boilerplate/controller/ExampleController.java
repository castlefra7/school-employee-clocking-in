/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.starter.starter_boilerplate.controller;

/**
 *
 * @author lacha
 */
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.Date;
import javax.servlet.ServletContext;
import mg.starter.starter_boilerplate.bl.test.Example;
import mg.human_resources.gen.FctGen;
import mg.starter.starter_boilerplate.rsc.ExampleAttr;
import mg.starter.starter_boilerplate.rsc.ExampleAttrClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author lacha
 */
@Controller
public class ExampleController {

    @Autowired
    ServletContext context;

    Logger logger = LoggerFactory.getLogger(ExampleController.class);
    
    /* GENERALIZED CRUD */
    @GetMapping("/form-example")
    public String formCrud() {
        return "form-example";
    }

    /* FILE UPLOAD */
    @GetMapping("/file-upload")
    public String fileUpload() {
        return "file-upload";
    }

    @PostMapping("/file-upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes attributes) {
        if (file.isEmpty()) {
            attributes.addFlashAttribute("error", "please select a file to upload");
            return "redirect:/exampleclass";
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            logger.info(context.getRealPath(""));
            Path path = Paths.get(fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex) {
            logger.error("erreur", ex);
        }
   
        return "file-upload";
    }

    /* END FILE UPLOAD */

 /* EXAMPLE CLASS */
    @GetMapping("/exampleclass")
    public String exampleClass(Model model) {
        try {
            ExampleAttrClass exampleAttr = new ExampleAttrClass();
            exampleAttr.setSalary(2500);
            exampleAttr.setDate_birth(FctGen.parseDt("1998-01-02 15:00"));
            model.addAttribute("exampleClassAttr", exampleAttr);
            //model.addAttribute("error", "There are no errors");
        } catch (Exception ex) {
            logger.info("Exception in GET exampleclass");
            logger.error(ex.getMessage());
        }

        return "form-exampleclass";
    }

    @PostMapping("/exampleclass")
    public String postExampleClass(@ModelAttribute ExampleAttrClass exampleAttr, Model model) {
        try {
            Example example = new Example(exampleAttr);
            System.out.println(exampleAttr.getDate_birth());
            System.out.println(example);
            example.insert();

            return "redirect:/exampleclass";
        } catch (Exception ex) {
            logger.info("Exception in POST exampleclass");
            logger.error(ex.getMessage());
            model.addAttribute("exampleClassAttr", exampleAttr);
            return "form-exampleclass";
        }

    }

    /* END EXAMPLE CLASS */
    @GetMapping("/example")
    public String test(Model model) {
        model.addAttribute("name", "John koukou");
        try {
            logger.info("This is a logger test");
        } catch (Exception ex) {
            logger.error(ex.toString());
        }
        ExampleAttr exampleAttr = new ExampleAttr();
        exampleAttr.setDate(new Date());
        model.addAttribute("exampleAttr", exampleAttr);
        return "example";
    }

    @GetMapping("/path-example/{name}")
    public String testPath(@PathVariable("name") String name, Model model) {
        logger.info(name);
        ExampleAttr exampleAttr = new ExampleAttr();
        exampleAttr.setDate(new Date());
        model.addAttribute("exampleAttr", exampleAttr);
        return "example";
    }

    @GetMapping("/param-example")
    public String testParam(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        logger.info(name);
        model.addAttribute("name", name);
        ExampleAttr exampleAttr = new ExampleAttr();
        exampleAttr.setDate(new Date());
        model.addAttribute("exampleAttr", exampleAttr);
        return "example";
    }

    @PostMapping("/post-example")
    public ModelAndView enregsitrerReservation(@ModelAttribute ExampleAttr exampleAttr, Model model) throws SQLException {
        logger.info("Example of a Post");
        // call a function here (Insert, update, delete, ...)
        logger.info(exampleAttr.getText() + " " + exampleAttr.getDate());
        return new ModelAndView("redirect:/example");
    }

}
