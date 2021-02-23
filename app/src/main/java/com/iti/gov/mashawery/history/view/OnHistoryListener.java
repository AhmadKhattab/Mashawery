package com.iti.gov.mashawery.history.view;

import com.iti.gov.mashawery.model.Trip;

public interface OnHistoryListener {

    void onHistoryClick(Trip trip);
    void onHistoryDelete(Trip trip);


}
