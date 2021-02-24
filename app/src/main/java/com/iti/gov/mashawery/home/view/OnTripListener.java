package com.iti.gov.mashawery.home.view;

import com.iti.gov.mashawery.model.Trip;

public interface OnTripListener {

    void onTripClick(Trip trip);
    void onTripDelete(Trip trip);
    void onTripStart(Trip trip );
}
