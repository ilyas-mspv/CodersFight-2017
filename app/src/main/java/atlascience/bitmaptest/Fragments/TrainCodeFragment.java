package atlascience.bitmaptest.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import atlascience.bitmaptest.Activities.ProfileActivity;
import atlascience.bitmaptest.Activities.TrainMode.TrainResultsActivity;
import atlascience.bitmaptest.Adapters.TrainAnswersAdapter;
import atlascience.bitmaptest.AppController;
import atlascience.bitmaptest.Constants;
import atlascience.bitmaptest.Models.SuccessResponse;
import atlascience.bitmaptest.Models.TrainMode.Answers;
import atlascience.bitmaptest.Models.TrainMode.Codes;
import atlascience.bitmaptest.R;
import atlascience.bitmaptest.Utils.ItemClickSupport;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ilyas on 9/17/2017.
 */

public class TrainCodeFragment extends Fragment {

    private static final String ARG_PAGE_NUMBER = "arg_page_number";
    private static final String ARG_FILE = "arg_file";
    private View v;
    private int pageNumber,page_counted;
    private SharedPreferences shPrefs;
    private SharedPreferences.Editor editor;

    private ArrayList<String> file;
    private String current_file,filename;
    private ViewPager vp;
    private String true_answer;
    //UI
    private WebView web_code_file;
    private Button next_code_btn;
    private RecyclerView flow_answers;

    public static TrainCodeFragment newInstance(int page,ArrayList<String> file,int final_size){
        TrainCodeFragment trainCodeFragment = new TrainCodeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER,page);
        args.putInt("final_size",final_size);
        args.putStringArrayList(ARG_FILE,file);
        trainCodeFragment.setArguments(args);
        return trainCodeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pageNumber = getArguments().getInt(ARG_PAGE_NUMBER);
        page_counted = getArguments().getInt("final_size");
        file = getArguments().getStringArrayList(ARG_FILE);
        current_file  = file.get(pageNumber);
        filename = current_file.substring(current_file.lastIndexOf("/")+1);

        vp = (ViewPager) getActivity().findViewById(R.id.codes_pager_train);
    }

    private void initAnswers() {
        flow_answers = (RecyclerView) v.findViewById(R.id.flow_answers);
        next_code_btn = (Button) v.findViewById(R.id.move_next_btn);

        AppController.getApi().answers_get_all(Constants.Methods.Version.VERSION2,Constants.TrainMode.Methods.ANSWERS_GET_ALL,filename).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                SuccessResponse s = new SuccessResponse(response);
                if(s.success()){
                    next_code_btn.setVisibility(View.VISIBLE);
                    next_code_btn.setEnabled(false);
                    initFlowAnswers(response);
                }else{
                    TextView error = (TextView) v.findViewById(R.id.error_answers_train);
                    next_code_btn.setVisibility(View.VISIBLE);
                    error.setText("Answers Error! We are sorry :( ");
                    error.setVisibility(View.VISIBLE);

                    next_code_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            vp.setCurrentItem(pageNumber+1);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }

    private void initFlowAnswers(Response<JsonObject> response) {
        final Answers answers = new Answers(response,getActivity().getApplicationContext());
        final TrainAnswersAdapter adapter = new TrainAnswersAdapter(getActivity().getApplicationContext(),answers);
        flow_answers.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        flow_answers.setAdapter(adapter);
        ItemClickSupport.addTo(flow_answers).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                for (int i = 0; i < answers.size(); i++) {
                    if(position == i)
                        if(true_answer.equals(answers.getAnswer(i))){
                            answers.set_correct(1);
                            break;
                        }else{
                            answers.set_incorrect(1);
                            break;
                        }
                }
                next_code_btn.setEnabled(true);
                next_code_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(pageNumber+1==page_counted){
                            Intent i = new Intent(getActivity().getApplicationContext(),TrainResultsActivity.class);
                            i.putExtra("flag",true);
                            startActivity(i);
                        }else{
                            vp.setCurrentItem(pageNumber+1);
                        }
                    }
                });
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_train_code,null);

        web_code_file = (WebView) v.findViewById(R.id.train_web_code);
        web_code_file.setWebViewClient(new MyBrowser());
        web_code_file.loadUrl("file:///"+ current_file);
        filename = current_file.substring(current_file.lastIndexOf("/")+1);

        initCodeMics();

        initAnswers();


        return v;

    }

    private void initCodeMics() {
        final TextView code_name = (TextView) v.findViewById(R.id.code_name_txt);
        AppController.getApi().get_code_info(Constants.Methods.Version.VERSION2, Constants.TrainMode.Methods.GET_CODE_INFO,filename).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                SuccessResponse s = new SuccessResponse(response);
                if(s.success()){
                    Codes codes = new Codes(response);
                    code_name.setText(codes.get_name());
                    true_answer = codes.get_true_answer();
                }else{
                    code_name.setText("Choose the correct answer");
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            //todo add second tap alert
            case android.R.id.home:
                startActivity(new Intent(getActivity().getApplicationContext(), ProfileActivity.class));
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
