import android.os.Parcel;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.dhis2.mobile.io.holders.DatasetInfoHolder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by george on 9/5/16.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class DatasetInfoHolderTest {
    private DatasetInfoHolder datasetInfo;

    @Before
    public void setup(){
        datasetInfo = new DatasetInfoHolder();
        datasetInfo.setPeriod(DummyDataAndroidTest.PERIOD);
        datasetInfo.setPeriodLabel(DummyDataAndroidTest.PERIOD_LABEL);
        datasetInfo.setOrgUnitId(DummyDataAndroidTest.ORG_UNIT_ID);
        datasetInfo.setOrgUnitLabel(DummyDataAndroidTest.ORG_UNIT_LABEL);
        datasetInfo.setFormId(DummyDataAndroidTest.FORM_ID);
        datasetInfo.setFormLabel(DummyDataAndroidTest.FORM_LABEL);
    }

    @Test
    public void testParcelable() {
        DatasetInfoHolder info = new DatasetInfoHolder();
        info.setFormLabel(DummyDataAndroidTest.FORM_LABEL);
        Parcel parcel = Parcel.obtain();
        info.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        DatasetInfoHolder parceledInfo = DatasetInfoHolder.CREATOR.createFromParcel(parcel);
        assertThat(info.getFormLabel(), is(parceledInfo.getFormLabel()));

    }

    @Test
    public void shouldBuildKey(){
        assertThat(DatasetInfoHolder.buildKey(datasetInfo), is(DummyDataAndroidTest.DATASETINFO_KEY));
    }
}
