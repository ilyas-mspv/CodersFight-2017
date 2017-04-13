package atlascience.bitmaptest.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.Authenticator.SessionManager;
import atlascience.bitmaptest.Models.RequestPlayer;
import atlascience.bitmaptest.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ilyas on 05-Apr-17.
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {


    View v;
    List<RequestPlayer> requestPlayerList;


    public RequestAdapter(List<RequestPlayer> requestPlayerList) {
        this.requestPlayerList = requestPlayerList;
    }

    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_queue_requests, parent, false);
        return new RequestViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RequestViewHolder holder, final int position) {
        holder.text_queue.setText(requestPlayerList.get(position).getUsername());
        holder.user_id.setText(String.valueOf(requestPlayerList.get(position).getUser_id()) + " wants to play with You!");
        holder.yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager session = new SessionManager(v.getContext().getApplicationContext());
                HashMap<String, String> u_data = session.getUserDetails();
                int my_id = Integer.parseInt(u_data.get(SessionManager.KEY_ID));
                int user = requestPlayerList.get(position).getUser_id();
                AppController.getApi().approved_request("approved_request", my_id, user, "yes").enqueue(new Callback<JSONObject>() {
                    @Override
                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                    }

                    @Override
                    public void onFailure(Call<JSONObject> call, Throwable t) {

                    }
                });

                requestPlayerList.remove(position);
                notifyItemRemoved(position);
            }
        });
        holder.no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager session = new SessionManager(v.getContext().getApplicationContext());
                HashMap<String, String> u_data = session.getUserDetails();

                int my_id = Integer.parseInt(u_data.get(SessionManager.KEY_ID));
                int user = requestPlayerList.get(position).getUser_id();
                AppController.getApi().approved_request("approved_request", my_id, user, "no").enqueue(new Callback<JSONObject>() {
                    @Override
                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                    }

                    @Override
                    public void onFailure(Call<JSONObject> call, Throwable t) {

                    }
                });
                requestPlayerList.remove(position);
                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return requestPlayerList.size();
    }

    class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView text_queue, user_id;
        Button yes_btn, no_btn;

        RequestViewHolder(View itemView) {
            super(itemView);
            text_queue = (TextView) itemView.findViewById(R.id.text_content_queue);
            user_id = (TextView) itemView.findViewById(R.id.user_id_request_queue);
            yes_btn = (Button) itemView.findViewById(R.id.button_agree_queue);
            no_btn = (Button) itemView.findViewById(R.id.button_disagree_queue);
        }
    }
}
