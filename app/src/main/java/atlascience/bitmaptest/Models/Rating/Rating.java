package atlascience.bitmaptest.Models.Rating;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rating {

    @SerializedName("id")
    public int id;
    @SerializedName("username")
    public String username;
    @SerializedName("rating")
    public int rating;

    public Rating(int id, String username, int rating) {
        this.id = id;
        this.username = username;
        this.rating = rating;
    }

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
