package atlascience.bitmaptest.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import atlascience.bitmaptest.Models.Rating.Rating;
import atlascience.bitmaptest.R;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingViewHolder> {

    private List<Rating> users;

    private Context context;

    public RatingAdapter(List<Rating> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @Override
    public RatingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;

        if (viewType == 0) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_top_rating, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_rating, parent, false);
        }
        return new RatingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RatingViewHolder holder, int position) {
        holder.textId.setText(String.valueOf(users.get(position).getId()));
        holder.order_number.setText(String.valueOf(users.get(position).getId()));
        holder.username.setText(users.get(position).getUsername());
        holder.rating_number.setText(String.valueOf(users.get(position).getRating()));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public class RatingViewHolder extends RecyclerView.ViewHolder {

        TextView order_number, username, rating_number, textId;

        public RatingViewHolder(View itemView) {
            super(itemView);
            textId = (TextView) itemView.findViewById(R.id.id_rating);
            order_number = (TextView) itemView.findViewById(R.id.order_number_queue);
            username = (TextView) itemView.findViewById(R.id.username_rating);
            rating_number = (TextView) itemView.findViewById(R.id.rating_number);

        }
    }
}