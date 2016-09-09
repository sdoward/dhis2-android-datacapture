import android.os.Parcel;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.dhis2.mobile.io.holders.BaseFormInfoHolder;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by george on 9/5/16.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class BaseFormInfoHolderTest {

    @Test
    public void testBaseFormParcelable() {
        BaseFormInfoHolder info = new BaseFormInfoHolder();
        info.setFormLabel(DummyDataAndroidTest.FORM_LABEL);
        Parcel parcel = Parcel.obtain();
        info.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        BaseFormInfoHolder parceledInfo = BaseFormInfoHolder.CREATOR.createFromParcel(parcel);
        assertThat(info.getFormLabel(), is(parceledInfo.getFormLabel()));

    }

}
