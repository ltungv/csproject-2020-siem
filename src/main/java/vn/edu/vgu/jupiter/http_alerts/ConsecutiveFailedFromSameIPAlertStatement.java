package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.common.client.util.TimePeriod;
import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;

/**
 * This class compile the EPL statement to select consecutive failed login attempts sourcing from one IP and raise the according events
 * and deploy the compiled EPL to the runtime
 *
 * @author Bui Xuan Phuoc
 */
public class ConsecutiveFailedFromSameIPAlertStatement {
    private String statement =
            "insert into httpConsecutiveFailedLoginFromSameIPAlertEvent\n " +
                    "select IPAddress, time, userID, count(*)\n " +
                    "from httpFailedLoginEvent#time(?:alertTimeWindow:integer second)\n " +
                    "group by IPAddress\n " +
                    "having count(*) > ?:consecutiveAttemptThreshold:integer\n" +
                    "output first every ?:alertInterval:integer second";

    private String listenStatement = "select * from httpConsecutiveFailedLoginFromSameIPAlertEvent";

    public ConsecutiveFailedFromSameIPAlertStatement(EPRuntime runtime, int consecutiveAttemptsThreshold, int timeWindowSeconds, int alertIntervalSeconds) {
        DeploymentOptions options = new DeploymentOptions();
        options.setStatementSubstitutionParameter(prepared -> {
            prepared.setObject("consecutiveAttemptThreshold", consecutiveAttemptsThreshold);
            prepared.setObject("alertTimeWindow", timeWindowSeconds);
            prepared.setObject("alertInterval", alertIntervalSeconds);
        });

        CEPSetupUtil.compileDeploy(statement, runtime, options);
        CEPSetupUtil.compileDeploy(listenStatement, runtime).addListener(new ConsecutiveFailedFromSameIPAlertListener());
    }
}
