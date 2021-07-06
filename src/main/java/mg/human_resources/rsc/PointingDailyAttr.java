/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.human_resources.rsc;

/**
 *
 * @author lacha
 */
public class PointingDailyAttr {

    private boolean isHoliday;
    private int weekOfDay;
    private int numberHoursDaily;
    private int numberHoursNightly;

    public boolean isIsHoliday() {
        return isHoliday;
    }

    public void setIsHoliday(boolean isHoliday) {
        this.isHoliday = isHoliday;
    }

    public int getWeekOfDay() {
        return weekOfDay;
    }

    public void setWeekOfDay(int weekOfDay) {
        this.weekOfDay = weekOfDay;
    }

    public int getNumberHoursDaily() {
        return numberHoursDaily;
    }

    public void setNumberHoursDaily(int numberHoursDaily) {
        this.numberHoursDaily = numberHoursDaily;
    }

    public int getNumberHoursNightly() {
        return numberHoursNightly;
    }

    public void setNumberHoursNightly(int numberHoursNightly) {
        this.numberHoursNightly = numberHoursNightly;
    }

}
