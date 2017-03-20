package atlascience.bitmaptest.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import atlascience.bitmaptest.Objects.Rating_User;
import atlascience.bitmaptest.R;

public class RatingAdapter extends ArrayList<Rating_User> {

    List<Rating_User> rating_users;
    Context context;
    private LayoutInflater mInflater;

    public RatingAdapter(List<Rating_User> rating_users, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.rating_users = rating_users;
        this.context = context;
    }

    public Rating_User getItem(int position){
        return rating_users.get(position);
    }






}