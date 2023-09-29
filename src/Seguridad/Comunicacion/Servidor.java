package Seguridad.Comunicacion;

import Seguridad.RSA;
import Seguridad.SHA;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {
    private static HashMap<String /*nombreCanal*/, HashMap<String /*ip:puerto*/, PublicKey>>canales=new HashMap<>();
    private static HashMap<String /*ip:puerto*/, PublicKey> clientes=new HashMap<>();

    public static HashMap<String, HashMap<String, PublicKey>> getCanales() {
        return canales;
    }

    public static void setCanales(HashMap<String, HashMap<String, PublicKey>> canales) {
        Servidor.canales = canales;
    }

    public static HashMap<String, PublicKey> getClientes() {
        return clientes;
    }

    public static void setClientes(HashMap<String, PublicKey> clientes) {
        Servidor.clientes = clientes;
    }

    public static void main(String[] args) {
        PublicKey publicKey;
        PrivateKey privateKey;
        KeyPair keyPair=RSA.RSA();
        privateKey=keyPair.getPrivate();
        publicKey=keyPair.getPublic();


        final int PUERTO = 5001;
        //  byte[] buffer = new byte[256];


        try {

            System.out.println("Iniciando el servidor UDP");
            //Creacion del socket
            DatagramSocket socketUDP = new DatagramSocket(PUERTO);

            //Siempre atendera peticiones
            while (true) {
                byte[] buffer = new byte[2048];
                //Preparo la respuesta
                DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);

                //Recibo el datagrama
                socketUDP.receive(peticion);

                String conjuntoIpPuerto=peticion.getAddress().toString()+":"+peticion.getPort();


                if(Servidor.clientes.containsKey(conjuntoIpPuerto)){

                    MensajeEncriptado mensajeEncriptado1 = (MensajeEncriptado) Cliente.convertBytesToObject(peticion.getData());
                    String mensajeConCanal=(RSA.decryptData(MensajeEncriptado.reconvertirBuffer(mensajeEncriptado1.getMensajeEncriptadoPublica()),privateKey)); // DESENCRIPTO MENSAJE

                    System.out.println("~me llega lo siguiente~");


                    if(mensajeConCanal.contains("SubsTop#")){
                        String topico="";
                        topico=mensajeConCanal.split("#")[1];
                        topico=topico.split("/")[0];

                        System.out.println("SubsTop#" + topico);

                        if(Servidor.getCanales().containsKey(topico)) {
                            for(Map.Entry<String,PublicKey> cliente: clientes.entrySet()){
                                if(conjuntoIpPuerto.equals(cliente.getKey())){
                                    Servidor.getCanales().get(topico).put("/"+cliente.getKey(), cliente.getValue());
                                }
                            }
                        } else{
                            HashMap<String, PublicKey>auxiliar=new HashMap<>();
                            for(Map.Entry<String,PublicKey> cliente: clientes.entrySet()){
                                if(conjuntoIpPuerto.equals(cliente.getKey())){
                                    auxiliar.put("/" + conjuntoIpPuerto, cliente.getValue());
                                }
                            }
                            Servidor.getCanales().put(topico,auxiliar);
                        }
                       // para probar que suscriba bien
                        int puertoCliente = peticion.getPort();
                        InetAddress direccion = peticion.getAddress();

                        byte[] buffer1 = new byte[2048];

                        String ack = "ACK";
                        for(Map.Entry<String,PublicKey> cliente: clientes.entrySet()){
                            if(conjuntoIpPuerto.equals(cliente.getKey())){
                                buffer1 = RSA.encryptData(ack,cliente.getValue());
                            }
                        }
                        byte[] buffer2 = RSA.signData(ack,privateKey);

                        // CAMBIO DE LUGAR LOS BYTES EN BUUFFER 1 POR el 2
                        MensajeEncriptado mensajeEncriptado=new MensajeEncriptado(Base64.getEncoder().encodeToString(buffer2),Base64.getEncoder().encodeToString(buffer1));

                        //creo el datagrama
                        DatagramPacket respuesta = new DatagramPacket(Cliente.convertObjectToBytes(mensajeEncriptado), Cliente.convertObjectToBytes(mensajeEncriptado).length, direccion, puertoCliente);

                        //Envio la información
                        System.out.println("~respondí esto~");
                        socketUDP.send(respuesta);
                        System.out.println(ack);
                        System.out.println();

                    }
                    else {
                        String mensaje="";

                        for (int i = 0; i < mensajeConCanal.subSequence(0, mensajeConCanal.indexOf("#")).length(); i++) {
                            mensaje = mensaje + mensajeConCanal.charAt(i);
                        }
                        System.out.println(mensaje);
                        //Obtengo el puerto y la direccion de origen

                        int puertoCliente = peticion.getPort();
                        InetAddress direccion = peticion.getAddress();



                        byte[] buffer1 = new byte[2048];

                        String ack = "ACK";
                        for(Map.Entry<String,PublicKey> cliente: clientes.entrySet()){
                            if(conjuntoIpPuerto.equals(cliente.getKey())){
                                buffer1 = RSA.encryptData(ack,cliente.getValue());
                            }
                        }
                        byte[] buffer2 = RSA.signData(ack,privateKey);
                        MensajeEncriptado mensajeEncriptado=new MensajeEncriptado(Base64.getEncoder().encodeToString(buffer2),Base64.getEncoder().encodeToString(buffer1));

                        byte[] mensajeEncriptadocompleto=Cliente.convertObjectToBytes(mensajeEncriptado);

                        //creo el datagrama
                        DatagramPacket respuesta = new DatagramPacket(mensajeEncriptadocompleto, mensajeEncriptadocompleto.length, direccion, puertoCliente);

                        //Envio la información
                        System.out.println("~respondí esto~");
                        socketUDP.send(respuesta);
                        System.out.println(ack);
                        System.out.println();

                        String canal = "";

                        canal=mensajeConCanal.split("#")[1];
                        canal=canal.split("/")[0];
                        System.out.println("~Se reenvió el mensaje a estas IPs: ~");
                        for (Map.Entry<String, HashMap<String, PublicKey>> canals : Servidor.getCanales().entrySet()) {
                            if (canals.getKey().equals(canal)) {
                                for (Map.Entry<String,PublicKey> anna:canals.getValue().entrySet()) {

                                    String ip = anna.getKey().split(":")[0];
                                    ip=ip.substring(1);
                                    ip=ip.substring(1);
                                    String puerto = anna.getKey().split(":")[1];



                                    byte [] bufferComprobacion = RSA.signData(SHA.hashear(mensaje),privateKey);
                                    byte [] bufferEncriptacion = RSA.encryptData(mensaje,anna.getValue());

                                    MensajeEncriptado mensajeCliente= new MensajeEncriptado(Base64.getEncoder().encodeToString(bufferComprobacion), Base64.getEncoder().encodeToString(bufferEncriptacion));
                                    byte [] mensajePadre =Cliente.convertObjectToBytes(mensajeCliente);



                                    //creo el datagrama
                                    DatagramPacket paqueteBroker = new DatagramPacket(mensajePadre, mensajePadre.length, InetAddress.getByName(ip),Integer.valueOf(puerto));

                                    //Envio la información
                                    socketUDP.send(paqueteBroker);
                                    System.out.println(anna.getKey().toString());

                                }

                            }
                        }
                    }
                }else{
                    PublicKey publicaCliente;
                    KeyFactory kf = KeyFactory.getInstance("RSA");
                    publicaCliente =  kf.generatePublic(new X509EncodedKeySpec(peticion.getData()));

                    System.out.println("Se conectacto el siguiente cliente : " + conjuntoIpPuerto);

                    byte[] bufferClaves = publicKey.getEncoded();
                    int puerto = Integer.parseInt(conjuntoIpPuerto.split(":")[1]);
                    conjuntoIpPuerto=conjuntoIpPuerto.substring(1);
                    InetAddress ip= InetAddress.getByName(conjuntoIpPuerto.split(":")[0]);

                    DatagramPacket envioClaves = new DatagramPacket(bufferClaves, bufferClaves.length,ip, puerto);
                    socketUDP.send(envioClaves);
                    conjuntoIpPuerto="/"+conjuntoIpPuerto;
                    Servidor.clientes.put(conjuntoIpPuerto,publicaCliente);

                }
            }
        } catch (SocketException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

    }

}