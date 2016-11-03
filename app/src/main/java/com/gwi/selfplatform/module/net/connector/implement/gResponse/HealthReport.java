package com.gwi.selfplatform.module.net.connector.implement.gResponse;

import com.gwi.selfplatform.module.net.beans.Model;

/**
 * 健康报告
 *
 * @author 彭毅
 *
 */
public class HealthReport extends Model {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 健康报告评分
     */
    private String Score;

    /**
     * 健康消息
     */
    private String Msg;

    /**
     * 姓名
     */
    private String Name;

    /**
     * 性别
     */
    private String Sex;

    /**
     * 出生日期
     */
    private String Birth;

    /**
     * 身份证
     */
    private String IDCard;

    /**
     * 常住地址
     */
    private String Address;

    /**
     * 健康数据
     */
    private HealthReportData RecData;

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getBirth() {
        return Birth;
    }

    public void setBirth(String birth) {
        Birth = birth;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String iDCard) {
        IDCard = iDCard;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public HealthReportData getRecData() {
        return RecData;
    }

    public void setRecData(HealthReportData recData) {
        RecData = recData;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    /**
     * 健康数据
     *
     * @author 彭毅
     *
     */
    public static class HealthReportData {

        public HealthReportData() {
            BMIMin = 18.5;
            BMIMax = 22.9;
        }

        /**
         * 身高
         */
        private String Stature;
        /**
         * 身高单位
         */
        private String StatureUnit;
        /**
         * 体重
         */
        private String Weight;

        /**
         * 体重单位
         */
        private String WeightUnit;

        /**
         * 体重身高比(体重/身高^2)
         */
        private String BMI;

        /**
         * BMI状态
         */
        private String BMIState;

        /**
         * BMI范围
         */
        private String BMIRange;

        /**
         * BMI最低值
         */
        private double BMIMin;

        /**
         * BMI最高值
         */
        private double BMIMax;

        /**
         * 收缩压
         */
        private String SystolicPressure;

        /**
         * 收缩压单位
         */
        private String SystolicPressureUnit;

        /**
         * 收缩压状态
         */
        private String SystolicPressureState;

        /**
         * 收缩压范围
         */
        private String SystolicPressureRange;

        /**
         * 舒张压
         */
        private String DiastolicPressure;

        /**
         * 舒张压单位
         */
        private String DiastolicPressureUnit;

        /**
         * 舒张压状态
         */
        private String DiastolicPressureState;

        /**
         * 舒张压范围
         */
        private String DiastolicPressureRange;

        /**
         * 心率
         */
        private String HeartRate;

        /**
         * 心率单位
         */
        private String HeartRateUnit;

        /**
         * 心率状态
         */
        private String HeartRateState;

        /**
         * 心率范围
         */
        private String HeartRateRange;

        /**
         * 血氧
         */
        private String BloodOxygen;

        /**
         * 血氧单位
         */
        private String BloodOxygenUnit;

        /**
         * 血氧状态
         */
        private String BloodOxygenState;

        /**
         * 血氧范围
         */
        private String BloodOxygenRange;

        /**
         * 体脂
         */
        private String BodyFat;

        /**
         * 体脂单位
         */
        private String BodyFatUnit;

        /**
         * 体脂状态
         */
        private String BodyFatState;

        /**
         * 体脂范围
         */
        private String BodyFatRange;

        public String getStature() {
            return Stature;
        }

        public void setStature(String stature) {
            Stature = stature;
        }

        public String getStatureUnit() {
            return StatureUnit;
        }

        public void setStatureUnit(String statureUnit) {
            StatureUnit = statureUnit;
        }

        public String getWeight() {
            return Weight;
        }

        public void setWeight(String weight) {
            Weight = weight;
        }

        public String getWeightUnit() {
            return WeightUnit;
        }

        public void setWeightUnit(String weightUnit) {
            WeightUnit = weightUnit;
        }

        public String getBMI() {
            return BMI;
        }

        public void setBMI(String bMI) {
            BMI = bMI;
        }

        public String getBMIState() {
            return BMIState;
        }

        public void setBMIState(String bMIState) {
            BMIState = bMIState;
        }

        public String getBMIRange() {
            return BMIRange;
        }

        public void setBMIRange(String bMIRange) {
            BMIRange = bMIRange;
        }

        public double getBMIMin() {
            return BMIMin;
        }

        public void setBMIMin(double bMIMin) {
            BMIMin = bMIMin;
        }

        public double getBMIMax() {
            return BMIMax;
        }

        public void setBMIMax(double bMIMax) {
            BMIMax = bMIMax;
        }

        public String getSystolicPressure() {
            return SystolicPressure;
        }

        public void setSystolicPressure(String systolicPressure) {
            SystolicPressure = systolicPressure;
        }

        public String getSystolicPressureUnit() {
            return SystolicPressureUnit;
        }

        public void setSystolicPressureUnit(String systolicPressureUnit) {
            SystolicPressureUnit = systolicPressureUnit;
        }

        public String getSystolicPressureState() {
            return SystolicPressureState;
        }

        public void setSystolicPressureState(String systolicPressureState) {
            SystolicPressureState = systolicPressureState;
        }

        public String getSystolicPressureRange() {
            return SystolicPressureRange;
        }

        public void setSystolicPressureRange(String systolicPressureRange) {
            SystolicPressureRange = systolicPressureRange;
        }

        public String getDiastolicPressure() {
            return DiastolicPressure;
        }

        public void setDiastolicPressure(String diastolicPressure) {
            DiastolicPressure = diastolicPressure;
        }

        public String getDiastolicPressureUnit() {
            return DiastolicPressureUnit;
        }

        public void setDiastolicPressureUnit(String diastolicPressureUnit) {
            DiastolicPressureUnit = diastolicPressureUnit;
        }

        public String getDiastolicPressureState() {
            return DiastolicPressureState;
        }

        public void setDiastolicPressureState(String diastolicPressureState) {
            DiastolicPressureState = diastolicPressureState;
        }

        public String getDiastolicPressureRange() {
            return DiastolicPressureRange;
        }

        public void setDiastolicPressureRange(String diastolicPressureRange) {
            DiastolicPressureRange = diastolicPressureRange;
        }

        public String getHeartRate() {
            return HeartRate;
        }

        public void setHeartRate(String heartRate) {
            HeartRate = heartRate;
        }

        public String getHeartRateUnit() {
            return HeartRateUnit;
        }

        public void setHeartRateUnit(String heartRateUnit) {
            HeartRateUnit = heartRateUnit;
        }

        public String getHeartRateState() {
            return HeartRateState;
        }

        public void setHeartRateState(String heartRateState) {
            HeartRateState = heartRateState;
        }

        public String getHeartRateRange() {
            return HeartRateRange;
        }

        public void setHeartRateRange(String heartRateRange) {
            HeartRateRange = heartRateRange;
        }

        public String getBloodOxygen() {
            return BloodOxygen;
        }

        public void setBloodOxygen(String bloodOxygen) {
            BloodOxygen = bloodOxygen;
        }

        public String getBloodOxygenUnit() {
            return BloodOxygenUnit;
        }

        public void setBloodOxygenUnit(String bloodOxygenUnit) {
            BloodOxygenUnit = bloodOxygenUnit;
        }

        public String getBloodOxygenState() {
            return BloodOxygenState;
        }

        public void setBloodOxygenState(String bloodOxygenState) {
            BloodOxygenState = bloodOxygenState;
        }

        public String getBloodOxygenRange() {
            return BloodOxygenRange;
        }

        public void setBloodOxygenRange(String bloodOxygenRange) {
            BloodOxygenRange = bloodOxygenRange;
        }

        public String getBodyFat() {
            return BodyFat;
        }

        public void setBodyFat(String bodyFat) {
            BodyFat = bodyFat;
        }

        public String getBodyFatUnit() {
            return BodyFatUnit;
        }

        public void setBodyFatUnit(String bodyFatUnit) {
            BodyFatUnit = bodyFatUnit;
        }

        public String getBodyFatState() {
            return BodyFatState;
        }

        public void setBodyFatState(String bodyFatState) {
            BodyFatState = bodyFatState;
        }

        public String getBodyFatRange() {
            return BodyFatRange;
        }

        public void setBodyFatRange(String bodyFatRange) {
            BodyFatRange = bodyFatRange;
        }

    }

}