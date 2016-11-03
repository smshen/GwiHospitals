package com.gwi.selfplatform.module.net.beans;

import com.gwi.selfplatform.db.gen.Base_AddressDict;
import com.gwi.selfplatform.db.gen.ExT_Phr_CardBindRec;
import com.gwi.selfplatform.db.gen.T_Base_DatumClass;
import com.gwi.selfplatform.db.gen.T_FeedBack_Rec;
import com.gwi.selfplatform.db.gen.T_HealthEdu_Datum;
import com.gwi.selfplatform.db.gen.T_Phone_AuthCode;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_Phr_SignRec;
import com.gwi.selfplatform.db.gen.T_UserInfo;

import java.util.List;

/**
 * 响应实体
 * 
 * @author 彭毅
 * 
 */
public class Response extends Phr {
    private int Status;
    private String ResultMsg;
    /** yyyy-MM-dd HH:mm:ss */
    private String OpTime;
    private String TranSerNo;
    private int FunCode;

    private int MobilePhoneExist;

    private List<Base_AddressDict> Base_AddressDicts;
    
    private List<KBDiseaseDetails> KB_Diseases;
    private List<KBDepart> Base_DeptDicts;
    private List<KBBodyPart> Base_BodyPartDicts;
	private List<KBDrugUseKind> Base_UseKindDicts;
    private List<KBDrugProperty> Base_PropertyKindDicts;
    private List<KBDrugDetails> KB_Drugs;
    private List<KBTestCheckKind> Base_TestKindDicts;
    private List<KBTestCheckDetails> KB_TestChecks;
    private List<KBTreatmentKind> Base_TreatmentKindDicts;
    private List<KBTreatmentDetails> KB_Treatments;
    
    private List<BodyToSymptom> Base_SymptomDicts;
    
    private List<SymptomToDisease> KB_SymptomAndDiseases;
    
    public List<SymptomToDisease> getKB_SymptomAndDiseases() {
        return KB_SymptomAndDiseases;
    }

    public void setKB_SymptomAndDiseases(List<SymptomToDisease> kB_SymptomAndDisease) {
        KB_SymptomAndDiseases = kB_SymptomAndDisease;
    }

    public List<BodyToSymptom> getBase_SymptomDicts() {
        return Base_SymptomDicts;
    }

    public void setBase_SymptomDicts(List<BodyToSymptom> base_SymptomDicts) {
        Base_SymptomDicts = base_SymptomDicts;
    }

    public List<KBDiseaseDetails> getKB_Diseases() {
        return KB_Diseases;
    }

    public List<KBDepart> getBase_DeptDicts() {
        return Base_DeptDicts;
    }

    public List<KBBodyPart> getBase_BodyPartDicts() {
        return Base_BodyPartDicts;
    }
    
    public List<KBDrugUseKind> getBase_UseKindDicts() {
        return Base_UseKindDicts;
    }

    public List<KBDrugProperty> getBase_PropertyKindDicts() {
        return Base_PropertyKindDicts;
    }

    public List<KBDrugDetails> getKB_Drugs() {
        return KB_Drugs;
    }

    public List<KBTestCheckKind> getBase_TestKindDicts() {
        return Base_TestKindDicts;
    }

    public List<KBTestCheckDetails> getKB_TestChecks() {
        return KB_TestChecks;
    }

    public List<KBTreatmentKind> getBase_TreatmentKindDicts() {
        return Base_TreatmentKindDicts;
    }

    public List<KBTreatmentDetails> getKB_Treatments() {
        return KB_Treatments;
    }

    public void setKB_Diseases(List<KBDiseaseDetails> kB_Diseases) {
        KB_Diseases = kB_Diseases;
    }

    public void setBase_DeptDicts(List<KBDepart> base_DeptDicts) {
        Base_DeptDicts = base_DeptDicts;
    }

    public void setBase_BodyPartDicts(List<KBBodyPart> base_BodyPartDicts) {
        Base_BodyPartDicts = base_BodyPartDicts;
    }

    public void setBase_UseKindDicts(List<KBDrugUseKind> base_UseKindDicts) {
        Base_UseKindDicts = base_UseKindDicts;
    }

    public void setBase_PropertyKindDicts(
            List<KBDrugProperty> base_PropertyKindDicts) {
        Base_PropertyKindDicts = base_PropertyKindDicts;
    }

    public void setKB_Drugs(List<KBDrugDetails> kB_Drugs) {
        KB_Drugs = kB_Drugs;
    }

    public void setBase_TestKindDicts(List<KBTestCheckKind> base_TestKindDicts) {
        Base_TestKindDicts = base_TestKindDicts;
    }

    public void setKB_TestChecks(List<KBTestCheckDetails> kB_TestChecks) {
        KB_TestChecks = kB_TestChecks;
    }

