package org.dhis2.ehealthMobile.io.models;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by george on 1/3/17.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class CategoryTest {
    private Category category;

    @Before
    public void setUp() throws Exception {
        String id = ModelsDummyData.ID;
        String label = ModelsDummyData.LABEL;
        Parcel parcel = Parcel.obtain();
        parcel.writeString(id);
        parcel.writeString(label);
        ModelUtils.setParcelCategoryOptions(parcel);
        parcel.setDataPosition(0);
        category = Category.CREATOR.createFromParcel(parcel);
    }

    @Test
    public void testParcelable(){
        assertEquals(ModelsDummyData.ID, category.getId());
        assertEquals(ModelsDummyData.LABEL, category.getLabel());
        assertEquals(ModelsDummyData.ID, category.getCategoryOptions().get(0).getId());
    }

    @Test
    public void testParcelableViaIntent(){
        final String nameOfExtra = Category.class.getSimpleName();
        Intent intent = new Intent();
        intent.putExtra(nameOfExtra, (Parcelable) category);
        assertEquals(category, intent.getParcelableExtra(nameOfExtra));
    }

}