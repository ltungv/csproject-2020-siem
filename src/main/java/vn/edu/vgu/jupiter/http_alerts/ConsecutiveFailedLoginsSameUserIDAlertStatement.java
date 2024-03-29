package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.EPUndeployException;
import vn.edu.vgu.jupiter.EPFacade;

import static vn.edu.vgu.jupiter.http_alerts.HTTPAlertsConfigurations.getEPConfiguration;

/**
 * This class compile the EPL statement to select consecutive failed log in targeting one userID, raise the alert events.
 * and deploy the compiled EPL to the runtime
 *
 * @author Dang Chi Cong
 */
public class ConsecutiveFailedLoginsSameUserIDAlertStatement {
    private static final String statementEPL =
            "@Name('ConsecutiveFailedLoginsSameUserIDAlert')\n" +
                    "insert into ConsecutiveFailedLoginsSameUserIDAlert\n " +
                    "select IPAddress, userID, time, timeZone, count(*)\n " +
                    "from HTTPFailedLogin#time(?:alertTimeWindow:integer second)\n " +
                    "group by userID\n " +
                    "having count(*) > ?:consecutiveAttemptThreshold:integer\n" +
                    "output last every ?:alertInterval:integer second";
    private static final String listenStatementEPL = "select * from ConsecutiveFailedLoginsSameUserIDAlert";

    private EPRuntime runtime;
    private EPStatement statement;
    private EPStatement listenStatement;

    public ConsecutiveFailedLoginsSameUserIDAlertStatement(EPRuntime runtime, int consecutiveAttemptsThreshold, int timeWindowSeconds, int alertIntervalSeconds, long highPriorityThreshold) {
        this.runtime = runtime;

        DeploymentOptions options = new DeploymentOptions();
        options.setStatementSubstitutionParameter(prepared -> {
            prepared.setObject("consecutiveAttemptThreshold", consecutiveAttemptsThreshold);
            prepared.setObject("alertTimeWindow", timeWindowSeconds);
            prepared.setObject("alertInterval", alertIntervalSeconds);
        });

        statement = EPFacade.compileDeploy(statementEPL, runtime, getEPConfiguration(), options);
        listenStatement = EPFacade.compileDeploy(listenStatementEPL, runtime, getEPConfiguration());
        listenStatement.addListener(new ConsecutiveFailedLoginsSameUserIDAlertListener(highPriorityThreshold));
    }

    public void undeploy() throws EPUndeployException {
        runtime.getDeploymentService().undeploy(statement.getDeploymentId());
        runtime.getDeploymentService().undeploy(listenStatement.getDeploymentId());
    }
}
