package com.gwi.selfplatform.module.pay.boc;

import android.content.Context;
import android.os.Environment;

import com.bocnet.common.security.PKCS7Tool;
import com.gwi.selfplatform.common.utils.BankUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

public class PKCSTool {
	//public static final String keyStorePath = "file:///android_asset/uat3.pfx";
	public static final String keyStorePassword = "111111";
	public static final String keyPassword = "111111";
	public static final String rootCertificatePath = "file:///android_asset/uat3.pfx";
	public static final String dn = "铁道部资金清算中心_0c7192965f46f7a0e9069c90016596ff55da3073_铁道部_11,C=CN";
	public static final String signData = "20140827160522|" + 
	        BankUtil.getBankDate().replaceAll("-", "")
			.replace(":", "").replaceAll(" ", "").trim() +
			"|001" + "|10.00" + "|104110059475555";
	/**
	 * 
	 * @param keyStorePath 证书库路径
	 * @param keyStorePassword  证书库口令
	 * @param keyPassword 签名私钥口令，与证书口令相同
	 * @param data 明文数据
	 * @return
	 */
	public static String getDigitalSign(String keyStorePath,String keyStorePassword,String keyPassword,byte[] data){
		PKCS7Tool tool = null;
		String signature = null;
		try {
			tool = PKCS7Tool.getSigner(keyStorePath, keyStorePassword, keyPassword);
			signature = tool.sign(data);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return signature;
	}
	
	/**
	 * @param rootCertificatePath 根证书路径
	 * @return
	 */
	public static void verifySign(String rootCertificatePath,String signature,byte[]data,String dn){
		//signature,签名；data,明文数据：dn,银行签名证书：DN
		try {
			PKCS7Tool tool = PKCS7Tool.getVerifier(rootCertificatePath);
			tool.verify(signature, data, dn);
		}catch (GeneralSecurityException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void createSignFile(Context context,InputStream inStream ,String fileName){
		String state = Environment.getExternalStorageState();
		File file = null ;
		if(state.equals(Environment.MEDIA_MOUNTED)){
			String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +context.getPackageName();
			file = new File(path);
			if(!file.exists()){
				 file.mkdirs();
			}else{
				file.delete();
				file.mkdirs();
			}
			file = new File(path + File.separator + fileName);
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			String path = "/data/data/" + context.getPackageName()+ File.separator + "secret";
			file = new File(path);
			if(!file.exists()){
				file.mkdirs();
			}else{
				file.delete();
				file.mkdirs();
			}
			String chmod = "chmod" + path + "777" + "&& busybox chmod" + path + "777";
			try {
				Runtime.getRuntime().exec(chmod);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			file = new File(path + File.separator + fileName);
			if(!file.exists()){
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if(file.exists()){
			FileOutputStream fStream;
			try {
				fStream = new FileOutputStream(file);
				byte[] buf = new byte[1024];
				int index = 0;
				while((index = inStream.read(buf)) > 0){
					fStream.write(buf, 0, index);
				}
				fStream.close();
				inStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	     }
	}
}
