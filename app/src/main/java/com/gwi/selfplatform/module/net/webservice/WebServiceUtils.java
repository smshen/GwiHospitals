package com.gwi.selfplatform.module.net.webservice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.module.net.webservice.WebServiceController.HttpRequestObject;
import com.gwi.selfplatform.module.net.webservice.WebServiceController.SoapRequestObject;

/**
 * 与Web Service交互的工具类
 * 
 * @author Peng Yi
 * 
 */
public class WebServiceUtils {
    private static final String TAG = "WebServiceUtils";

    private WebServiceUtils() {
    }

    /**
     * 向Web service 请求数据.
     * 
     * @param entity
     * @return SoapObject
     */
    public static SoapObject serviceConnect(SoapRequestObject object) throws Exception{
        Logger.d(TAG, "--- serviceConnect() start ---");
        System.setProperty("http.keepAlive", "true");
        SoapObject response = null;
        SoapObject request = new SoapObject(object.namespaces,
                object.methodName);
        // 添加请求参数
        try {
            if (object.paramsPair.size() != 0) {
                Iterator<Entry<String, String>> it = object.paramsPair.entrySet()
                        .iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = it.next();
                    String name = String.valueOf(entry.getKey());
                    Object value = entry.getValue();
                    request.addProperty(name, value);
                }
            }
        } catch (Exception e1) {
            Logger.e(TAG, "serviceConnect()",e1);
        }
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                object.versionType);
        envelope.dotNet = true;
        envelope.bodyOut = request;
        HttpTransportSE ht = new HttpTransportSE(object.serviceUrl);
        ht.debug = false;
        try {

//            if (Thread.currentThread().isInterrupted()) {
//                throw new Exception("currentThread is isInterrupted");
//            }
            ht.call(object.soapAction, envelope);
//            if (Thread.currentThread().isInterrupted()) {
//                throw new Exception("currentThread is isInterrupted");
//            }

            if (envelope.bodyIn instanceof SoapObject) {
                response = (SoapObject) envelope.bodyIn;
            }else {
                SoapFault fault = (SoapFault) envelope.bodyIn;
                throw new Exception(fault.getLocalizedMessage());
            }
        } catch (Exception e) {
                Logger.e(TAG, "serviceConnect()",e);
                throw e;
        }
        Logger.d(TAG, "--- serviceConnect() end ---");
        return response;
    }

    public static String serviceConnect(HttpRequestObject object)throws Exception{
        String result = null;
        HttpURLConnection conn = null;
        int timeout = 7000;
        try {
            conn = (HttpURLConnection) new URL(object.url).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.addRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            conn.setDefaultUseCaches(false);
            conn.setRequestMethod("POST");

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    os, "UTF-8"));
            writer.write(getQuery(object.params));
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

            int status = conn.getResponseCode();
            if (status == 200) {
                InputStream is = conn.getInputStream();
//                testStream(is);
                ResopnseXmlParser parser = new ResopnseXmlParser();
                result = parser.parserXml(is);
                is.close();
                
            } else {
                Logger.e(TAG, "status=" + status);
                throw new Exception(conn.getResponseMessage());
            }

        } catch (Exception e) {
            Logger.e(TAG, TAG, e);
            throw e;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return result;

    }
    
    private static void testStream(InputStream is) throws Exception{
        BufferedReader br = new BufferedReader(
                new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = br.readLine()) != null) {
                response.append(line);
                response.append("\r");
                }
                Logger.d(TAG, "testStream:\n"+response.toString());
    }
    
    private static String getQuery(List<NameValuePair> params)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    private static final class ResopnseXmlParser extends DefaultHandler {

        public ResopnseXmlParser() {
            super();
        }

        /** XML attribute: string */
        private static final String ATTR_RESULT = "string";

        public String parserXml(InputStream inputStream)
                throws XmlPullParserException, IOException {
            String result = null;
            // 初始化parser factory
            XmlPullParserFactory localXmlPullParserFactory = XmlPullParserFactory
                    .newInstance();
            // 关闭处理命名空间
            localXmlPullParserFactory.setNamespaceAware(false);
            // 初始化一个新的pull parser
            XmlPullParser localXmlPullParser = localXmlPullParserFactory
                    .newPullParser();

            // 设置pull parse的input stream
            localXmlPullParser.setInput(inputStream, "UTF-8");

            // 获取第一个eventType.
            int eventType = localXmlPullParser.getEventType();
            // 如果不是START_DOCUMENT
            if (eventType != XmlPullParser.START_DOCUMENT) {
                throw new XmlPullParserException("Invalid XML");
            }
            // xml读取完毕跳出循环,或者读取到数据跳出
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (localXmlPullParser.getName().equalsIgnoreCase(
                            ATTR_RESULT)) {
                        return localXmlPullParser.nextText();
                    }
                }
                eventType = localXmlPullParser.next();
            }

            return result;
        }
    }

}
