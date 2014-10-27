package com.sans.halfway;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by sans on 20.10.2014.
 */
public class FbReferencer {

    protected Firebase fb;

    public FbReferencer() {
        fb = new Firebase("https://boiling-inferno-8767.firebaseio.com/");

        fb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snap) {
                Log.v(snap.getName(), (String) snap.getValue());
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });
    }

}
