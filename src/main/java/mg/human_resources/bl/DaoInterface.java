/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.human_resources.bl;

import java.sql.Connection;
import java.util.List;

/**
 *
 * @author lacha
 */
public interface DaoInterface {
    public void insert() throws Exception;
    public void insert(Connection conn) throws Exception;
    public List<BaseModel> findAll() throws Exception;
    public List<BaseModel> findAll(Connection conn) throws Exception;
    
    public List<BaseModel> findAll(Object criteria, Connection conn) throws Exception;
    public List<BaseModel> findAll(Object criteria) throws Exception;
    
    public List<BaseModel> findAll(String req, Connection conn) throws Exception;
    public List<BaseModel> findAll(String req) throws Exception;
    
    public BaseModel findById(Object id, Connection conn) throws Exception;
    public BaseModel findById(Object id) throws Exception;
    
    public void update() throws Exception;
    public void update(Connection conn) throws Exception;
    
    public int count(Connection conn) throws Exception;
    public int count() throws Exception;
    
    public int numberPage(double _count) throws Exception;
    public int numberPage(double _count, Connection conn) throws Exception;
    
    public List<BaseModel> findAllPage(String req, int page, int count) throws Exception;
    public List<BaseModel> findAllPage(String req, int page, int count, Connection conn) throws Exception;
    
}
