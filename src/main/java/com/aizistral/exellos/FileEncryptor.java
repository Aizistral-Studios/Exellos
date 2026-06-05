package com.aizistral.exellos;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FileEncryptor {
	private static final byte SCHEMA_VERSION = 2;

	private static final int SALT_BYTES = 16;
	private static final int IV_BYTES = 12;
	private static final int GCM_TAG_BITS = 128;

	private static final int PBKDF2_ITERATIONS = 150_000;
	private static final int AES_KEY_BITS = 256;

	private static final SecureRandom RANDOM = new SecureRandom();

	@SneakyThrows
	public String encrypt(Path file, String password) throws IOException {
		if (StringUtils.isEmpty(password))
			throw new IllegalArgumentException("Expected password to not be empty!");
		else if (!Files.isRegularFile(file))
			throw new IllegalStateException("Expected file '" + file + "' to exist and be a file!");

		String fileName = file.getFileName().toString();
		byte[] fileNameBytes = fileName.getBytes(StandardCharsets.UTF_8);
		byte[] fileBytes = Files.readAllBytes(file);

		byte[] payload = createFilePayload(fileNameBytes, fileBytes);

		byte[] salt = randomBytes(SALT_BYTES);
		byte[] iv = randomBytes(IV_BYTES);

		SecretKey key = getEncryptionKey(password, salt);

		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_BITS, iv));

		byte[] cipherBytes = cipher.doFinal(payload);

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		output.write(SCHEMA_VERSION);
		output.write(salt);
		output.write(iv);
		output.write(cipherBytes);

		return Base64.getEncoder().encodeToString(output.toByteArray());
	}

	@SneakyThrows
	public DecryptedFile decrypt(String encryptedBase64, String password) {
		if (StringUtils.isEmpty(password))
			throw new IllegalArgumentException("Expected password to not be empty!");

		byte[] allBytes = Base64.getDecoder().decode(encryptedBase64);
		ByteBuffer buffer = ByteBuffer.wrap(allBytes);

		byte version = buffer.get();

		if (version != SCHEMA_VERSION)
			throw new IllegalArgumentException("Unsupported encrypted data version: " + version);

		byte[] salt = new byte[SALT_BYTES];
		buffer.get(salt);

		byte[] iv = new byte[IV_BYTES];
		buffer.get(iv);

		byte[] cipherBytes = new byte[buffer.remaining()];
		buffer.get(cipherBytes);

		SecretKey key = getEncryptionKey(password, salt);

		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_BITS, iv));

		byte[] payload = cipher.doFinal(cipherBytes);

		return readFilePayload(payload);
	}


	private byte[] createFilePayload(byte[] fileNameBytes, byte[] fileBytes) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		output.write(ByteBuffer.allocate(4).putInt(fileNameBytes.length).array());
		output.write(fileNameBytes);
		output.write(fileBytes);

		return output.toByteArray();
	}

	private DecryptedFile readFilePayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);

		int fileNameLength = buffer.getInt();

		if (fileNameLength < 0 || fileNameLength > buffer.remaining())
			throw new IllegalArgumentException("Invalid encrypted file payload.");

		byte[] fileNameBytes = new byte[fileNameLength];
		buffer.get(fileNameBytes);

		byte[] fileBytes = new byte[buffer.remaining()];
		buffer.get(fileBytes);

		String fileName = new String(fileNameBytes, StandardCharsets.UTF_8);

		return new DecryptedFile(fileName, fileBytes);
	}

	@SneakyThrows
	private SecretKey getEncryptionKey(String password, byte[] salt) {
		PBEKeySpec spec = new PBEKeySpec(
				password.toCharArray(),
				salt,
				PBKDF2_ITERATIONS,
				AES_KEY_BITS
				);

		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		byte[] keyBytes = factory.generateSecret(spec).getEncoded();

		return new SecretKeySpec(keyBytes, "AES");
	}

	private byte[] randomBytes(int length) {
		byte[] bytes = new byte[length];
		RANDOM.nextBytes(bytes);
		return bytes;
	}

	@Value
	public static class DecryptedFile {
		private String fileName;
		private byte[] bytes;
	}

}
