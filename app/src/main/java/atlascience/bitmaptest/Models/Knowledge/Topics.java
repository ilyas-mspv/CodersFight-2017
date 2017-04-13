package atlascience.bitmaptest.Models.Knowledge;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Topics {

    @SerializedName("topic")
    private String topic;
    @SerializedName("content")
    private String content;

    private List<Topics> topicsList;

    public Topics(String topics, String content) {
        this.topic = topics;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTopics() {
        return topic;
    }

    public void setTopics(String topics) {
        this.topic = topics;
    }

    public List<Topics> getResults() {
        return topicsList;
    }


}
