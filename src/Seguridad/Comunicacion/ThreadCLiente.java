package Seguridad.Comunicacion;

import Seguridad.RSA;
import Seguridad.SHA;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.security.PrivateKey;
import java.security.PublicKey;

public class ThreadCLiente implements Runnable {

    private DatagramSocket socketUDP;
    private PrivateKey privadaCliente;

    public ThreadCLiente(DatagramSocket socketCliente, PrivateKey privadaCliente) {
        this.socketUDP = socketCliente;
        this.privadaCliente=privadaCliente;
    }

    public DatagramSocket getSocketUDP() {
        return socketUDP;
    }

    public void setSocketUDP(DatagramSocket socketUDP) {
        this.socketUDP = socketUDP;
    }

    public PrivateKey getPrivadaCliente() {
        return privadaCliente;
    }

    public void setPrivadaCliente(PrivateKey privadaCliente) {
        this.privadaCliente = privadaCliente;
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[256];
            DatagramPacket paqueteRecibo =new DatagramPacket(buffer,buffer.length);
            System.out.println("Entro hilo escucha");
            socketUDP.receive(paqueteRecibo);
            MensajeEncriptado mensajeEncriptado1 = (MensajeEncriptado) Cliente.convertBytesToObject(paqueteRecibo.getData()); //Convierto de tipo buffer a tipo MensajeEncriptado
            String respuesta=(RSA.decryptData(mensajeEncriptado1.getMensajeEncriptadoPublica(),privadaCliente));
            System.out.println("Respuesta del servidor: " + respuesta);

            if(ThreadCLiente.corroboracion(respuesta, mensajeEncriptado1.getMensajeHasheadoPrivada())){
                System.out.println("El mensaje que llegó está íntegro.");
            } else {
                System.out.println("Los datos han sido corrompidos en algún momento del envío.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Boolean corroboracion(String mensaje, byte[] mensajeHasheado){
        Boolean verificador=false;
        String mensajeHashDesencriptado = null;

        try {
            mensajeHashDesencriptado = RSA.decryptHashData(mensajeHasheado, Cliente.getPublicaServidor());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        if((SHA.hashear(mensaje)).equals(mensajeHashDesencriptado)){
            verificador=true;
        }

        return verificador;
    }
}