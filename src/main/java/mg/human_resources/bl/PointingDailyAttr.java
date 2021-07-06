/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.human_resources.bl;

import java.sql.Connection;
import java.util.List;
import mg.human_resources.conn.ConnGen;
import mg.human_resources.gen.FctGen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lacha
 */
public class PointingDailyAttr extends BaseModel {

    Logger logger = LoggerFactory.getLogger(PointingDailyAttr.class);
    private static String COLUMNS = "id;id_employee;id_semaine;weekOfDay;numberHoursDaily;numberHoursNightly;numberHoursFerier";
    private static String COLUMNS_NO_ID = "id_employee;id_semaine;weekOfDay;numberHoursDaily;numberHoursNightly;numberHoursFerier";

    private boolean isHoliday;
    private float weekOfDay;
    private float numberHoursDaily;
    private float numberHoursNightly;
    private float numberHoursFerier;
    private int id_semaine;
    private int id_employee;

    public List<BaseModel> findAllByIdEmpAndSemaine(int _id_emp, int _id_semaine) throws Exception {
        try (Connection conn = ConnGen.getConn()) {
            logger.info("FIND ALL");
            String req = String.format("select * from pointings_daily where id_employee = %d and id_semaine = %d", _id_emp, _id_semaine);
            return this.findAll(req, conn);
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void insertAll(List<PointingDailyAttr> all, Connection conn) throws Exception {
        for (PointingDailyAttr attr : all) {
            attr.insert(conn);
        }
    }

    @Override
    public void insert(Connection conn) throws Exception {
        FctGen.insert(this, COLUMNS_NO_ID, "pointings_daily", conn);
    }

    @Override
    public List<BaseModel> findAll(Connection conn) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Connection conn) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int count(Connection conn) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isIsHoliday() {
        return isHoliday;
    }

    public void setIsHoliday(boolean isHoliday) {
        this.isHoliday = isHoliday;
    }

    public float getWeekOfDay() {
        return weekOfDay;
    }

    public void setWeekOfDay(float weekOfDay) {
        this.weekOfDay = weekOfDay;
    }

    public float getNumberHoursDaily() {
        return numberHoursDaily;
    }

    public void setNumberHoursDaily(float numberHoursDaily) {
        this.numberHoursDaily = numberHoursDaily;
    }

    public float getNumberHoursNightly() {
        return numberHoursNightly;
    }

    public void setNumberHoursNightly(float numberHoursNightly) {
        this.numberHoursNightly = numberHoursNightly;
    }

    public float getNumberHoursFerier() {
        return numberHoursFerier;
    }

    public void setNumberHoursFerier(float numberHoursFerier) {
        this.numberHoursFerier = numberHoursFerier;
    }

    public int getId_semaine() {
        return id_semaine;
    }

    public void setId_semaine(int id_semaine) {
        this.id_semaine = id_semaine;
    }

    public int getId_employee() {
        return id_employee;
    }

    public void setId_employee(int id_employee) {
        this.id_employee = id_employee;
    }

}
