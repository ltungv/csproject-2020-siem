package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A listener for ConsecutiveFileTooLargeAlertStatement, prioritize the alert by a threshold
 *
 * @author Bui Xuan Phuoc
 */
public class ConsecutiveFileTooLargeAlertListener implements UpdateListener {
    private static final Logger log = LoggerFactory.getLogger(ConsecutiveFailedLoginsAlertListener.class);

    private long highPriorityThreshold;

    public ConsecutiveFileTooLargeAlertListener(long highPriorityThreshold) {
        this.highPriorityThreshold = highPriorityThreshold;
    }

    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
        if (newEvents == null) {
            return; // ignore old events for events leaving the window
        }
        Long count = (Long) newEvents[0].get("failuresCount");
        if (count < highPriorityThreshold) {
            log.info("LOW PRIORITY: Consecutive file too large is being posted in short time frame detected");
        } else {
            log.warn("HIGH PRIORITY: Consecutive file too large is being posted in short time frame detected");
        }
    }
}
