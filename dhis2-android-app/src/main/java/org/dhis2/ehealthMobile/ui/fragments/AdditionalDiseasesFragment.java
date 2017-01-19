package org.dhis2.ehealthMobile.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.dhis2.ehealthMobile.R;
import org.dhis2.ehealthMobile.io.models.Form;
import org.dhis2.ehealthMobile.io.models.eidsr.Disease;
import org.dhis2.ehealthMobile.utils.DiseaseImporter;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by george on 9/28/16.
 */

public class AdditionalDiseasesFragment extends BottomSheetDialogFragment implements AdapterView.OnItemClickListener {

    public interface AdditionalDiseaseOnClickListener{
        void onClick(Disease disease);
    }


    private static final String KEY_ALREADY_DISPLAYED = "alreadyDisplayed";
    private static final String KEY_FORM_ID = "formId";

    private ListView listview;
    private ArrayList<String> additionalDiseases;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> additionalDiseasesIds;
    private Map diseases;
    //Diseases already displayed in the DataEntryActivity listView.
    private String alreadyDisplayed;
    private String formId;
    private AdditionalDiseaseOnClickListener onClickListener;

    public static AdditionalDiseasesFragment newInstance(String alreadyDisplayed, String formId){
        AdditionalDiseasesFragment fragment = new AdditionalDiseasesFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ALREADY_DISPLAYED, alreadyDisplayed);
        args.putString(KEY_FORM_ID, formId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_bottomsheet, null);
        dialog.setContentView(contentView);

        alreadyDisplayed = getArguments().getString(KEY_ALREADY_DISPLAYED);
        formId = getArguments().getString(KEY_FORM_ID);
        listview = (ListView) dialog.findViewById(R.id.additional_diseases_listview);
        setupListView();
        listview.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Disease disease = (Disease) diseases.get(additionalDiseasesIds.get(i));
        onClickListener.onClick(disease);
        dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            onClickListener = (AdditionalDiseaseOnClickListener) context;
        }catch(ClassCastException e ){
            throw new ClassCastException(context.toString() + " must implement AdditionalDiseaseOnClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onClickListener = null;
    }

    private void setupListView(){
        additionalDiseases = new ArrayList<>();
        additionalDiseasesIds = new ArrayList<>();

        // TODO: pass diseases as argument
        diseases = DiseaseImporter.importDiseases(getContext(), formId);

        for (Object key : diseases.keySet()) {
            Disease disease = (Disease) diseases.get(key);
            if(disease.isAdditionalDisease() && !alreadyDisplayed.contains(disease.getId())){
                additionalDiseases.add(disease.getLabel().split("EIDSR-")[1]);
                additionalDiseasesIds.add(disease.getId());
            }
        }

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_expandable_list_item_1, additionalDiseases);
        listview.setAdapter(adapter);

    }

}
