/**
 * Created by george on 9/2/16.
 */
public class DummyDataAndroidTest {

    public static String GOOD_IMPORT_RESPONSE = "{\"responseType\":\"ImportSummary\",\"status\":\"SUCCESS\",\"description\":\"Import process completed successfully\",\"importCount\":{\"imported\":0,\"updated\":2,\"ignored\":0,\"deleted\":0},\"dataSetComplete\":\"false\"}";

    public static String profileData ="{\"id\":\"mQCdPdUipPj\",\"username\":\"admin\",\"firstName\":\"admin\",\"surname\":\"admin\",\"email\":\"admin@sl.ehealthafrica.org\",\"phoneNumber\":\"+2327600000\",\"gender\":\"gender_male\",\"settings\":{\"keyDbLocale\":null,\"keyMessageSmsNotification\":\"true\",\"keyUiLocale\":\"en\",\"keyAnalysisDisplayProperty\":\"name\",\"keyMessageEmailNotification\":\"true\"}}";

    public static String proocessedProfileData = "{\"firstName\":\"admin\",\"surname\":\"admin\",\"email\":\"admin@sl.ehealthafrica.org\",\"phoneNumber\":\"+2327600000\",\"introduction\":\"\",\"jobTitle\":\"\",\"gender\":\"gender_male\",\"birthday\":\"\",\"nationality\":\"\",\"employer\":\"\",\"education\":\"\",\"interests\":\"\",\"languages\":\"\"}";

    public static final String PERIOD = "2016W33";
    public static final String PERIOD_LABEL = "W33 - 2016-08-15 - 2016-08-21";
    public static final String ORG_UNIT_ID = "tWi9XVPNNue";
    public static final String ORG_UNIT_LABEL = "Benkeh MCHP";
    public static final String FORM_ID = "w9c9UJ3Prus";
    public static final String FORM_LABEL = "IDSR Weekly Disease Report(WDR)_old";
    public static final String DATASETINFO_KEY = "tWi9XVPNNuew9c9UJ3Prus2016W33";

    public static final String GOOD_GET_FORM_RESPONSE = "{\"iQgaTATK59f\":{\"id\":\"iQgaTATK59f\",\"label\":\"Bonthe\",\"level\":2,\"parent\":\"Plmg8ikyfrK\",\"dataSets\":[{\"id\":\"C9bv64TkafG\",\"label\":\"Sabeen Test Form\"},{\"id\":\"PKV9PQ1YXc0\",\"label\":\"Jasper Test Form\"},{\"id\":\"UvsIrD50wUy\",\"label\":\"weekly disease reporting form\"},{\"id\":\"w9c9UJ3Prus\",\"label\":\"IDSR Weekly Disease Report(WDR)_old\"},{\"id\":\"PILVKnAwy4Q\",\"label\":\"eIDSR Report\"},{\"id\":\"hONZE29XBxq\",\"label\":\"Lutz Test Form\"},{\"id\":\"EB5hz0gpUnf\",\"label\":\"Paris Test Form\"},{\"id\":\"h6PK4knx7uW\",\"label\":\"Week report form Test\"},{\"id\":\"rq0LNr72Ndo\",\"label\":\"IDSR Weekly Disease Report(WDR)\"}]}}";


    public static final String GOOD_GET_FORM_RESPONSE_ARRAY = "[{\"iQgaTATK59f\":{\"id\":\"iQgaTATK59f\",\"label\":\"Bonthe\",\"level\":2,\"parent\":\"Plmg8ikyfrK\",\"dataSets\":[{\"id\":\"C9bv64TkafG\",\"label\":\"Sabeen Test Form\"},{\"id\":\"PKV9PQ1YXc0\",\"label\":\"Jasper Test Form\"},{\"id\":\"UvsIrD50wUy\",\"label\":\"weekly disease reporting form\"},{\"id\":\"w9c9UJ3Prus\",\"label\":\"IDSR Weekly Disease Report(WDR)_old\"},{\"id\":\"PILVKnAwy4Q\",\"label\":\"eIDSR Report\"},{\"id\":\"hONZE29XBxq\",\"label\":\"Lutz Test Form\"},{\"id\":\"EB5hz0gpUnf\",\"label\":\"Paris Test Form\"},{\"id\":\"h6PK4knx7uW\",\"label\":\"Week report form Test\"},{\"id\":\"rq0LNr72Ndo\",\"label\":\"IDSR Weekly Disease Report(WDR)\"}]}}]";

