package org.dhis2.ehealthMobile.utils;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.dhis2.ehealthMobile.io.models.Field;
import org.dhis2.ehealthMobile.io.models.Form;
import org.dhis2.ehealthMobile.io.models.Group;

import java.util.ArrayList;

/**
 * Created by george on 11/29/16.
 */

public class FormUtils {

    private FormUtils(){

    }

    public static Form squashFormGroups(Form originalForm){
        String squashedGroupsLabel = "";
        Parcel parcel = Parcel.obtain();
        parcel.writeValue(originalForm);
        Form squashedForm = Form.CREATOR.createFromParcel(parcel);

        ArrayList<Field> fields = new ArrayList<>();
        for(Group group: originalForm.getGroups()){
            fields.addAll(group.getFields());
            squashedGroupsLabel += group.getLabel() + " + ";
        }

        ArrayList<Group> groups = new ArrayList<>();
        Group group = new Group(squashedGroupsLabel, fields);
        groups.add(group);

        squashedForm.setGroups(groups);

        return  squashedForm;
    }
}
