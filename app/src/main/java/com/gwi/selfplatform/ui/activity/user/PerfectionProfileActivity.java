package com.gwi.selfplatform.ui.activity.user;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.TextUtil;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.view.ShakableEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/2/19 0019.
 */
@Deprecated
public class PerfectionProfileActivity extends HospBaseActivity {
    private static final String TAG = PerfectionProfileActivity.class.getSimpleName();

    @Bind(R.id.et_name)
    ShakableEditText mEtName;
    @Bind(R.id.et_telephone)
    ShakableEditText mEtTelephone;
    @Bind(R.id.et_idcard)
    ShakableEditText mEtIdcard;
    @Bind(R.id.btn_commit)
    Button mBtnCommit;


    @OnClick({R.id.btn_commit})
    public void submit(View view) {
        if (validate()) {
            doModifyNewAsync();
        }
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (!TextUtil.isEmpty(mEtName.getText().toString())
                    && !TextUtil.isEmpty(mEtTelephone.getText().toString())
                    && !TextUtil.isEmpty(mEtIdcard.getText().toString())) {
                mBtnCommit.setEnabled(true);
            } else {
                mBtnCommit.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    protected void initEvents() {
        mEtName.addTextChangedListener(mTextWatcher);
        mEtTelephone.addTextChangedListener(mTextWatcher);
        mEtIdcard.addTextChangedListener(mTextWatcher);
    }

    @Override
    protected void initViews() {
        setTitle("完善个人资料");
        mBtnCommit.setEnabled(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfection_profile);
        ButterKnife.bind(this);

        initViews();
        initEvents();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private boolean validate() {
        return true;
    }

    private void doModifyNewAsync() {

    }
}
