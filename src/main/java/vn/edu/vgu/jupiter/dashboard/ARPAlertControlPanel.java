package vn.edu.vgu.jupiter.dashboard;

import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPUndeployException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.edu.vgu.jupiter.arp_alerts.ARPAlertsConfigurations;
import vn.edu.vgu.jupiter.arp_alerts.ARPAlertsPlugin;

import javax.naming.NamingException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Bui Xuan Phuoc
 */
public class ARPAlertControlPanel implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(ARPAlertControlPanel.class);

    @FXML
    public TextField arpCacheFloodConsecutiveAttemptsThreshold;
    @FXML
    public TextField arpCacheFloodTimeWindow;
    @FXML
    public TextField arpCacheFloodAlertInterval;
    @FXML
    public TextField arpCacheFloodHighPriorityThreshold;

    @FXML
    public TextField arpGratuitousAnnouncementConsecutiveAttemptsThreshold;
    @FXML
    public TextField arpGratuitousAnnouncementTimeWindow;
    @FXML
    public TextField arpGratuitousAnnouncementAlertInterval;
    @FXML
    public TextField arpGratuitousAnnouncementHighPriorityThreshold;
    @FXML
    public TextField arpDuplicateIPAlertInterval;

    private EPRuntime runtime;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // upon initialization, set the value of the text fields to their default value
        arpDuplicateIPAlertInterval.setText(String.valueOf(10));

        arpCacheFloodConsecutiveAttemptsThreshold.setText(String.valueOf(40));
        arpCacheFloodTimeWindow.setText(String.valueOf(3));
        arpCacheFloodAlertInterval.setText(String.valueOf(10));
        arpCacheFloodHighPriorityThreshold.setText(String.valueOf(30));

        arpGratuitousAnnouncementConsecutiveAttemptsThreshold.setText(String.valueOf(4));
        arpGratuitousAnnouncementTimeWindow.setText(String.valueOf(10));
        arpGratuitousAnnouncementAlertInterval.setText(String.valueOf(10));
        arpGratuitousAnnouncementHighPriorityThreshold.setText(String.valueOf(3));
    }


    /**
     * Set the runtime that is used by this controller.
     *
     * @param runtime The runtime used
     */
    public void setRuntime(EPRuntime runtime) {
        this.runtime = runtime;
    }

    /**
     * Get the plugin loader from the runtime and redeploy the module with new parameters.
     *
     * @throws NamingException     Could not find plugin id
     * @throws EPUndeployException Could not undeploy deployments
     */
    @FXML
    public void applyVariableChanges() throws NamingException, EPUndeployException {
        // get runtime
        ARPAlertsPlugin plugin = (ARPAlertsPlugin) runtime.getContext().getEnvironment().get("plugin-loader/ARPAlertsPlugin");
        ARPAlertsConfigurations configs = new ARPAlertsConfigurations(
                new ARPAlertsConfigurations.ARPDuplicateIP(
                        parseUintOrDefault(arpDuplicateIPAlertInterval.getText(), 10)),
                new ARPAlertsConfigurations.ARPCacheFlood(
                        parseUintOrDefault(arpCacheFloodConsecutiveAttemptsThreshold.getText(), 40),
                        parseUintOrDefault(arpCacheFloodTimeWindow.getText(), 3),
                        parseUintOrDefault(arpCacheFloodAlertInterval.getText(), 10),
                        parseUintOrDefault(arpCacheFloodHighPriorityThreshold.getText(), 30)
                ),
                new ARPAlertsConfigurations.ARPGratuitousAnnouncement(
                        parseUintOrDefault(arpGratuitousAnnouncementConsecutiveAttemptsThreshold.getText(), 4),
                        parseUintOrDefault(arpCacheFloodTimeWindow.getText(), 10),
                        parseUintOrDefault(arpCacheFloodAlertInterval.getText(), 10),
                        parseUintOrDefault(arpCacheFloodHighPriorityThreshold.getText(), 3)
                )
        );

        // redeployment
        plugin.undeploy();
        plugin.deploy(configs);
        updateFieldsValue(configs);

        // message to user
        log.info("Update deployments");
    }

    /**
     * Update values of text fields based on the configurations.
     *
     * @param config Parameters for the CEP engine
     */
    private void updateFieldsValue(ARPAlertsConfigurations config) {
        arpCacheFloodConsecutiveAttemptsThreshold.setText(String.valueOf(config.getArpCacheFlood().getConsecutiveAttemptsThreshold()));
        arpCacheFloodTimeWindow.setText(String.valueOf(config.getArpCacheFlood().getTimeWindowSeconds()));
        arpCacheFloodAlertInterval.setText(String.valueOf(config.getArpCacheFlood().getAlertIntervalSeconds()));
        arpCacheFloodHighPriorityThreshold.setText(String.valueOf(config.getArpCacheFlood().getHighPriorityThreshold()));

        arpGratuitousAnnouncementConsecutiveAttemptsThreshold.setText(String.valueOf(config.getArpGratuitousAnnouncement().getConsecutiveAttemptsThreshold()));
        arpGratuitousAnnouncementTimeWindow.setText(String.valueOf(config.getArpGratuitousAnnouncement().getTimeWindowSeconds()));
        arpGratuitousAnnouncementAlertInterval.setText(String.valueOf(config.getArpGratuitousAnnouncement().getAlertIntervalSeconds()));
        arpGratuitousAnnouncementHighPriorityThreshold.setText(String.valueOf(config.getArpGratuitousAnnouncement().getHighPriorityThreshold()));
    }

    /**
     * Simple helper for parsing strings as integers.
     * <p>
     * If the given string is not an integer, returns the given default value.
     *
     * @param v          The string representation of the integer
     * @param defaultVal The default value that might be returned
     * @return The parsed integer, if the given string is valid, otherwise the default value
     */
    private int parseUintOrDefault(String v, int defaultVal) {
        int vv;
        try {
            vv = Integer.parseInt(v);
        } catch (NumberFormatException e) {
            return defaultVal;
        }

        if (vv < 0) {
            return defaultVal;
        }
        return vv;
    }

}
