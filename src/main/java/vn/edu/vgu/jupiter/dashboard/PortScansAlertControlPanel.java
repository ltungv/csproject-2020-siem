package vn.edu.vgu.jupiter.dashboard;

import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPUndeployException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.edu.vgu.jupiter.scan_alerts.PortScansAlertConfigurations;
import vn.edu.vgu.jupiter.scan_alerts.PortScansAlertPlugin;

import javax.naming.NamingException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This controller can access the runtime to get the plugin to deploy and undeploy deployments for port scans alert
 * <p>
 * This controller consists of text fields to get player inputs and a button if pressed, will apply the changes to
 * the system. Upon clicking the button, by using the plugin gotten from runtime, the system will undeploy all statements
 * related to port scans, and redeploy with new parameters. If the input is of incorrect format, default values will be used.
 *
 * @author Pham Nguyen Thanh An
 */
public class PortScansAlertControlPanel implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(PortScansAlertControlPanel.class);
    /**
     * FXML fields id
     */
    @FXML
    private TextField verticalMinCount;
    @FXML
    private TextField verticalTime;
    @FXML
    private TextField verticalTimeInterval;
    @FXML
    private TextField verticalWarnCount;

    @FXML
    private TextField horizontalMinCount;
    @FXML
    private TextField horizontalTime;
    @FXML
    private TextField horizontalTimeInterval;
    @FXML
    private TextField horizontalWarnCount;

    @FXML
    private TextField blockMinPortCount;
    @FXML
    private TextField blockMinAddressCount;
    @FXML
    private TextField blockTime;
    @FXML
    private TextField blockTimeInterval;
    @FXML
    private TextField blockWarnAddressCount;

    private EPRuntime runtime;

    /**
     * Set the runtime that is used by this controller.
     *
     * @param runtime The runtime
     */
    public void setRuntime(EPRuntime runtime) {
        this.runtime = runtime;
    }

    /**
     * Get plugin from runtime and undeploy, deploy with new parameters.
     *
     * @throws NamingException     plugin id not found
     * @throws EPUndeployException deployments can not be undeploy
     */
    @FXML
    public void applyVariableChanges() throws NamingException, EPUndeployException {
        PortScansAlertPlugin plugin = (PortScansAlertPlugin) runtime.getContext().getEnvironment()
                .get("plugin-loader/PortScansAlertPlugin");
        PortScansAlertConfigurations newConfig = new PortScansAlertConfigurations(
                new PortScansAlertConfigurations.VerticalScan(
                        parseIntoIntOrDefault(verticalTime.getText(), 60),
                        parseIntoIntOrDefault(verticalTimeInterval.getText(), 10),
                        parseIntoIntOrDefault(verticalWarnCount.getText(), 100),
                        parseIntoIntOrDefault(verticalMinCount.getText(), 60)),
                new PortScansAlertConfigurations.HorizontalScan(
                        parseIntoIntOrDefault(horizontalTime.getText(), 60),
                        parseIntoIntOrDefault(horizontalTimeInterval.getText(), 10),
                        parseIntoIntOrDefault(horizontalWarnCount.getText(), 100),
                        parseIntoIntOrDefault(horizontalMinCount.getText(), 60)),
                new PortScansAlertConfigurations.BlockScan(
                        parseIntoIntOrDefault(blockTime.getText(), 60),
                        parseIntoIntOrDefault(blockTimeInterval.getText(), 10),
                        parseIntoIntOrDefault(blockWarnAddressCount.getText(), 5),
                        parseIntoIntOrDefault(blockMinPortCount.getText(), 50),
                        parseIntoIntOrDefault(blockMinAddressCount.getText(), 2)));

        plugin.undeploy();
        plugin.deploy(newConfig);
        updateFieldsValue(newConfig);

        log.info("Apply variables changes for port scans alert.");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        verticalMinCount.setText(String.valueOf(60));
        verticalTime.setText(String.valueOf(60));
        verticalTimeInterval.setText(String.valueOf(10));
        verticalWarnCount.setText(String.valueOf(100));

        horizontalMinCount.setText(String.valueOf(60));
        horizontalTime.setText(String.valueOf(60));
        horizontalTimeInterval.setText(String.valueOf(10));
        horizontalWarnCount.setText(String.valueOf(100));

        blockMinPortCount.setText(String.valueOf(50));
        blockMinAddressCount.setText(String.valueOf(2));
        blockTime.setText(String.valueOf(60));
        blockTimeInterval.setText(String.valueOf(10));
        blockWarnAddressCount.setText(String.valueOf(5));
    }

    /**
     * Update values of text fields based on the configurations.
     *
     * @param config Parameters for the CEP engine
     */
    private void updateFieldsValue(PortScansAlertConfigurations config) {
        verticalMinCount.setText(String.valueOf(config.getVerticalScan().getConnectionsCountThreshold()));
        verticalTime.setText(String.valueOf(config.getVerticalScan().getTimeWindow()));
        verticalTimeInterval.setText(String.valueOf(config.getVerticalScan().getAlertInterval()));
        verticalWarnCount.setText(String.valueOf(config.getVerticalScan().getHighPriorityThreshold()));

        horizontalMinCount.setText(String.valueOf(config.getHorizontalScan().getConnectionsCountThreshold()));
        horizontalTime.setText(String.valueOf(config.getHorizontalScan().getTimeWindow()));
        horizontalTimeInterval.setText(String.valueOf(config.getHorizontalScan().getAlertInterval()));
        horizontalWarnCount.setText(String.valueOf(config.getHorizontalScan().getHighPriorityThreshold()));

        blockMinPortCount.setText(String.valueOf(config.getBlockScan().getPortsCountThreshold()));
        blockMinAddressCount.setText(String.valueOf(config.getBlockScan().getAddressesCountThreshold()));
        blockTime.setText(String.valueOf(config.getBlockScan().getTimeWindow()));
        blockTimeInterval.setText(String.valueOf(config.getBlockScan().getAlertInterval()));
        blockWarnAddressCount.setText(String.valueOf(config.getBlockScan().getHighPriorityThreshold()));
    }

    /**
     * Simple helper for parsing strings as integers.
     * <p>
     * If the given string is not an integer, returns the given default value.
     *
     * @param text       The string representation of the integer
     * @param defaultVal The default value that might be returned
     * @return The parsed integer, if the given string is valid, otherwise the default value
     */
    private int parseIntoIntOrDefault(String text, int defaultVal) {
        int number;
        try {
            number = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return defaultVal;
        }

        if (number < 0) {
            return defaultVal;
        }
        return number;
    }
}
