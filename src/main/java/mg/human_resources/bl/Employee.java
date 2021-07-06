/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.human_resources.bl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import mg.human_resources.conn.ConnGen;
import mg.human_resources.gen.FctGen;
import mg.human_resources.rsc.EmployeeAttr;
import mg.human_resources.rsc.PointingAttr;
import mg.human_resources.rsc.PointingDailyAttr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

public final class Employee extends BaseModel {

    Logger logger = LoggerFactory.getLogger(Employee.class);

    private static String COLUMNS = "id;first_name;last_name;id_category;date_birth;date_begin_employment;date_end_employment;registration_number";

    private String first_name;
    private String last_name;
    private int id_category;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Timestamp date_birth;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Timestamp date_begin_employment;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Timestamp date_end_employment;
    private int registration_number;

    private EmployeeCategory category;

    public Employee() {
        this.setReq("select * from all_employees_with_categ");
    }

    public Employee(EmployeeAttr attr) throws Exception {
        this.setFirst_name(attr.getFirst_name());
        this.setLast_name(attr.getLast_name());
        this.setId_category(attr.getId_category());
        if (attr.getDate_birth() != null) {
            this.setDate_birth(new Timestamp(attr.getDate_birth().getTime()));
        } else {
            throw new Exception("Veuillez spécifier une date de naissance");
        }
        if (attr.getDate_begin_employment() != null) {
            this.setDate_begin_employment(new Timestamp(attr.getDate_begin_employment().getTime()));
        } else {
            throw new Exception("Veuillez spécifier une date d'embauche");
        }
        if (attr.getDate_end_employment() != null) {
            this.setDate_begin_employment(new Timestamp(attr.getDate_end_employment().getTime()));
        }

    }

    public List<EmployeeWeeklyHours> calculateHours(PointingAttr pointing) throws Exception {
        List<EmployeeWeeklyHours> result = new ArrayList();

        try (Connection conn = ConnGen.getConn()) {
            Employee desiredEmp = (Employee) new Employee().findById(pointing.getEmployee().getId());
            EmployeeCategory empCateg = desiredEmp.getCategory();

            List<BaseModel> majorer = new Majorer().findAll(conn);
            Majorer nightMaj = null;
            Majorer sundayMaj = null;
            Majorer ferierMaj = null;
            for (BaseModel maj : majorer) {
                Majorer ma = (Majorer) maj;
                if ("nuit".equals(ma.getMajorer_type())) {
                    nightMaj = ma;
                } else if ("dimanche".equals(ma.getMajorer_type())) {
                    sundayMaj = ma;
                } else if ("ferier".equals(ma.getMajorer_type())) {
                    ferierMaj = ma;
                }
            }
            if (nightMaj == null || sundayMaj == null || ferierMaj == null) {
                throw new Exception("Veuillez entrez les heures majorées nuit, dimanche et férier");
            }

            int totalWeeklyHours = 0;
            int totalNightHours = 0;
            int totalSundayHours = 0;
            int totalFerierHours = 0;
            int totalWorkedHours = 0;
            List<PointingDailyAttr> dailyPoints = pointing.getPointings();

            List<BaseModel> suppls = new Supplementary().findAll(conn);

            for (int iD = 0; iD < dailyPoints.size(); iD++) {
                PointingDailyAttr point = dailyPoints.get(iD);
                totalWorkedHours += point.getNumberHoursDaily();
                totalWorkedHours += point.getNumberHoursNightly();
                if (point.isIsHoliday()) {
                    if (point.getNumberHoursNightly() > 0 && point.getWeekOfDay() == 7) {
                        if (nightMaj.getPercentage() > sundayMaj.getPercentage()) {
                            if (nightMaj.getPercentage() > ferierMaj.getPercentage()) {
                                totalNightHours += point.getNumberHoursNightly();
                                totalNightHours += point.getNumberHoursDaily();
                            } else {
                                totalFerierHours += point.getNumberHoursNightly();
                                totalFerierHours += point.getNumberHoursDaily();
                            }
                        } else {
                            if (sundayMaj.getPercentage() > ferierMaj.getPercentage()) {
                                totalSundayHours += point.getNumberHoursNightly();
                                totalSundayHours += point.getNumberHoursDaily();
                            } else {
                                totalFerierHours += point.getNumberHoursNightly();
                                totalFerierHours += point.getNumberHoursDaily();
                            }
                        }
                    } else if (point.getNumberHoursNightly() > 0) {
                        if (nightMaj.getPercentage() > ferierMaj.getPercentage()) {
                            totalNightHours += point.getNumberHoursNightly();
                            totalNightHours += point.getNumberHoursDaily();
                        } else {
                            totalFerierHours += point.getNumberHoursNightly();
                            totalFerierHours += point.getNumberHoursDaily();
                        }
                    } else if (point.getWeekOfDay() == 7) {
                        if (sundayMaj.getPercentage() > ferierMaj.getPercentage()) {
                            totalSundayHours += point.getNumberHoursNightly();
                            totalSundayHours += point.getNumberHoursDaily();
                        } else {
                            totalFerierHours += point.getNumberHoursNightly();
                            totalFerierHours += point.getNumberHoursDaily();
                        }
                    } else {
                        totalFerierHours += point.getNumberHoursDaily();
                    }

                    point.setNumberHoursDaily(0);
                } else {
                    if (point.getWeekOfDay() == 7) {
                        if (point.getNumberHoursDaily() > 0) {
                            if (nightMaj.getPercentage() > sundayMaj.getPercentage()) {
                                totalNightHours += point.getNumberHoursNightly();
                                totalNightHours += point.getNumberHoursDaily();
                            } else {
                                totalSundayHours += point.getNumberHoursNightly();
                                totalSundayHours += point.getNumberHoursDaily();
                            }
                        } else {
                            totalSundayHours += point.getNumberHoursDaily();
                        }
                        point.setNumberHoursDaily(0);
                    } else {
                        totalNightHours += point.getNumberHoursNightly();
                    }
                }
                point.setNumberHoursNightly(0);
                totalWeeklyHours += point.getNumberHoursDaily();
            }

            // /* MAJORER */
            for (BaseModel maj : majorer) {
                Majorer ma = (Majorer) maj;
                int totalHours = 0;
                if ("nuit".equals(ma.getMajorer_type())) {
                    totalHours = totalNightHours;
                } else if ("dimanche".equals(ma.getMajorer_type())) {
                    totalHours = totalSundayHours;
                } else if ("ferier".equals(ma.getMajorer_type())) {
                    totalHours = totalFerierHours;
                }
                result.add(new EmployeeWeeklyHours(ma.getCode(), totalHours));
            }

            // THE REMAINS IS SET TO SUPPLEMENTARY
            
            
            int totalSupplHours = totalWeeklyHours > empCateg.getWeekly_hour() ? totalWeeklyHours - empCateg.getWeekly_hour() : 0;
            int totalNormalHours = Math.min(totalWeeklyHours, empCateg.getWeekly_hour());
            result.add(new EmployeeWeeklyHours("Heures normales", totalNormalHours));
            for (BaseModel suppl : suppls) {
                Supplementary supp = (Supplementary) suppl;
                int totalHours = 0;
                if (totalSupplHours <= supp.getMax_hour_per_period()) {
                    totalHours = totalSupplHours;
                } else {
                    totalHours = supp.getMax_hour_per_period();
                }
                totalSupplHours -= totalHours;
                if (totalHours <= 0) {
                    totalHours = 0;
                }
                result.add(new EmployeeWeeklyHours(supp.getCode(), totalHours));
            }
            
            result.add(new EmployeeWeeklyHours("Total Heures travaillées", totalWorkedHours));
        } catch (Exception ex) {
            throw ex;
        }

        return result;
    }
    
