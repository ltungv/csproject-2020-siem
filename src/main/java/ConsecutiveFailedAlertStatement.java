import com.espertech.esper.common.client.util.TimePeriod;
import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;

public class ConsecutiveFailedAlertStatement {
    private String statement =
            "insert into httpConsecutiveFailedLoginAlert\n " +
                "select IPAddress, time, userID\n " +
                "from httpFailedLogin#time_batch(?:alertTimeWindow: integer second)\n " +
                "group by IPAddress\n " +
                "having count(*) > ?:consecutiveAttemptThreshold:integer";

    private String listenStatement = "select * from httpConsecutiveFailedLoginAlert";

    public ConsecutiveFailedAlertStatement(EPRuntime runtime, int consecutiveAttemptsThreshold, int timeWindowSeconds) {
        DeploymentOptions options = new DeploymentOptions();
        options.setStatementSubstitutionParameter(prepared -> {
           prepared.setObject("consecutiveAttemptThreshold", consecutiveAttemptsThreshold);
            TimePeriod ts = new TimePeriod().sec(timeWindowSeconds);
            prepared.setObject("alertTimeWindow", ts.getSeconds());
        });

        ConsecutiveFailedLoginsUtil.compileDeploy(statement, runtime, options);
        ConsecutiveFailedLoginsUtil.compileDeploy(listenStatement, runtime).addListener(new ConsecutiveFailedAlertListener());
    }
}