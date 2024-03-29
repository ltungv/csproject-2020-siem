package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.EPUndeployException;
import vn.edu.vgu.jupiter.EPFacade;

import static vn.edu.vgu.jupiter.scan_alerts.PortScansAlertConfigurations.getEPConfiguration;

/**
 * This class compile the EPL statement for selecting packets of TCP connection to closed port
 * and deploy the compiled EPL to the runtime
 *
 * @author Vo Le Tung
 */
public class TcpPacketWithClosedPortStatement {
    private static final String eplTcpPacketToClosedPort =
            "@Name('TcpPacketWithClosedPort')\n" +
                    "insert into TcpPacketWithClosedPort\n" +
                    "select a.timestamp, a.tcpHeader, a.ipHeader from pattern [\n" +
                    "every a=TcpPacket(tcpHeader.syn = true and tcpHeader.ack = false) ->\n" +
                    "b=TcpPacket(\n" +
                    "   tcpHeader.rst = true and\n" +
                    "   ipHeader.srcAddr = a.ipHeader.dstAddr and\n" +
                    "   ipHeader.dstAddr = a.ipHeader.srcAddr and\n" +
                    "   tcpHeader.srcPort = a.tcpHeader.dstPort and\n" +
                    "   tcpHeader.dstPort = a.tcpHeader.srcPort\n" +
                    ")\n" +
                    "where timer:within(100 millisecond)\n" +
                    "]";

    private EPRuntime runtime;
    private EPStatement stmtTcpPacketToClosedPort;


    public TcpPacketWithClosedPortStatement(EPRuntime runtime) {
        this.runtime = runtime;
        stmtTcpPacketToClosedPort = EPFacade.compileDeploy(eplTcpPacketToClosedPort, runtime, getEPConfiguration());
    }

    /**
     * Undeploy all statements managed by the object.
     *
     * @throws EPUndeployException Exception while undeploying statements.
     */
    public void undeploy() throws EPUndeployException {
        runtime.getDeploymentService().undeploy(stmtTcpPacketToClosedPort.getDeploymentId());
    }
}
