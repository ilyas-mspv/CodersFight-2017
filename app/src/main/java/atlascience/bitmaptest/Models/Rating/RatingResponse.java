package atlascience.bitmaptest.Models.Rating;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RatingResponse {

    @SerializedName("users")
    private List<Rating> users;

    public List<Rating> getResults() {
        return users;
    }

}
