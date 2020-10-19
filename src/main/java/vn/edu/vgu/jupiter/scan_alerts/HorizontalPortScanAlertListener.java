package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;
import org.pcap4j.packet.namednumber.Port;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A listener for the HorizontalPortScanAlert events
 * The new events are logged using the class Logger
 *
 * @author Pham Nguyen Thanh An
 */
public class HorizontalPortScanAlertListener implements UpdateListener {
    private static final Logger logger = LoggerFactory.getLogger(HorizontalPortScanAlertListener.class);
    private int countThreshold;
    private int count = 0;

    public HorizontalPortScanAlertListener(int countThreshold){
        this.countThreshold = countThreshold;
    }

    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
        if (newEvents == null) {
            return;
        }
        Long ts = (Long) newEvents[0].get("timestamp");
        Port hostPort = (Port) newEvents[0].get("hostPort");
        logger.info("[ts={}] POTENTIAL HORIZONTAL PORT SCAN ON PORT {}", ts, hostPort.valueAsInt());
        count++;
        if(count == countThreshold){ //warning
            logger.warn("HORIZONTAL PORT SCAN COUNT HAS REACHED {} times.", countThreshold);
            count = 0;
        }
    }
}
