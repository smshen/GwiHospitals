package com.gwi.selfplatform.ui.receiver;

import android.content.Context;
import android.widget.Toast;

import com.baidu.android.pushservice.PushManager;
import com.baidu.android.pushservice.PushMessageReceiver;
import com.gwi.ccly.android.commonlibrary.common.net.connector.GWIVolleyError;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.response.GResponse;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.common.utils.WebUtil;
import com.gwi.selfplatform.db.gen.T_UserInfo;
import com.gwi.selfplatform.module.net.beans.T1121;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Push消息处理receiver。请编写您需要的回调函数， 一般来说： onBind是必须的，用来处理startWork返回值；
 * onMessage用来接收透传消息； onSetTags、onDelTags、onListTags是tag相关操作的回调；
 * onNotificationClicked在通知被点击时回调； onUnbind是stopWork接口的返回值回调
 * <p/>
 * 返回值中的errorCode，解释如下：
 * 0 - Success
 * 10001 - Network Problem
 * 30600 - Internal Server Error
 * 30601 - Method Not Allowed
 * 30602 - Request Params Not Valid
 * 30603 - Authentication Failed
 * 30604 - Quota Use Up Payment Required
 * 30605 - Data Required Not Found
 * 30606 - Request Time Expires Timeout
 * 30607 - Channel Token Timeout
 * 30608 - Bind Relation Not Found
 * 30609 - Bind Number Too Many
 * <p/>
 * 当您遇到以上返回错误时，如果解释不了您的问题，请用同一请求的返回值requestId和errorCode联系我们追查问题。
 */
public class BaiduPushReceiver extends PushMessageReceiver {
    /**
     * TAG to Log
     */
    public static final String TAG = BaiduPushReceiver.class
            .getSimpleName();


    /**
     * 调用PushManager.startWork后，sdk将对push
     * server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。 如果您需要用单播推送，需要把这里获取的channel
     * id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。
     *
     * @param context   BroadcastReceiver的执行Context
     * @param errorCode 绑定接口返回值，0 - 成功
     * @param appid     应用id。errorCode非0时为null
     * @param userId    应用user id。errorCode非0时为null
     * @param channelId 应用channel id。errorCode非0时为null
     * @param requestId 向服务端发起的请求id。在追查问题时有用；
     * @return none
     */
    @Override
    public void onBind(Context context, int errorCode, String appid,
                       String userId, String channelId, String requestId) {
        String responseString = "onBind errorCode=" + errorCode + " appid="
                + appid + " userId=" + userId + " channelId=" + channelId
                + " requestId=" + requestId;
        Logger.d(TAG, responseString);

/*        // 绑定成功，设置已绑定flag，可以有效的减少不必要的绑定请求
        if (errorCode == 0) {
            Utils.setBind(context, true);
        }*/

        List<String> tags = new ArrayList<String>();
        tags.add(WebUtil.HOSP_CODE);
        PushManager.setTags(context, tags);

        T1121 pushUserInfo = new T1121();
        pushUserInfo.setBaiduChannelId(channelId);
        pushUserInfo.setBaiduTag(tags.toString());
        pushUserInfo.setBaiduUserId(userId);
        pushUserInfo.setHospitalCode(WebUtil.HOSP_CODE);
        T_UserInfo userInfo = GlobalSettings.INSTANCE.getCurrentUser();
        if (userInfo != null) {
            pushUserInfo.setUserId(userInfo.getUserId());
            upUserInfoToServer(pushUserInfo);
        }


    }

    /**
     * 接收透传消息的函数。
     *
     * @param context             上下文
     * @param message             推送的消息
     * @param customContentString 自定义内容,为空或者json字符串
     */
    @Override
    public void onMessage(Context context, String message,
                          String customContentString) {
        String messageString = "透传消息 message=\"" + message
                + "\" customContentString=" + customContentString;
        Logger.d(TAG, messageString);

/*        // 自定义内容获取方式，mykey和myvalue对应透传消息推送时自定义内容中设置的键和值
        if (!TextUtils.isEmpty(customContentString)) {
            JSONObject customJson = null;
            try {
                customJson = new JSONObject(customContentString);
                String myvalue = null;
                if (customJson.isNull("mykey")) {
                    myvalue = customJson.getString("mykey");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }*/

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        
/*
        Intent intent = new Intent(context, PushMessageActivity.class);   
        intent.putExtra("msg", message);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     
        context.startActivity(intent);
*/
    }

