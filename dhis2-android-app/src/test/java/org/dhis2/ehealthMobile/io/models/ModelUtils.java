package org.dhis2.ehealthMobile.io.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by george on 1/4/17.
 */

class ModelUtils {

    static Parcel setParcelCategoryOptions(Parcel parcel){
        List<CategoryOption> categoryOptions = new ArrayList<>();
        Parcel categoryOptionParcel = Parcel.obtain();

        categoryOptionParcel.writeString(ModelsDummyData.ID);
        categoryOptionParcel.setDataPosition(0);
        CategoryOption categoryOption = CategoryOption.CREATOR.createFromParcel(categoryOptionParcel);
        categoryOptions.add(categoryOption);
        categoryOptionParcel.recycle();

        parcel.writeTypedList(categoryOptions);
        return parcel;
    }

    static Parcel setParcelCategories(Parcel parcel){
        List<Category> categories = new ArrayList<>();
        Parcel categoryParcel = Parcel.obtain();

        categoryParcel.writeString(ModelsDummyData.ID);
        categoryParcel.writeString(ModelsDummyData.LABEL);
        setParcelCategoryOptions(categoryParcel);

        categoryParcel.setDataPosition(0);
        Category category = Category.CREATOR.createFromParcel(categoryParcel);
        categories.add(category);
        categoryParcel.recycle();

        parcel.writeTypedList(categories);
        return parcel;
    }

}
