import android.content.Context;
import android.os.Parcel;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.dhis2.mobile.io.Constants;
import org.dhis2.mobile.io.json.ParsingException;
import org.dhis2.mobile.io.models.Category;
import org.dhis2.mobile.io.models.CategoryCombo;
import org.dhis2.mobile.io.models.CategoryOption;
import org.dhis2.mobile.io.models.Field;
import org.dhis2.mobile.io.models.Form;
import org.dhis2.mobile.io.models.FormOptions;
import org.dhis2.mobile.io.models.Group;
import org.dhis2.mobile.io.models.Option;
import org.dhis2.mobile.io.models.OrganizationUnit;
import org.dhis2.mobile.io.models.eidsr.Disease;
import org.dhis2.mobile.utils.DiseaseImporter;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static android.R.attr.value;
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

    @Test
    public void test2() throws IOException, ParsingException, NoSuchFieldException {
        Context context = InstrumentationRegistry.getTargetContext();

//        JsonObject obj = DiseaseImporter.importDiseases(context);
////      JsonArray array = DiseaseImporter.getCriticalDiseases(context);
//        ArrayList<String> arrayList = DiseaseImporter.getCriticalDiseases(context);
//        Map disables = DiseaseImporter.getDiseasesWithDisabledFields(context);
//        Log.d("OBBBJJJ", obj+ "");
//        Log.d("OBBJJJ", obj.has("ZybYDdwk3O2")+"");
//        Log.d("OBBJJ", obj.get("BR2cqsVasFd").getAsJsonObject().get("isCritical")+"");
//        Log.d("OBBJJ", obj.get("zSzK3FHsJOy").getAsJsonObject().get("disabledFields")+"");
//        Log.d("OBBJJ", obj.get("zSzK3FHsJOy").getAsJsonObject().get("disabledFields").toString().contains("e079rjYqlMH")+"");

//        Log.d("Neewww", arrayList.toString()+"");
//        Log.d("Neewww", arrayList.toString().contains("BR2cqsVasFd")+"");
//
//        Log.d("RAAAAA", disables+"");


        Map diseasez = DiseaseImporter.importDiseases(context);

        Disease df = (Disease) diseasez.get("zSzK3FHsJOy");
        Log.d("LOOKIE", df+"");
        Log.d("COD", df.getLabel());
        Log.d("FIELDS", df.getDisabledFields().toString());
        Log.d("BOOLEAN", df.isCritical()+"");

        Log.d("tsfdsf", Constants.class.getField("UNDER_FIVE_DEATHS").toString());

        String cases = null;
        try {
            cases = String.valueOf(Constants.class.getDeclaredField("UNDER_FIVE_CASES").get(String.class));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Log.d("CONST", cases+"");
        Log.d("CONST", Arrays.toString(Constants.class.getDeclaredFields()).contains("public static final java.lang.String org.dhis2.mobile.io.Constants.UNDER_FIVE_DEATHS")+"");
    }


}
