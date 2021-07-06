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
    private float weekOfDay;
    private float numberHoursDaily;
    private float numberHoursNightly;
    private float numberHoursFerier;

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

}
