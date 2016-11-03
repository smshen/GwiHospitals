package com.gwi.selfplatform.module.net.connector.implement.tRequest;

import com.gwi.selfplatform.db.gen.ExT_Phr_CardBindRec;
import com.gwi.selfplatform.db.gen.T_FeedBack_Rec;
import com.gwi.selfplatform.db.gen.T_HealthEdu_Datum;
import com.gwi.selfplatform.db.gen.T_Phone_AuthCode;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_Phr_SignRec;
import com.gwi.selfplatform.db.gen.T_UserInfo;
import com.gwi.selfplatform.module.net.beans.PhrAddressDictQuery;
import com.gwi.selfplatform.module.net.beans.PhrSignRecQuery;
import com.gwi.selfplatform.module.net.beans.T1121;

import java.util.List;

/**
 * @author 彭毅
 * @date 2015/4/23.
 */
public class TBase {
    private String Account;
    private String AccountPassword;
    /**应用标识*/
    private String AppCode;

    private String AppTypeCode;

    private String TerminalNo;

    private T_UserInfo UserInfo;

    private ExT_Phr_CardBindRec Phr_CardBindRec;

    private T_Phr_BaseInfo Phr_BaseInfo;

    private T_FeedBack_Rec FeedBack_Rec;

    private PhrAddressDictQuery AddressDictQuery;
    // --- 健康百科 ---
    private String BodyPartCode;
    private String UseKindCode;
    private String PropertyKindCode;
    private String TestKindCode;
    private String TreatmentKindCode;

    private String DeptCode;
    private String DiseaseId;
    private String DrugId;
    private String TestId;
    private String TreatmentId;

    private String SearchMsg;
    // --- 健康百科 ---

    // --- 智能导诊 ---
    private String HexColor;
    private String Sex;
    private String SymptomId;
    private String SId;


    //百度推送
    private T1121 BaiduUserInfo;

    public String getSId() {
        return SId;
    }

    public void setSId(String sId) {
        SId = sId;
    }

    public String getAppCode() {
        return AppCode;
    }

    public void setAppCode(String appCode) {
        AppCode = appCode;
    }

    public T1121 getBaiduUserInfo() {
        return BaiduUserInfo;
    }

    public void setBaiduUserInfo(T1121 baiduUserInfo) {
        BaiduUserInfo = baiduUserInfo;
    }

    public String getSearchMsg() {
        return SearchMsg;
    }

    public void setSearchMsg(String searchMsg) {
        SearchMsg = searchMsg;
    }

    public String getHexColor() {
        return HexColor;
    }

    public String getSex() {
        return Sex;
    }

    public String getSymptomId() {
        return SymptomId;
    }

    public void setHexColor(String hexColor) {
        HexColor = hexColor;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public void setSymptomId(String symptomId) {
        SymptomId = symptomId;
    }

    public String getBodyPartCode() {
        return BodyPartCode;
    }

    public String getUseKindCode() {
        return UseKindCode;
    }

    public String getPropertyKindCode() {
        return PropertyKindCode;
    }

    public String getTestKindCode() {
        return TestKindCode;
    }

    public String getTreatmentKindCode() {
        return TreatmentKindCode;
    }

    public String getDeptCode() {
        return DeptCode;
    }

    public String getDiseaseId() {
        return DiseaseId;
    }

    public String getDrugId() {
        return DrugId;
    }

    public String getTestId() {
        return TestId;
    }

    public String getTreatmentId() {
        return TreatmentId;
    }

    public void setBodyPartCode(String bodyPartCode) {
        BodyPartCode = bodyPartCode;
    }

    public void setUseKindCode(String useKindCode) {
        UseKindCode = useKindCode;
    }

    public void setPropertyKindCode(String propertyKindCode) {
        PropertyKindCode = propertyKindCode;
    }

    public void setTestKindCode(String testKindCode) {
        TestKindCode = testKindCode;
    }

    public void setTreatmentKindCode(String treatmentKindCode) {
        TreatmentKindCode = treatmentKindCode;
    }

    public void setDeptCode(String deptCode) {
        DeptCode = deptCode;
    }

    public void setDiseaseId(String diseaseId) {
        DiseaseId = diseaseId;
    }

    public void setDrugId(String drugId) {
        DrugId = drugId;
    }

    public void setTestId(String testId) {
        TestId = testId;
    }

    public void setTreatmentId(String treatmentId) {
        TreatmentId = treatmentId;
    }

    public PhrAddressDictQuery getAddressDictQuery() {
        return AddressDictQuery;
    }

    public void setAddressDictQuery(PhrAddressDictQuery addressDictQuery) {
        AddressDictQuery = addressDictQuery;
    }

    private String HospitalCode;

    public String getHospitalCode() {
        return HospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        HospitalCode = hospitalCode;
    }

    public T_FeedBack_Rec getFeedBack_Rec() {
        return FeedBack_Rec;
    }

    public void setFeedBack_Rec(T_FeedBack_Rec feedBack_Rec) {
        FeedBack_Rec = feedBack_Rec;
    }

    public T_Phr_BaseInfo getPhr_BaseInfo() {
        return Phr_BaseInfo;
    }

    public void setPhr_BaseInfo(T_Phr_BaseInfo phr_BaseInfo) {
        Phr_BaseInfo = phr_BaseInfo;
    }

    public ExT_Phr_CardBindRec getPhr_CardBindRec() {
        return Phr_CardBindRec;
    }

    public void setPhr_CardBindRec(ExT_Phr_CardBindRec phr_CardBindRec) {
        Phr_CardBindRec = phr_CardBindRec;
    }

    private List<T_Phr_SignRec> Phr_SignRecs;
    private PhrSignRecQuery SignRecQuery;

    private T_HealthEdu_Datum HealthEdu_Datum;

    private T_Phone_AuthCode Phone_AuthCode;

    public T_Phone_AuthCode getPhone_AuthCode() {
        return Phone_AuthCode;
    }

    public void setPhone_AuthCode(T_Phone_AuthCode phone_AuthCode) {
        Phone_AuthCode = phone_AuthCode;
    }

    public T_HealthEdu_Datum getHealthEdu_Datum() {
        return HealthEdu_Datum;
    }

    public void setHealthEdu_Datum(T_HealthEdu_Datum healthEdu_Datum) {
        HealthEdu_Datum = healthEdu_Datum;
    }

    public PhrSignRecQuery getSignRecQuery() {
        return SignRecQuery;
    }

    public void setSignRecQuery(PhrSignRecQuery signRecQuery) {
        SignRecQuery = signRecQuery;
    }

    public String getAccount() {
        return Account;
    }

    public String getAccountPassword() {
        return AccountPassword;
    }

    public String getAppTypeCode() {
        return AppTypeCode;
    }


    public List<T_Phr_SignRec> getPhr_SignRecs() {
        return Phr_SignRecs;
    }

    public String getTerminalNo() {
        return TerminalNo;
    }

    public T_UserInfo getUserInfo() {
        return UserInfo;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public void setAccountPassword(String accountPassword) {
        AccountPassword = accountPassword;
    }

    public void setAppTypeCode(String appTypeCode) {
        AppTypeCode = appTypeCode;
    }

    public void setPhr_SignRecs(List<T_Phr_SignRec> phr_SignRecs) {
        Phr_SignRecs = phr_SignRecs;
    }

    public void setTerminalNo(String terminalNo) {
        TerminalNo = terminalNo;
    }

    public void setUserInfo(T_UserInfo userInfo) {
        UserInfo = userInfo;
    }
}
