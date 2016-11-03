
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
import com.gwi.selfplatform.ui.fragment.encyclopedia.TestKindsFragment;
import com.gwi.selfplatform.ui.fragment.encyclopedia.TestListFragment;


public class TestLibraryActivity extends HospBaseActivity {

    private EditText mEtSearchContent;
    private ImageButton mBtnClear;
    private ImageView mBtnSearch;

    private Fragment mKindFragment = null;

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
        setContentView(R.layout.activity_test_library);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addHomeButton();
        setTitle(getString(R.string.health_report_analysis));
        initViews();
        initEvents();
        showTestCheckList();
    }

    @Override
    protected void onDestroy() {
        GlobalSettings.INSTANCE.getAppMemoryCache().remove(TestListFragment.KEY_CACHE);
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
                    showTestCheckList();
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

    private void showTestCheckList() {
        if (mKindFragment == null) {
            Bundle b = new Bundle();
            b.putString("TestKindCode", "0");
            mKindFragment = Fragment.instantiate(this, TestKindsFragment.class.getName(), b);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.test_lib_content, mKindFragment)
                .commit();
    }

    private void showSearchResult() {
        getSupportFragmentManager().beginTransaction().remove(mKindFragment).commit();
        Fragment resultFragment = getSupportFragmentManager().findFragmentByTag("SearchResult");
        if (resultFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(resultFragment).commit();
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.test_lib_content
                        , TestListFragment.newInstance(mEtSearchContent.getText().toString())
                        , "SearchResult")
                .commit();
    }
}
