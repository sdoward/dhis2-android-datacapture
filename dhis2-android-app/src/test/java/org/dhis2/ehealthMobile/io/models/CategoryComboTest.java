package org.dhis2.ehealthMobile.io.models;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

/**
 * Created by george on 1/4/17.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class CategoryComboTest {
    private CategoryCombo categoryCombo;

    @Before
    public void setUp() throws Exception {
        String id = ModelsDummyData.ID;
        Parcel parcel = Parcel.obtain();
        parcel.writeString(id);
        ModelUtils.setParcelCategories(parcel);
        parcel.setDataPosition(0);
        categoryCombo = CategoryCombo.CREATOR.createFromParcel(parcel);
    }

    @Test
    public void testParcelable(){
        assertEquals(ModelsDummyData.ID, categoryCombo.getId());
        assertEquals(ModelsDummyData.ID, categoryCombo.getCategories().get(0).getId());
    }

    @Test
    public void testParcelableViaIntent(){
        String nameOfExtra = CategoryCombo.class.getSimpleName();
        Intent intent = new Intent();
        intent.putExtra(nameOfExtra, (Parcelable) categoryCombo);
        assertEquals(categoryCombo, intent.getParcelableExtra(nameOfExtra));
    }

}