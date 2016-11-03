package com.gwi.selfplatform.ui.activity.nav;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.gwi.ccly.android.commonlibrary.common.net.AsyncCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.Version;
import com.gwi.selfplatform.module.net.beans.BodyToSymptom;
import com.gwi.selfplatform.module.net.beans.KBBodyPart;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.webservice.WebServiceController;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * 智能导诊部位选择页面
 * @author 彭毅
 *
 */
public class BodyAndSymptomActivity extends HospBaseActivity {

    public static final String TAG = BodyAndSymptomActivity.class.getSimpleName();
    
    private ListView mLvBodyPart;
    private ListView mLvRelateDisease;
    private BodyPartAdapter mBodyPartAdapter;
    private BodyToSymptomAdapter mToSymptomAdapter;
    
    public static final String KEY_SEX = "key_sex";
    public static final String KEY_SYMPTOM_ID = "key_symptom_id";
    public static final String KEY_SYMPTOM_NAME = "key_symptom_name";
    public  static final String KEY_SID = "key_sid";
    
    private String mSex = "1";
    
    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            if(parent.getId()== R.id.body_symptom_body_part) {
                KBBodyPart bodyPart = mBodyPartAdapter.getItem(position);
                getBodyToSymptomAsync(mSex,bodyPart.getCode());
            }else {
                BodyToSymptom symptom = mToSymptomAdapter.getItem(position);
                Bundle b = new Bundle();
                b.putString(KEY_SEX, mSex);
                b.putString(KEY_SYMPTOM_ID, String.valueOf(symptom.getCode()));
                b.putString(KEY_SYMPTOM_NAME,symptom.getName());
                b.putString(KEY_SID,symptom.getSId());/*symptom.getSId()*/
//                openActivity(DiseaseCommonActivity.class, b);
                openActivity(SymptomToDiseaseActivity.class, b);
            }
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_symptom);
        addHomeButton();
        initViews();
        initEvents();
        
        Bundle b = getIntent().getExtras();
        if(b!=null) {
            if(b.containsKey(SmartTreatActivity.KEY_IS_MALE)) {
                mSex = b.getBoolean(SmartTreatActivity.KEY_IS_MALE)?"1":"2";
            }
            if(b.containsKey(SmartTreatActivity.KEY_HEX_COLOR)) {
                getBodyPartsByColorNewAsync(b.getString(SmartTreatActivity.KEY_HEX_COLOR));
            }
        }else {
            showToast(R.string.msg_error_common);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initViews() {
        mLvBodyPart = (ListView) findViewById(R.id.body_symptom_body_part);
        mLvRelateDisease = (ListView) findViewById(R.id.body_symptom_relate_disease);
        
        mBodyPartAdapter = new BodyPartAdapter(this, new ArrayList<KBBodyPart>());
        mLvBodyPart.setAdapter(mBodyPartAdapter);
        mLvBodyPart.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mLvBodyPart.setClickable(true);
        mLvBodyPart.setItemChecked(0, true);
        
        
        mToSymptomAdapter = new BodyToSymptomAdapter(this, new ArrayList<BodyToSymptom>());
        mLvRelateDisease.setAdapter(mToSymptomAdapter);
        mLvRelateDisease.setClickable(true);
    }

    /**
     * 根据颜色获取点击的身体部位及周围部位
     * @param hexColor
     */
    private void getBodyPartsByColorNewAsync(String hexColor) {
        ApiCodeTemplate.getBodyPartsByColorAsync(this, TAG, hexColor, new RequestCallback<List<KBBodyPart>>() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onRequestSuccess(List<KBBodyPart> result) {
                mBodyPartAdapter.clear();
                if (result != null && !result.isEmpty()) {
                    CommonUtils.removeNull(result);
                    if (Version.isGeHONEYCOMB()) {
                        mBodyPartAdapter.addAll(result);
                    } else {
                        for (KBBodyPart bts : result) {
                            mBodyPartAdapter.add(bts);
                        }
                    }
                    getBodyToSymptomAsync(mSex, result.get(0).getCode());
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(BodyAndSymptomActivity.this, (Exception) error.getException());
            }
        });
    }
    

    private void getBodyToSymptomAsync(final String sex,final String partCode) {
        ApiCodeTemplate.getBodyToSymptomsAsync(this, TAG, partCode, sex, new RequestCallback<List<BodyToSymptom>>() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onRequestSuccess(List<BodyToSymptom> result) {
                mToSymptomAdapter.clear();
                if (result != null && !result.isEmpty()) {
                    CommonUtils.removeNull(result);
                    final Collator collator = Collator.getInstance(Locale.CHINA);
                    Collections.sort(result, new Comparator<BodyToSymptom>() {

                        @Override
                        public int compare(BodyToSymptom lhs, BodyToSymptom rhs) {
                            return collator.compare(lhs.getName(), rhs.getName());
                        }
                    });
                    if (Version.isGeHONEYCOMB()) {
                        mToSymptomAdapter.addAll(result);
                    } else {
                        for (BodyToSymptom bts : result) {
                            mToSymptomAdapter.add(bts);
                        }
                    }
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(BodyAndSymptomActivity.this, (Exception) error.getException());
            }
        });
    }
    
    @Override
    protected void initEvents() {
        mLvBodyPart.setOnItemClickListener(mOnItemClickListener);
        mLvRelateDisease.setOnItemClickListener(mOnItemClickListener);
    }
    
    private class BodyPartAdapter extends ArrayAdapter<KBBodyPart> {
        
        public BodyPartAdapter(Context context, List<KBBodyPart> objects) {
            super(context, 0, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CheckedTextView partText;
            if(convertView==null) {
                convertView = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_smart_body_part, parent,false);
                partText = (CheckedTextView) convertView.findViewById(android.R.id.text1);
                convertView.setTag(partText);
            } else {
                partText = (CheckedTextView) convertView.getTag();
            }
            KBBodyPart bodyPart = getItem(position);
            partText.setText(bodyPart.getName());
            return convertView;
        }
        
        
    }
    
    private class BodyToSymptomAdapter extends ArrayAdapter<BodyToSymptom> {
        public BodyToSymptomAdapter(Context context, List<BodyToSymptom> objects) {
            super(context, 0, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView symptomText = null;
            if(convertView==null) {
                convertView = LayoutInflater.from(getBaseContext()).inflate(android.R.layout.simple_list_item_1, parent,false);
                symptomText = (TextView) convertView.findViewById(android.R.id.text1);
                symptomText.setTextColor(getResources().getColor(R.color.text_gray_depart_item));
                convertView.setTag(symptomText);
            } else {
                symptomText = (TextView) convertView.getTag();
            }
            BodyToSymptom bodyPart = getItem(position);
            symptomText.setText(bodyPart.getName());
            return convertView;
        }
        
        
        
    }

}
