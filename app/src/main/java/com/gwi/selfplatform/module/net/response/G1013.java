package com.gwi.selfplatform.module.net.response;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class G1013 {
    // private String HospCode;
    // private String HospName;
    // private String Rules;
    // private String Introduction;
    // private String Feature;
    // private String Telephone;
    // private String Address;
    // private String ProvinceCode;
    // private String Province;
    // private String CityCode;
    // private String City;
    // private String CountyCode;
    // private String County;
    // private String HomeUrl;
    // private String Note;
    // public String getHospCode() {
    // return HospCode;
    // }
    // public void setHospCode(String hospCode) {
    // HospCode = hospCode;
    // }
    // public String getHospName() {
    // return HospName;
    // }
    // public void setHospName(String hospName) {
    // HospName = hospName;
    // }
    // public String getRules() {
    // return Rules;
    // }
    // public void setRules(String rules) {
    // Rules = rules;
    // }
    // public String getIntroduction() {
    // return Introduction;
    // }
    // public void setIntroduction(String introduction) {
    // Introduction = introduction;
    // }
    // public String getFeature() {
    // return Feature;
    // }
    // public void setFeature(String feature) {
    // Feature = feature;
    // }
    // public String getTelephone() {
    // return Telephone;
    // }
    // public void setTelephone(String telephone) {
    // Telephone = telephone;
    // }
    // public String getAddress() {
    // return Address;
    // }
    // public void setAddress(String address) {
    // Address = address;
    // }
    // public String getProvinceCode() {
    // return ProvinceCode;
    // }
    // public void setProvinceCode(String provinceCode) {
    // ProvinceCode = provinceCode;
    // }
    // public String getProvince() {
    // return Province;
    // }
    // public void setProvince(String province) {
    // Province = province;
    // }
    // public String getCityCode() {
    // return CityCode;
    // }
    // public void setCityCode(String cityCode) {
    // CityCode = cityCode;
    // }
    // public String getCity() {
    // return City;
    // }
    // public void setCity(String city) {
    // City = city;
    // }
    // public String getCountyCode() {
    // return CountyCode;
    // }
    // public void setCountyCode(String countyCode) {
    // CountyCode = countyCode;
    // }
    // public String getCounty() {
    // return County;
    // }
    // public void setCounty(String county) {
    // County = county;
    // }
    // public String getHomeUrl() {
    // return HomeUrl;
    // }
    // public void setHomeUrl(String homeUrl) {
    // HomeUrl = homeUrl;
    // }
    // public String getNote() {
    // return Note;
    // }
    // public void setNote(String note) {
    // Note = note;
    // }
    
//    private int Status;
//    private String ResultMsg;
//    /** yyyy-MM-dd HH:mm:ss */
//    private String OpTime;
//    private String TranSerNo;
//    private int FunCode;
//
//    public int getStatus() {
//        return Status;
//    }
//
//    public void setStatus(int status) {
//        Status = status;
//    }
//
//    public String getResultMsg() {
//        return ResultMsg;
//    }
//
//    public void setResultMsg(String resultMsg) {
//        ResultMsg = resultMsg;
//    }
//
//    public String getOpTime() {
//        return OpTime;
//    }
//
//    public void setOpTime(String opTime) {
//        OpTime = opTime;
//    }
//
//    public String getTranSerNo() {
//        return TranSerNo;
//    }
//
//    public void setTranSerNo(String tranSerNo) {
//        TranSerNo = tranSerNo;
//    }
//
//    public int getFunCode() {
//        return FunCode;
//    }
//
//    public void setFunCode(int funCode) {
//        FunCode = funCode;
//    }

    private List<HospitalInfo> Body;

    public List<HospitalInfo> getBody() {
        return Body;
    }

    public void setBody(List<HospitalInfo> body) {
        this.Body = body;
    }

    public static class HospitalInfo implements Serializable {
        /**
         * 医院ID
         */
        public long HospitalId;
        /**
         * 医院代码
         */
        public String HospitalCode;

        /**
         * 医院名称
         */
        public String HospitalName;

        /**
         * 医院等级
         */
        public int HospitaLevel;

        /**
         * 医院性质
         */
        public int Nature;
        /**
         * 专科特色
         */
        public String Feature;

        /**
         * 医院简介
         */
        public String Introduction;

        /**
         * 医院主页
         */
        public String HomePage;

        /**
         * 联系电话
         */
        public String PhoneNumber;

        /**
         * 医院地址
         */
        public String Address;

        /**
         * 所在区域
         */
        public String AreaCode;

        /**
         * 交通指南
         */
        public String TrafficGuide;

        /**
         * 医院描述
         */
        public String Description;

        /**
         * 医院主图
         */
        public String MainPic;

        /**
         * 医院logo
         */
        public String LogoPic;

        /**
         * 服务地址
         */
        public String WebUrl;

        /**
         * 账号
         */
        public String Account;

        /**
         * 密码
         */
        public String Pwd;

        /**
         * 密钥
         */
        public String SecretKey;

        /**
         * APP包名
         */
        public String PackName;

        /**
         * Apk路径
         */
        public String ApkPath;

        /**
         * 创建人
         */
        public String CreateUser;

        /**
         * 创建日期
         */
        public Date CreateDate;

        /**
         * 更新人
         */
        public String UpdateUser;

        /**
         * 更新日期
         */
        public Date UpdateDate;

        /**
         * 状态
         */
        public int Status;

        public HospitalInfo() {
        }

        public long getHospitalId() {
            return HospitalId;
        }

        public void setHospitalId(long hospitalId) {
            HospitalId = hospitalId;
        }

        public String getHospitalCode() {
            return HospitalCode;
        }

        public void setHospitalCode(String hospitalCode) {
            HospitalCode = hospitalCode;
        }

        public String getHospitalName() {
            return HospitalName;
        }

        public void setHospitalName(String hospitalName) {
            HospitalName = hospitalName;
        }

        public int getHospitaLevel() {
            return HospitaLevel;
        }

        public void setHospitaLevel(int hospitaLevel) {
            HospitaLevel = hospitaLevel;
        }

        public int getNature() {
            return Nature;
        }

        public void setNature(int nature) {
            Nature = nature;
        }

        public String getFeature() {
            return Feature;
        }

        public void setFeature(String feature) {
            Feature = feature;
        }

        public String getIntroduction() {
            return Introduction;
        }

        public void setIntroduction(String introduction) {
            Introduction = introduction;
        }

        public String getHomePage() {
            return HomePage;
        }

        public void setHomePage(String homePage) {
            HomePage = homePage;
        }

        public String getPhoneNumber() {
            return PhoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            PhoneNumber = phoneNumber;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public String getAreaCode() {
            return AreaCode;
        }

        public void setAreaCode(String areaCode) {
            AreaCode = areaCode;
        }

        public String getTrafficGuide() {
            return TrafficGuide;
        }

        public void setTrafficGuide(String trafficGuide) {
            TrafficGuide = trafficGuide;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

        public String getMainPic() {
            return MainPic;
        }

        public void setMainPic(String mainPic) {
            MainPic = mainPic;
        }

        public String getLogoPic() {
            return LogoPic;
        }

        public void setLogoPic(String logoPic) {
            LogoPic = logoPic;
        }

        public String getWebUrl() {
            return WebUrl;
        }

        public void setWebUrl(String webUrl) {
            WebUrl = webUrl;
        }

        public String getAccount() {
            return Account;
        }

        public void setAccount(String account) {
            Account = account;
        }

        public String getPwd() {
            return Pwd;
        }

        public void setPwd(String pwd) {
            Pwd = pwd;
        }

        public String getSecretKey() {
            return SecretKey;
        }

        public void setSecretKey(String secretKey) {
            SecretKey = secretKey;
        }

        public String getPackName() {
            return PackName;
        }

        public void setPackName(String packName) {
            PackName = packName;
        }

        public String getApkPath() {
            return ApkPath;
        }

        public void setApkPath(String apkPath) {
            ApkPath = apkPath;
        }

        public String getCreateUser() {
            return CreateUser;
        }

        public void setCreateUser(String createUser) {
            CreateUser = createUser;
        }

        public Date getCreateDate() {
            return CreateDate;
        }

        public void setCreateDate(Date createDate) {
            CreateDate = createDate;
        }

        public String getUpdateUser() {
            return UpdateUser;
        }

        public void setUpdateUser(String updateUser) {
            UpdateUser = updateUser;
        }

        public Date getUpdateDate() {
            return UpdateDate;
        }

        public void setUpdateDate(Date updateDate) {
            UpdateDate = updateDate;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int status) {
            Status = status;
        }
    }
}
