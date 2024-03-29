# HORIZONTAL PORT SCAN DETECTION

The following diagram describes that event hierarchy that is used to alert to user about a potential horizontal port scan that is happening in the network.

```
    TcpPacketEvent
          |
          |
          v
TcpPacketToClosedPortEvent
          |			
          |			
          v			
HorizontalPortScanEvent
```

+ `TcpPacketEvent` will be produced whenever there is network packet that uses TCP protocol and is passed between the host address and some external address.

+ `TcpConnectionToClosedPortEvent` will be produced whenever a host is trying to send TCP packets to a closed port on another host.
	+ A TCP packet is determined to be sent to a closed port by detecting a pattern of packet transmission. The initial TCP packet (SYN) will be replied with a RST-packet, if it is sent to a closed port.

+ `HorizontalPortScanEvent` will be raised whenever the number of `TcpConnectionToClosedPortEvent` reaches a certain threshold of connections to a certain port and a set time window



