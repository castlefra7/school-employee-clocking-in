/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.human_resources.bl;

import java.sql.Connection;
import java.util.List;
import java.util.Locale;
import mg.human_resources.gen.FctGen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lacha
 */
public final class EmployeeCategory extends BaseModel {

    Logger logger = LoggerFactory.getLogger(EmployeeCategory.class);

    private static String COLUMNS = "id;name;standard_hour_per_day;day_week_start;day_week_end;standard_salary;indemnity_percent;weekly_hour";

    private String name;
    private int standard_hour_per_day;
    private int day_week_start;
    private int day_week_end;
    private double standard_salary;
    private double indemnity_percent;

    private int weekly_hour;
    
    public EmployeeCategory() {
        this.setReq("select * from all_employee_categories_with_weekly_hour");
    }

    @Override
    public void insert(Connection conn) throws Exception {
        String columns = "name;standard_hour_per_day;day_week_start;day_week_end;standard_salary;indemnity_percent";
        FctGen.insert(this, columns, "employee_categories", conn);
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
        String req = "select * from all_employee_categories_with_weekly_hour where id = " + id;
        List<BaseModel> result = this.findAll(req, conn);
        if (result.size() <= 0) {
            throw new Exception("Cette catégorie n'existe pas");
        }
        return result.get(0);
    }

    @Override
    public void update(Connection conn) throws Exception {
        String req = String.format(Locale.ENGLISH, "update employee_categories set name = '%s', standard_hour_per_day = %d, day_week_start = %d, day_week_end= %d , standard_salary = %f ,indemnity_percent = %f where id = %d",
                this.getName(), this.getStandard_hour_per_day(), this.getDay_week_start(), this.getDay_week_end(), this.getStandard_salary(), this.getIndemnity_percent(), this.getId());
        logger.info(req);
        FctGen.update(req, conn);
    }

    @Override
    public int count(Connection conn) throws Exception {
        return FctGen.getInt("numb", "select count(*) as numb from all_employee_categories_with_weekly_hour", conn);
    }

    public static String getCOLUMNS() {
        return COLUMNS;
    }

    public static void setCOLUMNS(String COLUMNS) {
        EmployeeCategory.COLUMNS = COLUMNS;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStandard_hour_per_day() {
        return standard_hour_per_day;
    }

    public void setStandard_hour_per_day(int standard_hour_per_day) {
        this.standard_hour_per_day = standard_hour_per_day;
    }

    public int getDay_week_start() {
        return day_week_start;
    }

    public void setDay_week_start(int day_week_start) {
        this.day_week_start = day_week_start;
    }

    public int getDay_week_end() {
        return day_week_end;
    }

    public void setDay_week_end(int day_week_end) {
        this.day_week_end = day_week_end;
    }

    public double getStandard_salary() {
        return standard_salary;
    }

    public void setStandard_salary(double standard_salary) {
        this.standard_salary = standard_salary;
    }

    public double getIndemnity_percent() {
        return indemnity_percent;
    }

    public void setIndemnity_percent(double indemnity_percent) {
        this.indemnity_percent = indemnity_percent;
    }

    public int getWeekly_hour() {
        return weekly_hour;
    }

    public void setWeekly_hour(int weekly_hour) {
        this.weekly_hour = weekly_hour;
    }

}
