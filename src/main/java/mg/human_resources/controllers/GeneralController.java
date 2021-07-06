/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.human_resources.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author lacha
 */
@Controller
public class GeneralController {
    @GetMapping("/")
    public String getHome() {
        return "example.html";
    }
    
    @GetMapping("/error")
    public String handleError() {
        return "error";
    }
}
