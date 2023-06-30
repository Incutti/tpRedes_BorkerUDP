package tpSockets_BrokerUDP;

import java.net.DatagramPacket;
import java.util.HashSet;

public class DatagramPacketSubscripto {
    private DatagramPacket dp;
    private HashSet<String> canales;
    public DatagramPacketSubscripto(byte[] bytes, int i){
        dp = new DatagramPacket(bytes, i);
        canales=new HashSet<>();
    }

    public DatagramPacket getDp() {
        return dp;
    }

    public void setDp(DatagramPacket dp) {
        this.dp = dp;
    }

    public HashSet<String> getCanales() {
        return canales;
    }

    public void setCanales(HashSet<String> canales) {
        this.canales = canales;
    }
}
