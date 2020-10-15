package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.common.client.EPCompiled;
import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.compiler.client.CompilerArguments;
import com.espertech.esper.compiler.client.EPCompilerProvider;
import com.espertech.esper.runtime.client.DeploymentOptions;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import vn.edu.vgu.jupiter.eventbean.*;

/**
 * Utility functions for compiling and deploying statements.
 *
 * @author Vo Le Tung
 * @author Pham Nguyen Thanh An
 */
public class PortScansAlertUtil {

    /**
     * Compile and deploy the compiled statement with the given deployment options
     *
     * @param epl        the epl statement
     * @param runtime    the runtime to deploy to
     * @param deployOpts deployment options
     * @return the statement that was deployed
     */
    protected static EPStatement compileDeploy(String epl, EPRuntime runtime, DeploymentOptions deployOpts) {
        try {
            CompilerArguments args = new CompilerArguments(getConfiguration());
            args.getPath().add(runtime.getRuntimePath());
            EPCompiled compiled = EPCompilerProvider.getCompiler().compile(epl, args);
            return runtime.getDeploymentService().deploy(compiled, deployOpts).getStatements()[0];
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Compile and deploy the compiled statement
     *
     * @param epl     the epl statement
     * @param runtime the runtime to deploy to
     * @return the statement that was deployed
     */
    protected static EPStatement compileDeploy(String epl, EPRuntime runtime) {
        try {
            CompilerArguments args = new CompilerArguments(getConfiguration());
            args.getPath().add(runtime.getRuntimePath());
            EPCompiled compiled = EPCompilerProvider.getCompiler().compile(epl, args);
            return runtime.getDeploymentService().deploy(compiled).getStatements()[0];
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Make and returns that default configuration for Esper
     *
     * @return the configuration
     */
    public static Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        configuration.getCommon().addEventType(TcpPacketEvent.class);
        configuration.getCommon().addEventType(TcpPacketWithClosedPortEvent.class);
        configuration.getCommon().addEventType(ClosedPortsCountPerAddress.class);
        configuration.getCommon().addEventType(VerticalPortScanAlert.class);
        configuration.getCommon().addEventType(HorizontalPortScanAlert.class);
        configuration.getCommon().addEventType(BlockPortScanAlert.class);
        configuration.getRuntime().getLogging().setEnableExecutionDebug(false);
        configuration.getRuntime().getLogging().setEnableTimerDebug(false);
        return configuration;
    }
}