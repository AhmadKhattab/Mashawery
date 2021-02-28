package com.iti.gov.mashawery.model;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class FirebaseTripDao {
    public static void addUserTrips(List<FirebaseTrip> tripList, String currentUserId,
                                    OnCompleteListener<Void> onCompleteListener){
        FireBaseDB.getUsers().child(currentUserId).child(FireBaseDB.USER_Trip_REF)
                .setValue(Arrays.asList(tripList))
                .addOnCompleteListener(onCompleteListener);
    }
    public static void getUserTrips(String currentUserId, ValueEventListener valueEventListener){
        FireBaseDB.getUsers().child(currentUserId).child(FireBaseDB.USER_Trip_REF).child(FireBaseDB.USER_Trip_branch)
                .addValueEventListener(valueEventListener);
    }
}