    public static final String ASSIGNED_DATA_SETS_RESPONSE= "{\"organisationUnits\":{\"rvphgxdAUQw\":{\"id\":\"rvphgxdAUQw\",\"label\":\"Ngelehun CHP\",\"level\":4,\"parent\":\"t3OhkRzFmGl\",\"dataSets\":[{\"id\":\"rq0LNr72Ndo\",\"label\":\"IDSR Weekly Disease Report(WDR)\"}]}},\"forms\":{\"rq0LNr72Ndo\":{\"label\":\"IDSR Weekly Disease Report(WDR)\",\"subtitle\":\"IDSR Weekly Disease Report(WDR)\",\"options\":{\"periodType\":\"Weekly\",\"openFuturePeriods\":0,\"expiryDays\":0},\"groups\":[{\"label\":\"default\",\"description\":\"default\",\"dataElementCount\":55,\"fields\":[{\"label\":\"EIDSR-Timely Report\",\"dataElement\":\"BpG5Yq4EWMT\",\"categoryOptionCombo\":\"DAv5bv9oqMW\",\"type\":\"BOOLEAN\"},{\"label\":\"EIDSR-Foodborne illnesses EIDSR-<5 years old, cases\",\"dataElement\":\"iHJsq3zGWvM\",\"categoryOptionCombo\":\"e079rjYqlMH\",\"type\":\"INTEGER_ZERO_OR_POSITIVE\"},{\"label\":\"EIDSR-Foodborne illnesses EIDSR-<5 years old, deaths\",\"dataElement\":\"iHJsq3zGWvM\",\"categoryOptionCombo\":\"C5qLGpw4uet\",\"type\":\"INTEGER_ZERO_OR_POSITIVE\"},{\"label\":\"EIDSR-Foodborne illnesses EIDSR->5 years old, cases\",\"dataElement\":\"iHJsq3zGWvM\",\"categoryOptionCombo\":\"jpmVF8rhBNA\",\"type\":\"INTEGER_ZERO_OR_POSITIVE\"}]}]}}}";

    public static final String GOOD_REPORTS_DOWNLOAD_RESPONSE =  "{\"label\":\"IDSR Weekly Disease Report(WDR)_old\",\"subtitle\":\"(WDR)\",\"options\":{\"periodType\":\"Weekly\",\"openFuturePeriods\":0,\"expiryDays\":0},\"groups\":[{\"label\":\"default\",\"description\":\"default\",\"dataElementCount\":36,\"fields\":[{\"label\":\"EIDSR-Yellow fever EIDSR-<5 years old, cases\",\"dataElement\":\"KPMZxGkiqiQ\",\"categoryOptionCombo\":\"e079rjYqlMH\",\"type\":\"INTEGER_ZERO_OR_POSITIVE\"},{\"label\":\"EIDSR-Yellow fever EIDSR-<5 years old, deaths\",\"dataElement\":\"KPMZxGkiqiQ\",\"categoryOptionCombo\":\"C5qLGpw4uet\",\"type\":\"INTEGER_ZERO_OR_POSITIVE\"},{\"label\":\"EIDSR-Yellow fever EIDSR->5 years old, cases\",\"dataElement\":\"KPMZxGkiqiQ\",\"categoryOptionCombo\":\"jpmVF8rhBNA\",\"type\":\"INTEGER_ZERO_OR_POSITIVE\"},{\"label\":\"EIDSR-Yellow fever EIDSR->5 years old, deaths\",\"dataElement\":\"KPMZxGkiqiQ\",\"categoryOptionCombo\":\"Ciavs7v4qYa\",\"type\":\"INTEGER_ZERO_OR_POSITIVE\"}]}]}";

    public static final String FIELD_LABEL="EIDSR-Yellow fever EIDSR-<5 years old, cases";
    public static final String FIELD_DATA_ELEMENT="KPMZxGkiqiQ";
    public static final String FIELD_CATEGORY_OPTION_COMBO="e079rjYqlMH";
    public static final String FIELD_TYPE = "INTEGER_ZERO_OR_POSITIVE";
    public static final String GROUP_LABEL = "default";

    public static final String REPORT_UPLOAD_REQUEST_BODY="{\"orgUnit\":\"tWi9XVPNNue\",\"dataSet\":\"w9c9UJ3Prus\",\"period\":\"2016W33\",\"completeDate\":\"2016-09-15\",\"dataValues\":[{\"dataElement\":\"KPMZxGkiqiQ\",\"categoryOptionCombo\":null,\"value\":\"2\"},{\"dataElement\":\"BpG5Yq4EWMT\",\"value\":false}]}";

}
