package Seguridad.Comunicacion;

import Seguridad.RSA;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {
//    private String canal;
//
//    public Cliente(String canal) {
//        this.canal = canal;
//    }
//
//    public String getCanal() {
//        return canal;
//    }
//
//    public void setCanal(String canal) {
//        this.canal = canal;
//    }

    public static void main(String[] args) throws SocketException {
        RSA rsaObj = new RSA();
        Scanner scanner = new Scanner(System.in);
        //puerto del servidor
        final int PUERTO_SERVIDOR = 5001;
        //buffer donde se almacenara los mensajes
        byte[] buffer = new byte[256];
        byte[] buffer1 = new byte[256];
        byte[] buffer2 = new byte[256];
        HashSet<String> topicoSubscriptos=new HashSet<>();
        DatagramSocket socketUDP = new DatagramSocket();
        InetAddress direccionServidor = null;


        try {
            direccionServidor = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }


        byte[] bufferClaves = new byte[256];

        while(true) {
            try {
                Thread hiloAck = new Thread(new ThreadCLiente(socketUDP));
                Thread hiloEscucha1 = new Thread(new ThreadCLiente(socketUDP));
                hiloAck.start();
                hiloEscucha1.start();
                //Obtengo la localizacion de localhost


                //Creo el socket de UDP


                // creo un hilo para recibir msgs
//                ThreadCliente hiloEscucha = new ThreadCliente(socketUDP);
//                hiloEscucha.start();

                String mensajeConCanal /*="¡hola!#futbol/"*/;
                mensajeConCanal = scanner.nextLine();
                if(mensajeConCanal.contains("#")) {
                    String topico="";
                    topico=mensajeConCanal.split("#")[1];
                    topico=topico.split("/")[0];
                    if(mensajeConCanal.contains("SubsTop#")){
                        topicoSubscriptos.add(topico);
                    }

                    //Convierto el mensaje a bytes
                    buffer = mensajeConCanal.getBytes(StandardCharsets.UTF_8);


                    //Creo un datagrama
                    DatagramPacket pregunta = new DatagramPacket(buffer, buffer.length, direccionServidor, PUERTO_SERVIDOR);

                    //Lo envío con send
                    System.out.println("Envío el datagrama");
                    socketUDP.send(pregunta);

                    //Preparo la respuesta
                    //DatagramPacket peticion = new DatagramPacket(buffer1, buffer1.length);

                   //Recibo la respuesta
                  // socketUDP.receive(peticion);
                   System.out.println("Recibo la confimación");




                    //Cojo los datos y lo muestro
                    //String mensaje1 = new String(peticion.getData());
                    //System.out.println(mensaje1);


                    if(!(mensajeConCanal.contains("SubsTop#"))){
                        for (String topiquito: topicoSubscriptos){
                            if (topiquito.equals(topico)) {

                               // DatagramPacket mensajeTopico = new DatagramPacket(buffer2, buffer2.length);
                             //   socketUDP.receive(mensajeTopico);
                                // muestro Mensaje Recibido
                             //   String mensaje2 = new String(mensajeTopico.getData());
                             //   System.out.println(mensaje2);
                            }
                        }

                    }

                    //cierro el socke
                    // socketUDP.close();
                } else {
                    System.out.println("Ingrese un mensaje con el formato correcto. El mismo sería: mensaje#tópico/");
                }
            } catch (SocketException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}