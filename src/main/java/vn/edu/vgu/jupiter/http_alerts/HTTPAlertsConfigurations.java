package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.common.client.configuration.Configuration;
import vn.edu.vgu.jupiter.http_alerts.eventbean.*;

/**
 * Sets of parameters for raising http alerts.
 *
 * @author Vo Le Tung
 */
public class HTTPAlertsConfigurations {

    private FailedLogin failedLogin;
    private FailedLoginFromSameIP failedLoginFromSameIP;
    private FailedLoginSameUserID failedLoginSameUserID;

    private FileTooLarge fileTooLarge;
    private FileTooLargeSameUserID fileTooLargeSameUserID;
    private FileTooLargeFromSameIP fileTooLargeFromSameIP;

    /**
     * Make and returns that default configuration for Esper
     *
     * @return the configuration
     */
    protected static Configuration getEPConfiguration() {
        Configuration configuration = new Configuration();
        configuration.getCommon().addEventType(HTTPLog.class);

        configuration.getCommon().addEventType(HTTPFailedLogin.class);
        configuration.getCommon().addEventType(ConsecutiveFailedLoginsFromSameIPAlert.class);
        configuration.getCommon().addEventType(ConsecutiveFailedLoginsAlert.class);
        configuration.getCommon().addEventType(ConsecutiveFailedLoginsSameUserIDAlert.class);

        configuration.getCommon().addEventType(HTTPFileTooLarge.class);
        configuration.getCommon().addEventType(ConsecutiveFileTooLargeFromSameIPAlert.class);
        configuration.getCommon().addEventType(ConsecutiveFileTooLargeSameUserIDAlert.class);
        configuration.getCommon().addEventType(ConsecutiveFileTooLargeAlert.class);

        configuration.getRuntime().getLogging().setEnableExecutionDebug(false);
        configuration.getRuntime().getLogging().setEnableTimerDebug(false);
        configuration.getRuntime().getMetricsReporting().setEnableMetricsReporting(true);
        return configuration;
    }

    public HTTPAlertsConfigurations(FailedLogin failedLogin, FailedLoginFromSameIP failedLoginFromSameIP, FailedLoginSameUserID failedLoginSameUserID, FileTooLarge fileTooLarge, FileTooLargeFromSameIP fileTooLargeFromSameIP, FileTooLargeSameUserID fileTooLargeSameUserID) {
        this.failedLogin = failedLogin;
        this.failedLoginFromSameIP = failedLoginFromSameIP;
        this.failedLoginSameUserID = failedLoginSameUserID;
        this.fileTooLarge = fileTooLarge;
        this.fileTooLargeFromSameIP = fileTooLargeFromSameIP;
        this.fileTooLargeSameUserID = fileTooLargeSameUserID;
    }

    public FailedLogin getFailedLogin() {
        return failedLogin;
    }

    public FailedLoginFromSameIP getFailedLoginFromSameIP() {
        return failedLoginFromSameIP;
    }

    public FailedLoginSameUserID getFailedLoginSameUserID() {
        return failedLoginSameUserID;
    }

    public FileTooLarge getFileTooLarge() {
        return fileTooLarge;
    }

    public FileTooLargeSameUserID getFileTooLargeSameUserID() {
        return fileTooLargeSameUserID;
    }

    public FileTooLargeFromSameIP getFileTooLargeFromSameIP() {
        return fileTooLargeFromSameIP;
    }

    /**
     * Set of parameters that are common across all alerts
     *
     * @author Vo Le Tung
     */
    public static abstract class GeneralHTTPAlert {
        private int consecutiveAttemptsThreshold;
        private int timeWindow;
        private int alertInterval;
        private long highPriorityThreshold;

        /**
         * Set of parameters for HTTP alerts.
         *
         * @param consecutiveAttemptsThreshold Threshold for the number of events that can happen
         * @param timeWindow                   In seconds, the sliding time window of events to be considered
         * @param alertInterval                In seconds, the interval of alert messages raising to the user
         * @param highPriorityThreshold        Threshold for classifying a high priority event
         */
        public GeneralHTTPAlert(int consecutiveAttemptsThreshold, int timeWindow, int alertInterval, long highPriorityThreshold) {
            this.consecutiveAttemptsThreshold = consecutiveAttemptsThreshold;
            this.timeWindow = timeWindow;
            this.alertInterval = alertInterval;
            this.highPriorityThreshold = highPriorityThreshold;
        }

        public int getConsecutiveAttemptsThreshold() {
            return consecutiveAttemptsThreshold;
        }

        public int getTimeWindow() {
            return timeWindow;
        }

        public int getAlertInterval() {
            return alertInterval;
        }

        public long getHighPriorityThreshold() {
            return highPriorityThreshold;
        }
    }

    /**
     * Set of parameters for raising failed login alerts.
     *
     * @author Vo Le Tung
     */
    public static class FailedLogin extends GeneralHTTPAlert {
        public FailedLogin(int consecutiveAttemptsThreshold, int timeWindow, int alertInterval, long highPriorityThreshold) {
            super(consecutiveAttemptsThreshold, timeWindow, alertInterval, highPriorityThreshold);
        }
    }

    /**
     * Set of parameters for raising failed login from one same ip address alerts.
     *
     * @author Vo Le Tung
     */
    public static class FailedLoginFromSameIP extends GeneralHTTPAlert {
        public FailedLoginFromSameIP(int consecutiveAttemptsThreshold, int timeWindow, int alertInterval, long highPriorityThreshold) {
            super(consecutiveAttemptsThreshold, timeWindow, alertInterval, highPriorityThreshold);
        }
    }

    /**
     * Set of parameters for raising failed login using one same user id alerts.
     *
     * @author Vo Le Tung
     */
    public static class FailedLoginSameUserID extends GeneralHTTPAlert {
        public FailedLoginSameUserID(int consecutiveAttemptsThreshold, int timeWindow, int alertInterval, long highPriorityThreshold) {
            super(consecutiveAttemptsThreshold, timeWindow, alertInterval, highPriorityThreshold);
        }
    }

    public static class FileTooLarge extends GeneralHTTPAlert {
        public FileTooLarge(int consecutiveAttemptsThreshold, int timeWindow, int alertInterval, long highPriorityThreshold) {
            super(consecutiveAttemptsThreshold, timeWindow, alertInterval, highPriorityThreshold);
        }
    }

    public static class FileTooLargeFromSameIP extends GeneralHTTPAlert {
        public FileTooLargeFromSameIP(int consecutiveAttemptsThreshold, int timeWindow, int alertInterval, long highPriorityThreshold) {
            super(consecutiveAttemptsThreshold, timeWindow, alertInterval, highPriorityThreshold);
        }
    }

    public static class FileTooLargeSameUserID extends GeneralHTTPAlert {
        public FileTooLargeSameUserID(int consecutiveAttemptsThreshold, int timeWindow, int alertInterval, long highPriorityThreshold) {
            super(consecutiveAttemptsThreshold, timeWindow, alertInterval, highPriorityThreshold);
        }
    }
}
