package vn.edu.vgu.jupiter.scan_alerts;

/**
 * Classes that contain parameters for raising port scan alerts.
 *
 * @author Vo Le tung
 */
public class PortScansAlertConfigurations {
    private VerticalScan verticalScan;
    private HorizontalScan horizontalScan;
    private BlockScan blockScan;

    public PortScansAlertConfigurations(VerticalScan verticalScan, HorizontalScan horizontalScan, BlockScan blockScan) {
        this.verticalScan = verticalScan;
        this.horizontalScan = horizontalScan;
        this.blockScan = blockScan;
    }

    public VerticalScan getVerticalScan() {
        return verticalScan;
    }

    public HorizontalScan getHorizontalScan() {
        return horizontalScan;
    }

    public BlockScan getBlockScan() {
        return blockScan;
    }

    /**
     * Contains paramters that are common among different types of alerts
     *
     * @author Vo Le Tung
     */
    public static abstract class GeneralScan {
        private int timeWindow;
        private int alertInterval;
        private int highPriorityThreshold;

        /**
         * Setting parameters for port scan alert
         *
         * @param timeWindow            in seconds, the sliding time window of events to be considered
         * @param alertInterval         in seconds, the interval of alert messages raising to the user
         * @param highPriorityThreshold threshold for classifying a high priority event
         */
        public GeneralScan(int timeWindow, int alertInterval, int highPriorityThreshold) {
            this.timeWindow = timeWindow;
            this.alertInterval = alertInterval;
            this.highPriorityThreshold = highPriorityThreshold;
        }

        public int getTimeWindow() {
            return timeWindow;
        }

        public int getAlertInterval() {
            return alertInterval;
        }

        public int getHighPriorityThreshold() {
            return highPriorityThreshold;
        }
    }

    /**
     * Contains parameters for raising vertical port scan alerts
     *
     * @author Vo Le Tung
     */
    public static class VerticalScan extends GeneralScan {
        private int connectionsCountThreshold;

        /**
         * @param timeWindow                in seconds, the sliding time window of events to be considered
         * @param alertInterval             in seconds, the interval of alert messages raising to the user
         * @param highPriorityThreshold     threshold for classifying a high priority event
         * @param connectionsCountThreshold threshold for the number of connections that can happen
         */
        public VerticalScan(int timeWindow, int alertInterval, int highPriorityThreshold, int connectionsCountThreshold) {
            super(timeWindow, alertInterval, highPriorityThreshold);
            this.connectionsCountThreshold = connectionsCountThreshold;
        }

        public int getConnectionsCountThreshold() {
            return connectionsCountThreshold;
        }
    }

    /**
     * Contains parameters for raising horizontal port scan alerts
     *
     * @author Vo Le Tung
     */
    public static class HorizontalScan extends GeneralScan {
        private int connectionsCountThreshold;

        /**
         * @param timeWindow                in seconds, the sliding time window of events to be considered
         * @param alertInterval             in seconds, the interval of alert messages raising to the user
         * @param highPriorityThreshold     threshold for classifying a high priority event
         * @param connectionsCountThreshold threshold for the number of connections that can happen
         */
        public HorizontalScan(int timeWindow, int alertInterval, int highPriorityThreshold, int connectionsCountThreshold) {
            super(timeWindow, alertInterval, highPriorityThreshold);
            this.connectionsCountThreshold = connectionsCountThreshold;
        }

        public int getConnectionsCountThreshold() {
            return connectionsCountThreshold;
        }
    }

    /**
     * Contains parameters for raising block port scan alerts
     *
     * @author Vo Le Tung
     */
    public static class BlockScan extends GeneralScan {
        private int portsCountThreshold;
        private int addressesCountThreshold;

        /**
         * @param timeWindow              in seconds, the sliding time window of events to be considered
         * @param alertInterval           in seconds, the interval of alert messages raising to the user
         * @param highPriorityThreshold   threshold for classifying a high priority event
         * @param portsCountThreshold     threshold for the number of distinct ports accesses that can happen on an address
         * @param addressesCountThreshold threshold for the number of addresses that were accesses whose ports count passes the threshold
         */
        public BlockScan(int timeWindow, int alertInterval, int highPriorityThreshold, int portsCountThreshold, int addressesCountThreshold) {
            super(timeWindow, alertInterval, highPriorityThreshold);
            this.portsCountThreshold = portsCountThreshold;
            this.addressesCountThreshold = addressesCountThreshold;
        }

        public int getPortsCountThreshold() {
            return portsCountThreshold;
        }

        public int getAddressesCountThreshold() {
            return addressesCountThreshold;
        }
    }

}
