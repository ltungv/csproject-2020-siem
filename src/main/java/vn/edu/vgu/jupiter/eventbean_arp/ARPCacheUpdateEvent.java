package vn.edu.vgu.jupiter.eventbean_arp;

public class ARPCacheUpdateEvent {
    String IP;
    String MAC;
    String time;

    public ARPCacheUpdateEvent(String IP, String MAC, String time) {
        this.IP = IP;
        this.MAC = MAC;
        this.time = time;
    }

    public String getIP() {
        return IP;
    }

    public String getMAC() {
        return MAC;
    }

    public String getTime() {
        return time;
    }
}
