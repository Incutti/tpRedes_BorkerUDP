package Seguridad;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import javax.crypto.Cipher;


public class RSA {

    public static KeyPair RSA() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = null;
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048); //1024 used for normal securities
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return keyPair;
    }


    public static byte[] encryptData(String data, PublicKey pubKey) throws IOException {
        System.out.println("\n----------------ENCRYPTION STARTED------------");

        System.out.println("Original Data :" + data);
        byte[] dataToEncrypt = data.getBytes();
        byte[] encryptedData = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            encryptedData = cipher.doFinal(dataToEncrypt);
            System.out.println("Encryted Data: " + encryptedData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("----------------ENCRYPTION COMPLETED------------");
        return encryptedData;
    }


    public static String  decryptData(byte[] data, PrivateKey privateKey) throws IOException {
        //System.out.println("\n----------------DECRYPTION STARTED------------");
        String mensaje =null;

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[]  descryptedData = cipher.doFinal(data);
            mensaje = new String(descryptedData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("----------------DECRYPTION COMPLETED------------");
        return mensaje;
    }

    public static String decryptHashData(byte[] data, PublicKey publicKey) throws IOException { // LO UNICO QUE CAMBIA CON DECRYPT NORMAL ES PRIVKEY O PUBKEY
        System.out.println("\n----------------HASH DECRYPTION STARTED------------");
        byte[] descryptedData = null;
        String mensaje = null;

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            descryptedData = cipher.doFinal(data);
            mensaje = new String(descryptedData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("----------------HASH DECRYPTION COMPLETED------------");
        return mensaje;
    }

    public static byte[] signData(String data, PrivateKey privKey) throws IOException {
        System.out.println("\n----------------SIGNING STARTED------------");

        System.out.println("Original Data: " + data);
        data=SHA.hashear(data);
        System.out.println("Hashed Data: " + data);
        byte[] dataToEncrypt = data.getBytes();
        byte[] encryptedData = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privKey);
            encryptedData = cipher.doFinal(dataToEncrypt);
            System.out.println("Encryted Data: " + encryptedData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("----------------SIGNING COMPLETED------------");
        return encryptedData;
    }
}