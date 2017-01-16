package org.dhis2.ehealthMobile.ui.activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.dhis2.ehealthMobile.R;
import org.dhis2.ehealthMobile.WorkService;
import org.dhis2.ehealthMobile.io.Constants;
import org.dhis2.ehealthMobile.io.holders.DatasetInfoHolder;
import org.dhis2.ehealthMobile.io.json.JsonHandler;
import org.dhis2.ehealthMobile.io.json.ParsingException;
import org.dhis2.ehealthMobile.io.models.Field;
import org.dhis2.ehealthMobile.io.models.Form;
import org.dhis2.ehealthMobile.io.models.Group;
import org.dhis2.ehealthMobile.network.HTTPClient;
import org.dhis2.ehealthMobile.network.NetworkUtils;
import org.dhis2.ehealthMobile.network.Response;
import org.dhis2.ehealthMobile.processors.ReportUploadProcessor;
import org.dhis2.ehealthMobile.processors.SubmissionDetailsProcessor;
import org.dhis2.ehealthMobile.ui.adapters.dataEntry.FieldAdapter;
import org.dhis2.ehealthMobile.ui.adapters.dataEntry.rows.PosOrZeroIntegerRow2;
import org.dhis2.ehealthMobile.ui.fragments.AdditionalDiseasesFragment;
import org.dhis2.ehealthMobile.utils.IsDisabled;
import org.dhis2.ehealthMobile.utils.PrefUtils;
import org.dhis2.ehealthMobile.utils.TextFileUtils;
import org.dhis2.ehealthMobile.utils.ToastManager;
import org.dhis2.ehealthMobile.utils.ViewUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.text.TextUtils.isEmpty;

public class DataEntryActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Form> {
    public static final String TAG = DataEntryActivity.class.getSimpleName();

    // state keys
    private static final String STATE_REPORT = "state:report";
    private static final String STATE_DOWNLOAD_ATTEMPTED = "state:downloadAttempted";
    private static final String STATE_DOWNLOAD_IN_PROGRESS = "state:downloadInProgress";

    private String compulsoryData;

    // loader ids
    private static final int LOADER_FORM_ID = 896927645;

    // views
    private View uploadButton;
    private View addDiseaseButton;
    private View persistentButtonsFooter;
    private RelativeLayout progressBarLayout;
    private AppCompatSpinner formGroupSpinner;
    private View submissionDetailsLayout;
    private View submissionDetailsExpandableLayout;
    private TextView completionDate;
    private TextView submissionMethod;
    private ImageView isTimelyIcon;
    private View listViewHeader;
    //expands the submission details view on click
    private Button expandButton;

    // data entry view
    private ListView dataEntryListView;
    public List<FieldAdapter> adapters;

    // state
    private boolean downloadAttempted;

    //info
    private static DatasetInfoHolder infoHolder;

    //delete disease alert dialog
    private AlertDialog deleteDiseaseDialog;
    //compulsory disease alert dialog;
    private AlertDialog compulsoryDataDialog;

    // key for additional diseases that have been displayed on the list.
    public static final String ALREADY_DISPLAYED = "alreadyDisplayed";

    private Map additionalDiseaseIds = new HashMap();

    private EditText commentField;
    private IsDisabled isDisabled;

    public static void navigateTo(Activity activity, DatasetInfoHolder info) {
        if (info != null && activity != null) {
            infoHolder = info;
            Intent intent = new Intent(activity, DataEntryActivity.class);
            intent.putExtra(DatasetInfoHolder.TAG, info);



            activity.startActivity(intent);
            activity.overridePendingTransition(
                    R.anim.slide_up, R.anim.activity_open_exit);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);


        setupToolbar();
        setupFormSpinner();
        setupProgressBar(savedInstanceState);

        setupListView();
        setupScrollView();
        persistentButtonsFooter = findViewById(R.id.persistent_buttons_footer);
        setupUploadButton();
        setupAddDiseaseBtn();
        setupDeleteDialog();
        setupCompulsoryFieldsDialog();
        isDisabled = new IsDisabled(getApplicationContext(), infoHolder);
        setupSubmissionDetailsViews();

        // let's try to get latest values from API
        attemptToDownloadReport(savedInstanceState);

        // if we are downloading values, build form
        buildReportDataEntryForm(savedInstanceState);


    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(RECEIVER);

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(RECEIVER, new IntentFilter(TAG));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (adapters != null) {
            ArrayList<Group> groups = new ArrayList<>();
            for (FieldAdapter adapter : adapters) {
                groups.add(adapter.getGroup());
            }

            outState.putParcelableArrayList(STATE_REPORT, groups);
            outState.putBoolean(STATE_DOWNLOAD_ATTEMPTED, downloadAttempted);
            outState.putBoolean(STATE_DOWNLOAD_IN_PROGRESS, isProgressBarVisible());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_close_enter, R.anim.slide_down);
    }

