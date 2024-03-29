package vn.edu.vgu.jupiter.dashboard;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * This controller asks for user inputs in net device for port scans alerts and log location for http alerts
 * Upon receiving the correct values, it will switch to Dashboard
 *
 * @author Pham Nguyen Thanh An
 */
public class StartupConfig implements Initializable {
    @FXML
    private ComboBox<String> netDeviceComboBox;
    @FXML
    private TextField apacheLogLocationField;

    /**
     * Get controller from Dashboard and uses it to set variables and plugin, thus start to run
     *
     * @throws IOException
     */
    @FXML
    private void applyVariables(ActionEvent evt) throws IOException {
        String chosenLogLocation = apacheLogLocationField.getText();
        if (isFileExist(chosenLogLocation)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Parent root = loader.load();

            Dashboard dashboardController = loader.getController();
            dashboardController.setNetDeviceName(netDeviceComboBox.getValue());
            dashboardController.setLogFileLocation(chosenLogLocation);
            dashboardController.setPlugin();

            Node eventSource = (Node) evt.getSource();
            Stage stage = (Stage) eventSource.getScene().getWindow();
            stage.setScene(new Scene(root, 960, 640));
        } else {
            //make an big error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Your apache log file may not exist, or it is not readable.");
            alert.showAndWait();
        }
    }

    /**
     * Get network interfaces and set them
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Enumeration<NetworkInterface> netInterfaces;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
            Platform.exit();
            return;
        }

        ArrayList<String> netInterfaceNames = new ArrayList<>();
        Iterator<NetworkInterface> netInterfacesIt = netInterfaces.asIterator();
        while (netInterfacesIt.hasNext()) {
            netInterfaceNames.add(netInterfacesIt.next().getDisplayName());
        }

        netDeviceComboBox.setItems(FXCollections.observableList(netInterfaceNames));
        netDeviceComboBox.getSelectionModel().selectFirst();
    }

    /**
     * A simple log checking location without checking if the log file is in correct format
     */
    private boolean isFileExist(String pathname) {
        //checking for location
        File file = new File(pathname);
        return file.canRead();
    }
}
