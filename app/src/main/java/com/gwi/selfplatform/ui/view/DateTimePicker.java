package com.gwi.selfplatform.ui.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

import com.gwi.phr.hospital.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * ѡ�����ں�ʱ���widget����֧����ʾ����
 *
 * @author Peng Yi
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DateTimePicker extends FrameLayout {

    private static final String TAG = DatePicker.class.getSimpleName();

    private static final String DATE_FORMAT = "MM/dd/yyyy";

    private static final int DEFAULT_START_YEAR = 1900;

    private static final int DEFAULT_END_YEAR = 2100;

    private static final boolean DEFAULT_TIME_SHOWN = true;

    private static final boolean DEFAULT_ENABLED_STATE = true;

    // --- UI ---
    private final LinearLayout mSpinners;

    private final NumberPicker mDaySpinner;

    private final NumberPicker mMonthSpinner;

    private final NumberPicker mYearSpinner;

    private final NumberPicker mHourSpinner;

    private final NumberPicker mMinuteSpinner;

    private final EditText mDaySpinnerInput;

    private final EditText mMonthSpinnerInput;

    private final EditText mYearSpinnerInput;

    private final EditText mHourSpinnerInput;

    private final EditText mMinuteSpinnerInput;

    /**
     * @author ����
     */
    public enum Mode {
        /**
         * ȫ��ʾ 2014��12��12��12 12:00
         */
        all,
        /**
         * ֻ��ʾ����� 2014��12��
         */
        yearAndMonth,
        /**
         * ֻ��ʾ���� 2014��12��12��
         */
        date,
        /**
         * ֻ��ʾʱ�� 13:00
         */
        time
    }

    ;


    // --- UI ---

    private Locale mCurrentLocale;

    private OnDateTimeChangedListener mOnDateTimeChangedListener;

    private String[] mShortMonths;

    private final java.text.DateFormat mDateFormat = new SimpleDateFormat(
            DATE_FORMAT);

    private int mNumberOfMonths;

    private Calendar mTempDate;

    private Calendar mTempTime;

    private Calendar mMinDate;

    private Calendar mMaxDate;

    private Calendar mCurrentDate;

    // state
    private boolean mIsEnabled = DEFAULT_ENABLED_STATE;

    /**
     * The callback used to indicate the user changes\d the date.
     */
    public interface OnDateTimeChangedListener {

        /**
         * Called upon a date and time change.
         *
         * @param view        The view associated with this listener.
         * @param year        The year that was set.
         * @param monthOfYear The month that was set (0-11) for compatibility with
         *                    {@link java.util.Calendar}.
         * @param dayOfMonth  The day of the month that was set.
         * @param hourOfDay   The current hour.
         * @param minute      The current minute.
         */
        void onDateTimeChanged(DateTimePicker view, int year, int monthOfYear,
                               int dayOfMonth, int hourOfDay, int minute);
    }

    public DateTimePicker(Context context) {
        this(context, null);
    }

    public DateTimePicker(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.DateTimePickerStyle);
    }

    @SuppressLint("SimpleDateFormat")
    @SuppressWarnings("deprecation")
    public DateTimePicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // initialization based on locale
        setCurrentLocale(Locale.getDefault());

        TypedArray attributesArray = context.obtainStyledAttributes(attrs,
                R.styleable.DateTimePicker, defStyle, 0);
        // TODO:��ʱδ��
        // boolean timeShown = attributesArray.getBoolean(
        // R.styleable.DateTimePicker_spinnersShown, DEFAULT_TIME_SHOWN);
        int startYear = attributesArray.getInt(
                R.styleable.DateTimePicker_startYear, DEFAULT_START_YEAR);
        int endYear = attributesArray.getInt(
                R.styleable.DateTimePicker_endYear, DEFAULT_END_YEAR);
        String minDate = attributesArray
                .getString(R.styleable.DateTimePicker_minDate);
        String maxDate = attributesArray
                .getString(R.styleable.DateTimePicker_maxDate);
        int layoutResourceId = R.layout.layout_datetime_picker;
        attributesArray.recycle();

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(layoutResourceId, this, true);

        mSpinners = (LinearLayout) findViewById(R.id.datetime_date_picker);
        // day
        mDaySpinner = (NumberPicker) findViewById(R.id.datetime_day);
        mDaySpinnerInput = (EditText) mDaySpinner.getChildAt(0);
        // month
        mMonthSpinner = (NumberPicker) findViewById(R.id.datetime_month);
        mMonthSpinnerInput = (EditText) mMonthSpinner.getChildAt(0);
        // year
        mYearSpinner = (NumberPicker) findViewById(R.id.datetime_year);
        mYearSpinnerInput = (EditText) mYearSpinner.getChildAt(0);
        // hour
        mHourSpinner = (NumberPicker) findViewById(R.id.datetime_hour);
        mHourSpinnerInput = (EditText) mHourSpinner.getChildAt(0);
        // minute
        mMinuteSpinner = (NumberPicker) findViewById(R.id.datetime_minute);
        mMinuteSpinnerInput = (EditText) mMinuteSpinner.getChildAt(0);

        initEvents();

        // set the min date giving priority of the minDate over startYear
        mTempDate.clear();
        if (!TextUtils.isEmpty(minDate)) {
            if (!parseDate(minDate, mTempDate)) {
                mTempDate.set(startYear, 0, 1);
            }
        } else {
            mTempDate.set(startYear, 0, 1);
        }
        setMinDate(mTempDate.getTimeInMillis());
        // set the max date giving priority of the maxDate over endYear
        mTempDate.clear();
        if (!TextUtils.isEmpty(maxDate)) {
            if (!parseDate(maxDate, mTempDate)) {
                mTempDate.set(endYear, 11, 31);
            }
        } else {
            mTempDate.set(endYear, 11, 31);
        }
        setMaxDate(mTempDate.getTimeInMillis());

        // initialize to current date
        mCurrentDate.setTimeInMillis(System.currentTimeMillis());
        init(mCurrentDate.get(Calendar.YEAR), mCurrentDate.get(Calendar.MONTH),
                mCurrentDate.get(Calendar.DAY_OF_MONTH), null);

        // re-order the number spinners to match the current date format
        reorderSpinners();
        updateHourControl();

        // set to current time
        setCurrentHour(mTempTime.get(Calendar.HOUR_OF_DAY));
        setCurrentMinute(mTempTime.get(Calendar.MINUTE));
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setCurrentLocale(newConfig.locale);
    }

    // Override so we are in complete control of save / restore for this widget.
    @Override
    protected void dispatchRestoreInstanceState(
            SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, getYear(), getMonth(),
                getDayOfMonth(), getCurrentHour(), getCurrentMinute());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setDate(ss.mYear, ss.mMonth, ss.mDay);
        setCurrentHour(ss.mMonth);
        setCurrentMinute(ss.mMinute);
        updateSpinners();
    }

    /**
     * Initialize the state. If the provided values designate an inconsistent
     * date the values are normalized before updating the spinners.
     *
     * @param year                  The initial year.
     * @param monthOfYear           The initial month <strong>starting from zero</strong>.
     * @param dayOfMonth            The initial day of the month.
     * @param onDateChangedListener How user is notified date is changed by user, can be null.
     */
    public void init(int year, int monthOfYear, int dayOfMonth,
                     OnDateTimeChangedListener onDateTimeChangedListener) {
        setDate(year, monthOfYear, dayOfMonth);
        updateSpinners();
        mOnDateTimeChangedListener = onDateTimeChangedListener;
    }

    private void initEvents() {
        OnValueChangeListener onDateChangeListener = new OnValueChangeListener() {

            public void onValueChange(NumberPicker picker, int oldVal,
                                      int newVal) {
                updateInputState();
                mTempDate.setTimeInMillis(mCurrentDate.getTimeInMillis());
                if (picker == mDaySpinner) {
                    int maxDayOfMonth = mTempDate
                            .getActualMaximum(Calendar.DAY_OF_MONTH);
                    if (oldVal == maxDayOfMonth && newVal == 1) {
                        mTempDate.add(Calendar.DAY_OF_MONTH, 1);
                    } else if (oldVal == 1 && newVal == maxDayOfMonth) {
                        mTempDate.add(Calendar.DAY_OF_MONTH, -1);
                    } else {
                        mTempDate.add(Calendar.DAY_OF_MONTH, newVal - oldVal);
                    }
                } else if (picker == mMonthSpinner) {
                    if (oldVal == 11 && newVal == 0) {
                        mTempDate.add(Calendar.MONTH, 1);
                    } else if (oldVal == 0 && newVal == 11) {
                        mTempDate.add(Calendar.MONTH, -1);
                    } else {
                        mTempDate.add(Calendar.MONTH, newVal - oldVal);
                    }
                } else if (picker == mYearSpinner) {
                    mTempDate.set(Calendar.YEAR, newVal);
                } else {
                    throw new IllegalArgumentException();
                }

                setDate(mTempDate.get(Calendar.YEAR),
                        mTempDate.get(Calendar.MONTH),
                        mTempDate.get(Calendar.DAY_OF_MONTH));
                updateSpinners();
                notifyDateTimeChanged();
            }

        };

        OnValueChangeListener onTimeChangeListenr = new OnValueChangeListener() {

            public void onValueChange(NumberPicker picker, int oldVal,
                                      int newVal) {
                updateInputState();
                if (picker == mHourSpinner) {

                } else if (picker == mMinuteSpinner) {
                    int minValue = mMinuteSpinner.getMinValue();
                    int maxValue = mMinuteSpinner.getMaxValue();
                    if (oldVal == maxValue && newVal == minValue) {
                        int newHour = mHourSpinner.getValue() + 1;
                        mHourSpinner.setValue(newHour);
                    } else if (oldVal == minValue && newVal == maxValue) {
                        int newHour = mHourSpinner.getValue() - 1;
                        mHourSpinner.setValue(newHour);
                    }
                } else {
                    throw new IllegalArgumentException();
                }
                notifyDateTimeChanged();
            }
        };

        mDaySpinner.setFormatter(TWO_DIGIT_FORMATTER);
        mDaySpinner.setOnLongPressUpdateInterval(100);
        mDaySpinner.setOnValueChangedListener(onDateChangeListener);
        mDaySpinnerInput.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        mMonthSpinner.setMinValue(0);
        mMonthSpinner.setMaxValue(mNumberOfMonths - 1);
        mMonthSpinner.setDisplayedValues(mShortMonths);
        mMonthSpinner.setOnLongPressUpdateInterval(200);
        mMonthSpinner.setOnValueChangedListener(onDateChangeListener);
        mMonthSpinnerInput.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        mYearSpinner.setOnLongPressUpdateInterval(100);
        mYearSpinner.setOnValueChangedListener(onDateChangeListener);
        mYearSpinnerInput.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        mHourSpinner.setOnValueChangedListener(onTimeChangeListenr);
        mHourSpinnerInput.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        mMinuteSpinner.setOnValueChangedListener(onTimeChangeListenr);
        mMinuteSpinnerInput.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mMinuteSpinner.setMinValue(0);
        mMinuteSpinner.setMaxValue(59);
        mMinuteSpinner.setOnLongPressUpdateInterval(100);
        mMinuteSpinner.setFormatter(TWO_DIGIT_FORMATTER);
    }

    /**
     * Sets the current locale.
     *
     * @param locale The current locale.
     */
    private void setCurrentLocale(Locale locale) {
        if (locale.equals(mCurrentLocale)) {
            return;
        }

        mCurrentLocale = locale;

        mTempTime = Calendar.getInstance(locale);

        mTempDate = getCalendarForLocale(mTempDate, locale);
        mMinDate = getCalendarForLocale(mMinDate, locale);
        mMaxDate = getCalendarForLocale(mMaxDate, locale);
        mCurrentDate = getCalendarForLocale(mCurrentDate, locale);

        mNumberOfMonths = mTempDate.getActualMaximum(Calendar.MONTH) + 1;
        mShortMonths = new String[mNumberOfMonths];
        for (int i = 0; i < mNumberOfMonths; i++) {
            mShortMonths[i] = DateUtils.getMonthString(Calendar.JANUARY + i,
                    DateUtils.LENGTH_MEDIUM);
        }
    }

    public void setOnDateTimeChangeListener(OnDateTimeChangedListener l) {
        mOnDateTimeChangedListener = l;
    }

    /**
     * Sets the minimal date supported by this {@link NumberPicker} in
     * milliseconds since January 1, 1970 00:00:00 in
     * {@link TimeZone#getDefault()} time zone.
     *
     * @param minDate The minimal supported date.
     */
    public void setMinDate(long minDate) {
        mTempDate.setTimeInMillis(minDate);
        if (mTempDate.get(Calendar.YEAR) == mMinDate.get(Calendar.YEAR)
                && mTempDate.get(Calendar.DAY_OF_YEAR) != mMinDate
                .get(Calendar.DAY_OF_YEAR)) {
            return;
        }
        mMinDate.setTimeInMillis(minDate);
        if (mCurrentDate.before(mMinDate)) {
            mCurrentDate.setTimeInMillis(mMinDate.getTimeInMillis());
        }
        updateSpinners();
    }

    /**
     * Sets the maximal date supported by this {@link DatePicker} in
     * milliseconds since January 1, 1970 00:00:00 in
     * {@link TimeZone#getDefault()} time zone.
     *
     * @param maxDate The maximal supported date.
     */
    public void setMaxDate(long maxDate) {
        mTempDate.setTimeInMillis(maxDate);
        if (mTempDate.get(Calendar.YEAR) == mMaxDate.get(Calendar.YEAR)
                && mTempDate.get(Calendar.DAY_OF_YEAR) != mMaxDate
                .get(Calendar.DAY_OF_YEAR)) {
            return;
        }
        mMaxDate.setTimeInMillis(maxDate);
        if (mCurrentDate.after(mMaxDate)) {
            mCurrentDate.setTimeInMillis(mMaxDate.getTimeInMillis());
        }
        updateSpinners();
    }

    /**
     * Set the current hour.
     */
    public void setCurrentHour(Integer currentHour) {
        // why was Integer used in the first place?
        if (currentHour == null || currentHour.intValue() == getCurrentHour().intValue()) {
            return;
        }
        mHourSpinner.setValue(currentHour);
        notifyDateTimeChanged();
    }

    public void setCurrentMonth(Integer currentMonth) {
        Integer value = mMonthSpinner.getValue();
        if (currentMonth == null || currentMonth.intValue() == value.intValue()) {
            return;
        }
        mMonthSpinner.setValue(currentMonth);
        notifyDateTimeChanged();
    }

    public void setCurrentYear(Integer currentYear) {
        Integer value = mYearSpinner.getValue();
        if (currentYear == null || currentYear.intValue() == value.intValue()) {
            return;
        }
        mYearSpinner.setValue(currentYear);
        notifyDateTimeChanged();
    }

    public void setCurrentDay(Integer currentDay) {
        Integer value = mDaySpinner.getValue();
        if (currentDay == null || currentDay.intValue() == value.intValue()) {
            return;
        }
        mDaySpinner.setValue(currentDay);
        notifyDateTimeChanged();
    }

    /**
     * Set the current minute (0-59).
     */
    public void setCurrentMinute(Integer currentMinute) {
        if (currentMinute == getCurrentMinute()) {
            return;
        }
        mMinuteSpinner.setValue(currentMinute);
        notifyDateTimeChanged();
    }

    /**
     * @return The selected year.
     */
    public int getYear() {
        return mCurrentDate.get(Calendar.YEAR);
    }

    /**
     * @return The selected month.
     */
    public int getMonth() {
        return mCurrentDate.get(Calendar.MONTH);
    }

    /**
     * @return The selected day of month.
     */
    public int getDayOfMonth() {
        return mCurrentDate.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * @return The current hour in the range (0-23).
     */
    public Integer getCurrentHour() {
        int currentHour = mHourSpinner.getValue();
        return currentHour;
    }

    /**
     * @return The current minute.
     */
    public Integer getCurrentMinute() {
        return mMinuteSpinner.getValue();
    }

    /**
     * Gets a calendar for locale bootstrapped with the pos of a given
     * calendar.
     *
     * @param oldCalendar The old calendar.
     * @param locale      The locale.
     */
    private Calendar getCalendarForLocale(Calendar oldCalendar, Locale locale) {
        if (oldCalendar == null) {
            return Calendar.getInstance(locale);
        } else {
            final long currentTimeMillis = oldCalendar.getTimeInMillis();
            Calendar newCalendar = Calendar.getInstance(locale);
            newCalendar.setTimeInMillis(currentTimeMillis);
            return newCalendar;
        }
    }

    private void updateInputState() {
        // Make sure that if the user changes the pos and the IME is active
        // for one of the inputs if this widget, the IME is closed. If the user
        // changed the pos via the IME and there is a next input the IME will
        // be shown, otherwise the user chose another means of changing the
        // pos and having the IME up makes no sense.
        InputMethodManager inputMethodManager = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            if (inputMethodManager.isActive(mYearSpinnerInput)) { // Date
                mYearSpinnerInput.clearFocus();
                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            } else if (inputMethodManager.isActive(mMonthSpinnerInput)) {
                mMonthSpinnerInput.clearFocus();
                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            } else if (inputMethodManager.isActive(mDaySpinnerInput)) {
                mDaySpinnerInput.clearFocus();
                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            } else if (inputMethodManager.isActive(mHourSpinnerInput)) { // Time
                mHourSpinnerInput.clearFocus();
                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            } else if (inputMethodManager.isActive(mMinuteSpinnerInput)) {
                mMinuteSpinnerInput.clearFocus();
                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            }
        }
    }

    private void setDate(int year, int month, int dayOfMonth) {
        mCurrentDate.set(year, month, dayOfMonth);
        if (mCurrentDate.before(mMinDate)) {
            mCurrentDate.setTimeInMillis(mMinDate.getTimeInMillis());
        } else if (mCurrentDate.after(mMaxDate)) {
            mCurrentDate.setTimeInMillis(mMaxDate.getTimeInMillis());
        }
    }

    private void updateSpinners() {
        // set the spinner ranges respecting the min and max dates
        if (mCurrentDate.equals(mMinDate)) {
            mDaySpinner.setMinValue(mCurrentDate.get(Calendar.DAY_OF_MONTH));
            mDaySpinner.setMaxValue(mCurrentDate
                    .getActualMaximum(Calendar.DAY_OF_MONTH));
            mDaySpinner.setWrapSelectorWheel(false);
            mMonthSpinner.setDisplayedValues(null);
            mMonthSpinner.setMinValue(mCurrentDate.get(Calendar.MONTH));
            mMonthSpinner.setMaxValue(mCurrentDate
                    .getActualMaximum(Calendar.MONTH));
            mMonthSpinner.setWrapSelectorWheel(false);
        } else if (mCurrentDate.equals(mMaxDate)) {
            mDaySpinner.setMinValue(mCurrentDate
                    .getActualMinimum(Calendar.DAY_OF_MONTH));
            mDaySpinner.setMaxValue(mCurrentDate.get(Calendar.DAY_OF_MONTH));
            mDaySpinner.setWrapSelectorWheel(false);
            mMonthSpinner.setDisplayedValues(null);
            mMonthSpinner.setMinValue(mCurrentDate
                    .getActualMinimum(Calendar.MONTH));
            mMonthSpinner.setMaxValue(mCurrentDate.get(Calendar.MONTH));
            mMonthSpinner.setWrapSelectorWheel(false);
        } else {
            mDaySpinner.setMinValue(1);
            mDaySpinner.setMaxValue(mCurrentDate
                    .getActualMaximum(Calendar.DAY_OF_MONTH));
            mDaySpinner.setWrapSelectorWheel(true);
            mMonthSpinner.setDisplayedValues(null);
            mMonthSpinner.setMinValue(0);
            mMonthSpinner.setMaxValue(11);
            mMonthSpinner.setWrapSelectorWheel(true);
        }

        // make sure the month names are a zero based array
        // with the months in the month spinner
        String[] displayedValues = Arrays.copyOfRange(mShortMonths,
                mMonthSpinner.getMinValue(), mMonthSpinner.getMaxValue() + 1);
        mMonthSpinner.setDisplayedValues(displayedValues);

        // year spinner range does not change based on the current date
        mYearSpinner.setMinValue(mMinDate.get(Calendar.YEAR));
        mYearSpinner.setMaxValue(mMaxDate.get(Calendar.YEAR));
        mYearSpinner.setWrapSelectorWheel(false);

        // set the spinner values
        mYearSpinner.setValue(mCurrentDate.get(Calendar.YEAR));
        mMonthSpinner.setValue(mCurrentDate.get(Calendar.MONTH));
        mDaySpinner.setValue(mCurrentDate.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Notifies the listener, if such, for a change in the selected date.
     */
    private void notifyDateTimeChanged() {
        if (mOnDateTimeChangedListener != null) {
            mOnDateTimeChangedListener.onDateTimeChanged(this, getYear(),
                    getMonth(), getDayOfMonth(), getCurrentHour(),
                    getCurrentMinute());
        }
    }

    /**
     * Parses the given <code>date</code> and in case of success sets the result
     * to the <code>outDate</code>.
     *
     * @return True if the date was parsed.
     */
    private boolean parseDate(String date, Calendar outDate) {
        try {
            outDate.setTime(mDateFormat.parse(date));
            return true;
        } catch (ParseException e) {
            Log.w(TAG, "Date: " + date + " not in format: " + DATE_FORMAT);
            return false;
        }
    }

    /**
     * Reorders the spinners according to the date format that is explicitly set
     * by the user and if no such is set fall back to the current locale's
     * default format.
     */
    private void reorderSpinners() {
        mSpinners.removeAllViews();
        char[] order = DateFormat.getDateFormatOrder(getContext());
        final int spinnerCount = order.length;
        int i = 0;
        for (i = 0; i < spinnerCount; i++) {
            switch (order[i]) {
                case 'd':
                    mSpinners.addView(mDaySpinner);
                    setImeOptions(mDaySpinner, false);
                    break;
                case 'M':
                    mSpinners.addView(mMonthSpinner);
                    setImeOptions(mDaySpinner, false);
                    break;
                case 'y':
                    mSpinners.addView(mYearSpinner);
                    setImeOptions(mDaySpinner, false);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        // �ڰ�Time spinner�ӻ���
        mSpinners.addView(mHourSpinner);
        setImeOptions(mHourSpinner, false);
        mSpinners.addView(mMinuteSpinner);
        setImeOptions(mMinuteSpinner, true);
    }

    /**
     * Sets the IME options for a spinner based on its ordering.
     *
     * @param spinner      The spinner.
     * @param spinnerCount The total spinner count.
     * @param spinnerIndex The index of the given spinner.
     */
    private void setImeOptions(NumberPicker spinner, boolean isLast) {
        final int imeOptions;
        if (!isLast) {
            imeOptions = EditorInfo.IME_ACTION_NEXT;
        } else {
            imeOptions = EditorInfo.IME_ACTION_DONE;
        }
        TextView input = (TextView) spinner.getChildAt(0);
        input.setImeOptions(imeOptions);
    }

    private void updateHourControl() {
        mHourSpinner.setMinValue(0);
        mHourSpinner.setMaxValue(23);
        mHourSpinner.setFormatter(TWO_DIGIT_FORMATTER);
    }

    /**
     * ���õ�ǰDateImerPicker����ʾģʽ
     *
     * @param mode
     */
    public void setMode(Mode mode) {
        switch (mode) {
            case all:
                break;
            case yearAndMonth:
                mDaySpinner.setVisibility(GONE);
                mHourSpinner.setVisibility(GONE);
                mMinuteSpinner.setVisibility(GONE);
                break;
            case date:
                mHourSpinner.setVisibility(GONE);
                mMinuteSpinner.setVisibility(GONE);
                break;
            case time:
                mYearSpinner.setVisibility(GONE);
                mMonthSpinner.setVisibility(GONE);
                mDaySpinner.setVisibility(GONE);
                break;
        }
    }


    /**
     * Use a custom NumberPicker formatting callback to use two-digit minutes
     * strings like "01". Keeping a static formatter etc. is the most efficient
     * way to do this; it avoids creating temporary objects on every call to
     * format().
     */
    public static final NumberPicker.Formatter TWO_DIGIT_FORMATTER = new NumberPicker.Formatter() {
        final StringBuilder mBuilder = new StringBuilder();

        final java.util.Formatter mFmt = new java.util.Formatter(mBuilder,
                java.util.Locale.US);

        final Object[] mArgs = new Object[1];

        public String format(int value) {
            mArgs[0] = value;
            mBuilder.delete(0, mBuilder.length());
            mFmt.format("%02d", mArgs);
            return mFmt.toString();
        }
    };

    /**
     * Class for managing state storing/restoring.
     */
    private static class SavedState extends BaseSavedState {

        private final int mYear;

        private final int mMonth;

        private final int mDay;

        private final int mHour;

        private final int mMinute;

        /**
         * Constructor called from {@link DateTimePicker#onSaveInstanceState()}
         */
        private SavedState(Parcelable superState, int year, int month, int day,
                           int hour, int minute) {
            super(superState);
            mYear = year;
            mMonth = month;
            mDay = day;
            mHour = hour;
            mMinute = minute;
        }

        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(Parcel in) {
            super(in);
            mYear = in.readInt();
            mMonth = in.readInt();
            mDay = in.readInt();
            mHour = in.readInt();
            mMinute = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mYear);
            dest.writeInt(mMonth);
            dest.writeInt(mDay);
            dest.writeInt(mHour);
            dest.writeInt(mMinute);
        }

        @SuppressWarnings("all")
        // suppress unused and hiding
        public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
