package core.android.xuele.net.crhlibcore.resource;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.webkit.WebView;

/**
 * Created by louweijun on 2018-05-15.
 */

public abstract class LoadResourceImpl implements LoadResourceListener {

    private Context context;
    private WebView webView;
    private AlertDialog mDialog;
    private ProgressDialog mProgressDialog;

    public LoadResourceImpl(Context context) {
        this.context = context;
    }

    @Override
    public boolean showDialog(final ResourceManager resourceManager) {
        mDialog = new AlertDialog.Builder(context).create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.setMessage("应用现在有新的版本可供更新");
        mDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                resourceManager.startDownload();
            }
        });
        mDialog.show();
        return false;
    }

    @Override
    public void onProgressChange(int progress) {
        //关闭dialog
        showDownloadDialog();

        mProgressDialog.setProgress(progress);
    }

    private void showDownloadDialog() {
        if (mProgressDialog != null) return;

        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setTitle("软件更新中");
        mProgressDialog.setMax(100);
        mProgressDialog.show();
    }

    @Override
    public void downloadSuccess() {
        if (mDialog != null) {
            mDialog.dismiss();
        }

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public String loadFileUrl(String url) {
        return url;
    }
}
