import android.content.Intent;
import android.os.Parcel;

import org.dhis2.ehealthMobile.io.holders.BaseFormInfoHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by george on 1/3/17.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class BaseFormInfoHolderTest {

    @Test
    public void testBaseFormParcelable(){
        BaseFormInfoHolder info = new BaseFormInfoHolder();
        info.setFormLabel(DummyTestData.FORM_LABEL);
        Parcel parcel = Parcel.obtain();
        info.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        BaseFormInfoHolder parceledInfo = BaseFormInfoHolder.CREATOR.createFromParcel(parcel);
        assertThat(info.getFormLabel(), is(parceledInfo.getFormLabel()));
    }

    @Test
    public void testBaseFormParcelableViaIntent() {
        String nameOfExtra = BaseFormInfoHolder.class.getSimpleName();
        BaseFormInfoHolder info = new BaseFormInfoHolder();
        info.setFormLabel(DummyTestData.FORM_LABEL);
        Intent intent = new Intent();
        intent.putExtra(nameOfExtra, info);
        assertEquals(info, intent.getParcelableExtra(nameOfExtra));
    }

    @Test
    public void testSettersAndGetters(){
        BaseFormInfoHolder info = new BaseFormInfoHolder();
        info.setFormId(DummyTestData.ID);
        info.setFormLabel(DummyTestData.FORM_LABEL);
        info.setOrgUnitId(DummyTestData.ID);
        info.setOrgUnitLabel(DummyTestData.ORG_UNIT_LABEL);

        assertEquals(info.getFormId(), DummyTestData.ID);
        assertEquals(info.getFormLabel(), DummyTestData.FORM_LABEL);
        assertEquals(info.getOrgUnitId(), DummyTestData.ID);
        assertEquals(info.getOrgUnitLabel(), DummyTestData.ORG_UNIT_LABEL);
    }

}