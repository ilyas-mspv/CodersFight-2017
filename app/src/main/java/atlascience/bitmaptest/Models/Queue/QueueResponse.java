package atlascience.bitmaptest.Models.Queue;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ilyas on 03-Apr-17.
 */

public class QueueResponse {

    @SerializedName("users")
    private List<Queue> users;

    public List<Queue> getResults() {
        return users;
    }
}