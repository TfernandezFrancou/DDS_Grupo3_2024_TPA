package org.example.autenticacion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class HashGenerator {

    private HashGenerator(){}

    private static final Logger logger = LoggerFactory.getLogger(HashGenerator.class);

    // Generar un hash con salt y retornarlo en formato "salt:hash"
    public static String hash(String password) {
        try {
            // Generar salt aleatorio
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            // Generar el hash
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hashBytes = factory.generateSecret(spec).getEncoded();

            // Convertir salt y hash a Base64 y concatenarlos
            String saltBase64 = Base64.getEncoder().encodeToString(salt);
            String hashBase64 = Base64.getEncoder().encodeToString(hashBytes);

            return saltBase64 + ":" + hashBase64; // Formato: "salt:hash"
        } catch (Exception e) {
            logger.error("Error al generar el hash", e);
            return "";
        }
    }

    // Verificar una contraseña
    public static boolean verify(String password, String storedHash) {
        try {
            // Dividir el salt y el hash guardados
            String[] parts = storedHash.split(":");
            if (parts.length != 2) {
                throw new IllegalArgumentException("El hash almacenado tiene un formato inválido");
            }
            String saltBase64 = parts[0];
            String hashBase64 = parts[1];

            // Convertir Base64 a bytes
            byte[] salt = Base64.getDecoder().decode(saltBase64);
            byte[] storedHashBytes = Base64.getDecoder().decode(hashBase64);

            // Generar el hash usando el salt guardado
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hashBytes = factory.generateSecret(spec).getEncoded();

            // Comparar hashes
            return MessageDigest.isEqual(hashBytes, storedHashBytes);
        } catch (Exception e) {
            logger.error("Error al generar el hash", e);
            return false;
        }
    }
}
