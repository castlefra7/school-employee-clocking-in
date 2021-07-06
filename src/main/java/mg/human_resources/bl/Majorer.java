/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.human_resources.bl;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import mg.human_resources.gen.FctGen;
import mg.human_resources.rsc.MajorerAttr;

/**
 *
 * @author lacha
 */
public final class Majorer extends BaseModel {

    private static String COLUMNS = "id;majorer_type;created_date;code;percentage";
    private static String COLUMNS_NO_ID = "majorer_type;code;percentage";

    private String majorer_type;
    private Timestamp created_date;
    private String code;
    private double percentage;

    public Majorer() {
        this.setReq("select * from majorer_config");
    }

    public Majorer(MajorerAttr attr) {
        this.setMajorer_type(attr.getMajorer_type());
        this.setCode(attr.getCode());
        this.setPercentage(attr.getPercentage());
    }

    @Override
    public void insert(Connection conn) throws Exception {
        try {
            conn.setAutoCommit(false);
            FctGen.insert(this, COLUMNS_NO_ID, "majorer_config", conn);
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
        String req = "select * from majorer_config where id = " + id;
        List<BaseModel> result = this.findAll(req, conn);
        if (result.size() <= 0) {
            throw new Exception("Cette heure majorée n'existe pas");
        }
        return result.get(0);
    }

    @Override
    public void update(Connection conn) throws Exception {
        Timestamp today = new Timestamp(new Date().getTime());
        String req = String.format(Locale.ENGLISH, "update majorer_config set  majorer_type = '%s', created_date = '%s', code='%s', percentage= %f where id = %d",
                this.getMajorer_type(), today, this.getCode(), this.getPercentage(), this.getId());
        FctGen.update(req, conn);
    }

    @Override
    public int count(Connection conn) throws Exception {
        return FctGen.getInt("numb", "select count(*) as numb from majorer_config", conn);
    }

    public String getMajorer_type() {
        return majorer_type;
    }

    public void setMajorer_type(String majorer_type) {
        this.majorer_type = majorer_type;
    }

    public Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Timestamp created_date) {
        this.created_date = created_date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

}
