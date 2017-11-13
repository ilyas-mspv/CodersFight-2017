package atlascience.bitmaptest.Activities.Learn;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import atlascience.bitmaptest.BaseAppCompatActivity;
import atlascience.bitmaptest.Constants;
import atlascience.bitmaptest.Models.Game.Question;
import atlascience.bitmaptest.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ilyas on 04-Apr-17.
 */

public class KnowledgeContentActivity extends BaseAppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.title_content) TextView title_content;
    @BindView(R.id.knowledge_content_web_view) WebView web_content;
    @BindView(R.id.go_ahead) Button go_ahead;


    //TODO CHANGE THIS CRAP
    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_content);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        HashMap<String,String> content_d = Question.getContentSes();
        String id = content_d.get("id");
        String content = content_d.get("content");
        String topic = content_d.get("topic");
        title_content.setText(topic);
        if(isNetworkAvailable()){
            showProgress();

            String url = Constants.URLS.TOPIC_CONTENT_URL  +content + ".html";
            web_content.setWebViewClient(new WebBrowser());
            web_content.loadUrl(url);
            web_content.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    int height = (int) Math.floor(web_content.getContentHeight() * web_content.getScale());
                    int webViewHeight = web_content.getMeasuredHeight();
                    if(web_content.getScrollY() + webViewHeight >= height){
                        Toast.makeText(KnowledgeContentActivity.this, "REACHED END", Toast.LENGTH_SHORT).show();

                    }

                }
            });

            go_ahead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(KnowledgeContentActivity.this, "TEST", Toast.LENGTH_SHORT).show();
                }
            });

            dismissProgress();
        }else{
            setErrorAlert(0);
        }
    }

    public class WebBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }


    }
}
