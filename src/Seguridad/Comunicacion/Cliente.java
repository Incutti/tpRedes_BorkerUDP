package Seguridad.Comunicacion;

import Seguridad.RSA;

import java.io.*;
import java.net.*;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashSet;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {
    private static PublicKey publicaServidor;

    public static PublicKey getPublicaServidor() {
        return publicaServidor;
    }

    public static void setPublicaServidor(PublicKey publicaServidor) {
        Cliente.publicaServidor = publicaServidor;
    }

    public static byte[] convertObjectToBytes(MensajeEncriptado mensaje) {
       ByteArrayOutputStream boas = new ByteArrayOutputStream();
       try (ObjectOutputStream ois = new ObjectOutputStream(boas)) {
           ois.writeObject(mensaje);
           return boas.toByteArray();
       } catch (IOException ioe) {
           ioe.printStackTrace();
       }
       throw new RuntimeException();
    }

    public static Object convertBytesToObject(byte[] bytes) {
        InputStream is = new ByteArrayInputStream(bytes);
        try (ObjectInputStream ois = new ObjectInputStream(is)) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException ioe) {
            ioe.printStackTrace();
        }
        throw new RuntimeException();
    }


    public static void main(String[] args) throws IOException {
        PublicKey publicKey;
        PrivateKey privateKey;
        KeyPair keyPair=RSA.RSA();
        privateKey=keyPair.getPrivate();
        publicKey=keyPair.getPublic();

        Scanner scanner = new Scanner(System.in);
        final int PUERTO_SERVIDOR = 5001;//puerto del servidor
        //buffers donde se almacenara a los mensajes
//        byte[] buffer = new byte[256];
        byte[] buffer1 = new byte[2048];
        byte[] buffer2 = new byte[2048];
        HashSet<String> topicoSubscriptos=new HashSet<>();
        DatagramSocket socketUDP = new DatagramSocket();
        InetAddress direccionServidor = null;


        try {
            direccionServidor = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }


        byte[] bufferClaves = publicKey.getEncoded();
        DatagramPacket envioClaves = new DatagramPacket(bufferClaves, bufferClaves.length, direccionServidor, PUERTO_SERVIDOR);
        socketUDP.send(envioClaves);


        DatagramPacket reciboClaveServer = new DatagramPacket(buffer1, buffer1.length);
        socketUDP.receive(reciboClaveServer);
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            publicaServidor =  kf.generatePublic(new X509EncodedKeySpec(reciboClaveServer.getData()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }


        while(true) {
            try {
                // creo un hilo para recibir msgs
                Thread hiloAck = new Thread(new ThreadCLiente(socketUDP,privateKey));
                Thread hiloEscucha1 = new Thread(new ThreadCLiente(socketUDP,privateKey));
                hiloAck.start();
                hiloEscucha1.start();
                //Obtengo la localizacion de localhost


                //Creo el socket de UDP

                String mensajeConCanal /*="¡hola!#futbol/"*/;
                mensajeConCanal = scanner.nextLine();

                /*PARTE FIRMA*/
                byte[] bufferFirma=RSA.signData(mensajeConCanal,privateKey);
                byte[] buffer = RSA.encryptData(mensajeConCanal, publicaServidor);

                MensajeEncriptado mensajeEncriptado = new MensajeEncriptado(Base64.getEncoder().encodeToString(bufferFirma), Base64.getEncoder().encodeToString(buffer));
                // EN LA LINEA DE ABAJO GUARDO ESTA VARIABLE COMO BYTE[]
                byte[] bufferEncriptadoCompleto = convertObjectToBytes(mensajeEncriptado);

                // CONVERTIR DATOS DEL BUFFER A OBJETO
                //MensajeEncriptado mensajeEncriptado1 = (MensajeEncriptado) convertBytesToObject(bufferEncriptadoCompleto);

                if(mensajeConCanal.contains("#")) {
                    String topico="";
                    topico=mensajeConCanal.split("#")[1];
                    topico=topico.split("/")[0];
                    if(mensajeConCanal.contains("SubsTop#")){
                        topicoSubscriptos.add(topico);
                    }



                    //Creo un datagrama
                    DatagramPacket pregunta = new DatagramPacket(bufferEncriptadoCompleto, bufferEncriptadoCompleto.length, direccionServidor, PUERTO_SERVIDOR);

                    //Lo envío con send
                    System.out.println("Envío el datagrama");
                    socketUDP.send(pregunta);


                } else {
                    System.out.println("Ingrese un mensaje con el formato correcto. El mismo sería: mensaje#tópico/");
                }
            } catch (SocketException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}