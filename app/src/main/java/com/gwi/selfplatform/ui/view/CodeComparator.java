package com.gwi.selfplatform.ui.view;

import java.util.Comparator;

import com.gwi.selfplatform.module.net.response.G1211;

public class CodeComparator implements Comparator<G1211>{

    @Override
    public int compare(G1211 lhs, G1211 rhs) {
        String lhs_py = lhs.getPinYinCode().substring(0, 1);
        String rhs_py = lhs.getPinYinCode().substring(0, 1);
        int flag = -1;
        if(lhs_py != null &&  rhs_py != null){
           flag = lhs_py.compareToIgnoreCase(rhs_py);
        }
        return flag;
    }

}
