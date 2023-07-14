package tpSockets_BrokerUDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ThreadCliente extends Thread{
    private DatagramSocket socket;
    public ThreadCliente(DatagramSocket socket){
        super();
        this.socket= socket;
    }

    @Override
    public void run(){
        byte[] buffer = new byte[2048];
        try{
            while(true){
                //Preparo un posible mensaje a recibir
                DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);

                //Recibo la respuesta
                socket.receive(peticion);

                //Cojo los datos y lo muestro
                String mensaje = new String(peticion.getData());
                System.out.println(mensaje);

            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
