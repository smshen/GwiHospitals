package com.gwi.selfplatform.ui.activity.encyclopedia;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.fragment.encyclopedia.DrugClassifyFragment;
import com.gwi.selfplatform.ui.fragment.encyclopedia.DrugCommonFragment;


public class DrugLibraryActivity extends HospBaseActivity {

    private static final String TAG = DrugLibraryActivity.class.getSimpleName();

    private EditText mEtSearchContent;
    private ImageButton mBtnClear;
    private ImageView mBtnSearch;
    private View mItemsLayout;
    public static final String KEY_SEARCH_KEY = "key_search_key";
    private static final String TAG_SEARCH_RESULT = "tag_search_result";
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

    DrugCommonFragment mSearchResultFragment = null;

    private void showSearchResult() {
        Bundle b = new Bundle();
        b.putString(KEY_SEARCH_KEY, mEtSearchContent.getText().toString());
        Fragment f = getSupportFragmentManager().findFragmentByTag(TAG_SEARCH_RESULT);
        if (f != null) {
            return;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.disease_lib_result
                        , (DrugClassifyFragment) Fragment.instantiate(this, DrugClassifyFragment.class.getName(), b)
                        , TAG_SEARCH_RESULT)
                .commit();
        mItemsLayout.setVisibility(View.GONE);
        Imm.hideSoftInputFromWindow(mBtnSearch.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_library);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.health_drug_dictionary));
        addHomeButton();
        initViews();
        initEvents();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initViews() {
        mEtSearchContent = (EditText) findViewById(R.id.text_search);
        mBtnClear = (ImageButton) findViewById(R.id.clear);
        mBtnSearch = (ImageView) findViewById(R.id.search);
        mItemsLayout = findViewById(R.id.disease_lib_layout);
        mBtnSearch.setEnabled(false);
        Imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
                    mItemsLayout.setVisibility(View.VISIBLE);
                }
                Fragment f = getSupportFragmentManager().findFragmentByTag(TAG_SEARCH_RESULT);
                if (f != null) {
                    getSupportFragmentManager().beginTransaction().remove(f).commit();
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

    public void onItemClick(View v) {
        int id = v.getId();
        if (id == R.id.drug_lib_part) {
            openActivity(DrugPropertyActivity.class);
        }
//else if (id == R.id.drug_lib_common) {
//openActivity(DrugCommonActivity.class);
//}
        else if (id == R.id.drug_baike_resuce) {
            openActivity(DrugResuceActivity.class);
        }
    }

}
