package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.common.client.EventSender;
import com.espertech.esper.common.client.module.ParseException;
import com.espertech.esper.compiler.client.EPCompileException;
import com.espertech.esper.runtime.client.EPDeployException;
import org.pcap4j.core.*;

import java.io.IOException;

public class Demo {
    private static final String COUNT_KEY = Demo.class.getName() + ".count";
    private static final int COUNT = Integer.getInteger(COUNT_KEY, -1);

    private static final String READ_TIMEOUT_KEY = Demo.class.getName() + ".readTimeout";
    private static final int READ_TIMEOUT = Integer.getInteger(READ_TIMEOUT_KEY, 100); // [ms]

    private static final String SNAPLEN_KEY = Demo.class.getName() + ".snaplen";
    private static final int SNAPLEN = Integer.getInteger(SNAPLEN_KEY, 65536); // [bytes]

    public static void main(String[] args) throws PcapNativeException, NotOpenException, EPCompileException, EPDeployException, ParseException, IOException {
        String devName = args.length > 0 ? args[0] : "";
        String filter = args.length > 1 ? args[1] : "";

        System.out.println(COUNT_KEY + ": " + COUNT);
        System.out.println(READ_TIMEOUT_KEY + ": " + READ_TIMEOUT);
        System.out.println(SNAPLEN_KEY + ": " + SNAPLEN);
        System.out.println("\n");

        PcapNetworkInterface nif = Pcaps.getDevByName(devName);
        System.out.println(nif.getName() + "(" + nif.getDescription() + ")");

        final PcapHandle handle = nif.openLive(SNAPLEN, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, READ_TIMEOUT);
        if (filter.length() != 0) {
            handle.setFilter(filter, BpfProgram.BpfCompileMode.OPTIMIZE);
        }

        VerticalPortScan service = new VerticalPortScan();
        service.deploy(100, 1);
        EventSender evtSender = service.getRawEventSender();

        try {
            handle.loop(COUNT, new TcpPacketListener(evtSender));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        handle.close();
    }
}
