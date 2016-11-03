package com.gwi.selfplatform.ui.activity.encyclopedia;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html.ImageGetter;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.google.gson.reflect.TypeToken;
import com.gwi.ccly.android.commonlibrary.common.net.AsyncCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.GWINet;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.WebUtil;
import com.gwi.selfplatform.module.image.ImageLoaderConfig;
import com.gwi.selfplatform.module.net.beans.KBTreatmentDetails;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.Request;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.TBase;
import com.gwi.selfplatform.module.net.webservice.WebServiceController;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.apache.http.protocol.HTTP;

import java.util.List;

public class TreatmentDetailsActivity extends HospBaseActivity {

    ProgressBar mLoading = null;
    WebView mContent = null;

    String mTreatmentId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_details);
        addHomeButton();
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey("TreatmentId")) {
                mTreatmentId = b.getString("TreatmentId");
            }
            if (b.containsKey("TreatmentName")) {
                setTitle(b.getString("TreatmentName"));
            }
        }

        initViews();
        initEvents();

        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                loadingContentNew(mTreatmentId);
            }
        }, 300);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initViews() {
        mLoading = (ProgressBar) findViewById(R.id.treatment_details_progress_bar);
        mContent = (WebView) findViewById(android.R.id.text1);
    }

    @Override
    protected void initEvents() {

    }

    private void loadingContentNew(String treatmentId) {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_TEST_DETAIL, true);

        request.setBody(new TBase());
        request.getBody().setHospitalCode(WebUtil.HOSP_CODE);

        request.getBody().setTreatmentId(treatmentId);

        GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI()
                .setLoadingView(mLoading)
                .mappingInto(new TypeToken<List<KBTreatmentDetails>>() {
                })
                .execute("TreatmentDetailsActivity", new RequestCallback<List<KBTreatmentDetails>>() {
                    @Override
                    public void onRequestSuccess(List<KBTreatmentDetails> result) {
                        if (result != null && !result.isEmpty()) {
                            KBTreatmentDetails detail = result.get(0);
                            mContent.setVisibility(View.VISIBLE);
                            if (detail.getTreatmentContent() != null) {
                                mContent.loadDataWithBaseURL(null, detail.getTreatmentContent(), "text/html", HTTP.UTF_8, null);
                            } else {
                                mContent.loadDataWithBaseURL(null, "没有记录", "text/html", HTTP.UTF_8, null);
                            }
                        }
                    }

                    @Override
                    public void onRequestError(RequestError error) {

                    }
                });
    }

    private void loadingContent(final String id) {
        doProgressAsyncTask(mLoading, new AsyncCallback<List<KBTreatmentDetails>>() {

            @Override
            public List<KBTreatmentDetails> callAsync() throws Exception {
                return WebServiceController.getTreatmentDetail(id);
            }

            @Override
            public void onPostCall(List<KBTreatmentDetails> result) {
                if (result != null && !result.isEmpty()) {
                    KBTreatmentDetails detail = result.get(0);
                    mContent.setVisibility(View.VISIBLE);
                    if (detail.getTreatmentContent() != null) {
                        mContent.loadDataWithBaseURL(null, detail.getTreatmentContent(), "text/html", HTTP.UTF_8, null);
                    } else {
                        mContent.loadDataWithBaseURL(null, "没有记录", "text/html", HTTP.UTF_8, null);
                    }
                }
            }

            @Override
            public void onCallFailed(Exception exception) {

            }
        });
    }

    /**
     * HTML异步加载图片
     *
     * @author 彭毅
     */
    class NetworkImageGetter implements ImageGetter {

        private View container;

        public NetworkImageGetter(View container) {
            this.container = container;
        }

        @Override
        public Drawable getDrawable(String source) {
            final URLDrawable drawable = new URLDrawable();
            ImageLoader.getInstance().loadImage(source, ImageLoaderConfig.initDisplayOptions(true), new ImageLoadingListener() {

                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view,
                                            FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    Drawable result = new BitmapDrawable(getResources(), loadedImage);
//                    drawable.setBounds(0, 0, 0 + result.getIntrinsicWidth(), 0 
//                            + result.getIntrinsicHeight()); 
                    drawable.setBounds(0, 0, 0, 0);
                    drawable.setDrawable(drawable);
                    container.postInvalidate();
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
            return drawable;
        }

    }

    public class URLDrawable extends BitmapDrawable {
        // the drawable that you need to set, you could set the initial drawing
        // with the loading image if you need to
        protected Drawable drawable;

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }

        @Override
        public void draw(Canvas canvas) {
            // override the draw to facilitate refresh function later
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }
    }

}
