package atlascience.bitmaptest.Activities.Learn;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import atlascience.bitmaptest.BaseAppCompatActivity;
import atlascience.bitmaptest.Constants;
import atlascience.bitmaptest.Models.Game.Question;
import atlascience.bitmaptest.R;

/**
 * Created by Ilyas on 04-Apr-17.
 */

public class KnowledgeContentActivity extends BaseAppCompatActivity {

    TextView title_content;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_content);

        final WebView web_content = (WebView) findViewById(R.id.knowledge_content_web_view);

        title_content = (TextView) findViewById(R.id.title_content);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        HashMap<String,String> content_d = Question.getContentSes();
        String id = content_d.get("id");
        String content = content_d.get("content");
        String topic = content_d.get("topic");
        title_content.setText(topic);
        if(isNetworkAvailable()){
            showProgress(getResources().getString(R.string.dialog_load_type));

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

            dismissProgress();
        }else{
            setErrorAlert(getResources().getString(R.string.dialog_error_type_summary));
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
