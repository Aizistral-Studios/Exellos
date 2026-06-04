package com.aizistral.exellos;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.common.primitives.Bytes;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
class EncryptionUtils {

	@SneakyThrows
	public byte[] encrypt(byte[] bytes, String password) {
		SecretKey key = getEncryptionKey(password);

		byte[] iv = new byte[12];
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(iv);

		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(128, iv));

		return Bytes.concat(iv, cipher.doFinal(bytes));
	}

	@SneakyThrows
	public byte[] decrypt(byte[] bytes, String password) {
		SecretKey key = getEncryptionKey(password);

		byte[] iv = BitUtils.firstNBytes(bytes, 12);
		byte[] encryptedContent = BitUtils.lastNBytes(bytes, bytes.length - iv.length);

		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, iv));

		return cipher.doFinal(encryptedContent);
	}

	private SecretKey getEncryptionKey(String password) {
		byte[] keyBytes = DigestUtils.sha256(password);
		return new SecretKeySpec(keyBytes, "AES");
	}

}
