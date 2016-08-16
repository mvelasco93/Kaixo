package GUI;

import elements.Consulta;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import kaixo.Main;
import static utils.JavaSQL.existsCons;

/**
 * FXML Controller class
 *
 * @author made
 */
public class ConsultaController extends Main implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private DatePicker conFecha;
    @FXML
    private ChoiceBox conHora;
    @FXML
    private ChoiceBox conEstado;
    ObservableList<String> conEstados = FXCollections.observableArrayList(
    "Pendiente", "Cancelada","Asistio");
    ObservableList<String> conHoras = FXCollections.observableArrayList(
    "11:00", "12:00","13:00");
    
    private Stage dialogStage;
    private Consulta con;
    private boolean okClicked = false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conEstado.setItems(conEstados);
        conHora.setItems(conHoras);
        conEstado.setValue("Pendiente");
    }    
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public void setConsulta(Consulta con){
        this.con = con;
        /*SET*/
    }
    
    public void disableEstado(){
        conEstado.setDisable(true);
    }
    
    public boolean isOkClicked(){
        return okClicked;
    }
    
    @FXML
    public void handleOk() throws SQLException{
        if(isInputValid()){         
            con.setEstado(new SimpleStringProperty(conEstado.getValue().toString()));
            con.setFecha(new SimpleStringProperty(conFecha.getValue().toString()+ " "+ conHora.getValue()));
            okClicked = true;
            dialogStage.close();
        }
    }
    
    @FXML
    public void handleCancel(){
        dialogStage.close();
    }
    
    private boolean isInputValid() throws SQLException{
        String errorMessage = "";
        
        if(conFecha.getValue().isBefore(LocalDate.now())){
            errorMessage += "La fecha de la consulta es incorrecta. "
                    + "No puede ser menor al día de hoy \n";
        }
        
        String date = conFecha.getValue().toString()+ " "+ conHora.getValue();
        
        if (existsCons(actualDB, date)){
            errorMessage += "Ya tienes una consulta programada para ese día y hora \n";
        }
        
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Tienes un(os) error(es) al ingresar los datos");
            alert.setContentText(errorMessage);
            alert.showAndWait();
        	
            return false;
        }
    }
    
    
}