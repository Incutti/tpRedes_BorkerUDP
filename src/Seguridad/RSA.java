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

/**
 *
 * @author Anuj
 * Blog www.goldenpackagebyanuj.blogspot.com
 * RSA - Encrypt Data using Public Key
 * RSA - Descypt Data using Private Key
 */
public class RSA {

//    private static final String PUBLIC_KEY_FILE = "Public.key";
//    private static final String PRIVATE_KEY_FILE = "Private.key";
//    private PrivateKey privateKey;
//    private PublicKey publicKey;

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

//    public PrivateKey getPrivateKey() {
//        return privateKey;
//    }
//
//    public void setPrivateKey(PrivateKey privateKey) {
//        this.privateKey = privateKey;
//    }
//
//    public PublicKey getPublicKey() {
//        return publicKey;
//    }
//
//    public void setPublicKey(PublicKey publicKey) {
//        this.publicKey = publicKey;

//    }
    public static void main(String[] args) throws IOException {

        RSA rsaObj = new RSA();
//        System.out.println("-------GENERATE PUBLIC and PRIVATE KEY-------------");
//        System.out.println(rsaObj.getPrivateKey());
//        System.out.println(rsaObj.getPublicKey());
        //Pullingout parameters which makes up Key
//            System.out.println("\n------- PULLING OUT PARAMETERS WHICH MAKES KEYPAIR----------\n");
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//            /*publica*/RSAPublicKeySpec rsaPubKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
//            /*privada*/RSAPrivateKeySpec rsaPrivKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);
//
//           System.out.println("PubKey Modulus : " + rsaPubKeySpec.getModulus());
//           System.out.println("PubKey Exponent : " + rsaPubKeySpec.getPublicExponent());
//           System.out.println("PrivKey Modulus : " + rsaPrivKeySpec.getModulus());
//           System.out.println("PrivKey Exponent : " + rsaPrivKeySpec.getPrivateExponent());

        //Share public key with other, so they can encrypt data and decrypt thoses using private key(Don't share with Other)
//            System.out.println("\n--------SAVING PUBLIC KEY AND PRIVATE KEY TO FILES-------\n");
//            rsaObj.saveKeys(PUBLIC_KEY_FILE, rsaPubKeySpec.getModulus(), rsaPubKeySpec.getPublicExponent());
//            rsaObj.saveKeys(PRIVATE_KEY_FILE, rsaPrivKeySpec.getModulus(), rsaPrivKeySpec.getPrivateExponent());

        //Encrypt Data using Public Key
    // ENCRIPTAR    byte[] encryptedData = rsaObj.encryptData("Anuj Patel - Classified Information !", rsaObj.getPublicKey());

        //Descypt Data using Private Key
    //DESENCRIPTAR    rsaObj.decryptData(encryptedData,rsaObj.getPrivateKey());

        //        catch (InvalidKeySpecException e) {
//            e.printStackTrace();
//        }

    }

    /**
     * Save Files
     * @param fileName
     * @param mod
     * @param exp
     * @throws IOException
     */
    private void saveKeys(String fileName,BigInteger mod,BigInteger exp) throws IOException{
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            System.out.println("Generating "+fileName + "...");
            fos = new FileOutputStream(fileName);
            oos = new ObjectOutputStream(new BufferedOutputStream(fos));

            oos.writeObject(mod);
            oos.writeObject(exp);

            System.out.println(fileName + " generated successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(oos != null){
                oos.close();

                if(fos != null){
                    fos.close();
                }
            }
        }
    }

    /**
     * Encrypt Data
     * @param data
     * @throws IOException
     */
    public static byte[] encryptData(String data, PublicKey pubKey) throws IOException {
        System.out.println("\n----------------ENCRYPTION STARTED------------");

        System.out.println("Data Before Encryption :" + data);
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

    /**
     * Encrypt Data
     * @param data
     * @throws IOException
     */
    public static String  decryptData(byte[] data, PrivateKey privateKey) throws IOException {
        System.out.println("\n----------------DECRYPTION STARTED------------");
        byte[] descryptedData = null;
        String mensaje = null;

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            descryptedData = cipher.doFinal(data);
            mensaje = new String(descryptedData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("----------------DECRYPTION COMPLETED------------");
        return mensaje;
    }

    public static byte[] signData(String data, PrivateKey privKey) throws IOException {
        System.out.println("\n----------------ENCRYPTION STARTED------------");

        System.out.println("Data Before Hashing :"+data);
        data=SHA.hashear(data);
        System.out.println("Data Before Encryption :" + data);
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

        System.out.println("----------------ENCRYPTION COMPLETED------------");
        return encryptedData;
    }
//
//    /**
//     * read Public Key From File
//     * @param fileName
//     * @return PublicKey
//     * @throws IOException
//     */
//    public PublicKey readPublicKeyFromFile(String fileName) throws IOException{
//        FileInputStream fis = null;
//        ObjectInputStream ois = null;
//        try {
//            fis = new FileInputStream(new File(fileName));
//            ois = new ObjectInputStream(fis);
//
//            BigInteger modulus = (BigInteger) ois.readObject();
//            BigInteger exponent = (BigInteger) ois.readObject();
//
//            //Get Public Key
//            RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, exponent);
//            KeyFactory fact = KeyFactory.getInstance("RSA");
//            PublicKey publicKey = fact.generatePublic(rsaPublicKeySpec);
//
//            return publicKey;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        finally{
//            if(ois != null){
//                ois.close();
//                if(fis != null){
//                    fis.close();
//                }
//            }
//        }
//        return null;

//    }
//    /**
//     * read Public Key From File
//     * @param fileName
//     * @return
//     * @throws IOException
//     */
//    public PrivateKey readPrivateKeyFromFile(String fileName) throws IOException{
//        FileInputStream fis = null;
//        ObjectInputStream ois = null;
//        try {
//            fis = new FileInputStream(new File(fileName));
//            ois = new ObjectInputStream(fis);
//
//            BigInteger modulus = (BigInteger) ois.readObject();
//            BigInteger exponent = (BigInteger) ois.readObject();
//
//            //Get Private Key
//            RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(modulus, exponent);
//            KeyFactory fact = KeyFactory.getInstance("RSA");
//            PrivateKey privateKey = fact.generatePrivate(rsaPrivateKeySpec);
//
//            return privateKey;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        finally{
//            if(ois != null){
//                ois.close();
//                if(fis != null){
//                    fis.close();
//                }
//            }
//        }
//        return null;
//    }
}