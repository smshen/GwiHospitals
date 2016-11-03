package com.gwi.selfplatform.module.net.response;

import java.io.Serializable;

public class G1035 implements Serializable {
    private String HospCode;
    private String DirectoryId;
    private String Title;
    private String TypeID;
    private String CreateDateTime;
    private String CreateUserName;
    private String UpdateDateTime;
    private String UpdateUserName;

    public String getCreateDateTime() {
        return CreateDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        CreateDateTime = createDateTime;
    }

    public String getUpdateUserName() {
        return UpdateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        UpdateUserName = updateUserName;
    }

    public String getUpdateDateTime() {
        return UpdateDateTime;
    }

    public void setUpdateDateTime(String updateDateTime) {
        UpdateDateTime = updateDateTime;
    }

    public String getTypeID() {
        return TypeID;
    }

    public void setTypeID(String typeID) {
        TypeID = typeID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getHospCode() {
        return HospCode;
    }

    public void setHospCode(String hospCode) {
        HospCode = hospCode;
    }

    public String getDirectoryId() {
        return DirectoryId;
    }

    public void setDirectoryId(String directoryId) {
        DirectoryId = directoryId;
    }

    public String getCreateUserName() {
        return CreateUserName;
    }

    public void setCreateUserName(String createUserName) {
        CreateUserName = createUserName;
    }

    @Override
    public String toString() {
        return "G1035{" +
                "CreateDateTime='" + CreateDateTime + '\'' +
                ", HospCode='" + HospCode + '\'' +
                ", DirectoryId='" + DirectoryId + '\'' +
                ", Title='" + Title + '\'' +
                ", TypeID='" + TypeID + '\'' +
                ", CreateUserName='" + CreateUserName + '\'' +
                ", UpdateDateTime='" + UpdateDateTime + '\'' +
                ", UpdateUserName='" + UpdateUserName + '\'' +
                '}';
    }
}
