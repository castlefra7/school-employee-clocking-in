/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.human_resources.bl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import mg.human_resources.conn.ConnGen;
import mg.human_resources.gen.FctGen;
import mg.human_resources.gen.PDFBoxable;
import mg.human_resources.rsc.EmployeeAttr;
import mg.human_resources.rsc.PointingAttr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

public final class Employee extends BaseModel {

    private boolean isStat;

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

    private double totalSalary;
    private int id_semaine = -1;
    private EmployeeCategory category;

    private EmployeePaie empPaie;

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
    
    public void savePDF(String dir, int id_emp, int _id_semaine) throws IOException, Exception {
        PDFBoxable boxable = new PDFBoxable();
        boxable.drawPageTitle();
        boxable.setUploadDir(dir);

        boxable.id_semaine = _id_semaine;
        Employee emp = (Employee) new Employee().findById(id_emp);
        boxable.drawEmployeeInf(emp);
        logger.info(String.valueOf(boxable.id_semaine) + " "  + String.valueOf(id_emp));
        
        EmployeePaie empPaie = new Employee().calculatePaie(id_emp, _id_semaine);
        boxable.drawTablePaie(empPaie);
        boxable.save();
    }

    public List<EmployeeWeeklyHoursAndAmount> sumHoursAndAmount() throws Exception {
        try (Connection conn = ConnGen.getConn()) {
            List<EmployeeWeeklyHoursAndAmount> result = new ArrayList();
            List<BaseModel> allEmp = this.getAll();
            HashMap<String, Float> tempHours = new HashMap();
            HashMap<String, Double> tempAmounts = new HashMap();
            for (BaseModel base : allEmp) {
                EmployeePaie _empPaie = ((Employee) base).getEmpPaie();
                List<EmployeeWeeklyHoursAndAmount> _empPaieHours = _empPaie.getPaie();
                for (EmployeeWeeklyHoursAndAmount paie : _empPaieHours) {
                    tempHours.put(paie.getCode(), paie.getHours());
                    tempAmounts.put(paie.getCode(), paie.getTotalAmount());
                }
            }
            Set<String> keys = tempHours.keySet();
            for (String key : keys) {
                EmployeeWeeklyHoursAndAmount empK = new EmployeeWeeklyHoursAndAmount();
                empK.setCode(key);
                empK.setHours(tempHours.get(key));
                empK.setTotalAmount(tempAmounts.get(key));
                result.add(empK);
            }
            return result;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public EmployeePaie calculatePaie(int _id_emp, int _id_semaine) throws Exception {
        try (Connection conn = ConnGen.getConn()) {
            return this.calculatePaie(_id_emp, _id_semaine, conn);
        } catch (Exception ex) {
            throw ex;
        }
    }

    public EmployeePaie calculatePaie(int _id_emp, int _id_semaine, Connection conn) throws Exception {
        try {
            EmployeePaie empPaie = new EmployeePaie();
            List<BaseModel> pointages = new Pointage().findByIdAndWeek(_id_emp, _id_semaine, conn);

            List<EmployeeWeeklyHoursAndAmount> result = new ArrayList();
            Employee desiredEmp = (Employee) new Employee().findById(_id_emp);
            EmployeeCategory empCateg = desiredEmp.getCategory();

            double _totalSalary = 0;
            float totalHours = 0;
            double baseHourlyRate = empCateg.getStandard_salary() / empCateg.getWeekly_hour();
            for (BaseModel base : pointages) {

                Pointage hour = (Pointage) base;
                if (hour.getPercentage() < 0) {
                    continue;
                }
                EmployeeWeeklyHoursAndAmount hourAmount = new EmployeeWeeklyHoursAndAmount(hour.getCode(), hour.getHours(), hour.getPercentage());
                if (hour.getPercentage() > 0) {
                    if(hour.getCode().toLowerCase().contains("supp")) {
                        hourAmount.setHourlyRate((baseHourlyRate * hour.getPercentage()));
                    } else {
                        hourAmount.setHourlyRate(baseHourlyRate + (baseHourlyRate * hour.getPercentage()));
                    }
                    
                } else {
                    hourAmount.setHourlyRate(baseHourlyRate);
                }
                hourAmount.setTotalAmount(hourAmount.getHours() * hourAmount.getHourlyRate());
                _totalSalary += hourAmount.getTotalAmount();
                result.add(hourAmount);
                totalHours += hour.getHours();
            }
            empPaie.setPaie(result);
            double indemnity = empCateg.getStandard_salary() * empCateg.getIndemnity_percent(); // TODO HOW TO CALCULATE base salary * percent_indemnity
            double[] amounts = new double[4];
            double prorata = 0;
            if (totalHours < empCateg.getWeekly_hour() && totalHours > 0) {
                indemnity = 0;
                prorata = (empCateg.getWeekly_hour() - totalHours) * baseHourlyRate;
            }
            if(totalHours <= 0) {
                indemnity = 0;
            }
            amounts[0] = indemnity;

            amounts[1] = _totalSalary + indemnity - prorata;
            amounts[2] = totalHours;
            amounts[3] = _totalSalary;
            empPaie.setAmounts(amounts);

            return empPaie;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public List<EmployeeWeeklyHours> calculateHours(PointingAttr pointing) throws Exception {
        try (Connection conn = ConnGen.getConn()) {
            return this.calculateHours(pointing, conn);
        } catch (Exception ex) {
            throw ex;
        }
    }

    public List<EmployeeWeeklyHours> calculateHours(PointingAttr pointing, Connection conn) throws Exception {
        List<EmployeeWeeklyHours> result = new ArrayList();

        try {
            Employee desiredEmp = (Employee) new Employee().findById(pointing.getEmployee().getId());
            if(desiredEmp.getDate_end_employment() != null) {
                throw new Exception("Date fin embauche");
            }
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
                throw new Exception("Veuillez entrez toues les heures majorées nuit, dimanche et férier");
            }

            float totalNightHours = 0;
            float totalSundayHours = 0;
            float totalWorkedFerierHours = 0;
            float totalNotWorkedFerierHours = 0;
            float totalDayHours = 0;
            float totalWorkedHours = 0;
            List<PointingDailyAttr> dailyPoints = pointing.getPointings();

            List<BaseModel> suppls = new Supplementary().findAll(conn);

            for (int iD = 0; iD < dailyPoints.size(); iD++) {
                PointingDailyAttr point = dailyPoints.get(iD);
                if(point.getNumberHoursDaily() < 0 || point.getNumberHoursNightly() < 0 || point.getNumberHoursFerier() < 0) {
                    throw new Exception("Veuillez spécifier des heures non négatives");
                }
                if(point.getNumberHoursDaily() > 17 || point.getNumberHoursNightly() > 7) {
                    throw new Exception("Veuillez spécifier aux plus 17 h pour le jour et 7 h pour la nuit");
                }
                if(point.getNumberHoursFerier() > 24) {
                    throw new Exception("Veuillez spécifier un nombre d'heures < 24");
                }
                totalWorkedHours += point.getNumberHoursDaily();
                totalWorkedHours += point.getNumberHoursNightly();
                totalWorkedHours += point.getNumberHoursFerier();

                if (point.getNumberHoursFerier() > 0) {
                    totalNotWorkedFerierHours += point.getNumberHoursFerier();
                    // NEED TO GET MAX FERIER, MAX DAY, MAX NIGHT ?
                    totalWorkedFerierHours += point.getNumberHoursDaily();
                    totalWorkedFerierHours += point.getNumberHoursNightly();
                } else {
                    if (point.getWeekOfDay() == 7) {
                        if (nightMaj.getPercentage() > sundayMaj.getPercentage()) {
                            totalNightHours += point.getNumberHoursNightly();
                            totalNightHours += point.getNumberHoursDaily();
                        } else {
                            totalSundayHours += point.getNumberHoursNightly();
                            totalSundayHours += point.getNumberHoursDaily();
                        }
                    } else {
                        totalDayHours += point.getNumberHoursDaily();
                        totalNightHours += point.getNumberHoursNightly();
                    }
                }
            }

            double rateNormal = 0; // TODO: GET IT FROM DATABASE ?
            result.add(new EmployeeWeeklyHours("Nb heures jour", totalDayHours, rateNormal));
            result.add(new EmployeeWeeklyHours("Nb heures Nuit", totalNightHours, nightMaj.getPercentage()));
            result.add(new EmployeeWeeklyHours("Nb heures Dimanche", totalSundayHours, sundayMaj.getPercentage()));
            result.add(new EmployeeWeeklyHours("Nb heures jour férié où il a travaillé", totalWorkedFerierHours, ferierMaj.getPercentage()));
            result.add(new EmployeeWeeklyHours("Nb heures jour férié", totalNotWorkedFerierHours, rateNormal));

            // SUPPLEMENTARY
            float totalSupplHours = totalWorkedHours > empCateg.getWeekly_hour() ? totalWorkedHours - empCateg.getWeekly_hour() : 0;

            for (BaseModel suppl : suppls) {
                Supplementary supp = (Supplementary) suppl;
                float totalHours = 0;
                if (totalSupplHours <= supp.getMax_hour_per_period()) {
                    totalHours = totalSupplHours;
                } else {
                    totalHours = supp.getMax_hour_per_period();
                }
                totalSupplHours -= totalHours;
                if (totalHours <= 0) {
                    totalHours = 0;
                }
                result.add(new EmployeeWeeklyHours("Nb heures supp", totalHours, supp.getPercentage()));
            }

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

                if (this.getId_semaine() > 0 && this.isIsStat()) {
                    double[] _amounts = emp.calculatePaie(emp.getId(), this.getId_semaine(), conn).getAmounts();
                    if (_amounts.length > 2) {
                        emp.setTotalSalary(_amounts[1]);
                    }
                }
                if (this.isIsStat()) {
                    emp.setEmpPaie(emp.calculatePaie(emp.getId(), this.getId_semaine(), conn));
                }

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
        if (this.isIsStat()) {
            this.setAll(result);
        }
        return result;
    }

    private List<BaseModel> all;

    public List<BaseModel> getAll() {
        return all;
    }

    public void setAll(List<BaseModel> all) {
        this.all = all;
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

    public int getId_semaine() {
        return id_semaine;
    }

    public void setId_semaine(int id_semaine) {
        this.id_semaine = id_semaine;
    }

    public double getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(double totalSalary) {
        this.totalSalary = totalSalary;
    }

    public boolean isIsStat() {
        return isStat;
    }

    public void setIsStat(boolean isStat) {
        this.isStat = isStat;
    }

    public EmployeePaie getEmpPaie() {
        return empPaie;
    }

    public void setEmpPaie(EmployeePaie empPaie) {
        this.empPaie = empPaie;
    }

}
