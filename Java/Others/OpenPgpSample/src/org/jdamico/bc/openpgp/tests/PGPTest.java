package org.jdamico.bc.openpgp.tests;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;

import org.bouncycastle.openpgp.PGPException;

public class PGPTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		TestBCOpenPGP tp = new TestBCOpenPGP();
//		tp.genKeyPair();
//		tp.encrypt();
		tp.decrypt();
	}

}
