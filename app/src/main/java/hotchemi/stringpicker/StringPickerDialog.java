/**
 * Copyright 2014 Shintaro Katafuchi

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package hotchemi.stringpicker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.gwi.phr.hospital.R;

public class StringPickerDialog extends DialogFragment {

    private OnClickListener mListener;

    private Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        if (!(activity instanceof OnClickListener)) {
            throw new RuntimeException(
                    "callback is must implements StringPickerDialog.OnClickListener!");
        }
        mListener = (OnClickListener) activity;
        mActivity = activity;
        super.onAttach(activity);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View view = inflater
                .inflate(R.layout.string_picker_dialog, null, false);

        final StringPicker stringPicker = (StringPicker) view
                .findViewById(R.id.string_picker);
        final Bundle params = getArguments();
        if (params == null) {
            throw new RuntimeException("params is null!");
        }

        String title = params.getString(getValue(R.string.string_picker_dialog_title_values));
        if (TextUtils.isEmpty(title)) {
            title = getValue(R.string.string_picker_dialog_title);
        }

        final String[] values = params
                .getStringArray(getValue(R.string.string_picker_dialog_values));
        stringPicker.setValues(values);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(title);
        builder.setPositiveButton(getValue(R.string.string_picker_dialog_ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onClick(getTag(),stringPicker.getCurrentValue());
                    }
                });
        builder.setNegativeButton(
                getValue(R.string.string_picker_dialog_cancel), null);
        builder.setView(view);
        return builder.create();
    }

    private String getValue(final int resId) {
        return mActivity.getString(resId);
    }

    public interface OnClickListener {
        void onClick(final String tag,final String value);
    }

}