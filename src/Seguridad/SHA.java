package Seguridad;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA{

    public static String hashear(String mensajeOriginal){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        // compute the hash of the input string
        byte[] hash = md.digest(mensajeOriginal.getBytes());
        // convert the hash to a hexadecimal string
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    public static Boolean comprobacion(String hash, String hash2){
        return hash.equals(hash2);
    }
}
