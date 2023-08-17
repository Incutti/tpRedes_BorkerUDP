package tpSockets_BrokerUDP;

import tpSockets_BrokerUDP.ThreadCliente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {
    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;
    private byte[] buffer;

    public Cliente(DatagramSocket datagramSocket, InetAddress inetAddress) {
        this.datagramSocket = datagramSocket;
        this.inetAddress = inetAddress;
    }

    public DatagramSocket getDatagramSocket() {
        return datagramSocket;
    }

    public void setDatagramSocket(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public void mandarRecibir() {
        Scanner entrada = new Scanner(System.in);

        try {
            Thread recibir = new Thread(() -> {
                try {
                    while (true) {
                        byte[] buffer2 = new byte[1024];
                        DatagramPacket datagramPacket2 = new DatagramPacket(buffer2, buffer2.length);
                        datagramSocket.receive(datagramPacket2);
                        String recibido = new String(datagramPacket2.getData(), 0, datagramPacket2.getLength());
                        System.out.println(recibido);
                        Arrays.fill(buffer, (byte) 0);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            recibir.start();

            while (true) {
                String mensaje = entrada.nextLine();
                buffer = mensaje.getBytes();
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, 5001); // 5001 es el numero de puerto del servidor
                datagramSocket.send(datagramPacket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


// de aca para arriba es de juli, a ver si algo nos servia pq ademas hicimos las cosas directo en el main

// de aca para abajo es lo nuestro; anda casitodo menos la parte de recibir mensajes de otros clientes ademas de q está bastante desordenado




    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        //puerto del servidor
        final int PUERTO_SERVIDOR = 5001;
        //buffer donde se almacenara los mensajes
        byte[] buffer  = new byte[256];
        byte[] buffer1 = new byte[256];
        byte[] buffer2 = new byte[256];


        while(true) {
            try {

                HashSet<String> topicoSubscriptos=new HashSet<>();
                //Obtengo la localizacion de localhost
                InetAddress direccionServidor = InetAddress.getByName("127.0.0.1");

                //Creo el socket de UDP
                DatagramSocket socketUDP = new DatagramSocket();
                String mensajeConCanal /*="¡hola!#futbol/"*/;
                mensajeConCanal = scanner.nextLine();
                String topico="";
                topico=mensajeConCanal.split("#")[1];
                topico=topico.split("/")[0];

                if(mensajeConCanal.contains("SubsTop#")){
                    topicoSubscriptos.add(topico);
                }

                if(mensajeConCanal.contains("#")) {
                    //Convierto el mensaje a bytes
                    buffer = mensajeConCanal.getBytes(StandardCharsets.UTF_8);

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


                    // esto no hace nada
                    /*if(!(mensajeConCanal.contains("SubsTop#"))){
                        for (String topiquito: topicoSubscriptos){
                            if (topiquito.equals(topico)) {
                                DatagramPacket mensajeTopico = new DatagramPacket(buffer2, buffer2.length);
                                socketUDP.receive(mensajeTopico);

                                // muestro Mensaje Recibido
                                String mensaje2 = new String(mensajeTopico.getData());
                                System.out.println(mensaje2);
                            }
                        }
                    }*/


                    //cierro el socket
                    socketUDP.close();
                } else {
                    System.out.println("Ingrese un mensaje con el formato correcto. El mismo sería: mensaje#tópico");
                }
            } catch (SocketException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnknownHostException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}