package Seguridad.Comunicacion;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ThreadCLiente implements Runnable {

    private DatagramSocket socketUDP;

    public ThreadCLiente(DatagramSocket socketCliente) {
        this.socketUDP = socketCliente;
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[256];
            DatagramPacket paqueteRecibo =new DatagramPacket(buffer,buffer.length);
            System.out.println("Entro hilo escucha");
            socketUDP.receive(paqueteRecibo);
            String respuesta;
            respuesta=new String(paqueteRecibo.getData());

            System.out.println("Respuesta del servidor: " + respuesta);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}