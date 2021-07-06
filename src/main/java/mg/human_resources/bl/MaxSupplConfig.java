/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.human_resources.bl;

import java.sql.Connection;
import java.util.List;
import mg.human_resources.gen.FctGen;

/**
 *
 * @author lacha
 */
public class MaxSupplConfig extends BaseModel {

    private int max_hour_supp;
    private static String COLUMNS = "id;max_hour_supp";

    public MaxSupplConfig() {
        setReq("select * from suppl_config_max order by id desc");
    }

    public MaxSupplConfig getLast(Connection conn) throws Exception {

        String req = "select * from suppl_config_max order by id desc";
        List<BaseModel> result = this.findAll(req, conn);
        if (result.size() <= 0) {
            throw new Exception("Veuillez insérer au moins un maximum");
        }
        return (MaxSupplConfig) result.get(0);
    }

    @Override
    public void insert(Connection conn) throws Exception {
        try {
            conn.setAutoCommit(false);
            FctGen.insert(this, "max_hour_supp", "suppl_config_max", conn);
            conn.commit();
        } catch (Exception ex) {
            conn.rollback();
            throw ex;
        }
    }

    @Override
    public List<BaseModel> findAll(Connection conn) throws Exception {
        return this.findAll(getReq(), conn);
    }

    @Override
    public List<BaseModel> findAll(Object criteria, Connection conn) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BaseModel> findAll(String req, Connection conn) throws Exception {
        return (List<BaseModel>) (List<?>) FctGen.findAll(this, req, COLUMNS, conn);
    }

    @Override
    public BaseModel findById(Object id, Connection conn) throws Exception {
        String req = "select * from suppl_config_max where id = " + id;
        List<BaseModel> result = this.findAll(req, conn);
        if (result.size() <= 0) {
            throw new Exception("Cette supplémentaire maximum n'existe pas");
        }
        return result.get(0);
    }

    @Override
    public void update(Connection conn) throws Exception {
        String req = String.format("update suppl_config_max set max_hour_supp = %d where id = %d", this.getMax_hour_supp(), this.getId());
        FctGen.update(req, conn);
    }

    @Override
    public int count(Connection conn) throws Exception {
        return FctGen.getInt("numb", "select count(*) as numb from suppl_config_max", conn);
    }

    public int getMax_hour_supp() {
        return max_hour_supp;
    }

    public void setMax_hour_supp(int max_hour_supp) {
        this.max_hour_supp = max_hour_supp;
    }

}
