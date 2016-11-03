package com.gwi.selfplatform.common.utils;

import com.gwi.selfplatform.module.net.response.OrderQueryResults;
import com.gwi.selfplatform.module.net.response.RecipeInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticValueUtils {
    //缴费选中项
    public  static final Map<RecipeInfo, Boolean> checkSataus = new HashMap<>();

    public static final List<String> regIDs = new ArrayList<String>();

    public static void putCheckStatus(RecipeInfo reg, boolean status) {
        checkSataus.put(reg, status);
    }

    public static void removeCheckStatus(RecipeInfo reg) {
        checkSataus.remove(reg);
    }

    public static void clearCheckStatus() {
        checkSataus.clear();
    }

    public static void putReg(String regID) {
        regIDs.add(regID);
    }

    public static void removeReg(String regID) {
        regIDs.remove(regID);
    }

    public static void clearRegs() {
        regIDs.clear();
    }

    public static List<String> getRegs() {
        return regIDs;
    }

    //支付业务办理成功
    public static Map<OrderQueryResults.OrderQueryResult, Boolean> orderNoSataus = new HashMap<OrderQueryResults.OrderQueryResult, Boolean>();

    public static void putOrderNoSataus(OrderQueryResults.OrderQueryResult orderNo, boolean status) {
        orderNoSataus.put(orderNo, status);
    }

    public static void removeOrderSataus(OrderQueryResults.OrderQueryResult orderNo) {
        orderNoSataus.remove(orderNo);
    }

    public static void clearOrderNoSataus() {
        orderNoSataus.clear();
    }
}
