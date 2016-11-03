package com.gwi.selfplatform.module.net.connector.implement.gResponse;

/**
 * 判断手机号码已注册
 * @author 彭毅
 * @date 2015/5/20.
 */
public class G5117 extends GBase{
    public static final int REGISTERED = 1;
    public static final int UNREGISTERED = 0;

    private int MobilePhoneExist;

    public int getMobilePhoneExist() {
        return MobilePhoneExist;
    }

    public void setMobilePhoneExist(int mobilePhoneExist) {
        MobilePhoneExist = mobilePhoneExist;
    }

    //    public static void main(String args[]) {
//        String json = "[{\"Alias\":null,\"BirthDay\":\"\\/Date(-547632000000+0800)\\/\",\"BloodType\":0,\"CreateDate\":null,\"CreateMan\":null,\"CreateOrg\":null,\"EduDegree\":0,\"EhrCode\":null,\"EhrID\":\"2e99753c-1b57-46b7-81bc-2f23b0b23b83\",\"IDCard\":\"330400195208254319\",\"LiveType\":0,\"MaritalStatus\":0,\"Name\":\"李俊彦\",\"Nation\":0,\"NowAddress\":null,\"NowAddressCode\":null,\"Occupation\":0,\"OlderDisease\":null,\"PayType\":0,\"RegAddress\":null,\"RegAddressCode\":null,\"RelationName\":null,\"RelationPhone\":null,\"SelfPhone\":\"15222451123\",\"Sex\":1,\"UserId\":50681,\"WorkUnit\":null},{\"Alias\":null,\"BirthDay\":\"\\/Date(263923200000+0800)\\/\",\"BloodType\":0,\"CreateDate\":null,\"CreateMan\":null,\"CreateOrg\":null,\"EduDegree\":0,\"EhrCode\":null,\"EhrID\":\"70b28265-9c7f-4cd9-aea3-8ca96dbd36f1\",\"IDCard\":\"350322197805146872\",\"LiveType\":0,\"MaritalStatus\":0,\"Name\":\"巫锐进\",\"Nation\":0,\"NowAddress\":null,\"NowAddressCode\":null,\"Occupation\":0,\"OlderDisease\":null,\"PayType\":0,\"RegAddress\":null,\"RegAddressCode\":null,\"RelationName\":null,\"RelationPhone\":null,\"SelfPhone\":\"18945455544\",\"Sex\":1,\"UserId\":50681,\"WorkUnit\":null},{\"Alias\":null,\"BirthDay\":\"\\/Date(-547632000000+0800)\\/\",\"BloodType\":0,\"CreateDate\":null,\"CreateMan\":null,\"CreateOrg\":null,\"EduDegree\":0,\"EhrCode\":null,\"EhrID\":\"99e17e31-8c65-43ad-b1b1-4fc4ff57f1fd\",\"IDCard\":\"330400195208254319\",\"LiveType\":0,\"MaritalStatus\":0,\"Name\":\"李俊彦\",\"Nation\":0,\"NowAddress\":null,\"NowAddressCode\":null,\"Occupation\":0,\"OlderDisease\":null,\"PayType\":0,\"RegAddress\":null,\"RegAddressCode\":null,\"RelationName\":null,\"RelationPhone\":null,\"SelfPhone\":\"15222451123\",\"Sex\":1,\"UserId\":50681,\"WorkUnit\":null},{\"Alias\":null,\"BirthDay\":\"\\/Date(307641600000+0800)\\/\",\"BloodType\":0,\"CreateDate\":\"\\/Date(1430293279683+0800)\\/\",\"CreateMan\":null,\"CreateOrg\":null,\"EduDegree\":0,\"EhrCode\":null,\"EhrID\":\"99e17e31-8c65-43ad-b1b1-4fc4ff57f1fc\",\"IDCard\":\"140401197910022279\",\"LiveType\":0,\"MaritalStatus\":0,\"Name\":\"测试asd\",\"Nation\":0,\"NowAddress\":null,\"NowAddressCode\":null,\"Occupation\":0,\"OlderDisease\":null,\"PayType\":0,\"RegAddress\":null,\"RegAddressCode\":null,\"RelationName\":null,\"RelationPhone\":null,\"SelfPhone\":\"13455458845\",\"Sex\":0,\"UserId\":50681,\"WorkUnit\":null}]";
//        TypeToken<List<T_Phr_BaseInfo>> typeToken = new TypeToken<List<T_Phr_BaseInfo>>(){};
//        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,new WebServiceController.DateAdapter()).create();
//        List<T_Phr_BaseInfo> result = gson.fromJson(json,typeToken.getType());
//        System.out.println(result.size());
//    }
}
