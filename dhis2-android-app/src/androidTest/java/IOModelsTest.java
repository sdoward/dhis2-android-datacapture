import android.os.Parcel;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.dhis2.mobile.io.models.Category;
import org.dhis2.mobile.io.models.CategoryCombo;
import org.dhis2.mobile.io.models.CategoryOption;
import org.dhis2.mobile.io.models.Field;
import org.dhis2.mobile.io.models.Form;
import org.dhis2.mobile.io.models.FormOptions;
import org.dhis2.mobile.io.models.Group;
import org.dhis2.mobile.io.models.Option;
import org.dhis2.mobile.io.models.OrganizationUnit;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by george on 9/5/16.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class IOModelsTest {

    @Test
    public void testCategoryModelParcelable(){
        Category category = new Category();
        category.setLabel(DummyDataAndroidTest.FORM_LABEL);
        Parcel parcel = Parcel.obtain();
        category.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Category parceledCategory = Category.CREATOR.createFromParcel(parcel);
        assertThat(category.getLabel(), is(parceledCategory.getLabel()));
    }

    @Test
    public void testCategoryComboParcelable(){
        CategoryCombo combo = new CategoryCombo();
        combo.setId(DummyDataAndroidTest.FORM_ID);
        Parcel parcel = Parcel.obtain();
        combo.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        CategoryCombo parceledCombo = CategoryCombo.CREATOR.createFromParcel(parcel);
        assertThat(combo.getId(), is(parceledCombo.getId()));

    }

    @Test
    public void testCategoryOptionParcelable(){
        CategoryOption option = new CategoryOption();
        option.setId(DummyDataAndroidTest.FORM_ID);
        Parcel parcel = Parcel.obtain();
        option.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        CategoryOption parceledOption  = CategoryOption.CREATOR.createFromParcel(parcel);
        assertThat(option.getId(), is(parceledOption.getId()));
    }

    @Test
    public void testFieldParcelable(){
        Field field = new Field();
        field.setLabel(DummyDataAndroidTest.FORM_LABEL);
        Parcel parcel = Parcel.obtain();
        field.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Field parceledField = Field.CREATOR.createFromParcel(parcel);
        assertThat(field.getLabel(), is(parceledField.getLabel()));
    }

    @Test
    public void testFormPracelable(){
        Form form = new Form();
        form.setId(DummyDataAndroidTest.FORM_ID);
        Parcel parcel = Parcel.obtain();
        form.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Form parceledForm = Form.CREATOR.createFromParcel(parcel);
        assertThat(form.getLabel(), is(parceledForm.getLabel()));
    }

    @Test
    public void testFormOptionsParcelable(){
        FormOptions options = new FormOptions();
        options.setDescription(DummyDataAndroidTest.PERIOD_LABEL);
        Parcel parcel = Parcel.obtain();
        options.writeToParcel(parcel,0);
        parcel.setDataPosition(0);
        FormOptions parceledOptions = FormOptions.CREATOR.createFromParcel(parcel);
        assertThat(options.getDescription(), is(parceledOptions.getDescription()));
    }

    @Test
    public void testGroupParcelable(){
        Group group = new Group();
        group.setLabel(DummyDataAndroidTest.FORM_LABEL);
        Parcel parcel = Parcel.obtain();
        group.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Group parceledGroup = Group.CREATOR.createFromParcel(parcel);
        assertThat(group.getLabel(), is(parceledGroup.getLabel()));
    }

    @Test
    public void testOptionParcelable() throws IOException {
        Option option = new Option();
        option.setId(DummyDataAndroidTest.FORM_ID);
      //  new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(myObject);


    }

    @Test
    public void testOrganizationUnitParcelable(){
        OrganizationUnit org = new OrganizationUnit();
        org.setId(DummyDataAndroidTest.ORG_UNIT_ID);
        Parcel parcel = Parcel.obtain();
        org.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        OrganizationUnit parceledOrg = OrganizationUnit.CREATOR.createFromParcel(parcel);
        assertThat(org.getId(), is(parceledOrg.getId()));
    }

}
