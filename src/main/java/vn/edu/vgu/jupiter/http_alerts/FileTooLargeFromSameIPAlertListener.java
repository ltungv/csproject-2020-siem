package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A simple listener for httpFileTooLargeFromSameIPAlert Event
 * <p>
 * The information of the new events is logged to the system using the class's logger
 *
 * @author Bui Xuan Phuoc
 */
public class FileTooLargeFromSameIPAlertListener implements UpdateListener {
    private static final Logger log = LoggerFactory.getLogger(FileTooLargeFromSameIPAlertListener.class);

    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
        if (newEvents == null) {
            return; // ignore old events for events leaving the window
        }
        String IPAddress = (String) newEvents[0].get("IPAddress");
        log.info("Attempts to send large entity from the same IP address {} detected", IPAddress);
    }
}
