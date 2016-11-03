package com.gwi.phr;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * @author 彭毅
 * @date 2015/6/15.
 */
public class Main {
    public static void main(String[] args) throws Exception{
        Schema schema = new Schema(12,"com.gwi.selfplatform.db.gen");

        addUser(schema);
        addSignCollection(schema);
        addBaseInfo(schema);
        addCardBindRec(schema);
        addHealthEdu(schema);
        addAuthCode(schema);
        addHealthEduCategory(schema);
        addFeedBack(schema);
        addAddressDict(schema);

        new DaoGenerator().generateAll(schema, "app/src/main/java");
    }

    private static void addUser(Schema schema) {
        Entity user = schema.addEntity("T_UserInfo");
        user.addLongProperty("UserId").primaryKey().unique();
        user.addStringProperty("UserCode");
        user.addStringProperty("UserName");
        user.addStringProperty("NickName");
        user.addStringProperty("UserPwd");
        user.addStringProperty("MobilePhone");
        user.addStringProperty("EMail");
        user.addStringProperty("RoleCode");
        user.addStringProperty("RegDate");
        user.addStringProperty("UserStatus");
        user.addStringProperty("RecordMan");
        user.addDateProperty("RecordDate");
        user.addStringProperty("EhrId");
    }

    private static void addSignCollection(Schema schema) {
        Entity fc = schema.addEntity("T_Phr_SignRec");
        fc.addLongProperty("RecNo").primaryKey().unique();
        fc.addStringProperty("EhrID").notNull();
        fc.addStringProperty("GroupId").notNull();
        fc.addIntProperty("SignCode").notNull();
        fc.addStringProperty("SignValue");
        fc.addStringProperty("SignEstimate");
        fc.addDateProperty("RecordDate");
        fc.addStringProperty("TerminalNo");
        fc.addBooleanProperty("UpdateFlag");
    }

    private static void addBaseInfo(Schema schema) {
        Entity bi = schema.addEntity("T_Phr_BaseInfo");
        bi.addStringProperty("EhrID").primaryKey().unique();
        bi.addStringProperty("EhrCode");
        bi.addStringProperty("Name");
        bi.addIntProperty("Sex");
        bi.addDateProperty("BirthDay");
        bi.addStringProperty("IDCard");
        bi.addStringProperty("NowAddressCode");
        bi.addStringProperty("NowAddress");
        bi.addStringProperty("RegAddressCode");
        bi.addStringProperty("RegAddress");
        bi.addStringProperty("WorkUnit");
        bi.addStringProperty("SelfPhone");
        bi.addStringProperty("RelationName");
        bi.addStringProperty("RelationPhone");
        bi.addIntProperty("LiveType");
        bi.addIntProperty("Nation");
        bi.addIntProperty("BloodType");
        bi.addIntProperty("EduDegree");
        bi.addIntProperty("Occupation");
        bi.addIntProperty("MaritalStatus");
        bi.addIntProperty("PayType");
        bi.addStringProperty("OlderDisease");
        bi.addStringProperty("CreateOrg");
        bi.addStringProperty("CreateMan");
        bi.addLongProperty("UserId");
        bi.addStringProperty("Alias");
    }

    private static void addCardBindRec(Schema schema){
        Entity cbr = schema.addEntity("T_Phr_CardBindRec");
        cbr.addLongProperty("RecNo").primaryKey().autoincrement().unique();
        cbr.addStringProperty("EhrId").notNull();
        cbr.addStringProperty("CardNo");
        cbr.addIntProperty("CardType");
        cbr.addIntProperty("CardStatus");
        cbr.addStringProperty("BindMan");
        cbr.addDateProperty("BindDate");
        //10
        cbr.addStringProperty("HospitalCode");
        cbr.addStringProperty("HospitalName");
        //13
        cbr.addStringProperty("PatientID");
    }

    private static void addHealthEdu(Schema schema) {
        Entity hed = schema.addEntity("T_HealthEdu_Datum");
        hed.addLongProperty("DatumCode").primaryKey().unique();
        hed.addStringProperty("DatumName");
        hed.addStringProperty("DatumIntro");
        hed.addIntProperty("DatumClass");
        hed.addStringProperty("DatumContent");
        hed.addStringProperty("DatumPath");
        hed.addStringProperty("DatumSource");
        hed.addStringProperty("DatumAuthor");
        hed.addDateProperty("RecordDate");
        hed.addStringProperty("RecordMan");
        hed.addIntProperty("Status");
        hed.addStringProperty("DatumLogo");
    }

