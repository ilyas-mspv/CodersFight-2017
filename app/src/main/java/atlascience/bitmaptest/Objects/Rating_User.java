package atlascience.bitmaptest.Objects;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rating_User {

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("username")
    @Expose
    public String username;

    @SerializedName("rating")
    @Expose
    public int rating;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }



}
