package tpSockets_BrokerUDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {

    private HashMap<String /*nombreCanal*/, HashSet<String /*ip:puerto*/>>canales;

    public Servidor() {
        canales= new HashMap<String,HashSet<String>>();
    }

    public Servidor(HashMap<String, HashSet<String>> canales) {
        this.canales = canales;
    }

    public HashMap<String, HashSet<String>> getCanales() {
        return canales;
    }

    public void setCanales(HashMap<String, HashSet<String>> canales) {
        this.canales = canales;
    }

    public static void main(String[] args) {

        Servidor servidor = new Servidor();
        HashSet<String> ipPuerto = new HashSet<>();
        ipPuerto.add("172.16.255.226:5000");
        servidor.getCanales().put("futbol", ipPuerto);



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
                for(int i=0;i<mensajeConCanal.subSequence(0,mensajeConCanal.indexOf("#")-1).length();i++){
                    mensaje=mensaje+mensajeConCanal.charAt(i);
                }
                System.out.println(mensaje);


                //Obtengo el puerto y la direccion de origen

                //Si no se quiere responder, no es necesario

                int puertoCliente = peticion.getPort();
                InetAddress direccion = peticion.getAddress();

                byte[] buffer1 = new byte[2048];
                String ack = "~acuse de recibo~";
                buffer1 = ack.getBytes();

                //creo el datagrama
                DatagramPacket respuesta = new DatagramPacket(buffer1, buffer1.length, direccion, puertoCliente);

                //Envio la información
                System.out.println("~respondí esto~");
                socketUDP.send(respuesta);
                System.out.println(ack);
                System.out.println();

                String canal="";
                for(int i=0;i<mensajeConCanal.subSequence(mensajeConCanal.indexOf("#"),mensajeConCanal.length()-1).length();i++) {
                    canal = canal + mensajeConCanal.charAt(i);
                }

                System.out.println("~Se reenvió el mensaje a estas IPs: ~");
                for(Map.Entry<String, HashSet<String>> canales : servidor.getCanales().entrySet()){
                    if (canales.getKey().equals(canal)){
                        for(String anna:canales.getValue()){
                            //String ip= (String) anna.subSequence(0, anna.indexOf(":")-1);
                            InetAddress ipSubscriptor = InetAddress.getByName((String) anna.subSequence(0, anna.indexOf(":")-1));
                            String puertoaux = (String) anna.subSequence(anna.indexOf(":"),anna.length()-1);
                            int puertoSubscriptor=Integer.parseInt(puertoaux);
                            byte[] bufferBroker = new byte[2048];
                            String mensajeReenviado = mensaje;
                            buffer1 = mensajeReenviado.getBytes();

                            //creo el datagrama
                            DatagramPacket paqueteBroker = new DatagramPacket(buffer1, buffer1.length, ipSubscriptor, puertoSubscriptor);

                            //Envio la información
                            socketUDP.send(paqueteBroker);
                            System.out.println(ipSubscriptor);
                        }
                    }
                }

            }

        } catch (SocketException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}