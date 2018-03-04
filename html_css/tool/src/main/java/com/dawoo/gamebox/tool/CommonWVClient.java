package com.dawoo.gamebox.tool;

import android.graphics.Bitmap;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dawoo.gamebox.constant.Const;
import com.dawoo.gamebox.constant.URLConst;

/**
 *
 * Created by bill on 17-4-14.
 */
public class CommonWVClient extends WebViewClient {
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        view.loadUrl(Const.PAGE_UN_NET);
    }
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        view.loadUrl(Const.PAGE_UN_NET);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (url.endsWith(URLConst.ERROR_605)) {
            view.loadUrl(Const.PAGE_605);
        }
    }
//    // 6.0 及以上版本调用
//    @TargetApi(Build.VERSION_CODES.M)
//    @Override
//    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
//        super.onReceivedHttpError(view, request, errorResponse);
//
//        int code = errorResponse.getStatusCode();
//        switch (code) {
//            case 403:
//                view.loadUrl(Const.PAGE_403);
//                break;
//            case 404:
//                view.loadUrl(Const.PAGE_404);
//                break;
//            case 500:
//                view.loadUrl(Const.PAGE_500);
//                break;
//            case 605:
//                view.loadUrl(Const.PAGE_605);
//                break;
//            default:
//                view.loadUrl(Const.PAGE_UN_NET);
//                break;
//        }
//    }

}
