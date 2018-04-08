package goparento.com.sample.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import goparento.com.sample.R;
import goparento.com.sample.Utilities.Utils;


public class BrowserActivity extends AppCompatActivity {

    private String url, name;
    private Boolean ad;
    private WebView webView;
    private ProgressBar progressBar;
    private float m_downX;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_web);
        if (getSupportActionBar()!=null)
        getSupportActionBar().setDisplayShowTitleEnabled(true);



        url = getIntent().getStringExtra("url");
        name = getIntent().getStringExtra("name");
        ad = getIntent().getBooleanExtra("ad",false);
        pos = getIntent().getIntExtra("pos",1);


        toolbar.setTitle(name);
        // if no url is passed, close the activity
        if (TextUtils.isEmpty(url)) {
            finish();
        }
        setSupportActionBar(toolbar);

        webView = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        initWebView();

        webView.loadUrl(url);

    }

    private void initWebView() {
        webView.setWebChromeClient(new MyWebChromeClient(this));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                invalidateOptionsMenu();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                invalidateOptionsMenu();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                progressBar.setVisibility(View.GONE);
                invalidateOptionsMenu();
            }
        });
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getPointerCount() > 1) {
                    //Multi touch detected
                    return true;
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        // save the x
                        m_downX = event.getX();
                    }
                    break;

                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP: {
                        // set x so that it doesn't move
                        event.setLocation(m_downX, event.getY());
                    }
                    break;
                }

                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.browser, menu);

        Utils.tintMenuIcon(this, menu.getItem(0), R.color.white);
        Utils.tintMenuIcon(this, menu.getItem(1), R.color.white);
        Utils.tintMenuIcon(this, menu.getItem(2), R.color.white);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        // menu item 0-index is bookmark icon

        // enable - disable the toolbar navigation icons
        if (!webView.canGoBack()) {
            menu.getItem(1).setEnabled(false);
            menu.getItem(1).getIcon().setAlpha(130);
        } else {
            menu.getItem(1).setEnabled(true);
            menu.getItem(1).getIcon().setAlpha(255);
        }

        if (!webView.canGoForward()) {
            menu.getItem(2).setEnabled(false);
            menu.getItem(2).getIcon().setAlpha(130);
        } else {
            menu.getItem(2).setEnabled(true);
            menu.getItem(2).getIcon().setAlpha(255);
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        if (item.getItemId() == R.id.action_share) {
            Utils.shareWebPage(this, url);
        }

        if (item.getItemId() == R.id.action_back) {
            back();
        }

        if (item.getItemId() == R.id.action_forward) {
            forward();
        }

        return super.onOptionsItemSelected(item);
    }

    // backward the browser navigation
    private void back() {
        if (webView.canGoBack()) {
            webView.goBack();
        }
    }

    // forward the browser navigation
    private void forward() {
        if (webView.canGoForward()) {
            webView.goForward();
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        public MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }
    }
}
