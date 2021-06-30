package com.blessapp.fito.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blessapp.fito.R;

public class myviewholder extends RecyclerView.ViewHolder {
    TextView Name, SponsoredName, Points;
    //TextView couponName, sponsoredName, points;
    ImageView couponImg;
    public myviewholder(@NonNull View itemView) {
        super(itemView);

        couponImg = itemView.findViewById(R.id.couponImage);
        Name = itemView.findViewById(R.id.couponName);
        SponsoredName = itemView.findViewById(R.id.couponSponsoredName);
        Points = itemView.findViewById(R.id.textPoints);
    }
}
