package com.example.productmngmt.util;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

@Component
public class CryptoUtil {

	private static final String ALGO = "AES";
	private static final String MESSAGE_DIGEST_ALGO = "SHA-1";
	private static final int KEY_LENGTH = 16;

	private SecretKeySpec generateSecretKey(String key) throws NoSuchAlgorithmException {
		final MessageDigest sha = MessageDigest.getInstance(MESSAGE_DIGEST_ALGO);
		byte[] secretKey = sha.digest(key.getBytes());
		secretKey = Arrays.copyOf(secretKey, KEY_LENGTH);
		return new SecretKeySpec(secretKey, ALGO);
	}

	public String encrypt(String text, String key) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		final SecretKeySpec secretkey = generateSecretKey(key);
		final Cipher cipher = Cipher.getInstance(ALGO);
		cipher.init(Cipher.ENCRYPT_MODE, secretkey);
		return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes()));
	}

	public String decrypt(String encryptedtext, String key) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		final SecretKeySpec secretkey = generateSecretKey(key);
		final Cipher cipher = Cipher.getInstance(ALGO);
		cipher.init(Cipher.DECRYPT_MODE, secretkey);
		return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedtext)), StandardCharsets.UTF_8);
	}

}
