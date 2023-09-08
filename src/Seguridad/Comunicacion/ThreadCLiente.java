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

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[256];
            DatagramPacket paqueteRecibo =new DatagramPacket(buffer,buffer.length);
            System.out.println("Entro hilo escucha");
            socketUDP.receive(paqueteRecibo);
            String respuesta=(RSA.decryptData(paqueteRecibo.getData(),privadaCliente));
            System.out.println("Respuesta del servidor: " + respuesta);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String corroboracion(String mensaje){
        return SHA.hashear(mensaje); //falta compararlo con el mensaje hasheado
    }
}