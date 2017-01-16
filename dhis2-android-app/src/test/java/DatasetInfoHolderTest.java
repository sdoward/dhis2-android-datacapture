import android.content.Intent;
import android.os.Parcel;

import org.dhis2.ehealthMobile.io.holders.DatasetInfoHolder;
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
public class DatasetInfoHolderTest {
    private DatasetInfoHolder datasetInfo;

    @Before
    public void setUp() throws Exception {
        datasetInfo = new DatasetInfoHolder();
        datasetInfo.setFormId(DummyTestData.FORM_ID);
        datasetInfo.setFormLabel(DummyTestData.FORM_LABEL);
        datasetInfo.setOrgUnitId(DummyTestData.ORG_UNIT_ID);
        datasetInfo.setOrgUnitLabel(DummyTestData.ORG_UNIT_LABEL);
        datasetInfo.setPeriod(DummyTestData.PERIOD);
        datasetInfo.setPeriodLabel(DummyTestData.PERIOD_LABEL);
    }

    @Test
    public void testParcelable(){
        Parcel parcel = Parcel.obtain();
        datasetInfo.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        DatasetInfoHolder parceledInfo = DatasetInfoHolder.CREATOR.createFromParcel(parcel);
        assertThat(datasetInfo.getFormLabel(), is(parceledInfo.getFormLabel()));
    }

    @Test
    public void testParcelableViaIntent(){
        String nameOfExtra = DatasetInfoHolder.class.getSimpleName();
        Intent intent = new Intent();
        intent.putExtra(nameOfExtra, datasetInfo);
        assertEquals(datasetInfo, intent.getParcelableExtra(nameOfExtra));
    }

    @Test
    public void testFormRelatedGetters(){
        assertEquals(datasetInfo.getFormId(),DummyTestData.FORM_ID);
        assertEquals(datasetInfo.getFormLabel(),DummyTestData.FORM_LABEL);
    }

    @Test
    public void testOrgUnitRelatedGetters(){
        assertEquals(datasetInfo.getOrgUnitId(),DummyTestData.ORG_UNIT_ID);
        assertEquals(datasetInfo.getOrgUnitLabel(),DummyTestData.ORG_UNIT_LABEL);
    }

    @Test
    public void testPeriodRelatedGetters(){
        assertEquals(datasetInfo.getPeriod(),DummyTestData.PERIOD);
        assertEquals(datasetInfo.getPeriodLabel(),DummyTestData.PERIOD_LABEL);
    }

    @Test
    public void shouldBuildKey(){
        assertThat(DatasetInfoHolder.buildKey(datasetInfo), is(DummyTestData.DATASETINFO_KEY));
    }

}