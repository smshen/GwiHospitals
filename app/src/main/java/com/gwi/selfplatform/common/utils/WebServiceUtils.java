package com.gwi.selfplatform.common.utils;

import com.gwi.selfplatform.common.utils.WebUtil.SoapRequestObject;
import com.gwi.selfplatform.module.net.webservice.WebServiceController.HttpRequestObject;

import org.apache.http.NameValuePair;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

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

/**
 * 
 * @author Peng Yi
 * 
 */
public class WebServiceUtils {
    private static final String TAG = "WebServiceUtils";

    private WebServiceUtils() {
    }

    /**
     * 鍚慦eb service 璇锋眰鏁版嵁.
     * 
     * @param entity
     * @return SoapObject
     */
    public static SoapObject serviceConnect(SoapRequestObject object) {
        Logger.d(TAG, "--- serviceConnect() start ---");
        System.setProperty("http.keepAlive", "true");
        SoapObject response = null;
        SoapObject request = new SoapObject(object.namespaces,
                object.methodName);
        // 娣诲姞璇锋眰鍙傛暟
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
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                object.versionType);
        envelope.dotNet = true;
        envelope.bodyOut = request;
        HttpTransportSE ht = new HttpTransportSE(object.serviceUrl);
        ht.debug = false;
        try {

            ht.call(object.namespaces+object.methodName , envelope);

            if (envelope.bodyIn instanceof SoapObject) {
                response = (SoapObject) envelope.bodyIn;
            }else {
                SoapFault fault = (SoapFault) envelope.bodyIn;
                fault.fillInStackTrace();
            }
        } catch (Exception e) {
                Logger.e(TAG, "serviceConnect()",e);
        }
        Logger.d(TAG, "--- serviceConnect() end ---");
        return response;
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

    public static String serviceConnect(HttpRequestObject object) {
        String result = null;
        HttpURLConnection conn = null;
        int timeout = 10000;
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
//                ResopnseXmlParser parser = new ResopnseXmlParser();
//                result = parser.parserXml(is);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder resultBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    resultBuilder.append(line);
                }
                result = resultBuilder.toString();
                reader.close();
                is.close();
            } else {
                Logger.e(TAG, "serviceConnect: status=" + status);
            }

        } catch(Exception e) {
            Logger.e(TAG, "serviceConnect()",e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return result;

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
            // 鍒濆鍖杙arser factory
            XmlPullParserFactory localXmlPullParserFactory = XmlPullParserFactory
                    .newInstance();
            // 鍏抽棴澶勭悊鍛藉悕绌洪棿
            localXmlPullParserFactory.setNamespaceAware(false);
            // 鍒濆鍖栦竴涓柊鐨刾ull parser
            XmlPullParser localXmlPullParser = localXmlPullParserFactory
                    .newPullParser();

            // 璁剧疆pull parse鐨刬nput stream
            localXmlPullParser.setInput(inputStream, "UTF-8");

            // 鑾峰彇绗竴涓猠ventType.
            int eventType = localXmlPullParser.getEventType();
            // 濡傛灉涓嶆槸START_DOCUMENT
            if (eventType != XmlPullParser.START_DOCUMENT) {
                throw new XmlPullParserException("Invalid XML");
            }
            // xml璇诲彇瀹屾瘯璺冲嚭寰幆,鎴栬�璇诲彇鍒版暟鎹烦鍑�
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
