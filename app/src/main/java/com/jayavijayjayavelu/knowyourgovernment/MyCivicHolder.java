package com.jayavijayjayavelu.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by jayavijayjayavelu on 4/3/17.
 */

public class MyCivicHolder extends RecyclerView.ViewHolder {
    public TextView postName;
    public TextView candidateName;

    public MyCivicHolder(View itemView) {
        super(itemView);
        postName = (TextView) itemView.findViewById(R.id.postName);
        candidateName = (TextView) itemView.findViewById(R.id.candidateName);
    }
}
