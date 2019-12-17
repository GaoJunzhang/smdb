package com.zgj.mps.tool;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.io.*;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };
	protected static MessageDigest messagedigest = null;
	static {
		try {
			messagedigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
		}
	}

	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

	public static boolean checkPassword(String password, String md5PwdStr) {
		String s = getMD5String(password);
		return s.equals(md5PwdStr);
	}

	public static String getFileMD5String(File file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		FileChannel ch = in.getChannel();
		MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
		messagedigest.update(byteBuffer);
		return bufferToHex(messagedigest.digest());
	}

	public static String getMD5String(String s) {
		try {
			return getMD5String(s.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}

	public static String getMD5String(byte[] bytes) {
		messagedigest.update(bytes);
		return bufferToHex(messagedigest.digest());
	}

	public static String getLargeMD5(String path) throws IOException, NoSuchAlgorithmException {

		File f = new File(path);

		if (!f.exists()) {
			return null;
		}

		InputStream ins = new FileInputStream(f);

		byte[] buffer = new byte[8192];

		int len;
		while ((len = ins.read(buffer)) != -1) {
			messagedigest.update(buffer, 0, len);
		}

		ins.close();

		return DigestUtils.md5Hex(messagedigest.digest());
	}

	public static String getMD5(String path) throws IOException {
		InputStream ins = null;
		try {
			File f = new File(path);

			if (!f.exists()) {
				return null;
			}
			ins = new FileInputStream(f);
			return DigestUtils.md5Hex(ins);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ins.close();
		}
		return null;
	}

	public static String getMD5FOss(String path) throws IOException {
		InputStream ins = null;
		try {
			ins = new URL(path).openStream();
			return DigestUtils.md5Hex(ins);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ins.close();
		}
		return null;
	}

	public static String getMyMd5(String path) {
		try {
			String File_md5 = getMD5FOss(path);
			String md5 = getMD5String(File_md5 + "20160822");
			return md5;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getPassword(String account,String password){
		String salt = getMD5String(account);
		System.out.println(salt);
		System.out.println("salt");
		SimpleHash hash = new SimpleHash("md5", password,
				salt, 2);
		return hash.toHex();
	}

	public static void main(String[] args) {
		System.out.println(getPassword("admin","zgj1234"));
	}
}