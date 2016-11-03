package com.gwi.phr;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MainFssdwrmyy {

//    public static void main(String[] args) {
//        String regex = "\\d+$";
//        String testStr = "124554_12";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(testStr);
//        matcher.find();
////        System.out.print(matcher.group(matcher.groupCount()-1));
////        while(matcher.find()) {
//            System.out.println(matcher.group());
////        }
//    }

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(11, "com.gwi.selfplatform.db");

        addWristBandUser(schema);
        addWristBandLocalSport(schema);
        addSignCollection(schema);

        //addUser(schema);
        new DaoGenerator().generateAll(schema, "app/src/fssdwrmyy/java");
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

    private static void addWristBandUser(Schema schema) {
        Entity wbu = schema.addEntity("T_WristBandUser");
        wbu.addStringProperty("EhrID").primaryKey().unique().notNull();
        wbu.addIntProperty("Sex");
        wbu.addIntProperty("Height");
        wbu.addIntProperty("Weight");
        wbu.addIntProperty("Age");
        wbu.addIntProperty("Goal");
    }


    private static void addWristBandLocalSport(Schema schema) {
        Entity wbu = schema.addEntity("T_LocalSport");
        wbu.addStringProperty("EhrID").notNull();
        wbu.addIntProperty("Year");
        wbu.addIntProperty("Month");
        wbu.addIntProperty("Day");
        wbu.addIntProperty("Hour");
        wbu.addIntProperty("Minute");
        wbu.addIntProperty("Steps");
        wbu.addFloatProperty("Distance");
        wbu.addFloatProperty("Calorie");
    }

    //Example:
    private static void addUser(Schema schema) {
        Entity user = schema.addEntity("T_UserInfo");
    }
}
