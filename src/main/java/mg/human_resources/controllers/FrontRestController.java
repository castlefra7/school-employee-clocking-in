/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.human_resources.controllers;

import mg.human_resources.bl.Employee;
import mg.human_resources.rsc.PointingAttr;
import mg.human_resources.rsc.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author lacha
 */
@RestController
public class FrontRestController {

    @PostMapping("/employees-pointage-front")
    public ResponseBody postEmployeesPointage(@RequestBody PointingAttr pointAttr) throws Exception {
        ResponseBody response = new ResponseBody();
        try {
            response.getData().add(new Employee().calculateHours(pointAttr));
        } catch (Exception ex) {
            response.getStatus().setMessage(ex.getMessage());
        }
        return response;
        //return "redirect:/employees-pointage-front/" + id;
    }

}
