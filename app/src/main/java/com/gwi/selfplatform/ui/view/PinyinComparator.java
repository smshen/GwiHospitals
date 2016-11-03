package com.gwi.selfplatform.ui.view;

import com.gwi.selfplatform.module.net.response.G1211;

import java.util.Comparator;

public class PinyinComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        G1211 g1 = (G1211) o1;
        G1211 g2 = (G1211) o2;
        String g1Name = null;
        String g2Name = null;
        int flag = -1;
        if (g1 != null && g2 != null) {
            g1Name = PingYinUtil.getPingYin(g1.getDeptName().toString());
            g2Name = PingYinUtil.getPingYin(g2.getDeptName().toString());
            flag = g1Name.compareToIgnoreCase(g2Name);
        }
        return flag;
    }

}
