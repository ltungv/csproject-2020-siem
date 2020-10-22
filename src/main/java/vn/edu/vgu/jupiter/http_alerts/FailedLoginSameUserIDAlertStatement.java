package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;

/**
 * This class compile the EPL statement to select consecutive failed log in targeting one userID, raise the alert events.
 * and deploy the compiled EPL to the runtime
 *
 * @author Dang Chi Cong
 */
public class FailedLoginSameUserIDAlertStatement {
    private String statement =
            "insert into FailedLoginSameUserIDAlert\n " +
                    "select IPAddress, userID, time, timeZone, count(*)\n " +
                    "from HTTPFailedLogin#time(?:alertTimeWindow:integer second)\n " +
                    "group by userID\n " +
                    "having count(*) > ?:consecutiveAttemptThreshold:integer\n" +
                    "output last every ?:alertInterval:integer second";

    private String listenStatement = "select * from FailedLoginSameUserIDAlert";

    public FailedLoginSameUserIDAlertStatement(EPRuntime runtime, int consecutiveAttemptsThreshold, int timeWindowSeconds, int alertIntervalSeconds, long highPriorityThreshold) {
        DeploymentOptions options = new DeploymentOptions();
        options.setStatementSubstitutionParameter(prepared -> {
            prepared.setObject("consecutiveAttemptThreshold", consecutiveAttemptsThreshold);
            prepared.setObject("alertTimeWindow", timeWindowSeconds);
            prepared.setObject("alertInterval", alertIntervalSeconds);
        });

        CEPSetupUtil.compileDeploy(statement, runtime, options);
        CEPSetupUtil.compileDeploy(listenStatement, runtime).addListener(new FailedLoginSameUserIDAlertListener(highPriorityThreshold));
    }
}