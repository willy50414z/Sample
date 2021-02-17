package com.willy.tools.filestringconverter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.willy.test.sample.BinaryObjct;

import lombok.Cleanup;

public class FileStringConverter {

	public static void main(String[] args) {
		String converBinaryObjctype, fromPath, toPath;
		Scanner scanner = new Scanner(System.in);
		System.out.println("1: File -> String");
		System.out.println("2: String -> File");
		System.out.println("請選擇轉換方式: ");
		converBinaryObjctype = scanner.nextLine();

		System.out.println("請輸入來源檔路徑: ");
		fromPath = scanner.nextLine();
		System.out.println("請輸入目的檔路徑: ");
		toPath = scanner.nextLine();

		if (converBinaryObjctype.equals("1")) {
			binaryFile2StringFile(fromPath, toPath);
		} else {
			stringFile2BinaryFile(fromPath, toPath);
		}
		scanner.close();
	}
	
	private static void binaryFile2StringFile(String fromPath, String toPath) {
		try {
			@Cleanup
			InputStream input = new FileInputStream(fromPath);
			byte[] buf = new byte[1024];
			int byteLengh;
			ArrayList<BinaryObjct> byteList = new ArrayList<>();
			while ((byteLengh = input.read(buf)) > 0) {
				byteList.add(new BinaryObjct(byteLengh, buf));
			}
			writeTxt(toPath, byteList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void stringFile2BinaryFile(String fromPath, String toPath) {
		try {
			@Cleanup
			OutputStream output = new FileOutputStream(toPath);
			ArrayList<BinaryObjct> binaryObjList = readTxBinaryObjctoByteArList(fromPath);
			binaryObjList.forEach(binaryObj -> {
				try {
					output.write(binaryObj.getByteAr(), 0, binaryObj.getByteArLength());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void writeTxt(String filePath, ArrayList<BinaryObjct> byteObjList) throws IOException {
		BufferedWriter fw = null;
		try {
			File file = new File(filePath);
			if (file.exists()) {
				file.delete();
			}
			fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8")); // 指點編碼格式，以免讀取時中文字符異常
			fw.append(compress(serializeToString(byteObjList)));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				fw.flush();
				fw.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static ArrayList<BinaryObjct> readTxBinaryObjctoByteArList(String filePath)
			throws IOException, ClassNotFoundException {
		String strLine;
		StringBuffer resultStr = new StringBuffer();
		@Cleanup
		FileInputStream is = new FileInputStream(filePath);
		@Cleanup
		InputStreamReader isr = new InputStreamReader(is, "UTF-8");
		@Cleanup
		BufferedReader br = new BufferedReader(isr);// bufferedReader
		while ((strLine = br.readLine()) != null) {// 將CSV檔字串一列一列讀入並存起來直到沒有列為止
			resultStr.append(strLine).append("\r\n");
		}
//  return ((ArrayList<BinaryObjct1> unSerializeByString(unCompress(resultStr.toString())));
		return (ArrayList<BinaryObjct>) unSerializeByString(unCompress(resultStr.toString()));
	}

	public static String serializeToString(Serializable ser) throws IOException {
		if (ser == null) {
			return null;
		}
		ByteArrayOutputStream baoStream = null;
		ObjectOutputStream ooStream = null;
		try {
			baoStream = new ByteArrayOutputStream();
			ooStream = new ObjectOutputStream(baoStream);
			ooStream.writeObject(ser);
		} catch (IOException e) {
			throw e;
		} finally {
			if (ooStream != null) {
				ooStream.close();
			}
			if (baoStream != null) {
				baoStream.close();
			}
		}
		return Base64.getEncoder().encodeToString(baoStream.toByteArray());
	}

	public static Object unSerializeByString(String serializedString) throws IOException, ClassNotFoundException {
		if (serializedString == null || serializedString.length() == 0) {
			return null;
		}
		Object o;
		ObjectInputStream ois = null;
		try {
			byte[] data = Base64.getDecoder().decode(serializedString);
			ois = new ObjectInputStream(new ByteArrayInputStream(data));
			o = ois.readObject();
		} catch (ClassNotFoundException e) {
			throw e;
		} finally {
			if (ois != null) {
				ois.close();
			}
		}
		return o;
	}

	public static String compress(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return str;
		}
		ByteArrayOutputStream out = null;
		GZIPOutputStream gzip = null;
		String compress = "";
		try {
			out = new ByteArrayOutputStream();
			gzip = new GZIPOutputStream(out);
			gzip.write(str.getBytes());
			gzip.close();
			compress = new sun.misc.BASE64Encoder().encode(out.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return compress;
	}

	public static String unCompress(String str) throws IOException, ClassNotFoundException {
		if (str == null || str.length() == 0) {
			return str;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(new sun.misc.BASE64Decoder().decodeBuffer(str));
		GZIPInputStream gunzip = new GZIPInputStream(in);
		byte[] buffer = new byte[256];
		int n;
		while ((n = gunzip.read(buffer)) >= 0) {
			out.write(buffer, 0, n);
		}
		// toString()使用平臺預設編碼，也可以顯式的指定如toString("GBK")
		return out.toString();
	}

}

