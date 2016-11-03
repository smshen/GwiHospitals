package com.gwi.selfplatform.module.net.connector.implement.gResponse;

import com.gwi.selfplatform.db.gen.T_Phone_AuthCode;

/**
 *找回用户密码验证
 * @author 彭毅
 * @date 2015/5/20.
 */
public class G5115 extends GBase {
    private T_Phone_AuthCode PhoneAuth;

    public T_Phone_AuthCode getPhoneAuth() {
        return PhoneAuth;
    }

    public void setPhoneAuth(T_Phone_AuthCode phoneAuth) {
        PhoneAuth = phoneAuth;
    }

//    public static void main(String args[]) {
//        String json = "{\"PhoneAuth\":{\"AuthCode\":\"6833\",\"BuildTime\":\"/Date(1432087238849+0800)/\",\"PhoneNumber\":\"13974551234\",\"RecNo\":51676,\"SendTimes\":0},\"UserInfo\":{\"AlipayOpenId\":null,\"AppCode\":\"444907439\",\"EMail\":null,\"EhrId\":\"99e17e31-8c65-43ad-b1b1-4fc4ff57f1fc\",\"MicroOpenId\":null,\"MobilePhone\":\"13974551234\",\"NickName\":null,\"RecordDate\":\"/Date(1432087104390+0800)/\",\"RecordMan\":\"个人健康服务平台\",\"RegDate\":\"/Date(1430293279683+0800)/\",\"RoleCode\":\"01\",\"RoleInfo\":null,\"SubAccountSid\":null,\"SubToken\":null,\"UserCode\":\"13974551234\",\"UserId\":50681,\"UserName\":\"测试asd\",\"UserPwd\":\"e10adc3949ba59abbe56e057f20f883e\",\"UserStatus\":1,\"VoIPAccount\":null,\"VoIPPwd\":null}}";
//        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,new WebServiceController.DateAdapter()).create();
//        G5115 g5115 = gson.fromJson(json,G5115.class);
//        System.out.println(g5115.getPhoneAuth().getAuthCode());
//    }
}