    @Override
    public Loader<Form> onCreateLoader(int id, Bundle args) {
        DatasetInfoHolder info = getIntent().getExtras()
                .getParcelable(DatasetInfoHolder.TAG);

        if (id == LOADER_FORM_ID && info != null) {
            return new DataLoader(DataEntryActivity.this, info);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Form> loader, Form form) {
        if (loader != null && loader.getId() == LOADER_FORM_ID) {
            loadGroupsIntoAdapters(form.getGroups());
            setupCommentRowAsFooter(form);
        }
    }

    @Override
    public void onLoaderReset(Loader<Form> loader) {
    }

    private void restartLoader(){
        getSupportLoaderManager().restartLoader(LOADER_FORM_ID, null, this).forceLoad();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            toolbar.setTitle(infoHolder.getOrgUnitLabel());
            String weekNumber = getString(R.string.toolbar_week_prefix) +" "+ infoHolder.getPeriodLabel().substring(1,3);
            toolbar.setSubtitle(weekNumber);
        }
    }

    private void setupFormSpinner() {
        formGroupSpinner = (AppCompatSpinner) findViewById(R.id.spinner_drop_down);

        if (formGroupSpinner != null) {
            formGroupSpinner.setVisibility(View.GONE);
        }
    }

    private void setupProgressBar(Bundle savedInstanceState) {
        progressBarLayout = (RelativeLayout) findViewById(
                R.id.relativelayout_progress_bar);

        if (savedInstanceState != null) {
            boolean downloadInProgress = savedInstanceState
                    .getBoolean(STATE_DOWNLOAD_IN_PROGRESS, false);

            if (downloadInProgress) {
                showProgressBar();
            } else {
                hideProgressBar();
            }
        } else {
            hideProgressBar();
        }
    }

