package atlascience.bitmaptest.Models.TrainMode;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ilyas on 9/19/2017.
 */

public class TopicFiles implements Parcelable {

    private int id;
    private List<String> files;

    public TopicFiles(int id, List<String> files) {
        this.id = id;
        this.files = files;
    }

    private TopicFiles(Parcel in) {
        id = in.readInt();
        List<String> d = new ArrayList<>();
        in.readStringList(d);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        List<String> d = new ArrayList<>();
        d.addAll(files);
        dest.writeList(d);
    }


    public static final Creator<TopicFiles> CREATOR = new Creator<TopicFiles>(){

        @Override
        public TopicFiles createFromParcel(Parcel in) {
            return new TopicFiles(in);
        }

        @Override
        public TopicFiles[] newArray(int size) {
            return new TopicFiles[size];
        }
    };
}
