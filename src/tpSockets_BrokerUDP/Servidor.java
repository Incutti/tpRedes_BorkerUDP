package tpSockets_BrokerUDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {

    public static void main(String[] args) {

        final int PUERTO = 5000;
        byte[] buffer = new byte[2048];

        try {
            System.out.println("Iniciando el servidor UDP");
            //Creacion del socket
            DatagramSocket socketUDP = new DatagramSocket(PUERTO);

            //Siempre atendera peticiones
            while (true) {

                //Preparo la respuesta
                DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);

                //Recibo el datagrama
                socketUDP.receive(peticion);
                System.out.println("~me llega lo siguiente~");

                //Convierto lo recibido y mostrar el mensaje
                String mensaje = new String(peticion.getData());
                System.out.println(mensaje);

                //Obtengo el puerto y la direccion de origen
                //Sino se quiere responder, no es necesario
                int puertoCliente = peticion.getPort();
                InetAddress direccion = peticion.getAddress();

                byte[] buffer1 = new byte[2048];
                String mensaje1 = "~recibido~";
                buffer1 = mensaje1.getBytes();

                //creo el datagrama
                DatagramPacket respuesta = new DatagramPacket(buffer1, buffer1.length, direccion, puertoCliente);

                //Envio la información
                System.out.println("~respondí esto~");
                socketUDP.send(respuesta);
                System.out.println(mensaje1);
                System.out.println();

            }

        } catch (SocketException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}