    public void setBase_TreatmentKindDicts(
            List<KBTreatmentKind> base_TreatmentKindDicts) {
        Base_TreatmentKindDicts = base_TreatmentKindDicts;
    }

    public void setKB_Treatments(List<KBTreatmentDetails> kB_Treatments) {
        KB_Treatments = kB_Treatments;
    }

    public List<Base_AddressDict> getBase_AddressDicts() {
        return Base_AddressDicts;
    }

    public void setBase_AddressDicts(List<Base_AddressDict> base_AddressDicts) {
        Base_AddressDicts = base_AddressDicts;
    }

    public int getMobilePhoneExist() {
        return MobilePhoneExist;
    }

    public void setMobilePhoneExist(int mobilePhoneExist) {
        MobilePhoneExist = mobilePhoneExist;
    }

    private T_UserInfo UserInfo;

    private List<T_Phr_BaseInfo> Phr_BaseInfos;

    private T_Phr_BaseInfo Phr_BaseInfo;

    private List<ExT_Phr_CardBindRec> Phr_CardBindRecs;

    private List<T_HealthEdu_Datum> HealthEdu_Datums;

    private MobileVerParam Mobile_VerParam;

    private T_Phone_AuthCode Phone_AuthCode;

    private List<T_Base_DatumClass> Base_DatumClasses;

    private T_FeedBack_Rec FeedBack_Rec;

    private HealthReport HealthReport;

    public HealthReport getHealthReport() {
        return HealthReport;
    }

    public void setHealthReport(HealthReport healthReport) {
        HealthReport = healthReport;
    }

    public T_FeedBack_Rec getFeedBack_Rec() {
        return FeedBack_Rec;
    }

    public void setFeedBack_Rec(T_FeedBack_Rec feedBack_Rec) {
        FeedBack_Rec = feedBack_Rec;
    }

    public List<T_Base_DatumClass> getBase_DatumClasses() {
        return Base_DatumClasses;
    }

    public void setBase_DatumClasses(List<T_Base_DatumClass> base_DatumClasses) {
        Base_DatumClasses = base_DatumClasses;
    }

    public T_Phone_AuthCode getPhone_AuthCode() {
        return Phone_AuthCode;
    }

    public void setPhone_AuthCode(T_Phone_AuthCode phone_AuthCode) {
        Phone_AuthCode = phone_AuthCode;
    }

    public MobileVerParam getMobile_VerParam() {
        return Mobile_VerParam;
    }

    public void setMobile_VerParam(MobileVerParam mobile_VerParam) {
        Mobile_VerParam = mobile_VerParam;
    }

    public List<T_HealthEdu_Datum> getHealthEdu_Datums() {
        return HealthEdu_Datums;
    }

    public void setHealthEdu_Datums(List<T_HealthEdu_Datum> healthEdu_Datums) {
        HealthEdu_Datums = healthEdu_Datums;
    }

    public List<ExT_Phr_CardBindRec> getPhr_CardBindRecs() {
        return Phr_CardBindRecs;
    }

    public void setPhr_CardBindRecs(List<ExT_Phr_CardBindRec> phr_CardBindRecs) {
        Phr_CardBindRecs = phr_CardBindRecs;
    }

    public T_Phr_BaseInfo getPhr_BaseInfo() {
        return Phr_BaseInfo;
    }

    public void setPhr_BaseInfo(T_Phr_BaseInfo phr_BaseInfo) {
        Phr_BaseInfo = phr_BaseInfo;
    }

    public List<T_Phr_BaseInfo> getPhr_BaseInfos() {
        return Phr_BaseInfos;
    }

    public void setPhr_BaseInfos(List<T_Phr_BaseInfo> phr_BaseInfos) {
        Phr_BaseInfos = phr_BaseInfos;
    }

    public List<T_Phr_SignRec> getPhr_SignRecs() {
        return Phr_SignRecs;
    }

    public void setPhr_SignRecs(List<T_Phr_SignRec> phr_SignRecs) {
        Phr_SignRecs = phr_SignRecs;
    }

    private List<T_Phr_SignRec> Phr_SignRecs;

    public T_UserInfo getUserInfo() {
        return UserInfo;
    }

    public void setUserInfo(T_UserInfo userInfo) {
        UserInfo = userInfo;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getResultMsg() {
        return ResultMsg;
    }

    public void setResultMsg(String resultMsg) {
        ResultMsg = resultMsg;
    }

    public String getOpTime() {
        return OpTime;
    }

    public void setOpTime(String opTime) {
        OpTime = opTime;
    }

    public String getTranSerNo() {
        return TranSerNo;
    }

    public void setTranSerNo(String tranSerNo) {
        TranSerNo = tranSerNo;
    }

    public int getFunCode() {
        return FunCode;
    }

    public void setFunCode(int funCode) {
        FunCode = funCode;
    }

    public Response() {
        super();
    }

}
