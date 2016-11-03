package com.gwi.selfplatform.common.utils;

import android.text.TextUtils;

import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_UserInfo;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.Request;
import com.gwi.selfplatform.module.net.request.PayInfo;
import com.gwi.selfplatform.module.net.request.UserInfo;
import com.gwi.selfplatform.module.pay.xml.XmlParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BankUtil {
    public static final String BANK_URL = "https://domainname/PGWPortal/";
    
    public static final String BANK_NET = "RecvOrder.do";
    public static final String BANK_WAP = "B2CMobileRecvOrder.do";
    public static final String BANK_HOME = "HomeBankRecvOrder.do";
    public static final String BANK_QUERY = "CommonQueryOrder.do";
    public static final String BANK_REFUND = "RefundOrder.do";
    
    public static final String BOC_ID = "merchantNo";
    public static final String PayType = "payType";
    public static final String OrderNo = "orderNo";
    public static final String CurCode = "curCode";
    public static final String OrderAmount = "orderAmount";
    public static final String OrderTime = "orderTime";
    public static final String OrderNote = "orderNote";
    public static final String OrderUrl = "orderUrl";
    public static final String SpMobile = "spMobile";
    public static final String OrderTimeoutDate = "orderTimeoutDate";
    public static final String SignData = "signData";
    
    public static final double consumeFee = 0.00;

    public static final double TEST_FEE = 0.01;
    
    //支付方式
    public static final String PayMode_nPay = "0";//诊疗卡
    public static final String PayMode_Card = "1";//诊疗卡
    public static final String PayMode_Ali = "7";//支付宝
    
    /*
     *  业务方式
     *  挂号 = 1,
                预约挂号 = 2,
                预交金充值 = 3,
                缴费 = 4,
                取消预约 = 5,
     */
    public static final String BUSINESSTYPE_NOT_AVALIABLE = "-1";
    public static final String BusinessType_Rgt = "1";
    public static final String BusinessType_Recipe = "4";
    public static final String BusinessType_Refound = "5";
    public static final String BusinessType_CARD_CHARGE = "3";
    public static final String BusinessType_Rgt_Order = "2";
    
    public static String httpPostToBank(String url ,String param){
         PrintWriter out = null;
         BufferedReader in = null;
         String result = "";
         try {
             URL realUrl = new URL(url);
             // 打开和URL之间的连接
             URLConnection conn = realUrl.openConnection();
             // 设置通用的请求属性
             conn.setRequestProperty("accept", "*/*");
             conn.setRequestProperty("connection", "Keep-Alive");
             conn.setRequestProperty("user-agent",
                     "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
             // 发送POST请求必须设置如下两行
             conn.setDoOutput(true);
             conn.setDoInput(true);
             // 获取URLConnection对象对应的输出流
             out = new PrintWriter(conn.getOutputStream());
             // 发送请求参数
             out.print(param);
             // flush输出流的缓冲
             out.flush();
             // 定义BufferedReader输入流来读取URL的响应
             in = new BufferedReader(
                     new InputStreamReader(conn.getInputStream()));
             String line;
             while ((line = in.readLine()) != null) {
                 result += line;
             }
         } catch (Exception e) {
             System.out.println("发送 POST 请求出现异常！"+e);
             e.printStackTrace();
         }
         //使用finally块来关闭输出流、输入流
         finally{
             try{
                 if(out!=null){
                     out.close();
                 }
                 if(in!=null){
                     in.close();
                 }
             }
             catch(IOException ex){
                 ex.printStackTrace();
             }
         }
         return result;
     }    
    
    public static String getBankDate(){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        String startDate = format.format(cd.getTime());
        return startDate;
    }
    
    
    public static String getPayMode(String payMode){
        /**TODO:
         *  不付费 = 0,
            诊疗预交金账户 = 1,
            银行卡 = 2,
            个人医保账户 = 3,
            医保预交金混合支付 = 4,
            医保银行卡混合支付 = 5,
            微信支付 = 6,
            支付宝支付 = 7
         */
        String result = null;
        if(payMode.equals(Constants.PAY_MODE_NONE)){
            result = "未支付";
        }else if(payMode.equals(Constants.PAY_MODE_CARD_MEDICAL)){
            result = "诊疗卡预交金";
        }else if(payMode.equals(Constants.PAY_MODE_HOSP_ACCOUNT_PESONAL)){
            result = "个人医保账户";
        }else if(payMode.equals(Constants.PAY_MODE_HYBRID_PRE_PAY)){
            result = "医保预交金混合";
        }else if(payMode.equals(Constants.PAY_MODE_HYBRID_BANK_PAY)){
            result = "医保银行卡混合";
        }else if(payMode.equals(Constants.PAY_MODE_WEIXIN)){
            result = "微信支付";
        }else if(payMode.equals(Constants.PAY_MODE_ALIPAY)){
            result = "支付宝支付";
        }else if(payMode.equals(Constants.PAY_MODE_CASH)){
            result = "现金";
        }else if(payMode.equals(Constants.PAY_MODE_UNIONPAY)){
            result = "银联支付";
        }else if(payMode.equals(Constants.PAY_MODE_BOC)){
            result = "中国银行 ";
        }
        return result;
    }

    public static String getBusXMLy(String type ,Request request){
        StringBuilder builder = new StringBuilder();
        //builder.append("![CDATA[");
        if(type.equals(BankUtil.BusinessType_Rgt)){
            builder.append(XmlParser.getRegisterJSON(request));
        }else if(type.equals(BankUtil.BusinessType_CARD_CHARGE)){
            builder.append(XmlParser.getChargeJSON(request));
        }else if(type.equals(BusinessType_Rgt_Order)) {
            builder.append(XmlParser.getOrderRegisterJSON(request));
        }else if (type.equals(BusinessType_Recipe)) {
            builder.append(XmlParser.getRecipePayJSON(request));
        }else if (type.equals(BusinessType_Rgt_Order)) {
            builder.append(XmlParser.getOrderRegisterJSON(request));
        }
        //builder.append("]]");
        return builder.toString();
    }

    public static PayInfo initPayInfo(T_UserInfo userInfo,double transactionValue){
        String dataStr = CommonUtils.phareDateFormat(Constants.FORMAT_ISO_DATE_TIME, new Date());
        PayInfo payInfo = new PayInfo();
        payInfo.setAuthorizeNo("");
        payInfo.setBankPNo("");
        payInfo.setPOSID("");
        payInfo.setPOSSerNo("");
        payInfo.setReceiptNo("");
        payInfo.setTranAccount(userInfo.getUserId() + "");
        payInfo.setTranAmt(transactionValue);
        if(BankUtil.consumeFee == 0.01){
            payInfo.setTranAmt(BankUtil.consumeFee);
        }
        if(!TextUtils.isEmpty(dataStr)){
            payInfo.setTranDate(dataStr.replace("-", "").substring(0, 8));
            payInfo.setTranTime(dataStr.replace(":", "").substring(11));
        }else{
            payInfo.setTranDate("");
            payInfo.setTranTime("");
        }
        payInfo.setTranSerNo("");
        return payInfo;
    }
    public static UserInfo initUserInfo(T_Phr_BaseInfo member){
        final UserInfo userInfo = new UserInfo();
        userInfo.setCorpCode(WebUtil.CorpCode);
        userInfo.setCusOrderNum(null);
        userInfo.setIDCardNo(member.getIDCard());
        userInfo.setMobile(member.getSelfPhone());
        try {
            //邵阳市第一人民一医院
            userInfo.setSex(String.valueOf(CommonUtils.getSexFromIdCard(member.getIDCard())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        userInfo.setUserName(member.getName());
        return userInfo;
    }

}
