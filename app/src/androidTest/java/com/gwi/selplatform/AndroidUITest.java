package com.gwi.selplatform;

import com.google.gson.reflect.TypeToken;
import com.gwi.ccly.android.commonlibrary.common.net.connector.GWINet;
import com.gwi.ccly.android.commonlibrary.common.net.connector.GWINetConfig;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.request.THeader;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.response.GResponse;
import com.gwi.phr.hospital.BuildConfig;
import com.gwi.selfplatform.common.security.MD5Util;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.connector.implement.gResponse.G5110;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.Request;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.TBase;
import com.gwi.selfplatform.ui.activity.first.SplashActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
/**
 * @author 彭毅
 * @date 2015/4/23.
 */
@RunWith(RobolectricTestRunner.class)
public class AndroidUITest {

    @Before
    public void setup() {
        //do whatever is necessary before every test
    }

    @Test
    public void testLogin(){
        GWINetConfig.Builder builder = new GWINetConfig.Builder();
        SplashActivity activity = Robolectric.setupActivity(SplashActivity.class);
        GWINetConfig config = builder.setBaseUrl(BuildConfig.SERVICE_URL_DEBUG).setContext(activity).build();

        GWINet.init(config);

        Request<TBase> request = new Request<>();
        request.setHeader(new THeader());
        request.getHeader().setAccount("18674843603");
        request.getHeader().setAccountPassword(MD5Util.string2MD5("123456"));

        TypeToken<?> typeToken = new TypeToken<GResponse<G5110>>(){};

        GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
                .execute("login", new RequestCallback() {
                    @Override
                    public void onRequestSuccess(Object o) {
                        assertEquals(o, null);
                    }

                    @Override
                    public void onRequestError(RequestError error) {
                        assertEquals(error, null);
                    }
                });

    }
}
