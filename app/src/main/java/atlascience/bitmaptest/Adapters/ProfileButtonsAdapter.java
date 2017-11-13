package atlascience.bitmaptest.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import atlascience.bitmaptest.Models.Profile.ProfileButtonsModel;
import atlascience.bitmaptest.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ilyas on 05-Nov-17.
 */

public class ProfileButtonsAdapter extends RecyclerView.Adapter<ProfileButtonsAdapter.MyButtonViewHolder> {

    private View v;
    List<ProfileButtonsModel> modelList;


    public ProfileButtonsAdapter(List<ProfileButtonsModel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public MyButtonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_profile_btn_item,parent,false);
        return new MyButtonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyButtonViewHolder holder, int position) {
        holder.btn_text.setText(modelList.get(position).getTv());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class MyButtonViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.row_profile_btn_text)
        TextView btn_text;

        @BindView(R.id.row_profile_card_view)
        CardView cardView;

        public MyButtonViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }
    }
}
