package atlascience.bitmaptest.Models.Knowledge;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopicsResponse {
    @SerializedName("topicsList")
    private List<Topics> topicsList;

    public List<Topics> getResults() {
        return topicsList;
    }

}