    /**
     * 接收通知点击的函数。注：推送通知被用户点击前，应用无法通过接口获取通知的内容。
     *
     * @param context             上下文
     * @param title               推送的通知的标题
     * @param description         推送的通知的描述
     * @param customContentString 自定义内容，为空或者json字符串
     */
    @Override
    public void onNotificationClicked(Context context, String title,
                                      String description, String customContentString) {
        String notifyString = "通知点击 title=\"" + title + "\" description=\""
                + description + "\" customContent=" + customContentString;
        Logger.d(TAG, notifyString);
/*
        // 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
        if (!TextUtils.isEmpty(customContentString)) {
            JSONObject customJson = null;
            try {
                customJson = new JSONObject(customContentString);
                String myvalue = null;
                if (customJson.isNull("mykey")) {
                    myvalue = customJson.getString("mykey");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
*/

    }

    @Override
    public void onNotificationArrived(Context context, String title,
                                      String description, String customContentString) {
        String notifyString = "通知点击 title=\"" + title + "\" description=\""
                + description + "\" customContent=" + customContentString;
        Logger.d(TAG, notifyString);
/*
        // 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
        if (!TextUtils.isEmpty(customContentString)) {
            JSONObject customJson = null;
            try {
                customJson = new JSONObject(customContentString);
                String myvalue = null;
                if (customJson.isNull("mykey")) {
                    myvalue = customJson.getString("mykey");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
*/

    }

    /**
     * setTags() 的回调函数。
     *
     * @param context     上下文
     * @param errorCode   错误码。0表示某些tag已经设置成功；非0表示所有tag的设置均失败。
     * @param successTags 设置成功的tag
     * @param failTags    设置失败的tag
     * @param requestId   分配给对云推送的请求的id
     */
    @Override
    public void onSetTags(Context context, int errorCode,
                          List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onSetTags errorCode=" + errorCode
                + " sucessTags=" + sucessTags + " failTags=" + failTags
                + " requestId=" + requestId;
        Logger.d(TAG, responseString);

    }

    /**
     * delTags() 的回调函数。
     *
     * @param context     上下文
     * @param errorCode   错误码。0表示某些tag已经删除成功；非0表示所有tag均删除失败。
     * @param successTags 成功删除的tag
     * @param failTags    删除失败的tag
     * @param requestId   分配给对云推送的请求的id
     */
    @Override
    public void onDelTags(Context context, int errorCode,
                          List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onDelTags errorCode=" + errorCode
                + " sucessTags=" + sucessTags + " failTags=" + failTags
                + " requestId=" + requestId;
        Logger.d(TAG, responseString);

    }

    /**
     * listTags() 的回调函数。
     *
     * @param context   上下文
     * @param errorCode 错误码。0表示列举tag成功；非0表示失败。
     * @param tags      当前应用设置的所有tag。
     * @param requestId 分配给对云推送的请求的id
     */
    @Override
    public void onListTags(Context context, int errorCode, List<String> tags,
                           String requestId) {
        String responseString = "onListTags errorCode=" + errorCode + " tags="
                + tags;
        Logger.d(TAG, responseString);

    }

    /**
     * PushManager.stopWork() 的回调函数。
     *
     * @param context   上下文
     * @param errorCode 错误码。0表示从云推送解绑定成功；非0表示失败。
     * @param requestId 分配给对云推送的请求的id
     */
    @Override
    public void onUnbind(Context context, int errorCode, String requestId) {
        String responseString = "onUnbind errorCode=" + errorCode
                + " requestId = " + requestId;
        Logger.d(TAG, responseString);

//        // 解绑定成功，设置未绑定flag，
//        if (errorCode == 0) {
//            Utils.setBind(context, false);
//        }

    }


    private void upUserInfoToServer(final T1121 pushUserInfo) {
        ApiCodeTemplate.uploadBaiduPushUser(TAG, pushUserInfo, new RequestCallback<Void>() {
            @Override
            public void onRequestSuccess(Void result) {

            }

            @Override
            public void onRequestError(RequestError error) {
                if (error != null && error.getException() instanceof GWIVolleyError) {
                    GWIVolleyError volleyError = (GWIVolleyError) error.getException();
                    if (volleyError.getStatus() == GResponse.SUCCESS && volleyError.getCause() != null) {
                        Logger.i(TAG, volleyError.getCause().getLocalizedMessage());
                    }
                }
            }
        });
//         new AsyncTask<Void, Void, Boolean>() {
//
//             @Override
//             protected void onPreExecute() {
//                 super.onPreExecute();
//             }
//
//             @Override
//             protected Boolean doInBackground(Void... params) {
//                 try {
//                        WebServiceController.bindPushUserInfo(pushUserInfo);
//                      return true ;
//
//                 } catch (Exception e) {
//                     Logger.e(TAG, "doInBackground", e);
//                     return false ;
//                 }
//             }
//
//         }.execute();

    }
}
