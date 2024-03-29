package vn.edu.vgu.jupiter.dashboard;

import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPUndeployException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.edu.vgu.jupiter.http_alerts.HTTPAlertsConfigurations;
import vn.edu.vgu.jupiter.http_alerts.HTTPAlertsPlugin;

import javax.naming.NamingException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This controller access the Esper's runtime to get the plugin that are responsible for raising HTTP alert messages.
 * <p>
 * The controller consists of text fields that can registered the user's inputs and a button that will trigger the
 * system to change parameters. To interact with the system, the plugin that is responsible for raising HTTP alerts can
 * be accessed through the runtime that was passed into the controller. Upon the user clicking the button, the system
 * will undeploys all the EPL statements (if any), and redeploys them with a new set of parameters. If any of the values
 * provided by the user is in incorrect format, the default value of for that parameter will be used instead.
 *
 * @author Vo Le Tung
 */
public class HTTPAlertControlPanel implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(HTTPAlertControlPanel.class);
    @FXML
    public TextField failedLoginAttemptsThreshold;
    @FXML
    public TextField failedLoginTimeWindow;
    @FXML
    public TextField failedLoginAlertInterval;
    @FXML
    public TextField failedLoginHighPriorityThreshold;
    @FXML
    public TextField sameIpFailedLoginAttemptsThreshold;
    @FXML
    public TextField sameIpFailedLoginTimeWindow;
    @FXML
    public TextField sameIpFailedLoginAlertInterval;
    @FXML
    public TextField sameIpFailedLoginHighPriorityThreshold;
    @FXML
    public TextField sameUserFailedLoginAttemptsThreshold;
    @FXML
    public TextField sameUserFailedLoginTimeWindow;
    @FXML
    public TextField sameUserFailedLoginAlertInterval;
    @FXML
    public TextField sameUserFailedLoginHighPriorityThreshold;

    @FXML
    public TextField fileTooLargeAttemptsThreshold;
    @FXML
    public TextField fileTooLargeTimeWindow;
    @FXML
    public TextField fileTooLargeAlertInterval;
    @FXML
    public TextField fileTooLargeHighPriorityThreshold;
    @FXML
    public TextField sameIpFileTooLargeAttemptsThreshold;
    @FXML
    public TextField sameIpFileTooLargeTimeWindow;
    @FXML
    public TextField sameIpFileTooLargeAlertInterval;
    @FXML
    public TextField sameIpFileTooLargeHighPriorityThreshold;
    @FXML
    public TextField sameUserFileTooLargeAttemptsThreshold;
    @FXML
    public TextField sameUserFileTooLargeTimeWindow;
    @FXML
    public TextField sameUserFileTooLargeAlertInterval;
    @FXML
    public TextField sameUserFileTooLargeHighPriorityThreshold;
    private EPRuntime runtime;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // upon initialization, set the value of the text fields to their default value
        failedLoginAttemptsThreshold.setText(String.valueOf(16));
        failedLoginTimeWindow.setText(String.valueOf(6));
        failedLoginAlertInterval.setText(String.valueOf(3));
        failedLoginHighPriorityThreshold.setText(String.valueOf(20));

        sameIpFailedLoginAttemptsThreshold.setText(String.valueOf(12));
        sameIpFailedLoginTimeWindow.setText(String.valueOf(2));
        sameIpFailedLoginAlertInterval.setText(String.valueOf(1));
        sameIpFailedLoginHighPriorityThreshold.setText(String.valueOf(15));

        sameUserFailedLoginAttemptsThreshold.setText(String.valueOf(3));
        sameUserFailedLoginTimeWindow.setText(String.valueOf(2));
        sameUserFailedLoginAlertInterval.setText(String.valueOf(1));
        sameUserFailedLoginHighPriorityThreshold.setText(String.valueOf(5));

        fileTooLargeAttemptsThreshold.setText(String.valueOf(16));
        fileTooLargeTimeWindow.setText(String.valueOf(6));
        fileTooLargeAlertInterval.setText(String.valueOf(3));
        fileTooLargeHighPriorityThreshold.setText(String.valueOf(20));

        sameIpFileTooLargeAttemptsThreshold.setText(String.valueOf(12));
        sameIpFileTooLargeTimeWindow.setText(String.valueOf(2));
        sameIpFileTooLargeAlertInterval.setText(String.valueOf(1));
        sameIpFileTooLargeHighPriorityThreshold.setText(String.valueOf(15));

        sameUserFileTooLargeAttemptsThreshold.setText(String.valueOf(3));
        sameUserFileTooLargeTimeWindow.setText(String.valueOf(2));
        sameUserFileTooLargeAlertInterval.setText(String.valueOf(1));
        sameUserFileTooLargeHighPriorityThreshold.setText(String.valueOf(5));

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
        HTTPAlertsPlugin plugin = (HTTPAlertsPlugin) runtime.getContext().getEnvironment().get("plugin-loader/HTTPAlertsPlugin");
        HTTPAlertsConfigurations configs = new HTTPAlertsConfigurations(
                new HTTPAlertsConfigurations.FailedLogin(
                        parseUintOrDefault(failedLoginAttemptsThreshold.getText(), 16),
                        parseUintOrDefault(failedLoginTimeWindow.getText(), 6),
                        parseUintOrDefault(failedLoginAlertInterval.getText(), 3),
                        parseUintOrDefault(failedLoginHighPriorityThreshold.getText(), 20)
                ),
                new HTTPAlertsConfigurations.FailedLoginFromSameIP(
                        parseUintOrDefault(sameIpFailedLoginAttemptsThreshold.getText(), 12),
                        parseUintOrDefault(sameIpFailedLoginTimeWindow.getText(), 2),
                        parseUintOrDefault(sameIpFailedLoginAlertInterval.getText(), 1),
                        parseUintOrDefault(sameIpFailedLoginHighPriorityThreshold.getText(), 15)
                ),
                new HTTPAlertsConfigurations.FailedLoginSameUserID(
                        parseUintOrDefault(sameUserFailedLoginAttemptsThreshold.getText(), 3),
                        parseUintOrDefault(sameUserFailedLoginTimeWindow.getText(), 2),
                        parseUintOrDefault(sameUserFailedLoginAlertInterval.getText(), 1),
                        parseUintOrDefault(sameUserFailedLoginHighPriorityThreshold.getText(), 5)
                ),
                new HTTPAlertsConfigurations.FileTooLarge(
                        parseUintOrDefault(fileTooLargeAttemptsThreshold.getText(), 16),
                        parseUintOrDefault(fileTooLargeTimeWindow.getText(), 6),
                        parseUintOrDefault(fileTooLargeAlertInterval.getText(), 3),
                        parseUintOrDefault(fileTooLargeHighPriorityThreshold.getText(), 20)
                ),
                new HTTPAlertsConfigurations.FileTooLargeFromSameIP(
                        parseUintOrDefault(sameIpFileTooLargeAttemptsThreshold.getText(), 12),
                        parseUintOrDefault(sameIpFileTooLargeTimeWindow.getText(), 2),
                        parseUintOrDefault(sameIpFileTooLargeAlertInterval.getText(), 1),
                        parseUintOrDefault(sameIpFileTooLargeHighPriorityThreshold.getText(), 15)
                ),
                new HTTPAlertsConfigurations.FileTooLargeSameUserID(
                        parseUintOrDefault(sameUserFileTooLargeAttemptsThreshold.getText(), 3),
                        parseUintOrDefault(sameUserFileTooLargeTimeWindow.getText(), 2),
                        parseUintOrDefault(sameUserFileTooLargeAlertInterval.getText(), 1),
                        parseUintOrDefault(sameUserFileTooLargeHighPriorityThreshold.getText(), 5)
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
    private void updateFieldsValue(HTTPAlertsConfigurations config) {
        failedLoginAttemptsThreshold.setText(String.valueOf(config.getFailedLogin().getConsecutiveAttemptsThreshold()));
        failedLoginTimeWindow.setText(String.valueOf(config.getFailedLogin().getTimeWindow()));
        failedLoginAlertInterval.setText(String.valueOf(config.getFailedLogin().getAlertInterval()));
        failedLoginHighPriorityThreshold.setText(String.valueOf(config.getFailedLogin().getHighPriorityThreshold()));

        sameIpFailedLoginAttemptsThreshold.setText(String.valueOf(config.getFailedLoginFromSameIP().getConsecutiveAttemptsThreshold()));
        sameIpFailedLoginTimeWindow.setText(String.valueOf(config.getFailedLoginFromSameIP().getTimeWindow()));
        sameIpFailedLoginAlertInterval.setText(String.valueOf(config.getFailedLoginFromSameIP().getAlertInterval()));
        sameIpFailedLoginHighPriorityThreshold.setText(String.valueOf(config.getFailedLoginFromSameIP().getHighPriorityThreshold()));

        sameUserFailedLoginAttemptsThreshold.setText(String.valueOf(config.getFailedLoginSameUserID().getConsecutiveAttemptsThreshold()));
        sameUserFailedLoginTimeWindow.setText(String.valueOf(config.getFailedLoginSameUserID().getTimeWindow()));
        sameUserFailedLoginAlertInterval.setText(String.valueOf(config.getFailedLoginSameUserID().getAlertInterval()));
        sameUserFailedLoginHighPriorityThreshold.setText(String.valueOf(config.getFailedLoginSameUserID().getHighPriorityThreshold()));

        fileTooLargeAttemptsThreshold.setText(String.valueOf(config.getFileTooLarge().getConsecutiveAttemptsThreshold()));
        fileTooLargeTimeWindow.setText(String.valueOf(config.getFileTooLarge().getTimeWindow()));
        fileTooLargeAlertInterval.setText(String.valueOf(config.getFileTooLarge().getAlertInterval()));
        fileTooLargeHighPriorityThreshold.setText(String.valueOf(config.getFileTooLarge().getHighPriorityThreshold()));

        sameIpFileTooLargeAttemptsThreshold.setText(String.valueOf(config.getFileTooLargeFromSameIP().getConsecutiveAttemptsThreshold()));
        sameIpFileTooLargeTimeWindow.setText(String.valueOf(config.getFileTooLargeFromSameIP().getTimeWindow()));
        sameIpFileTooLargeAlertInterval.setText(String.valueOf(config.getFileTooLargeFromSameIP().getAlertInterval()));
        sameIpFileTooLargeHighPriorityThreshold.setText(String.valueOf(config.getFileTooLargeFromSameIP().getHighPriorityThreshold()));

        sameUserFileTooLargeAttemptsThreshold.setText(String.valueOf(config.getFileTooLargeSameUserID().getConsecutiveAttemptsThreshold()));
        sameUserFileTooLargeTimeWindow.setText(String.valueOf(config.getFileTooLargeSameUserID().getTimeWindow()));
        sameUserFileTooLargeAlertInterval.setText(String.valueOf(config.getFileTooLargeSameUserID().getAlertInterval()));
        sameUserFileTooLargeHighPriorityThreshold.setText(String.valueOf(config.getFileTooLargeSameUserID().getHighPriorityThreshold()));
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
