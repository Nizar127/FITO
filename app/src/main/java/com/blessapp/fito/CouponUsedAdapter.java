package com.blessapp.fito;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blessapp.fito.model.couponUsed;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

public class CouponUsedAdapter extends FirebaseRecyclerAdapter<couponUsed, CouponUsedAdapter.couponUsedViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CouponUsedAdapter(@NonNull FirebaseRecyclerOptions<couponUsed> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull couponUsedViewHolder holder, int position, @NonNull couponUsed model) {
        holder.NameUsed.setText(model.getName());
        //Log.d(TAG, "query:"+ model.getName());

        holder.SponsoredNameUsed.setText(model.getSponsoredName());
        holder.PointsUsed.setText(model.getPoints());
        Picasso.get().load(model.getImage()).into(holder.couponImg);
    }

    @NonNull
    @Override
    public couponUsedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coupon_data,parent,false);
        return new couponUsedViewHolder(view);
    }

    class couponUsedViewHolder extends RecyclerView.ViewHolder{
        TextView NameUsed, SponsoredNameUsed, PointsUsed;
        //TextView couponName, sponsoredName, points;
        ImageView couponImg;
        Button used;
        public couponUsedViewHolder(@NonNull View itemView) {
            super(itemView);

            couponImg = itemView.findViewById(R.id.morecouponImage);
            NameUsed = itemView.findViewById(R.id.morecouponName);
            SponsoredNameUsed = itemView.findViewById(R.id.morecouponSponsoredName);
            PointsUsed = itemView.findViewById(R.id.pointItemSystem);
            used = itemView.findViewById(R.id.usedBtn);
        }
    }
}
