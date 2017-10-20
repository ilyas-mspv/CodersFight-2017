package atlascience.bitmaptest.Activities.TrainMode;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import atlascience.bitmaptest.Fragments.TrainCodeFragment;
import atlascience.bitmaptest.R;
import atlascience.bitmaptest.Utils.CustomViewPager;

public class TrainGameActivity extends ActionBarActivity {

    static final String TAG = "myLogs";
    static int PAGE_COUNT = 0;

    private ArrayList<String> topic_ids;
    private int final_codes_size;

    private CustomViewPager pager;
    private  PagerAdapter pagerAdapter;

    private ArrayList<String> code_file = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_game);

        topic_ids = (ArrayList<String>) getIntent().getSerializableExtra("topics_list");
        final_codes_size = getIntent().getIntExtra("all_codes_size",0);
        setupPage();
        initPager();

    }

    private void setupPage() {
        getFilePath();
    }


    private void getFilePath() {
        File directory = new File("/data/data/" + getPackageName() + "/games");
        File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);

        Log.d("Files","ARRAY: "+ Arrays.toString(files));
        Log.d("Files","TOPICS_ARRAY: "+ Arrays.toString(topic_ids.toArray()));


        int file_letter,topic_id;
        char[] d;
        for(int i = 0; i < topic_ids.size(); i++){
            topic_id = Integer.valueOf(topic_ids.get(i));
            Log.d("Files", "TOPIC_ID: "+String.valueOf(topic_id));

            for (File file : files) {
                d = file.getName().toCharArray();
                Log.d("Files", "FileName: " + file.getName());
                file_letter = d[0] - 0b110000;

                if (file_letter == topic_id) {
                    PAGE_COUNT++;
                    code_file.add(file.getAbsolutePath());
                }
            }
        }
    }

    private void initPager(){

        pager = (CustomViewPager) findViewById(R.id.codes_pager_train);
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),code_file);
        pager.setAdapter(pagerAdapter);
        pager.setPagingEnabled(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<String> code_file;


        public MyFragmentPagerAdapter(FragmentManager fm,ArrayList<String> code_file) {
            super(fm);
            this.code_file = code_file;
        }

        @Override
        public Fragment getItem(int position) {
            return TrainCodeFragment.newInstance(position,code_file,final_codes_size);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

    }
}
