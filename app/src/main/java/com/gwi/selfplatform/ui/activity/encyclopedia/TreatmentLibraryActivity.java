package com.gwi.selfplatform.ui.activity.encyclopedia;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.fragment.encyclopedia.TreatmentKindsFragment;
import com.gwi.selfplatform.ui.fragment.encyclopedia.TreatmentListFragment;


public class TreatmentLibraryActivity extends HospBaseActivity {

    private EditText mEtSearchContent;
    private ImageButton mBtnClear;
    private ImageView  mBtnSearch;

    private TreatmentKindsFragment mKindsFragment = null;

    InputMethodManager Imm;

    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.clear) {
                mEtSearchContent.setText("");
            } else if (id == R.id.search) {
                showSearchResult();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_library);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addHomeButton();
        setTitle(getString(R.string.health_first_aid));
        initViews();
        initEvents();
        showTreatmentList();
    }

    @Override
    protected void onDestroy() {
        GlobalSettings.INSTANCE.getAppMemoryCache().remove(TreatmentListFragment.KEY_CACHE);
        super.onDestroy();
    }

    @Override
    protected void initViews() {
        mEtSearchContent = (EditText) findViewById(R.id.text_search);
        mBtnClear = (ImageButton) findViewById(R.id.clear);
        mBtnSearch = (ImageView) findViewById(R.id.search);
    }

    @Override
    protected void initEvents() {
        mBtnClear.setOnClickListener(mOnClickListener);
        mBtnSearch.setOnClickListener(mOnClickListener);
        mEtSearchContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    mBtnClear.setVisibility(View.VISIBLE);
                    mBtnSearch.setEnabled(true);
                } else {
                    mBtnClear.setVisibility(View.GONE);
                    mBtnSearch.setEnabled(false);
                    mEtSearchContent.setVisibility(View.VISIBLE);
                    showTreatmentList();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void showTreatmentList() {
        if (mKindsFragment == null) {
            mKindsFragment = new TreatmentKindsFragment();
        }
        Fragment resultFragment = getSupportFragmentManager().findFragmentByTag("TreatmentSearchResult");
        if (resultFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(resultFragment).commit();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.treatment_lib_content, mKindsFragment,
                        "Treatment Kinds").commit();
    }

    private void showSearchResult() {
        getSupportFragmentManager().beginTransaction().remove(mKindsFragment)
                .commit();
        Fragment resultFragment = getSupportFragmentManager().findFragmentByTag("TreatmentSearchResult");
        if (resultFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(resultFragment).commit();
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.treatment_lib_content
                        , TreatmentListFragment.newInstantce(mEtSearchContent.getText().toString())
                        , "TreatmentSearchResult")
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(R.anim.push_right_in, R.anim.push_right_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
