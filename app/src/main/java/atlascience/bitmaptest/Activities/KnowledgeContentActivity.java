package atlascience.bitmaptest.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.util.HashMap;

import atlascience.bitmaptest.BaseAppCompatActivity;
import atlascience.bitmaptest.Constants;
import atlascience.bitmaptest.Models.Question;
import atlascience.bitmaptest.R;

/**
 * Created by Ilyas on 04-Apr-17.
 */

public class KnowledgeContentActivity extends BaseAppCompatActivity {

    TextView title_content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_content);

        WebView web_content = (WebView) findViewById(R.id.knowledge_content_web_view);
        title_content = (TextView) findViewById(R.id.title_content);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        HashMap<String,String> content_d = Question.getContentSes();
        String content = content_d.get("content");
        String topic = content_d.get("topic");
        title_content.setText(topic);
        if(isNetworkAvailable()){
            showProgress(getResources().getString(R.string.dialog_load_type));

            String url = Constants.URLS.TOPIC_CONTENT_URL  +content + ".html";
            web_content.setWebViewClient(new WebBrowser());
            web_content.loadUrl(url);

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
