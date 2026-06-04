package com.aizistral.papyrq;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.primitives.Bytes;

import lombok.experimental.UtilityClass;

@UtilityClass
class BitUtils {

	public static byte[] firstNBytes(byte[] input, int n) {
		if (input == null)
			throw new IllegalArgumentException("Input array cannot be null");
		else if (input.length < n)
			throw new IllegalArgumentException("Input array is shorter than " + n + " bytes");

		return Arrays.copyOfRange(input, 0, n);
	}

	public static byte[] lastNBytes(byte[] input, int n) {
		if (input == null)
			throw new IllegalArgumentException("Input array cannot be null");
		else if (input.length < n)
			throw new IllegalArgumentException("Input array is shorter than " + n + " bytes");

		return Arrays.copyOfRange(input, input.length - n, input.length);
	}

	public static byte[] intToByteArray(int value) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) ((value >> 24) & 0xFF); // Most significant byte
		bytes[1] = (byte) ((value >> 16) & 0xFF);
		bytes[2] = (byte) ((value >> 8) & 0xFF);
		bytes[3] = (byte) (value & 0xFF);         // Least significant byte
		return bytes;
	}

	public static int byteArrayToInt(byte... bytes) {
		if (bytes == null || bytes.length != 4)
			throw new IllegalArgumentException("Byte array must have length 4");

		return  ((bytes[0] & 0xFF) << 24) |
				((bytes[1] & 0xFF) << 16) |
				((bytes[2] & 0xFF) << 8)  |
				(bytes[3] & 0xFF);
	}

	public static byte[] longToByteArray(long value) {
		byte[] bytes = new byte[8];
		bytes[0] = (byte) ((value >> 56) & 0xFF); // Most significant byte
		bytes[1] = (byte) ((value >> 48) & 0xFF);
		bytes[2] = (byte) ((value >> 40) & 0xFF);
		bytes[3] = (byte) ((value >> 32) & 0xFF);
		bytes[4] = (byte) ((value >> 24) & 0xFF);
		bytes[5] = (byte) ((value >> 16) & 0xFF);
		bytes[6] = (byte) ((value >> 8) & 0xFF);
		bytes[7] = (byte) (value & 0xFF);         // Least significant byte
		return bytes;
	}

	public static long byteArrayToLong(byte... bytes) {
		if (bytes == null || bytes.length != 8)
			throw new IllegalArgumentException("Byte array must have length 8");

		return  ((long)(bytes[0] & 0xFF) << 56) |
				((long)(bytes[1] & 0xFF) << 48) |
				((long)(bytes[2] & 0xFF) << 40) |
				((long)(bytes[3] & 0xFF) << 32) |
				((long)(bytes[4] & 0xFF) << 24) |
				((long)(bytes[5] & 0xFF) << 16) |
				((long)(bytes[6] & 0xFF) << 8)  |
				(bytes[7] & 0xFF);
	}

	public static List<byte[]> splitIntoChunks(byte[] input, int chunkSize, Random random) {
		List<Byte> byteList = Bytes.asList(input);

		List<List<Byte>> partitions = Lists.partition(byteList, chunkSize);
		ImmutableList.Builder<byte[]> result = ImmutableList.builderWithExpectedSize(partitions.size());

		for (List<Byte> part : partitions) {
			byte[] chunk = new byte[chunkSize]; // zero-padded

			for (int i = 0; i < chunk.length; i++) {
				if (i < part.size()) {
					chunk[i] = part.get(i).byteValue();
				} else {
					byte[] randomBytes = new byte[1];
					random.nextBytes(randomBytes);
					chunk[i] = randomBytes[0];
				}
			}

			result.add(chunk);
		}

		return result.build();
	}

}
