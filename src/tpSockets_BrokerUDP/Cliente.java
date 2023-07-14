import tpSockets_BrokerUDP.ThreadCliente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {
    private String canal;

    public Cliente(String canal) {
        this.canal = canal;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public static void main(String[] args) {

        //puerto del servidor
        final int PUERTO_SERVIDOR = 5000;
        //buffer donde se almacenara los mensajes
        byte[] buffer = new byte[2048];
        byte[] buffer1 = new byte[2048];

        try {
            //Obtengo la localizacion de localhost
            InetAddress direccionServidor = InetAddress.getByName("127.0.0.1");

            //Creo el socket de UDP
            DatagramSocket socketUDP = new DatagramSocket();
            String mensajeConCanal ="¡hola!#futbol";



            //Convierto el mensaje a bytes
            buffer = mensajeConCanal.getBytes();

            //Creo un datagrama
            DatagramPacket pregunta = new DatagramPacket(buffer, buffer.length, direccionServidor, PUERTO_SERVIDOR);

            //Lo envío con send
            System.out.println("Envío el datagrama");
            socketUDP.send(pregunta);

            //Preparo la respuesta
            DatagramPacket peticion = new DatagramPacket(buffer1, buffer1.length);

            //Recibo la respuesta
            socketUDP.receive(peticion);
            System.out.println("Recibo la confimación");

            //Cojo los datos y lo muestro
            String mensaje1 = new String(peticion.getData());
            System.out.println(mensaje1);

            //cierro el socket
            socketUDP.close();

        } catch (SocketException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}