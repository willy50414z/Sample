package com.willy.tools.encrypt.rsa;

public class RSASample {
	private static final String privKeyFilePath = "D:/privateKey";
	private static final String publKeyFilePath = "D:/publicKey";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RSAUtils rsaUtil = new RSAUtils();
		rsaUtil.encByPrivAndDecByPublThroughNewKeys();
		rsaUtil.encByPublAndDecByPrivThroughNewKeys();
		
		rsaUtil.genPublKeyFile(publKeyFilePath);
		rsaUtil.genPrivKeyFile(privKeyFilePath);
		rsaUtil.encByPrivAndDecByPublThroughFileKeys(privKeyFilePath, publKeyFilePath);
	}

}
