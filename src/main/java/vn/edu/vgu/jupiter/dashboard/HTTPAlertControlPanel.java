package vn.edu.vgu.jupiter.dashboard;

import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPUndeployException;
import javafx.event.ActionEvent;
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
 * @author Vo Le Tung
 */
public class HTTPAlertControlPanel implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(HTTPAlertControlPanel.class);

    public TextField failedLoginAttemptsThreshold;
    public TextField failedLoginTimeWindow;
    public TextField failedLoginAlertInterval;
    public TextField failedLoginHighPriorityThreshold;

    public TextField sameIpFailedLoginAttemptsThreshold;
    public TextField sameIpFailedLoginTimeWindow;
    public TextField sameIpFailedLoginAlertInterval;
    public TextField sameIpFailedLoginHighPriorityThreshold;

    public TextField sameUserFailedLoginAttemptsThreshold;
    public TextField sameUserFailedLoginTimeWindow;
    public TextField sameUserFailedLoginAlertInterval;
    public TextField sameUserFailedLoginHighPriorityThreshold;

    private EPRuntime runtime;

    /**
     * Set the runtime that is used by this controller.
     *
     * @param runtime the runtime used
     */
    public void setRuntime(EPRuntime runtime) {
        this.runtime = runtime;
    }

    /**
     * Get the plugin loader from the runtime and redeploy the module with new parameters.
     *
     * @param actionEvent the event that was generated by the user's actions
     * @throws NamingException     could not find plugin id
     * @throws EPUndeployException could not undeploy deployments
     */
    public void deploy(ActionEvent actionEvent) throws NamingException, EPUndeployException {
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
                )
        );

        plugin.undeploy();
        plugin.deploy(configs);
        updateFieldsValue(configs);

        log.info("Update deployments");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

    }

    /**
     * Update values of text fields based on the configurations
     *
     * @param config parameters for the cep engine
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
    }

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