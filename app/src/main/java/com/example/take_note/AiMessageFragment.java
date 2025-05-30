package com.example.take_note;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AiMessageFragment extends Fragment {

    private WebView webView;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ai_message, container, false);

        webView = view.findViewById(R.id.webView);
        progressBar = view.findViewById(R.id.progressBar);

        // Enable JavaScript
        webView.getSettings().setJavaScriptEnabled(true);

        // Show progress while loading
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100 && progressBar.getVisibility() == View.GONE) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        // Ensure links open inside WebView
        webView.setWebViewClient(new WebViewClient());

        // Load AI Chat URL
        webView.loadUrl("https://talkai.info/chat/");

        return view;
    }
}
