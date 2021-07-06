/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.starter.starter_boilerplate.controller;

import mg.human_resources.conn.ExampleAccess;
import mg.starter.starter_boilerplate.rsc.ExampleJSON;
import mg.human_resources.rsc.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lacha
 */
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class ExampleRestController {
    
    Logger logger = LoggerFactory.getLogger(ExampleRestController.class);

    
    @GetMapping("/jsonexample")
    public ResponseBody jsonExample() {
        ResponseBody response = new ResponseBody();
        response.getStatus().setMessage("Coucou");
        return response;
    }
    
    @PostMapping("/jsonexample")
    public ResponseBody jsonPostExample(
        @RequestBody ExampleJSON example
    ) {
        logger.info(example.getDate() + " " + example.getText());
        ResponseBody response = new ResponseBody();
        response.getStatus().setMessage(example.getDate() + " " + example.getText());
        return response;
    }
    
    @GetMapping("/dbexample")
    public ResponseBody dbExample() throws Exception {
        ResponseBody response = new ResponseBody();
        ExampleAccess examp = new ExampleAccess();
        response.getStatus().setMessage(examp.testConnection());
        return response;
    }
    
    @GetMapping("/createtable")
    public ResponseBody createTable() throws Exception {
        ResponseBody response = new ResponseBody();
        ExampleAccess examp = new ExampleAccess();
        examp.createTable();
        response.getStatus().setMessage("table created");
        return response;
    }
    
    @GetMapping("/insertdata")
    public ResponseBody insertData() throws Exception {
        ResponseBody response = new ResponseBody();
        ExampleAccess examp = new ExampleAccess();
        examp.insertinto();
        response.getStatus().setMessage("inserted data");
        return response;
    }
}
