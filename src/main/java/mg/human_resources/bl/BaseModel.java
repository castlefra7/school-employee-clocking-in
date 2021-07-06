/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.human_resources.bl;

import java.sql.Connection;
import java.util.List;
import mg.human_resources.conn.ConnGen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lacha
 */
public abstract class BaseModel implements DaoInterface {
    
    private String req;

    public String getReq() {
        return req;
    }

    public void setReq(String req) {
        this.req = req;
    }
    

    private int id;

    public int getId() {
        return id;
    }

    public final void setId(int id) {
        this.id = id;
    }

    @Override
    public void update() throws Exception {
        try (Connection conn = ConnGen.getConn()) {
            this.update(conn);
        } catch (Exception ex) {
            throw ex;
        }
    }

    Logger logger = LoggerFactory.getLogger(BaseModel.class);
    @Override
    public void insert() throws Exception {
        try (Connection conn = ConnGen.getConn()) {
            this.insert(conn);
        } catch (Exception ex) {
            logger.info(ex.getMessage());
            throw new Exception("Veuillez vérifier vos données");
        }
    }

    @Override
    public List<BaseModel> findAll() throws Exception {
        try (Connection conn = ConnGen.getConn()) {
            return this.findAll(conn);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public List<BaseModel> findAll(String req) throws Exception {
        try (Connection conn = ConnGen.getConn()) {
            return this.findAll(req, conn);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public List<BaseModel> findAll(Object criteria) throws Exception {
        try (Connection conn = ConnGen.getConn()) {
            return findAll(conn);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public BaseModel findById(Object id) throws Exception {
        try (Connection conn = ConnGen.getConn()) {
            return this.findById(id, conn);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public int numberPage(double _count) throws Exception {
        try (Connection conn = ConnGen.getConn()) {
            return numberPage(_count, conn);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public int numberPage(double _count, Connection conn) throws Exception {
        try {
            return (int) Math.ceil(count(conn) / _count);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public int count() throws Exception {
        try (Connection conn = ConnGen.getConn()) {
            return count(conn);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public List<BaseModel> findAllPage(String req, int page, int count, Connection conn) throws Exception {
        try {
            if(req == null) {
                req = this.req;
            }
            page = page * count;
            req += String.format(" limit %d offset %d", count, page);
            return this.findAll(req, conn);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public List<BaseModel> findAllPage(String req, int page, int count) throws Exception {
        try (Connection conn = ConnGen.getConn()) {
            return this.findAllPage(req, page, count, conn);
        } catch (Exception ex) {
            throw ex;
        }
    }

}
