package atlascience.bitmaptest.Models.Knowledge;


import com.google.gson.annotations.SerializedName;

public class Topics {

    @SerializedName("topics")
    String topics;

    public Topics(String topics) {
        this.topics = topics;
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }
}
