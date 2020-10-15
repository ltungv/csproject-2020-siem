package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.runtime.client.EPRuntime;

/**
 * This class compile the EPL statement for selecting packets of TCP connection to closed port
 * and deploy the compiled EPL to the runtime
 *
 * @author Vo Le Tung
 */
public class TcpPacketWithClosedPortStatement {
    private static final String filterClosedPortStmt =
            "insert into TcpPacketWithClosedPortEvent\n" +
                    "select a.timestamp, a.tcpHeader, a.ipHeader from pattern [\n" +
                    "every a=TcpPacketEvent(tcpHeader.syn = true and tcpHeader.ack = false) ->\n" +
                    "b=TcpPacketEvent(\n" +
                    "   tcpHeader.rst = true and\n" +
                    "   ipHeader.srcAddr = a.ipHeader.dstAddr and\n" +
                    "   ipHeader.dstAddr = a.ipHeader.srcAddr and\n" +
                    "   tcpHeader.srcPort = a.tcpHeader.dstPort and\n" +
                    "   tcpHeader.dstPort = a.tcpHeader.srcPort\n" +
                    ")\n" +
                    "where timer:within(100 millisecond)\n" +
                    "]";

    public TcpPacketWithClosedPortStatement(EPRuntime runtime) {
        PortScansAlertUtil.compileDeploy(filterClosedPortStmt, runtime);
    }
}