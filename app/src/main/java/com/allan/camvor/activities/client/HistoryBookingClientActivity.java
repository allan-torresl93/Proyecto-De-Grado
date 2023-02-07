package com.allan.camvor.activities.client;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allan.camvor.R;
import com.allan.camvor.adapters.HistoryBookingClientAdapter;
import com.allan.camvor.includes.MyToolbar;
import com.allan.camvor.models.HistoryBooking;
import com.allan.camvor.providers.AuthProvider;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class HistoryBookingClientActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private HistoryBookingClientAdapter mAdapter;
    private AuthProvider mAuthProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_booking_client);
        MyToolbar.show(this, "Historial De Viajes", true);

        mRecyclerView = findViewById(R.id.recyclerViewHistoryBooking);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuthProvider = new AuthProvider();
        Query query = FirebaseDatabase.getInstance().getReference()
                        .child("HistoryBooking")
                        .orderByChild("idClient")
                        .equalTo(mAuthProvider.getId());
        FirebaseRecyclerOptions<HistoryBooking> options = new FirebaseRecyclerOptions.Builder<HistoryBooking>()
                                                                 .setQuery(query, HistoryBooking.class)
                                                                 .build();
        mAdapter = new HistoryBookingClientAdapter(options, HistoryBookingClientActivity.this);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}