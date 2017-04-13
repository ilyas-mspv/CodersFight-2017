package atlascience.bitmaptest.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import atlascience.bitmaptest.Models.Queue.Queue;
import atlascience.bitmaptest.R;

/**
 * Created by Ilyas on 03-Apr-17.
 */

public class QueueAdapter extends RecyclerView.Adapter<QueueAdapter.QueueViewHolder> {

    View v;
    List<Queue> queueList;
    Context context;


    public QueueAdapter(List<Queue> queueList, Context context) {
        this.queueList = queueList;
        this.context = context;
    }

    @Override
    public QueueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_queue, parent, false);
        return new QueueViewHolder(v);
    }

    @Override
    public void onBindViewHolder(QueueViewHolder holder, int position) {
        holder.id.setText(String.valueOf(queueList.get(position).getUser_id()));
        holder.order_number.setText(String.valueOf(queueList.get(position).getOrder_number()));
        holder.rating.setText(String.valueOf(queueList.get(position).getRating()));
        holder.username.setText(queueList.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return queueList.size();
    }

    public class QueueViewHolder extends RecyclerView.ViewHolder {

        TextView order_number, username, rating, id;

        public QueueViewHolder(View itemView) {
            super(itemView);

            order_number = (TextView) itemView.findViewById(R.id.order_number_queue);
            username = (TextView) itemView.findViewById(R.id.username_queue);
            rating = (TextView) itemView.findViewById(R.id.rating_queue);
            id = (TextView) itemView.findViewById(R.id.id_queue);

        }
    }

}
