package com.example.park;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class homeFragment extends Fragment implements View.OnClickListener{
    private CardView card1, card2, card3, card4;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_home, container, false);

            card1 = view.findViewById(R.id.card1);
            card2 = view.findViewById(R.id.card2);
            card3 = view.findViewById(R.id.card3);
            card4 = view.findViewById(R.id.card4);

            card1.setOnClickListener(this);
            card2.setOnClickListener(this);
            card3.setOnClickListener(this);
            card4.setOnClickListener(this);

            return view;
        }

        @Override
        public void onClick(View v) {
            Intent i;

            if(v.getId() == R.id.card1){
                i = new Intent(homeFragment.this.getActivity(), hotel.class);
                startActivity(i);
            }
            else if(v.getId() == R.id.card2){
                i = new Intent(homeFragment.this.getActivity(), hotel.class);
                startActivity(i);
            }
            else if(v.getId() == R.id.card3){
                i = new Intent(homeFragment.this.getActivity(), hotel.class);
                startActivity(i);
            }
            else if(v.getId() == R.id.card4){
                i = new Intent(homeFragment.this.getActivity(), hotel.class);
                startActivity(i);
            }
        }
}

