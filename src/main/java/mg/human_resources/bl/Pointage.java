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
import mg.human_resources.rsc.PointingAttr;
import mg.human_resources.rsc.PointingDailyAttr;

/**
 *
 * @author lacha
 */
public class Pointage extends BaseModel {

    private int id_employee;
    private int id_semaine;
    private String code;
    private float hours;
    private double percentage;

    private static String COLUMNS = "id;id_employee;id_semaine;code;hours;percentage";
    private static String COLUMNS_NO_ID = "id_employee;id_semaine;code;hours;percentage";

    public Pointage() {
        this.setReq("select * from pointages");
    }

    public void insert(PointingAttr attr) throws Exception {
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            conn.setAutoCommit(false);

            List<EmployeeWeeklyHours> allHours = new Employee().calculateHours(attr, conn);

            for (EmployeeWeeklyHours hour : allHours) {
                Pointage pointage = new Pointage();
                pointage.setId_employee(attr.getEmployee().getId());
                pointage.setCode(hour.getCode());
                pointage.setHours(hour.getHours());
                pointage.setPercentage(hour.getPercentage());
                pointage.setId_semaine(attr.getSemaine());

                pointage.insert(conn);
            }

            conn.commit();
        } catch (Exception ex) {
            if (conn != null) {
                conn.rollback();
            }
            throw ex;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public List<BaseModel> findByIdAndWeek(int _id_emp, int _id_semaine, Connection conn) throws Exception {
        String req = String.format("select * from pointages where id_employee = %d and id_semaine= %d ", _id_emp, _id_semaine);
        return this.findAll(req, conn);
    }

    @Override
    public void insert(Connection conn) throws Exception {
        FctGen.insert(this, COLUMNS_NO_ID, "pointages", conn);
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

    public int getId_employee() {
        return id_employee;
    }

    public void setId_employee(int id_employee) {
        this.id_employee = id_employee;
    }

    public int getId_semaine() {
        return id_semaine;
    }

    public void setId_semaine(int id_semaine) {
        this.id_semaine = id_semaine;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getHours() {
        return hours;
    }

    public void setHours(float hours) {
        this.hours = hours;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public static String getCOLUMNS() {
        return COLUMNS;
    }

    public static void setCOLUMNS(String COLUMNS) {
        Pointage.COLUMNS = COLUMNS;
    }

    public static String getCOLUMNS_NO_ID() {
        return COLUMNS_NO_ID;
    }

    public static void setCOLUMNS_NO_ID(String COLUMNS_NO_ID) {
        Pointage.COLUMNS_NO_ID = COLUMNS_NO_ID;
    }

}
