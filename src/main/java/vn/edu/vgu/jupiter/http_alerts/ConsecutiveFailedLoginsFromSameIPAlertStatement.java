package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.EPUndeployException;
import vn.edu.vgu.jupiter.EPFacade;

import static vn.edu.vgu.jupiter.http_alerts.HTTPAlertsConfigurations.getEPConfiguration;

/**
 * This class compile the EPL statement to select consecutive failed login attempts sourcing from one IP and raise the according events
 * and deploy the compiled EPL to the runtime
 *
 * @author Dang Chi Cong
 */
public class ConsecutiveFailedLoginsFromSameIPAlertStatement {
    private static final String statementEPL =
            "@Name('ConsecutiveFailedLoginsFromSameIPAlert')\n" +
                    "insert into ConsecutiveFailedLoginsFromSameIPAlert\n " +
                    "select IPAddress, time, userID, count(*)\n " +
                    "from HTTPFailedLogin#time(?:alertTimeWindow:integer second)\n " +
                    "group by IPAddress\n " +
                    "having count(*) > ?:consecutiveAttemptThreshold:integer\n" +
                    "output last every ?:alertInterval:integer second";
    private static final String listenEPL = "select * from ConsecutiveFailedLoginsFromSameIPAlert";

    private EPRuntime runtime;
    private EPStatement statement;
    private EPStatement listenStatement;

    public ConsecutiveFailedLoginsFromSameIPAlertStatement(EPRuntime runtime, int consecutiveAttemptsThreshold, int timeWindowSeconds, int alertIntervalSeconds, long highPriorityThreshold) {
        this.runtime = runtime;

        DeploymentOptions options = new DeploymentOptions();
        options.setStatementSubstitutionParameter(prepared -> {
            prepared.setObject("consecutiveAttemptThreshold", consecutiveAttemptsThreshold);
            prepared.setObject("alertTimeWindow", timeWindowSeconds);
            prepared.setObject("alertInterval", alertIntervalSeconds);
        });

        statement = EPFacade.compileDeploy(statementEPL, runtime, getEPConfiguration(), options);
        listenStatement = EPFacade.compileDeploy(listenEPL, runtime, getEPConfiguration());
        listenStatement.addListener(new ConsecutiveFailedLoginsFromSameIPAlertListener(highPriorityThreshold));
    }


    public void undeploy() throws EPUndeployException {
        runtime.getDeploymentService().undeploy(statement.getDeploymentId());
        runtime.getDeploymentService().undeploy(listenStatement.getDeploymentId());
    }
}
