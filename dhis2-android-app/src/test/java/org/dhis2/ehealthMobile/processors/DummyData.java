package org.dhis2.ehealthMobile.processors;

/**
 * Created by george on 1/5/17.
 */

class DummyData {
    static final String ASSIGNED_DATA_SETS_RESPONSE= "{\"organisationUnits\":{\"rvphgxdAUQw\":{\"id\":\"rvphgxdAUQw\",\"label\":\"Ngelehun CHP\",\"level\":4,\"parent\":\"t3OhkRzFmGl\",\"dataSets\":[{\"id\":\"rq0LNr72Ndo\",\"label\":\"IDSR Weekly DiseaseImporter Report(WDR)\"}]}},\"forms\":{\"rq0LNr72Ndo\":{\"label\":\"IDSR Weekly DiseaseImporter Report(WDR)\",\"subtitle\":\"IDSR Weekly DiseaseImporter Report(WDR)\",\"options\":{\"periodType\":\"Weekly\",\"openFuturePeriods\":0,\"expiryDays\":0},\"groups\":[{\"label\":\"default\",\"description\":\"default\",\"dataElementCount\":55,\"fields\":[{\"label\":\"EIDSR-Timely Report\",\"dataElement\":\"BpG5Yq4EWMT\",\"categoryOptionCombo\":\"DAv5bv9oqMW\",\"type\":\"BOOLEAN\"},{\"label\":\"EIDSR-Foodborne illnesses EIDSR-<5 years old, cases\",\"dataElement\":\"iHJsq3zGWvM\",\"categoryOptionCombo\":\"e079rjYqlMH\",\"type\":\"INTEGER_ZERO_OR_POSITIVE\"},{\"label\":\"EIDSR-Foodborne illnesses EIDSR-<5 years old, deaths\",\"dataElement\":\"iHJsq3zGWvM\",\"categoryOptionCombo\":\"C5qLGpw4uet\",\"type\":\"INTEGER_ZERO_OR_POSITIVE\"},{\"label\":\"EIDSR-Foodborne illnesses EIDSR->5 years old, cases\",\"dataElement\":\"iHJsq3zGWvM\",\"categoryOptionCombo\":\"jpmVF8rhBNA\",\"type\":\"INTEGER_ZERO_OR_POSITIVE\"}]}]}}}";
    static final String FIELD_LABEL="EIDSR-Yellow fever EIDSR-<5 years old, cases";
    static final String FIELD_DATA_ELEMENT="KPMZxGkiqiQ";
    static final String FIELD_VALUE = "value";
    static final String USER_PROFILE = "{\"id\":\"mQCdPdUipPj\",\"username\":\"foo\",\"firstName\":\"Foo\",\"surname\":\"Bar\",\"email\":\"foo@foobar.com\",\"phoneNumber\":\"+23276\",\"gender\":\"gender_male\",\"settings\":{\"keyDbLocale\":null,\"keyMessageSmsNotification\":\"true\",\"keyUiLocale\":\"en\",\"keyAnalysisDisplayProperty\":\"name\",\"keyMessageEmailNotification\":\"true\"}}";
    static final String GOOD_IMPORT_RESPONSE = "{\"responseType\":\"ImportSummary\",\"status\":\"SUCCESS\",\"description\":\"Import process completed successfully\",\"importCount\":{\"imported\":0,\"updated\":2,\"ignored\":0,\"deleted\":0},\"dataSetComplete\":\"false\"}";
    static final String API_USER_ACCOUNT_URL = "/api/me/user-account";
    static final String DATASETS_URL = "/api/me/assignedDataSets";
}
