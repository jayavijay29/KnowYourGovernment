package com.jayavijayjayavelu.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jayavijayjayavelu on 4/3/17.
 */

public class CivicAdapter extends RecyclerView.Adapter<MyCivicHolder> {
    private static final String TAG = "Civic Adapter";
    private ArrayList<CivicPositionNames> civicPositionList;
    ArrayList<CivicDetails> candidateDetails;
    private MainActivity mainAct;

    public CivicAdapter(ArrayList<CivicPositionNames> civicPositionNames, ArrayList<CivicDetails> candidateDetails, MainActivity ma) {
        this.civicPositionList = civicPositionNames;
        this.candidateDetails =candidateDetails;
        mainAct = ma;
    }

    @Override
    public MyCivicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.civic_entry, parent, false);
        itemView.setOnClickListener(mainAct);
        return new MyCivicHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyCivicHolder holder, int position) {
        CivicPositionNames civicPositionNames = civicPositionList.get(position);
        CivicDetails civicDetails = candidateDetails.get(position);
        holder.postName.setText(civicPositionNames.getPositionName());
        holder.candidateName.setText(civicDetails.getName()+" ("+civicDetails.getParty()+")");
    }

    @Override
    public int getItemCount() {
        return civicPositionList.size();
    }
}
