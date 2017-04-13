package atlascience.bitmaptest.Models.Knowledge;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopicsResponse {
    @SerializedName("topics")
    private List<Topics> topicsList;

    public List<Topics> getResults() {
        return topicsList;
    }

}