    private static void addAppViersion(Schema schema) {
        Entity mvp = schema.addEntity("T_Mobile_VerParam");
        mvp.addIntProperty("RecNo").primaryKey().unique().notNull();
        mvp.addStringProperty("FileName");
        mvp.addStringProperty("FilePath");
        mvp.addStringProperty("VerCode");
        mvp.addStringProperty("VerName");
        mvp.addDateProperty("UpdateTime");
        mvp.addStringProperty("UpdateInfo");
        mvp.addStringProperty("UpdateMan");
    }

    private static void addAuthCode(Schema schema) {
        Entity pac = schema.addEntity("T_Phone_AuthCode");
        pac.addLongProperty("RecNo");
        pac.addStringProperty("PhoneNumber");
        pac.addStringProperty("AuthCode");
        pac.addStringProperty("BuildTime");
        pac.addIntProperty("SendTimes");
    }

    private static void addHealthEduCategory(Schema schema) {
        Entity dc = schema.addEntity("T_Base_DatumClass");
        dc.addIntProperty("Code");
        dc.addStringProperty("Name");
        dc.addStringProperty("Memo");
        dc.addIntProperty("Status");
        dc.addStringProperty("WbCode");
        dc.addStringProperty("PyCode");
    }

    private static void addFeedBack(Schema schema) {
        Entity fbr = schema.addEntity("T_FeedBack_Rec");
        fbr.addLongProperty("RecNo").primaryKey().unique().notNull();
        fbr.addStringProperty("UserCode");
        fbr.addStringProperty("PhoneNumber");
        fbr.addStringProperty("Advice");
        fbr.addIntProperty("Source");
        fbr.addIntProperty("Type");
        fbr.addIntProperty("Status");
        fbr.addDateProperty("RecDate");
        fbr.addDateProperty("DealDate");
        fbr.addStringProperty("DealMan");
    }

    private static void addAddressDict(Schema schema) {
        Entity ad = schema.addEntity("Base_AddressDict");
        ad.addStringProperty("Code").primaryKey().unique().notNull();
        ad.addStringProperty("ParentCode");
        ad.addStringProperty("Name");
        ad.addStringProperty("Memo");
        ad.addIntProperty("Status");
        ad.addStringProperty("WbCode");
        ad.addStringProperty("PyCode");
    }

    private static void addDisease(Schema schema) {
        Entity disease = schema.addEntity("T_KB_Disease");
        disease.addLongProperty("DiseaseId").primaryKey().unique().notNull();
        disease.addStringProperty("DiseaseName");
        disease.addIntProperty("IsCommon");
        disease.addIntProperty("BodyPart");
        disease.addStringProperty("DeptId");
        disease.addStringProperty("Summary");
        disease.addStringProperty("DiseaseReason");
        disease.addStringProperty("Symptom");
        disease.addStringProperty("Test");
        disease.addStringProperty("Diagnosis");
        disease.addStringProperty("Prevent");
        disease.addStringProperty("Syndrome");
        disease.addStringProperty("Treatment");
        disease.addStringProperty("PyCode");
        disease.addStringProperty("WbCode");
        disease.addIntProperty("Status");
        disease.addDateProperty("RecordDate");
    }

    private static void addDrug(Schema schema) {
        Entity drug = schema.addEntity("T_KB_Drug");
        drug.addLongProperty("DrugId").primaryKey().unique().notNull();
        drug.addStringProperty("DrugName");
        drug.addStringProperty("DrugKind");
        drug.addIntProperty("UseKind");
        drug.addStringProperty("PropertyKind");
        drug.addStringProperty("DrugElement");
        drug.addStringProperty("Dosage");
        drug.addStringProperty("Indications");
        drug.addStringProperty("Contraindication");
        drug.addStringProperty("Notice");
        drug.addStringProperty("ADR");
        drug.addIntProperty("IsAid");
        drug.addStringProperty("PyCode");
        drug.addStringProperty("WbCode");
        drug.addIntProperty("Status");

    }

    private static void addTestCheck(Schema schema) {
        Entity check = schema.addEntity("T_KB_TestCheck");
        check.addLongProperty("TestId").primaryKey().unique().notNull();
        check.addStringProperty("TestName");
        check.addStringProperty("TestKind");
        check.addStringProperty("Summary");
        check.addStringProperty("Reference");
        check.addStringProperty("ClinicalSignificance");
        check.addStringProperty("PyCode");
        check.addStringProperty("WbCode");
        check.addIntProperty("Status");
    }

    private static void addTreatment(Schema schema) {
        Entity treatment = schema.addEntity("T_KB_Treatment");
        treatment.addLongProperty("TreatmentId");
        treatment.addStringProperty("TreatmentName");
        treatment.addIntProperty("TreatmentKind");
        treatment.addStringProperty("TreatmentContent");
        treatment.addStringProperty("PyCode");
        treatment.addStringProperty("WbCode");
        treatment.addIntProperty("Status");
    }
}
