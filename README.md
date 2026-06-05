# Exellos

A simple Java program that allows encrypting and decrypting files with `AES/GCM` encryption. Plaintext passwords are used to derive 256-bit encryption keys using `PBKDF2WithHmacSHA256`. Encrypted files are returned as a Base64 string, which can be pasted back into the app for decryption.
