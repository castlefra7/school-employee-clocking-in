/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.human_resources.bl;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;
import mg.human_resources.gen.FctGen;
import mg.human_resources.rsc.SupplementaryAttr;

/**
 *
 * @author lacha
 */
public final class Supplementary extends BaseModel {

    private static String COLUMNS = "id;code;max_hour_per_period;period_type;percentage";
    private static String COLUMNS_NO_ID = "code;max_hour_per_period;period_type;percentage";

    private String code;
    private int max_hour_per_period;
    private String period_type;
    private Timestamp created_date;
    private double percentage;

    public Supplementary() {
        this.setReq("select * from suppl_config order by max_hour_per_period");
    }

    public Supplementary(SupplementaryAttr attr) {
        this.setCode(attr.getCode());
        this.setMax_hour_per_period(attr.getMax_hour_per_period());
        this.setPeriod_type(attr.getPeriod_type());
        this.setPercentage(attr.getPercentage());
    }

    public int getSums(Connection conn) throws Exception {
        return FctGen.getInt("total", "select sum(max_hour_per_period) as total from suppl_config", conn);
    }

    @Override
    public void insert(Connection conn) throws Exception {
        try {
            conn.setAutoCommit(false);
            if (this.getPeriod_type() == null) {
                this.setPeriod_type("semaine");
            }

            MaxSupplConfig lastMax = new MaxSupplConfig().getLast(conn);
            if ((getSums(conn) >= lastMax.getMax_hour_supp()) || (getSums(conn) + this.getMax_hour_per_period() > lastMax.getMax_hour_supp())) {
                throw new Exception("Le supplémentaire maximum est de " + lastMax.getMax_hour_supp());
            }

            
            FctGen.insert(this, COLUMNS_NO_ID, "suppl_config", conn);
            conn.commit();
        } catch (Exception ex) {
            conn.rollback();
            throw ex;
        }

    }

    @Override
    public List<BaseModel> findAll(Connection conn) throws Exception {
        return this.findAll(this.getReq(), conn);
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
        String req = "select * from suppl_config where id = " + id;
        List<BaseModel> result = this.findAll(req, conn);
        if (result.size() <= 0) {
            throw new Exception("Cette supplémentaire n'existe pas");
        }
        return result.get(0);
    }

    @Override
    public void update(Connection conn) throws Exception {
        String req = String.format(Locale.ENGLISH, "update suppl_config set code = '%s', max_hour_per_period = %d, period_type='%s', percentage=%f where id = %d",
                this.getCode(), this.getMax_hour_per_period(), this.getPeriod_type(), this.getPercentage(), this.getId());
        FctGen.update(req, conn);
    }

    @Override
    public int count(Connection conn) throws Exception {
        return FctGen.getInt("numb", "select count(*) as numb from suppl_config", conn);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getMax_hour_per_period() {
        return max_hour_per_period;
    }

    public void setMax_hour_per_period(int max_hour_per_period) {
        this.max_hour_per_period = max_hour_per_period;
    }

    public String getPeriod_type() {
        return period_type;
    }

    public void setPeriod_type(String period_type) {
        this.period_type = period_type;
    }

    public Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Timestamp created_date) {
        this.created_date = created_date;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

}
