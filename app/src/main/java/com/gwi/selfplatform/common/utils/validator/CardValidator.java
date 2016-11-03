package com.gwi.selfplatform.common.utils.validator;

import android.text.TextUtils;

public class CardValidator extends Validator {

    public static final int CARD_HEALTH = 0;
    public static final int CARD_MEDICAL = 1;
    public static final int CARD_BANK = 2;
    public static final int CARD_SOCAIL_SECURITY = 3;
    public static final int CARD_CITIZEN = 4;
    public static final int CARD_ID = 5;

    @Override
    public int validate(String value, int type) {
        switch (type) {
        case CARD_ID:
        case CARD_HEALTH:
            return validateHealthCard(value);
        case CARD_MEDICAL:
            return validateMedical(value);
        case CARD_BANK:
            return validateBank(value);
        case CARD_SOCAIL_SECURITY:
            return validateSocalSecurity(value);
        case CARD_CITIZEN:
            return validateCitizen(value);
        }
        return ERROR_COMMON;
    }

    private int validateCitizen(String value) {
        if (TextUtils.isEmpty(value)) {
            return ERROR_EMPTY;
        }
        return SUCCESS;
    }

    private int validateSocalSecurity(String value) {
        if (TextUtils.isEmpty(value)) {
            return ERROR_EMPTY;
        }
        return SUCCESS;
    }

    private int validateBank(String value) {
        if (TextUtils.isEmpty(value)) {
            return ERROR_EMPTY;
        }
        return SUCCESS;
    }

    private int validateMedical(String value) {
        if (TextUtils.isEmpty(value)) {
            return ERROR_EMPTY;
        }

        return SUCCESS;
    }

    private int validateHealthCard(String value) {
        final int LENGTH_15 = 15;
        final int LENGTH_18 = 18;
        // 检查空
        if (TextUtils.isEmpty(value)) {
            return ERROR_EMPTY;
        }
        // 检查长度
        if (!(value.length() == LENGTH_15 || value.length() == LENGTH_18)) {
            return ERROR_COMMON;
        }
        // 检查字符
        if (value.length() == LENGTH_15) {
            byte[] temp = value.getBytes();
            for (int i = 0; i < value.length(); i++) {
                if (temp[i] < 48 || temp[i] > 57) {
                    return ERROR_COMMON;
                }
            }
        } else if (value.length() == LENGTH_18) {
            byte[] temp = value.getBytes();
            for (int i = 0; i < temp.length; i++) {
                if (temp[i] < 48 || temp[i] > 57) {
                    if (i == 17 && temp[i] != 88) {
                        return ERROR_COMMON;
                    }
                }
            }
        }
        // 检查时间
        /* 非闰年天数 */
        int[] monthDayN = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        /* 闰年天数 */
        int[] monthDayL = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        int month;
        if (value.length() == LENGTH_15) {
            month = Integer.parseInt(value.substring(8, 10));
        } else {
            month = Integer.parseInt(value.substring(10, 12));
        }

        int day;
        if (value.length() == LENGTH_15) {
            day = Integer.parseInt(value.substring(10, 12));
        } else {
            day = Integer.parseInt(value.substring(12, 14));
        }

        if (month > 12 || month <= 0) {
            return ERROR_COMMON;
        }

        // 闰年
        String yearStr;
        if (value.length() == LENGTH_15) {
            yearStr = "19" + value.substring(6, 8);
        } else {
            yearStr = value.substring(6, 10);
        }

        boolean isLeapYear = false;
        int year = Integer.parseInt(yearStr);
        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            isLeapYear = true;
        } else {
            isLeapYear = false;
        }

        if (isLeapYear) {
            if (day > monthDayL[month - 1] || day <= 0)
                return ERROR_COMMON;
        } else {
            if (day > monthDayN[month - 1] || day <= 0)
                return ERROR_COMMON;
        }
        // 获取校验位与最后一位进行比较
        if (value.length() == LENGTH_18) {
            String checkString = value.substring(17);

            String checkTable[] = new String[] { "1", "0", "X", "9", "8", "7",
                    "6", "5", "4", "3", "2" };
            int[] wi = new int[] { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5,
                    8, 4, 2, 1 };
            int sum = 0;

            for (int i = 0; i < 17; i++) {
                String ch = value.substring(i, i + 1);
                sum = sum + Integer.parseInt(ch) * wi[i];
            }
            int y = sum % 11;
            String checkBit = checkTable[y];

            if (!checkString.equals(checkBit)) {
                return ERROR_COMMON;
            }
        }
        return SUCCESS;
    }

}
