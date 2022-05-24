package es.antoniodominguez.editorialmangas;

import es.antoniodominguez.editorialmangas.entities.Manga;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javax.persistence.Query;

/**
 * FXML Controller class
 *
 * @author anton
 */
public class PrimaryController implements Initializable {

    private Manga mangaSeleccionado;
    @FXML
    private BorderPane rootPrimary;
    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldAutor;
    @FXML
    private TableView<Manga> tableViewManga;
    @FXML
    private TableColumn<Manga, String> columnNombre;
    @FXML
    private TableColumn<Manga, String> columnEmail;
    @FXML
    private TableColumn<Manga, String> columnAutor;
    @FXML
    private TableColumn<Manga, String> columnEditorial;
    @FXML
    private TextField textFieldBuscar;
    @FXML
    private CheckBox checkBoxCoincide;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("emailEditorial"));
        columnEditorial.setCellValueFactory(
                cellData -> {
                    SimpleStringProperty property = new SimpleStringProperty();
                    if (cellData.getValue().getEditorial() != null){
                        String nombreEdi = cellData.getValue().getEditorial().getNombre();
                        property.setValue(nombreEdi);
                    }
                    return property;
                });
        tableViewManga.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    mangaSeleccionado = newValue;
                    if (mangaSeleccionado != null) {
                        textFieldNombre.setText(mangaSeleccionado.getNombre());
                        textFieldAutor.setText(mangaSeleccionado.getAutor());
                    } else {
                        textFieldNombre.setText("");
                        textFieldAutor.setText("");
                    }
                });
        cargarTodosMangas();
    }    
    private void cargarTodosMangas(){
        Query queryMangaFindAll = App.em.createNamedQuery("Manga.findAll");
        List<Manga> listManga = queryMangaFindAll.getResultList();
        tableViewManga.setItems(FXCollections.observableArrayList(listManga));
//            FXCollections.
    }
    
     @FXML
    private void onActtionButtonNuevo(ActionEvent event) {
        try {
            App.setRoot("secondary");
            SecondaryController secondaryController = (SecondaryController)App.fxmlLoader.getController();
            mangaSeleccionado = new Manga();
            secondaryController.setManga(mangaSeleccionado);
        } catch (IOException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } 
    }

    @FXML
    private void onActtionButtonEditar(ActionEvent event) {
        if (mangaSeleccionado != null) {
            try {
                App.setRoot("secondary");
                SecondaryController secondaryController = (SecondaryController)App.fxmlLoader.getController();
                secondaryController.setManga(mangaSeleccionado);
            } catch (IOException ex) {
                Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Atención");
            alert.setHeaderText("Debe seleccionar un registro");
            alert.showAndWait();
        }
    }

    @FXML
    private void onActtionButtonSuprimir(ActionEvent event) {
        if(mangaSeleccionado != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar");
            alert.setHeaderText("¿Desea suprimir el siguiente registro?");
            alert.setContentText(mangaSeleccionado.getNombre() + " "
                    + mangaSeleccionado.getAutor());
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                App.em.getTransaction().begin();
                App.em.remove(mangaSeleccionado);
                App.em.getTransaction().commit();
                tableViewManga.getItems().remove(mangaSeleccionado);
                tableViewManga.getFocusModel().focus(null);
                tableViewManga.requestFocus();
            } else {
                int numFilaSeleccionada = tableViewManga.getSelectionModel().getSelectedIndex();
                tableViewManga.getItems().set(numFilaSeleccionada, mangaSeleccionado);
                TablePosition pos = new TablePosition(tableViewManga, numFilaSeleccionada, null);
                tableViewManga.getFocusModel().focus(pos);
                tableViewManga.requestFocus();
                } 
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Atención");
            alert.setHeaderText("Debe seleccionar un registro");
            alert.showAndWait();
        }
    }

    @FXML
    private void onActtionButtonGuardar(ActionEvent event) {
        if (mangaSeleccionado != null) {
            mangaSeleccionado.setNombre(textFieldNombre.getText());
            mangaSeleccionado.setAutor(textFieldAutor.getText());
            App.em.getTransaction().begin();
            App.em.merge(mangaSeleccionado);
            App.em.getTransaction().commit();
            
            int numFilaSeleccionada = tableViewManga.getSelectionModel().getSelectedIndex();
            tableViewManga.getItems().set(numFilaSeleccionada, mangaSeleccionado);
            TablePosition pos = new TablePosition(tableViewManga, numFilaSeleccionada, null);
            tableViewManga.getFocusModel().focus(pos);
            tableViewManga.requestFocus();
        }
    }

    @FXML
    private void onActtionButtonBuscar(ActionEvent event) {
        if (!textFieldBuscar.getText().isEmpty()) {
            if (checkBoxCoincide.isSelected()) {
                Query queryMangaFindByNombre = App.em.createNamedQuery("Manga.findByNombre");
                queryMangaFindByNombre.setParameter("nombre", textFieldBuscar.getText());
                List<Manga> listManga = queryMangaFindByNombre.getResultList();
                tableViewManga.setItems(FXCollections.observableArrayList(listManga));
            } else {
                String strQuery = "SELECT * FROM Manga WHERE LOWER(nombre) LIKE ";
                strQuery += "\'%" + textFieldBuscar.getText().toLowerCase() + "%\'";
                Query queryMangaFindLikeNombre = App.em.createNativeQuery(strQuery, Manga.class);
                
                List<Manga> listManga = queryMangaFindLikeNombre.getResultList();
                tableViewManga.setItems(FXCollections.observableArrayList(listManga));
            }
        } else {
            cargarTodosMangas();
        }
    }
}
