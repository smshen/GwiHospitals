package com.gwi.selfplatform.module.net.response;

public class G1410{
	
	private String TypeID;
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
	public String getNote() {
		return Note;
	}
	public void setNote(String note) {
		Note = note;
	}
	private String TypeName;
	private String Note;
	
	@Override
	public String toString() {
		return "G1410[TypeID = " + TypeID + ",TypeName =" + TypeName + ",Note = " + Note +  "]";
	}
}