    private void setupScrollView(){
        NestedScrollView scroll = (NestedScrollView)findViewById(R.id.scrollView);
        if(scroll != null){
            //Solves random editTexts getting focus error
            scroll.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
            scroll.setFocusable(true);
            scroll.setFocusableInTouchMode(true);
            scroll.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.requestFocusFromTouch();
                    return false;
                }
            });
        }
    }

    private void setupListView() {
        dataEntryListView = (ListView) findViewById(R.id.list_of_fields);
        /**
         *         When the focused view (EditText) goes off screen, the keyboard which would be in number mode switches to full qwerty mode.
         *         Now when the focused view goes off screen we want to hide the keyboard instead.
         */

        dataEntryListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                //Solves parameter must be a descendant of this view error
                if (SCROLL_STATE_TOUCH_SCROLL == scrollState) {
                    View currentFocus = getCurrentFocus();
                    if (currentFocus != null && !(currentFocus instanceof EditText)) {
                        currentFocus.clearFocus();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(!(getCurrentFocus() instanceof EditText)){
                    hideKeyboard();
                }

            }
        });

    }

    private void setupUploadButton() {
        uploadButton = findViewById(R.id.send_button);
        uploadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                upload();
            }
        });
    }

    private void setupAddDiseaseBtn(){
        final DatasetInfoHolder info = getIntent().getExtras()
                .getParcelable(DatasetInfoHolder.TAG);
        addDiseaseButton = findViewById(R.id.add_button);
        addDiseaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdditionalDiseasesFragment additionalDiseasesFragment = new AdditionalDiseasesFragment();
                Bundle args = new Bundle();
                args.putString(ALREADY_DISPLAYED, additionalDiseaseIds.keySet().toString());
                args.putString(Form.TAG, info.getFormId());
                additionalDiseasesFragment.setArguments(args);
                additionalDiseasesFragment.show(getSupportFragmentManager(), TAG);

            }
        });
    }

    private void setupDeleteDialog(){
        deleteDiseaseDialog =  new AlertDialog.Builder(this).create();
    }

    private void showDeleteDiseaseDialog(final String tag, final int pos){
        String title = getResources().getString(R.string.delete_disease_dialog_title);
        String confirmationText = getResources().getString(R.string.delete_disease_dialog_confirmation);
        String rejectionText = getResources().getString(R.string.delete_disease_dialog_rejection);
        deleteDiseaseDialog.setTitle(title);
        deleteDiseaseDialog.setMessage("Are you sure you want to delete "+
                additionalDiseaseIds.get(tag).toString().split("EIDSR-")[1]+" and all its values");
        deleteDiseaseDialog.setButton(DialogInterface.BUTTON_POSITIVE, confirmationText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                additionalDiseaseIds.remove(tag);
                adapters.get(0).removeItemAtPosition(pos);
                deleteDiseaseDialog.dismiss();
            }
        });
        deleteDiseaseDialog.setButton(DialogInterface.BUTTON_NEGATIVE, rejectionText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteDiseaseDialog.dismiss();
            }
        });
        deleteDiseaseDialog.setCanceledOnTouchOutside(false);
        deleteDiseaseDialog.show();
    }

    private void setupCompulsoryFieldsDialog(){
        compulsoryDataDialog = new AlertDialog.Builder(this).create();
    }

    private void showCompulsoryFieldsDialog(){
        compulsoryDataDialog.setTitle(getResources().getString(R.string.compulsory_data_dialog_title));
        compulsoryDataDialog.setMessage(getResources().getString(R.string.compulsory_data_dialog_message));
        compulsoryDataDialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.compulsory_data_dialog_confirmation),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                compulsoryDataDialog.dismiss();
            }
        });
        compulsoryDataDialog.setCanceledOnTouchOutside(false);
        compulsoryDataDialog.show();
    }

    private void setupSubmissionDetailsViews(){
        String rotation = "rotation";
        submissionDetailsLayout = findViewById(R.id.submissionDetails);
        submissionDetailsExpandableLayout = findViewById(R.id.submissionDetailsExpandable);
        completionDate = (TextView) findViewById(R.id.completionDate);
        submissionMethod = (TextView) findViewById(R.id.submissionMethod);
        isTimelyIcon = (ImageView) findViewById(R.id.isTimelyIcon);
        expandButton = (Button) findViewById(R.id.expandButton);

        listViewHeader = findViewById(R.id.listViewHeader);

        final ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(listViewHeader, "y", 1);
        headerAnimator.setDuration(200);

        final ObjectAnimator listViewAnimator = ObjectAnimator.ofFloat(dataEntryListView, "y", 1);
        listViewAnimator.setDuration(200);

        final ObjectAnimator buttonAnimator = ObjectAnimator.ofFloat(expandButton, "x", 180);
        buttonAnimator.setPropertyName(rotation);
        listViewAnimator.setDuration(200);

        expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(submissionDetailsExpandableLayout.isEnabled()){
                    //hide views and adjust layout
                    ViewUtils.perfomOutAnimation(getApplicationContext(), R.anim.fade_out,true, submissionDetailsExpandableLayout);
                    reverseAnimators(buttonAnimator, headerAnimator, listViewAnimator);
                }else {
                    //show views and adjust layout
                    ViewUtils.perfomInAnimation(getApplicationContext(), R.anim.fade_in, submissionDetailsExpandableLayout);
                    startAnimators(buttonAnimator, headerAnimator, listViewAnimator);
                }
            }
        });

        ViewUtils.hideAndDisableViews(submissionDetailsLayout, submissionDetailsExpandableLayout);
    }

    private void attemptToDownloadReport(Bundle savedInstanceState) {
        // first, we need to check if previous instances of
        // activities already tried to download values
        if (savedInstanceState != null) {
            downloadAttempted = savedInstanceState
                    .getBoolean(STATE_DOWNLOAD_ATTEMPTED, false);
        }

        if (!downloadAttempted && !isProgressBarVisible()) {
            downloadAttempted = true;

            // we need to check if connection is there first
            if (NetworkUtils.checkConnection(this)) {
                getCompletionDate();
            }else{
                compulsoryData = PrefUtils.getCompulsoryDiseases(getApplicationContext(), infoHolder.getFormId());
            }

        }
    }

    private void buildReportDataEntryForm(Bundle savedInstanceState) {
        if (!isProgressBarVisible()) {
            List<Group> dataEntryGroups = null;

            if (savedInstanceState != null &&
                    savedInstanceState.containsKey(STATE_REPORT)) {
                dataEntryGroups = savedInstanceState
                        .getParcelableArrayList(STATE_REPORT);
            }

            // we did not load form before,
            // so we need to do so now
            if (dataEntryGroups == null) {
                getSupportLoaderManager().restartLoader(LOADER_FORM_ID, null, this).forceLoad();
            } else {
                loadGroupsIntoAdapters(dataEntryGroups);
            }
        }
    }

    private void showProgressBar() {
        ViewUtils.hideAndDisableViews(persistentButtonsFooter, uploadButton, dataEntryListView,submissionDetailsLayout);
        ViewUtils.enableViews(progressBarLayout);
    }

    private void hideProgressBar() {
        ViewUtils.enableViews(persistentButtonsFooter, uploadButton, dataEntryListView);
        ViewUtils.hideAndDisableViews(progressBarLayout);
    }

    private boolean isProgressBarVisible() {
        return progressBarLayout.getVisibility() == View.VISIBLE;
    }

    private void loadGroupsIntoAdapters(List<Group> groups) {
        DatasetInfoHolder info = getIntent().getExtras()
                .getParcelable(DatasetInfoHolder.TAG);
        if (groups != null) {
            List<FieldAdapter> adapters = new ArrayList<>();

            try {
                for (Group group : groups) {
                    adapters.add(new FieldAdapter(info, group, this));
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }



            setupAdapters(adapters);
        }
    }

    private void setupAdapters(final List<FieldAdapter> adapters) {
        this.adapters = adapters;

        if (adapters.size() == 1) {
            formGroupSpinner.setVisibility(View.GONE);
            dataEntryListView.setAdapter(adapters.get(0));
            return;
        }

        List<String> formGroupLabels = new ArrayList<>();
        for (FieldAdapter fieldAdapter : adapters) {
            formGroupLabels.add(fieldAdapter.getLabel());
        }

        Log.i("Forms", formGroupLabels.toString());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(),
                R.layout.spinner_item, formGroupLabels);
        adapter.setDropDownViewResource(R.layout.dropdown_spinner_item);

        formGroupSpinner.setVisibility(View.VISIBLE);
        formGroupSpinner.setAdapter(adapter);
        formGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dataEntryListView.setAdapter(adapters.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // stub implementation
            }
        });
    }

    private void upload() {
        if (adapters == null) {
            ToastManager.makeToast(this, getString(R.string.something_went_wrong),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<Group> groups = new ArrayList<>();
        for (FieldAdapter adapter : adapters) {
            groups.add(adapter.getGroup());
        }

        //Add the comment in list view footer to group data.
        addFooterCommentToGroup(groups.get(0));

        DatasetInfoHolder info = getIntent().getExtras()
                .getParcelable(DatasetInfoHolder.TAG);

        Intent intent = new Intent(this, WorkService.class);
        //Check if network is available. If not send via sms or else just upload via internet
        if(!NetworkUtils.checkConnection(getApplicationContext())){
            intent.putExtra(WorkService.METHOD, WorkService.METHOD_SEND_VIA_SMS);
        }else {
            intent.putExtra(WorkService.METHOD, WorkService.METHOD_UPLOAD_DATASET);
        }
        intent.putExtra(DatasetInfoHolder.TAG, info);
        intent.putExtra(Group.TAG, groups);

        if(isInvalidForm(groups)){
            showCompulsoryFieldsDialog();
        }else {
            removeInProgressDataset(getApplicationContext(), info);
            startService(intent);
            finish();
        }

    }

    private Boolean isInvalidForm(ArrayList<Group> groups){
        for(Group group: groups){
            for(Field field : group.getFields()){
                if(compulsoryData != null && compulsoryData.contains(field.getDataElement()) && !isDisabled.check(field)
                        && field.getValue().equals("")){
                    return true;
                }
            }
        }
        return false;
    }

    private void getLatestValues() {
        // this should be one operation (instead of two)
        showProgressBar();

        DatasetInfoHolder info = getIntent().getExtras()
                .getParcelable(DatasetInfoHolder.TAG);

        Intent intent = new Intent(this, WorkService.class);
        intent.putExtra(WorkService.METHOD,
                WorkService.METHOD_DOWNLOAD_LATEST_DATASET_VALUES);
        intent.putExtra(DatasetInfoHolder.TAG, info);
        startService(intent);
    }

    private void getCompletionDate(){
        showProgressBar();
        DatasetInfoHolder info = getIntent().getExtras()
                .getParcelable(DatasetInfoHolder.TAG);

        Intent intent = new Intent(this, WorkService.class);
        intent.putExtra(WorkService.METHOD, WorkService.METHOD_DOWNLOAD_SUBMISSION_DETAILS);
        intent.putExtra(DatasetInfoHolder.TAG, info);

        startService(intent);
    }

    private final BroadcastReceiver RECEIVER = new BroadcastReceiver() {

        @Override
        public void onReceive(Context cxt, Intent intent) {
            hideProgressBar();

            //check if intent has a server response code. This would mean the WorkService has finished making its API call
            if(intent.getExtras().containsKey(Response.CODE)) {
                int code = intent.getExtras().getInt(Response.CODE);
                if (HTTPClient.isError(code)) {
                    // load form from disk
                    getSupportLoaderManager().restartLoader(LOADER_FORM_ID, null,
                            DataEntryActivity.this).forceLoad();
                    return;
                }

                if (intent.getExtras().containsKey(Response.BODY)) {
                    Form form = intent.getExtras().getParcelable(Response.BODY);

                    if (form != null) {
                        loadGroupsIntoAdapters(form.getGroups());
                        handleSubmissionDetails(form.getGroups());
                        setupCommentRowAsFooter(form);
                        DatasetInfoHolder info = getIntent().getExtras()
                                .getParcelable(DatasetInfoHolder.TAG);
                        String key = DatasetInfoHolder.getSubmissionKey(info);
                        if(TextFileUtils.findFile(
                                getApplicationContext(), TextFileUtils.Directory.IN_PROGRESS_DATASETS, key).exists()){
                            removeInProgressDataset(getApplicationContext(), info);
                        }
                    }
                }

                if (intent.getExtras().containsKey(SubmissionDetailsProcessor.SUBMISSION_DETAILS)) {
                    if(intent.getExtras().getString(SubmissionDetailsProcessor.SUBMISSION_DETAILS) != null){
                        handleCompletionDate(intent.getExtras().getString(SubmissionDetailsProcessor.SUBMISSION_DETAILS));
                        //Get form values if it has a completed date.
                        getLatestValues();
                    }else{
                        //Without completion date restart the data loader to load either an empty, in progress or offline form
                        restartLoader();
                    }
                }
            }
            //if not check if intent was called from a row in the listView. Meaning that the delete button was clicked.
            else if(intent.getExtras().containsKey(PosOrZeroIntegerRow2.TAG)){
                //Get tag (unique ID belonging to every row)
                String tag = intent.getExtras().getString(PosOrZeroIntegerRow2.TAG);
                ///get the view (Row) based on the TAG (unique ID)
                View view = dataEntryListView.findViewWithTag(tag);
                //Get the position of that view (Row) in the listView.
                int position = dataEntryListView.getPositionForView(view);
                showDeleteDiseaseDialog(tag, position);

            }
        }
    };

    private static class DataLoader extends AsyncTaskLoader<Form> {
        private final DatasetInfoHolder infoHolder;

        public DataLoader(Context context, DatasetInfoHolder infoHolder) {
            super(context);
            this.infoHolder = infoHolder;
        }

        @Override
        public Form loadInBackground() {
            if (infoHolder.getFormId() != null && TextFileUtils.findFile(
                    getContext(), TextFileUtils.Directory.DATASETS, infoHolder.getFormId()).exists()) {
                Form form = loadForm();

                // try to fit values
                // from storage into form
                loadValuesIntoForm(form);

                return form;
            }
            return null;
        }

        private Form loadForm() {
            String jForm = TextFileUtils.readTextFile(
                    getContext(), TextFileUtils.Directory.DATASETS, infoHolder.getFormId());
            try {
                JsonObject jsonForm = JsonHandler.buildJsonObject(jForm);
                return JsonHandler.fromJson(jsonForm, Form.class);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (ParsingException e) {
                e.printStackTrace();
            }

            return null;
        }

        private void loadValuesIntoForm(Form form) {
            if (form == null || form.getGroups() == null || form.getGroups().isEmpty()) {
                return;
            }

            String reportKey = DatasetInfoHolder.getSubmissionKey(infoHolder);
            if (isEmpty(reportKey)) {
                return;
            }

            String report = loadReport(reportKey);
            if (isEmpty(report)) {
                return;
            }

            Map<String, String> fieldMap = new HashMap<>();

            try {
                JsonObject jsonReport = JsonHandler.buildJsonObject(report);
                JsonArray jsonElements = jsonReport.getAsJsonArray(Constants.DATA_VALUES);

                fieldMap = buildFieldsMap(jsonElements);
            } catch (ParsingException e) {
                e.printStackTrace();
            }

            if (!fieldMap.keySet().isEmpty()) {
                // fill form with values

                for (Group group : form.getGroups()) {
                    if (group.getFields() == null || group.getFields().isEmpty()) {
                        continue;
                    }

                    for (Field field : group.getFields()) {
                        String key = buildFieldKey(field.getDataElement(),
                                field.getCategoryOptionCombo());

                        String value = fieldMap.get(key);
                        if (!isEmpty(value)) {
                            field.setValue(value);
                        }
                    }
                }
            }
        }

        private String loadReport(String reportKey) {
            if (isEmpty(reportKey)) {
                return null;
            }

            File progressFile = TextFileUtils.findFile(getContext(), TextFileUtils.Directory.IN_PROGRESS_DATASETS, reportKey);

            if(progressFile.exists()){
                return TextFileUtils.readTextFile(progressFile);
            }


            File offlineDatasetFile = TextFileUtils.findFile(getContext(), TextFileUtils.Directory.OFFLINE_DATASETS, reportKey);
            if (offlineDatasetFile.exists()) {
                return TextFileUtils.readTextFile(offlineDatasetFile);
            }

            return null;
        }

        private Map<String, String> buildFieldsMap(JsonArray jsonFields) {
            Map<String, String> fieldMap = new HashMap<>();
            if (jsonFields == null) {
                return fieldMap;
            }

            for (JsonElement jsonElement : jsonFields) {
                if (jsonElement instanceof JsonObject) {
                    String dataElement = (jsonElement.getAsJsonObject())
                            .get(Field.DATA_ELEMENT).getAsString();
                    String categoryCombination = (jsonElement.getAsJsonObject())
                            .get(Field.CATEGORY_OPTION_COMBO).getAsString();
                    /*
                        TEMPORARY FIX
                        When the app tries to load the form, if there are any blank category combinations
                        an error will be thrown causing the app to crash.
                        At the moment there will be blank category combinations stored in offline data because the user authorities
                        needed to post with a default category combo is unknown to us. ¯\_(ツ)_/¯
                     */
                    if(categoryCombination.equals("")){
                        categoryCombination = Constants.DEFAULT_CATEGORY_COMBO;
                    }

                    JsonElement jsonValue = (jsonElement.getAsJsonObject()).get(Field.VALUE);
                    String value = jsonValue != null ? jsonValue.getAsString() : "";

                    String fieldKey = buildFieldKey(dataElement, categoryCombination);

                    fieldMap.put(fieldKey, value);
                }
            }

            return fieldMap;
        }

        private String buildFieldKey(String dataElement, String categoryOptionCombination) {
            if (!isEmpty(dataElement) && !isEmpty(categoryOptionCombination)) {
                return String.format(Locale.getDefault(), "%s.%s",
                        dataElement, categoryOptionCombination);
            }

            return null;
        }
    }
    public void addToDiseasesShown(String id, String label ){
        this.additionalDiseaseIds.put(id, label);
    }

    public void scrollToBottomOfListView(){
        this.dataEntryListView.smoothScrollToPosition(adapters.get(0).getCount());
    }

    private void handleCompletionDate(String details){
        DateTime dateTime = new DateTime(details);
        ViewUtils.enableViews(submissionDetailsLayout);
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(Constants.DATE_COMPLETED_FORMAT);
        String text = getResources().getString(R.string.completion_date_prefix) +" "+ dateTime.toString(dateTimeFormatter);
        completionDate.setText(text);
    }

    private void handleSubmissionDetails(ArrayList<Group> groups){
        String submissionMethodValue = null;
        String isTimely = null;
        String submissionMethodText;
        Drawable isTimelyDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_done_teal_24dp);
        Drawable notTimelyDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_clear_red_24dp);

        for(Group group: groups){
            for(Field field: group.getFields()){
                if(field.getDataElement().equals(Constants.RECEIPT_OF_FORM)){
                    submissionMethodValue = field.getValue();
                }
                if(field.getDataElement().equals(Constants.TIMELY)){
                    isTimely = field.getValue();
                }
            }
        }

        assert isTimely != null;
        //Timely value not stored as boolean for some reason even though the data element is type boolean ¯\_(ツ)_/¯
        if(isTimely.equals("true")){
            isTimelyIcon.setImageDrawable(isTimelyDrawable);
        }else{
            isTimelyIcon.setImageDrawable(notTimelyDrawable);
        }

        assert submissionMethodValue != null;
        if(!submissionMethodValue.equals("")){
            submissionMethodText = getString(R.string.submission_method_prefix)+" "+submissionMethodValue;
            submissionMethod.setText(submissionMethodText);
        }else{
            submissionMethodText = getString(R.string.submission_method_prefix)+" Unknown";
            submissionMethod.setText(submissionMethodText);
        }

    }

    private void reverseAnimators(ObjectAnimator... animators){
        for(ObjectAnimator animator: animators){
            animator.reverse();
        }
    }

    private void startAnimators(ObjectAnimator... animators){
        for(ObjectAnimator animator: animators){
            animator.start();
        }
    }

    private void setupCommentRowAsFooter(Form form){
        String backgroundColor = "#E0ECEA";
        final String title = "Comment";
        for(Group group : form.getGroups()){
            for(Field field: group.getFields()){
                if(field.getDataElement().equals(Constants.COMMENT_FIELD)){
                    LayoutInflater inflater = getLayoutInflater();
                    ViewGroup footer = (ViewGroup)inflater.inflate(R.layout.listview_row_long_text, dataEntryListView, false);
                    footer.setBackgroundColor(Color.parseColor(backgroundColor));
                    dataEntryListView.addFooterView(footer, null, false);
                    TextView label = (TextView) footer.findViewById(R.id.text_label);
                    label.setText(title);
                    commentField = (EditText) findViewById(R.id.edit_long_text_row);
                    commentField.setText(field.getValue());
                }
            }
        }
    }

    private void addFooterCommentToGroup(Group group){
        Field comment = new Field();
        if(commentField != null) {
            comment.setDataElement(Constants.COMMENT_FIELD);
            comment.setValue(commentField.getText().toString());
            comment.setCategoryOptionCombo(Constants.DEFAULT_CATEGORY_COMBO);
            group.addField(comment);
        }
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if(view == null){
            view = new View(getApplicationContext());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        ArrayList<Group> groups = new ArrayList<>();
        DatasetInfoHolder info  = new DatasetInfoHolder();
        if(adapters != null) {
            for (FieldAdapter adapter : adapters) {
                groups.add(adapter.getGroup());
            }
            if(groups.size() > 0){
                //Add the comment in list view footer to group data.
                addFooterCommentToGroup(groups.get(0));
                info = getIntent().getExtras()
                        .getParcelable(DatasetInfoHolder.TAG);
            }
            if(!isFormBlank(groups)){
                String data = ReportUploadProcessor.prepareContent(info, groups);
                saveInProgressDataset(getApplicationContext(), data, info);
            }
        }
    }

    private void saveInProgressDataset(Context context, String data, DatasetInfoHolder info) {
        String key = DatasetInfoHolder.getSubmissionKey(info);
        TextFileUtils.writeTextFile(context, TextFileUtils.Directory.IN_PROGRESS_DATASETS, key, data);
    }

    private void removeInProgressDataset(Context context, DatasetInfoHolder info){
        String key = DatasetInfoHolder.getSubmissionKey(info);
        File file = TextFileUtils.findFile(getApplicationContext(), TextFileUtils.Directory.IN_PROGRESS_DATASETS, key);
        TextFileUtils.removeFile(file);
    }

    private Boolean isFormBlank(ArrayList<Group> groups){

        for(Group group: groups){
            for(Field field: group.getFields()){
                if(!field.getValue().equals("")){
                    return false;
                }
            }
        }

        return true;
    }

}
