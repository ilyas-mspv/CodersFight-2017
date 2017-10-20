package atlascience.bitmaptest.Models.TrainMode;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Ilyas on 8/24/2017.
 */

public class TrainSettings {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public TrainSettings(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("train_mode",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public HashMap<String,String> game_data (){
        HashMap<String,String> d = new HashMap<>();
        d.put("difficulty", String.valueOf(sharedPreferences.getInt("difficulty",0)));
        return d;
    }

    public void play(int difficulty){
        editor.putInt("difficulty",difficulty);
        editor.commit();
    }


}
