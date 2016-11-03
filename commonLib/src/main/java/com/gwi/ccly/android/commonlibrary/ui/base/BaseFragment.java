package com.gwi.ccly.android.commonlibrary.ui.base;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

/**
 * Created by  Kuofei Liu  on 2015-12-17.
 */
public abstract class BaseFragment extends Fragment {

    private Toast mToast;


    public void showToast(int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(this.getActivity(), getResources().getString(resId), Toast.LENGTH_SHORT);
        } else {
            mToast.setText(resId);
        }
        mToast.show();
    }

    public void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(this.getActivity(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    public void showLongToast(int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(this.getActivity(), getResources().getString(resId), Toast.LENGTH_LONG);
        } else {
            mToast.setText(resId);
        }
        mToast.show();
    }

    public void showLongToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(this.getActivity(), text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }


    public abstract void showLoadingDialog(final String text);


    public abstract void dismissLoadingDialog();
}
