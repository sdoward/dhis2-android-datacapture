package org.dhis2.mobile.utils;

import org.dhis2.mobile.io.Constants;
import org.dhis2.mobile.io.models.Field;
import org.dhis2.mobile.io.models.Group;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by George on 8/26/16.
 */

/**
 * Checks whether the report made is timely or not.
 * Being timely is based on whether the report was submitted before 12 o'lock on Monday
 */
public class IsTimely {
    public IsTimely(){

    }
    public static Boolean check(String period){
        Boolean timely = false;

        int periodWeek = Integer.parseInt(period.substring(5));
        int periodYear = Integer.parseInt(period.substring(0,5));
        int weekNumber = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int year = Calendar.getInstance().get(Calendar.YEAR);


        if(!(periodYear < year) &&  periodWeek == weekNumber -2 && currentDay == Calendar.MONDAY && currentHour < 12){
            timely = true;
        }
        return timely;
    }
    public static Boolean hasBeenSet(ArrayList<Group> groups){

        for(Group group : groups){
            for(Field field : group.getFields()){
                if(field.getDataElement().equals(Constants.TIMELY) && field.getValue() != null){
                    return true;
                }
            }
        }

        return false;
    }
}
