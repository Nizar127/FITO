package com.blessapp.fito;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blessapp.fito.model.coupon;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import static androidx.constraintlayout.motion.widget.MotionScene.TAG;

public class couponlistAdapter extends FirebaseRecyclerAdapter<coupon, couponlistAdapter.couponViewHolder>{
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public couponlistAdapter(@NonNull FirebaseRecyclerOptions<coupon> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull couponViewHolder holder, int position, @NonNull coupon model) {

        holder.Name.setText(model.getName());
        Log.d(TAG, "query:"+ holder.Name);

        holder.SponsoredName.setText(model.getSponsoredName());
        holder.Points.setText(model.getPoints());
        Picasso.get().load(model.getImage()).into(holder.couponImg);

    }

    @NonNull
    @Override
    public couponViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coupon_ui,parent,false);
        return new couponViewHolder(view);
    }

    class couponViewHolder extends RecyclerView.ViewHolder {

        TextView Name, SponsoredName, Points;
        //TextView couponName, sponsoredName, points;
        ImageView couponImg;
        public couponViewHolder(@NonNull View itemView) {
            super(itemView);

            couponImg = itemView.findViewById(R.id.couponImage);
            Name = itemView.findViewById(R.id.couponName);
            SponsoredName = itemView.findViewById(R.id.couponSponsoredName);
            Points = itemView.findViewById(R.id.textPoints);
        }
    }
}

/*public class couponlistAdapter extends RecyclerView.Adapter<couponlistAdapter.couponListViewHolder>{

    private List<coupon> mylist;

    public couponlistAdapter(List<coupon> mylist){
        this .mylist=mylist;
    }
    @NonNull
    @Override
    public couponlistAdapter.couponListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coupon_ui,parent,false);
        return new couponlistAdapter.couponListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull couponlistAdapter.couponListViewHolder holder, int position) {

       int resource = mylist.get(position).getCouponImg();
        String Name = mylist.get(position).getName();
        String sponsored = mylist.get(position).getSponsoredName();
        String rewards = mylist.get(position).getPoints();
        //Picasso.get().load(mylist.get(position).getImage()).into(holder.couponImg);


        holder.setData(resource, Name, sponsored, rewards);
       // holder.couponName.setText(list.get(position));
        //holder.sponsoredName.setText(model.getSponsoredName());
        //holder.points.setText(model.getPoints());
        //Picasso.get().load(model.getImage()).into(holder.couponImg);
    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }

    public class couponListViewHolder extends RecyclerView.ViewHolder {
        TextView couponName, sponsoredName, points;
        ImageView couponImg;

        public couponListViewHolder(@NonNull View itemView) {
            super(itemView);


            couponImg = itemView.findViewById(R.id.couponImage);
            couponName = itemView.findViewById(R.id.couponName);
            sponsoredName = itemView.findViewById(R.id.couponSponsoredName);
            points = itemView.findViewById(R.id.textPoints);
        }


        public void setData(int resource, String name, String sponsored, String rewards) {

            couponImg.setImageResource(resource);
            couponName.setText(name);
            sponsoredName.setText(sponsored);
            points.setText(rewards);
        }
    }
}*/
