package com.gwi.selfplatform.db.gen;

import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.config.HospitalParams;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 拦截卡号并格式化
 * @author 彭毅
 * @date 2015/4/13.
 */
public class ExT_Phr_CardBindRec extends T_Phr_CardBindRec{

    private static final String TAG = ExT_Phr_CardBindRec.class.getSimpleName();

    public void setCardNoWithNoFormat(String CardNo) {
        super.setCardNo(CardNo);
    }


    @Override
    public void setCardNo(String CardNo) {
        //东莞社保平台添加，如果以后出现新的格式，需要讲此文件移到各个医院source中。
        Map<String,String> params = GlobalSettings.INSTANCE.getHospitalParams();
        if(HospitalParams.getValue(params,HospitalParams.CODE_MEDICAL_CARD_FORMAT)!=null) {
            CardNo = GlobalSettings.INSTANCE.getCurrentFamilyAccount().getIDCard()+"_"+CardNo;
        }
        super.setCardNo(CardNo);
    }

    @Override
    public String getCardNo() {
        return super.getCardNo();
    }

    public String getCardNoFormat() {
        Map<String,String> params = GlobalSettings.INSTANCE.getHospitalParams();
        if(HospitalParams.getValue(params,HospitalParams.CODE_MEDICAL_CARD_FORMAT)!=null) {
            String regex = params.get(HospitalParams.CODE_MEDICAL_CARD_FORMAT);
            Pattern p = Pattern.compile("\\d+$");
            Matcher matcher = p.matcher(getCardNo());
//            if(matcher.matches()) {
//                p = Pattern.compile("\\d+$");
//                matcher = p.matcher(getCardNo());
                List<String> result = new LinkedList<>();
                while(matcher.find()) {
                    result.add(matcher.group());
                }
                if(result.size()==0) {
                    Logger.e(TAG,"Not matched,card format is not right!");
                    return getCardNo();
                }
                return result.get(result.size()-1);
//            }else {
//                Logger.e(TAG,"Card format is not right!");
//                return getCardNo();
//            }
        }else {
            return getCardNo();
        }
    }
}
