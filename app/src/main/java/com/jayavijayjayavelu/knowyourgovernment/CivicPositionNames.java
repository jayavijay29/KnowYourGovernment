package com.jayavijayjayavelu.knowyourgovernment;

import org.json.JSONArray;

/**
 * Created by jayavijayjayavelu on 4/2/17.
 */

public class CivicPositionNames {
    String positionName;
    int numberPositions;

    public CivicPositionNames(String positionName) {
        this.positionName = positionName;
        //this.numberPositions = numberPositions;
    }

    public int getNumberPositions() {
        return numberPositions;
    }

    public void setNumberPositions(int numberPositions) {
        this.numberPositions = numberPositions;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }


}
