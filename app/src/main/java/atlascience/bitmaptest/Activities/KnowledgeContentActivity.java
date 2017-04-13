package atlascience.bitmaptest.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import atlascience.bitmaptest.R;

/**
 * Created by Ilyas on 04-Apr-17.
 */

public class KnowledgeContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_content);
        WebView web_content = (WebView) findViewById(R.id.knowledge_content_web_view);

        Intent i = new Intent();
        String content = i.getStringExtra("content");

        String url = "http://codfight.atlascience.ru/learn/" + content + ".html";
        web_content.setWebViewClient(new WebBrowser());
        web_content.loadUrl(url);
    }

    public class WebBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


}
