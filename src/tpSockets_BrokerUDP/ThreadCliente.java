package tpSockets_BrokerUDP;

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
        try{

        }
        catch (){}
    }
}
