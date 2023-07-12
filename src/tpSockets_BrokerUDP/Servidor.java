package tpSockets_BrokerUDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {

    private HashMap<String /*nombreCanal*/, HashMap<Integer /*ip*/, Integer/*puerto*/>> canales;

    public Servidor(HashMap<String, HashMap<Integer, Integer>> canales) {
        this.canales = canales;
    }

    public HashMap<String, HashMap<Integer, Integer>> getCanales() {
        return canales;
    }

    public void setCanales(HashMap<String, HashMap<Integer, Integer>> canales) {
        this.canales = canales;
    }

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
                String mensajeConCanal = new String(peticion.getData());
                String mensaje="";
                for(int i=0;i<mensajeConCanal.subSequence(0,mensajeConCanal.indexOf("#")).length();i++){
                    mensaje=mensaje+mensajeConCanal.charAt(i);
                }
                System.out.println(mensaje);

                //Obtengo el puerto y la direccion de origen
                //Si no se quiere responder, no es necesario

                String canal="";
                for(int i=0;i<mensajeConCanal.subSequence(mensajeConCanal.indexOf("#")+1,mensajeConCanal.length()-1).length();i++){
                    canal=canal+mensajeConCanal.charAt(i);
                }
                if(canales){} // tengo q poner q agarre las direcciones y puertos de todos los q esten en el canal del msj.
                int puertoCliente = peticion.getPort();
                InetAddress direccion = peticion.getAddress();

                byte[] buffer1 = new byte[2048];
                String mensaje1 = "~acuse de recibo~";
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