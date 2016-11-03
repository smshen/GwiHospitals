package com.gwi.selfplatform.module.net.response;

import android.annotation.TargetApi;
import android.os.Build;

import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class G1910 implements Serializable {
    private static final long serialVersionUID = 1910L;

    private String ItemID;
    private String ItemName;
    private int BusiType;
    private double ItemFee;
    private String ExecDeptName;
    private String Date;
    private String Note;
    private String DocName;
    private String PrescDeptName;

    /**
     * 用于UI展示
     */
    private int customType = 2;

    /**
     * 流水号，中心医院添加
     */
    private String SerNo;

    public String getPrescDeptName() {
        return PrescDeptName;
    }

    public void setPrescDeptName(String prescDeptName) {
        PrescDeptName = prescDeptName;
    }

    public String getSerNo() {
        return SerNo;
    }

    public void setSerNo(String serNo) {
        SerNo = serNo;
    }

    public int getCustomType() {
        return customType;
    }

    public void setCustomType(int customType) {
        this.customType = customType;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public int getBusiType() {
        return BusiType;
    }

    public void setBusiType(int busiType) {
        BusiType = busiType;
    }

    public double getItemFee() {
        return ItemFee;
    }

    public void setItemFee(double itemFee) {
        ItemFee = itemFee;
    }

    public String getExecDeptName() {
        return ExecDeptName;
    }

    public void setExecDeptName(String execDeptName) {
        ExecDeptName = execDeptName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getDocName() {
        return DocName;
    }

    public void setDocName(String docName) {
        DocName = docName;
    }

    /**
     *
     * @param args args
     * @throws Exception
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Deprecated
    public static void main(String[] args) throws Exception {
        String json = "{\"Response\":{\"Header\":{\"FunCode\":\"1910\",\"Status\":\"1\",\"ResultMsg\":\"\",\"OpTime\":\"\",\"ErrorCode\":\"\"},\"Body\":{\"Items\":\"\",\"Item\":{\"ItemID\":\"1\",\"BusiType\":\"1\",\"ItemFee\":\"1\",\"ExecDeptName\":\"1\",\"Date\":\"1\",\"Note\":\"\"}}}}\n";
        JSONObject jsonObject = new JSONObject(json);
        //删除json中Response节点
        JSONObject responseObject = jsonObject.getJSONObject("Response");
        if (!responseObject.isNull("Body")) {
            if (responseObject.get("Body") instanceof JSONObject) {
                JSONObject bodyObject = responseObject.getJSONObject("Body");
                if (bodyObject.has("Items")) {
                    Object itemsObject = bodyObject.get("Items");
                    TypeToken<List<G1910>> token = new TypeToken<List<G1910>>() {
                    };
                    if (itemsObject instanceof JSONObject) {
                        JSONArray array = new JSONArray();
                        array.put(itemsObject);
                        System.out.print(array.toString());
                    } else {
                        System.out.print(itemsObject.toString());
                    }
                }
            }
        }
    }

}
