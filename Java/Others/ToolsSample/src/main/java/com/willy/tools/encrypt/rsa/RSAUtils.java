package com.willy.tools.encrypt.rsa;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;
import javax.crypto.Cipher;


public class RSAUtils {
	private RSAPublicKey rsaPublicKey;
	private RSAPrivateKey rsaPrivateKey;
	private final String rawStr = "12345678";
	private final String encoding = "UTF-8"; 
	
	public RSAUtils() {
		generateKeys();
	}
	/**
	 * 	生成公私鑰
	 */
	public void generateKeys() {
		KeyPairGenerator keyPairGenerator = null;
		try {
			keyPairGenerator = KeyPairGenerator.getInstance("RSA"); //NoSuchAlgorithmException
		} catch (Exception e) {
			e.printStackTrace();
		}
		keyPairGenerator.initialize(2048, new SecureRandom(UUID.randomUUID().toString().getBytes()));
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		rsaPublicKey = (RSAPublicKey)keyPair.getPublic();
		rsaPrivateKey = (RSAPrivateKey)keyPair.getPrivate();
	}
	
	public void encByPrivAndDecByPublThroughNewKeys() {
		try {
			Cipher cipher = Cipher.getInstance("RSA"); //NoSuchPaddingException
			
			cipher.init(Cipher.ENCRYPT_MODE,rsaPrivateKey); //InvalidKeyException
			byte[] b = cipher.doFinal(rawStr.getBytes()); //BadPaddingException
			System.out.println(new String(b));

			cipher.init(Cipher.DECRYPT_MODE,rsaPublicKey);
			b = cipher.doFinal(b);
			System.out.println(new String(b));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void encByPublAndDecByPrivThroughNewKeys() {
		try {
			Cipher cipher = Cipher.getInstance("RSA"); //NoSuchPaddingException
			cipher.init(Cipher.ENCRYPT_MODE,rsaPublicKey); //InvalidKeyException
			byte[] b = cipher.doFinal(rawStr.getBytes()); //BadPaddingException
			System.out.println(new String(b));
	        	
			cipher.init(Cipher.DECRYPT_MODE,rsaPrivateKey);
			b = cipher.doFinal(b);
			System.out.println(new String(b));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void genPrivKeyFile(String filePath) {
		try {
			Base64.Encoder encoder = Base64.getEncoder();
			writeBytes(filePath, encoder.encodeToString(rsaPrivateKey.getEncoded()).getBytes(encoding));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void genPublKeyFile(String filePath) {
		try {
			Base64.Encoder encoder = Base64.getEncoder();
			writeBytes(filePath, encoder.encodeToString(rsaPublicKey.getEncoded()).getBytes(encoding));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void writeBytes(String filePath, byte[] data) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);
			fos.write(data);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public byte[] readKeyByFile(String filePath) throws IOException {
		FileInputStream fis = new FileInputStream(filePath);
		Base64.Decoder decoder = Base64.getDecoder();
		byte[] buffer = new byte[4096];
		fis.read(buffer);
		fis.close();
		return decoder.decode(new String(buffer, encoding).trim().getBytes(this.encoding));
	}
	
	public void encByPrivAndDecByPublThroughFileKeys(String privKeyFilePath, String publKeyFilePath) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(readKeyByFile(privKeyFilePath));
			Cipher cipher = Cipher.getInstance("RSA"); //NoSuchPaddingException
			cipher.init(Cipher.ENCRYPT_MODE,keyFactory.generatePrivate(pkcs8EncodedKeySpec)); //InvalidKeyException
			byte[] b = cipher.doFinal(rawStr.getBytes()); //BadPaddingException
			System.out.println(new String(b));

			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(readKeyByFile(publKeyFilePath));
			cipher.init(Cipher.DECRYPT_MODE,keyFactory.generatePublic(x509EncodedKeySpec));
			b = cipher.doFinal(b);
			System.out.println(new String(b));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
