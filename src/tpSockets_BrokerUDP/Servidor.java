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
        //HashSet<String> ipPuerto = new HashSet<>();
        //ipPuerto.add("127.0.0.1:5565");
        //servidor.getCanales().put("futbol", ipPuerto);
        // eso es solo para llenar con algo


        final int PUERTO = 5001;
        //  byte[] buffer = new byte[256];

        try {

            System.out.println("Iniciando el servidor UDP");
            //Creacion del socket
            DatagramSocket socketUDP = new DatagramSocket(PUERTO);

            //Siempre atendera peticiones
            while (true) {
                byte[] buffer = new byte[256];
                //Preparo la respuesta
                DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);

                //Recibo el datagrama
                socketUDP.receive(peticion);


                System.out.println("~me llega lo siguiente~");

                //Convierto lo recibido y mostrar el mensaje
                String mensajeConCanal = new String(peticion.getData());


                if(mensajeConCanal.contains("SubsTop#")){
                    String topico="";
                    String ipMasPuerto="";
                    topico=mensajeConCanal.split("#")[1];
                    topico=topico.split("/")[0];
//                    for (int i = mensajeConCanal.indexOf("#"); i < mensajeConCanal.subSequence(mensajeConCanal.indexOf("#"), mensajeConCanal.length()-1).length(); i++) {
//                        topico = topico + mensajeConCanal.charAt(i);
//                    }
                    System.out.println("SubsTop#" + topico);
                    ipMasPuerto = peticion.getAddress().toString() + ":" + peticion.getPort();
                    ipMasPuerto=ipMasPuerto.substring(1, ipMasPuerto.length()-1);
                    //System.out.println(ipMasPuerto);
                    if(servidor.getCanales().containsKey(topico)) {
                        servidor.getCanales().get(topico).add(ipMasPuerto);
                    } else{
                        HashSet<String>auxiliar=new HashSet<>();
                        auxiliar.add(ipMasPuerto);
                        servidor.getCanales().put(topico,auxiliar);
                    }
                    /*for(Map.Entry<String, HashSet<String>> canales : servidor.getCanales().entrySet()){
                        if(canales.getKey().equals(topico)){
                            for (String suscrpitor: canales.getValue()) {
                                System.out.println(suscrpitor);
                            }
                        }

                    }*/ // para probar que suscriba bien
                    int puertoCliente = peticion.getPort();
                    InetAddress direccion = peticion.getAddress();

                    byte[] buffer1 = new byte[256];
                    String ack = "ACK";
                    buffer1 = ack.getBytes();

                    //creo el datagrama
                    DatagramPacket respuesta = new DatagramPacket(buffer1, buffer1.length, direccion, puertoCliente);

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
                    //Si no se quiere responder, no es necesario

                    int puertoCliente = peticion.getPort();
                    InetAddress direccion = peticion.getAddress();

                    byte[] buffer1 = new byte[256];
                    String ack = "ACK";
                    buffer1 = ack.getBytes();

                    //creo el datagrama
                    DatagramPacket respuesta = new DatagramPacket(buffer1, buffer1.length, direccion, puertoCliente);

                    //Envio la información
                    System.out.println("~respondí esto~");
                    socketUDP.send(respuesta);
                    System.out.println(ack);
                    System.out.println();

                    String canal = "";
                    //                  for (int i = 0; i < mensajeConCanal.subSequence(mensajeConCanal.indexOf("#"), mensajeConCanal.length() - 1).length(); i++) {
                    //                    canal = canal + mensajeConCanal.charAt(i);
                    //              }
                    canal=mensajeConCanal.split("#")[1];
                    canal=canal.split("/")[0];

                    System.out.println("~Se reenvió el mensaje a estas IPs: ~");
                    for (Map.Entry<String, HashSet<String>> canales : servidor.getCanales().entrySet()) {
                        if (canales.getKey().equals(canal)) {
                            for (String anna : canales.getValue()) {
                                //String ip= (String) anna.subSequence(0, anna.indexOf(":")-1);
//                                InetAddress ipSubscriptor = InetAddress.getByName((String) anna.subSequence(0, anna.indexOf(":")));
                                InetAddress ipSubscriptor = InetAddress.getByName((String) anna.split(":")[0]);
//                                String puertoaux = (String) anna.subSequence(anna.indexOf(":"), anna.length() - 1);
                                String puertoaux=anna.split(":")[1];
                                int puertoSubscriptor = Integer.parseInt(puertoaux);
                                byte[] bufferBroker = new byte[256];
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
            }

        } catch (SocketException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