    //

    @Override
    public void insert(Connection conn) throws Exception {
        String columns = "first_name;last_name;id_category;date_birth;date_begin_employment;date_end_employment;registration_number";
        try {
            conn.setAutoCommit(false);
            int _reg_number = FctGen.getInt("next_reg", "select nextval('registrationNumber') as next_reg", conn);
            this.setRegistration_number(_reg_number);
            FctGen.insert(this, columns, "employees", conn);
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
        PreparedStatement pstmt = null;
        ResultSet res = null;
        List<BaseModel> result = new ArrayList();
        try {
            pstmt = conn.prepareStatement(req);
            res = pstmt.executeQuery();
            while (res.next()) {
                Employee emp = new Employee();
                emp.setId(res.getInt("id_emp"));
                emp.setFirst_name(res.getString("first_name"));
                emp.setLast_name(res.getString("last_name"));
                emp.setDate_birth(res.getTimestamp("date_birth"));
                emp.setDate_begin_employment(res.getTimestamp("date_begin_employment"));
                emp.setDate_end_employment(res.getTimestamp("date_end_employment"));
                emp.setRegistration_number(res.getInt("registration_number"));
                emp.setId_category(res.getInt("id_category"));

                EmployeeCategory categ = new EmployeeCategory();
                categ.setId(res.getInt("id_category"));
                categ.setDay_week_end(res.getInt("day_week_end"));
                categ.setDay_week_start(res.getInt("day_week_start"));
                categ.setIndemnity_percent(res.getDouble("indemnity_percent"));
                categ.setName(res.getString("name"));
                categ.setStandard_hour_per_day(res.getInt("standard_hour_per_day"));
                categ.setStandard_salary(res.getDouble("standard_salary"));
                categ.setWeekly_hour(res.getInt("weekly_hour"));

                emp.setCategory(categ);

                result.add(emp);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (res != null) {
                res.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
        return result;
    }

    @Override
    public BaseModel findById(Object id, Connection conn) throws Exception {
        String req = "select * from all_employees_with_categ where id_emp = " + id;
        List<BaseModel> result = this.findAll(req, conn);
        if (result.size() <= 0) {
            throw new Exception("Cet employé n'existe pas");
        }
        return result.get(0);
    }

    @Override
    public void update(Connection conn) throws Exception {
        String req = "update employees set first_name = ?, last_name = ?, id_category= ?, date_birth= ?, date_begin_employment=?, date_end_employment=? where id = ?";
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(req);
            pstmt.setString(1, this.getFirst_name());
            pstmt.setString(2, this.getLast_name());
            pstmt.setInt(3, this.getId_category());
            pstmt.setTimestamp(4, this.getDate_birth());
            pstmt.setTimestamp(5, this.getDate_begin_employment());
            pstmt.setTimestamp(6, this.getDate_end_employment());

            pstmt.setInt(7, this.getId());

            pstmt.execute();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    @Override
    public int count(Connection conn) throws Exception {
        return FctGen.getInt("numb", "select count(*) as numb from all_employees_with_categ", conn);
    }

    public static String getCOLUMNS() {
        return COLUMNS;
    }

    public static void setCOLUMNS(String COLUMNS) {
        Employee.COLUMNS = COLUMNS;
    }

    public EmployeeCategory getCategory() {
        return category;
    }

    public void setCategory(EmployeeCategory category) {
        this.category = category;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public int getId_category() {
        return id_category;
    }

    public void setId_category(int id_category) {
        this.id_category = id_category;
    }

    public Timestamp getDate_birth() {
        return date_birth;
    }

    public void setDate_birth(Timestamp date_birth) {
        this.date_birth = date_birth;
    }

    public Timestamp getDate_begin_employment() {
        return date_begin_employment;
    }

    public void setDate_begin_employment(Timestamp date_begin_employment) {
        this.date_begin_employment = date_begin_employment;
    }

    public Timestamp getDate_end_employment() {
        return date_end_employment;
    }

    public void setDate_end_employment(Timestamp date_end_employment) {
        this.date_end_employment = date_end_employment;
    }

    public int getRegistration_number() {
        return registration_number;
    }

    public void setRegistration_number(int registration_number) {
        this.registration_number = registration_number;
    }

}

// int totalWeeklyHours = 0;
// int totalNightlyHours = 0;
// int totalSundayHours = 0;
// int totalFerierHours = 0;
// List<PointingDailyAttr> dailyPoints = pointing.getPointings();
// for (PointingDailyAttr dailyPoint : dailyPoints) {
//     totalWeeklyHours += dailyPoint.getNumberHoursDaily();
//     totalNightlyHours += dailyPoint.getNumberHoursNightly();
//     if(dailyPoint.getWeekOfDay() == 7) {
//         totalSundayHours += dailyPoint.getNumberHoursDaily();
//     }
//     if(dailyPoint.isIsHoliday()) {
//         totalFerierHours += dailyPoint.getNumberHoursDaily();
//         totalFerierHours += dailyPoint.getNumberHoursNightly();
//     }
// }
// int normalHours = Math.min(totalWeeklyHours, empCateg.getWeekly_hour());
// result.add(new EmployeeWeeklyHours("Heures normales", normalHours));
// /* SUPPLEMENTAIRE */
// List<BaseModel> suppls = new Supplementary().findAll(conn);
// int sumTotalSupplHours = 0;
// int totalSupplHours = totalWeeklyHours > empCateg.getWeekly_hour() ? totalWeeklyHours - empCateg.getWeekly_hour() : 0;
// for (BaseModel suppl : suppls) {
//     Supplementary supp = (Supplementary) suppl;
//     int totalHours = 0;
//     if (totalSupplHours <= supp.getMax_hour_per_period()) {
//         totalHours = totalSupplHours;
//     } else {
//         totalHours = supp.getMax_hour_per_period();
//     }
//     totalSupplHours -= totalHours;
//     if (totalHours <= 0) {
//         totalHours = 0;
//     }
//     sumTotalSupplHours += totalHours;
//     result.add(new EmployeeWeeklyHours(supp.getCode(), totalHours));
// }
// /* MAJORER */
// List<BaseModel> majorer = new Majorer().findAll(conn);
// for (BaseModel maj : majorer) {
//     Majorer ma = (Majorer) maj;
//     int totalHours = 0;
//     if ("nuit".equals(ma.getMajorer_type())) {
//         totalHours = totalNightlyHours;
//     } else if ("dimanche".equals(ma.getMajorer_type())) {
//         totalHours = totalSundayHours;
//     } else if ("ferier".equals(ma.getMajorer_type())) {
//         totalHours = totalFerierHours;
//     }
//     result.add(new EmployeeWeeklyHours(ma.getCode(), totalHours));
// }
// int bigTotalHours = normalHours + sumTotalSupplHours + totalNightlyHours + totalSundayHours + totalFerierHours;
            // result.add(new EmployeeWeeklyHours("Total Heures", bigTotalHours));
