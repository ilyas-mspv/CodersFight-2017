package atlascience.bitmaptest.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import atlascience.bitmaptest.Models.Knowledge.Topics;

/**
 * Created by Ilyas on 9/19/2017.
 */

public class KnowledgeDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "topicsBase";
    public static final String TABLE_TOPICS = "topics";

    public static final String KEY_ID = "id";
    public static final String KEY_TOPIC_ID = "topic_id";
    public static final String KEY_TITLE = "topic";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_IS_OPEN = "is_open";

    public KnowledgeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TOPIC_TABLE = "CREATE TABLE " + TABLE_TOPICS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TOPIC_ID + " INTEGER," + KEY_TITLE + " TEXT,"
                + KEY_CONTENT + " TEXT," + KEY_IS_OPEN + " INTEGER" +  ")";
        db.execSQL(CREATE_TOPIC_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPICS);
        onCreate(db);
    }

    public void addTopic(String id, String title, String content, int is_open){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TOPIC_ID,id);
        values.put(KEY_TITLE, title);
        values.put(KEY_CONTENT, content);
        values.put(KEY_IS_OPEN, is_open);

        db.insert(TABLE_TOPICS, null, values);
        db.close();
    }

    public List<Topics> getAllTopics() {
        List<Topics> topics = new ArrayList<Topics>();
        String selectQuery = "SELECT  * FROM " + TABLE_TOPICS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Topics topic = new Topics();
                topic.setId(cursor.getString(0));
                topic.setTitle(cursor.getString(1));
                topic.setContent(cursor.getString(2));
                topic.setIs_open(cursor.getInt(3));
                topics.add(topic);
            } while (cursor.moveToNext());
        }
        return topics;
    }

    public int getTopicCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TOPICS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        cursor.close();
        return cursor.getCount();
    }





}
