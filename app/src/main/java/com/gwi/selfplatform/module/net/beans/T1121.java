package com.gwi.selfplatform.module.net.beans;

/**
 * 百度推送
 * @author 
 *
 */
public class T1121 {
	private Long UserId;
    private String BaiduUserId;
    private String BaiduChannelId;
    private String HospitalCode;
    private String BaiduTag;
    
    public T1121()
    {
    	
    }
    
    public Long getUserId() {
		return UserId;
	}
	public void setUserId(Long userId) {
		UserId = userId;
	}
	public String getBaiduUserId() {
		return BaiduUserId;
	}
	public void setBaiduUserId(String baiduUserId) {
		BaiduUserId = baiduUserId;
	}
	public String getBaiduChannelId() {
		return BaiduChannelId;
	}
	public void setBaiduChannelId(String baiduChannelId) {
		BaiduChannelId = baiduChannelId;
	}
	public String getHospitalCode() {
		return HospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		HospitalCode = hospitalCode;
	}
	public String getBaiduTag() {
		return BaiduTag;
	}
	public void setBaiduTag(String baiduTag) {
		BaiduTag = baiduTag;
	}

}
