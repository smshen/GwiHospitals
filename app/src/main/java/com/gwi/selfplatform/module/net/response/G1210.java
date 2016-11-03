package com.gwi.selfplatform.module.net.response;

import java.io.Serializable;

public class G1210 implements Serializable {
	private String TypeID;
	private String TypeName;
	public String getTypeID() {
		return TypeID;
	}
	public void setTypeID(String typeID) {
		TypeID = typeID;
	}
	public String getTypeName() {
		return TypeName;
	}
	public void setTypeName(String typeName) {
		TypeName = typeName;
	}
	
}
