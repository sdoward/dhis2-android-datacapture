package org.dhis2.mobile.utils;

import org.dhis2.mobile.io.models.Field;

import java.util.Arrays;

/**
 * Created by george on 9/1/16.
 */
public class IsCritical {
    private static final String [] criticalItems = new String[] {"ZybYDdwk3O2", "gqQhU8INQKw", "BR2cqsVasFd","u67EkxdKDbQ",
            "JyHy4B7myv9", "FtexyU70UDV","dx9dXyT0Gt8","DyaUOTqqGip"} ;

    public static Boolean check(Field field){
        Boolean isCtritical = false;

        if(Arrays.asList(criticalItems).contains(field.getDataElement())){
            isCtritical = true;
        }

        return isCtritical;
    }
}
