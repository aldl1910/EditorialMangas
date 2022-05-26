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
        // MUESTRA EL CONTENIDO DE LA BD EN LAS COLUMNAS 
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
        
        // CÓDIGO QUE SE EJECUTA CUANDO SE SELECCIONA ALGÚN ELEMENTO DEL TABLEVIEW
        // EN UN PAR DE TEXTFIELD MUESTRA EL NOMBRE Y EL AUTOR 
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
    }
    
     @FXML
    private void onActtionButtonNuevo(ActionEvent event) {
        try {
            // LLAMA A LA SEGUNDA PANTALLA
            App.setRoot("secondary");
            SecondaryController secondaryController = (SecondaryController)App.fxmlLoader.getController();
            // CREA EL MANGA VACÍO
            mangaSeleccionado = new Manga();
            // SE PASA EL MANGA A LA SEGUNDA PANTALLA COMO UN OBJETO VACÍO
            secondaryController.setManga(mangaSeleccionado);
        } catch (IOException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } 
    }

    @FXML
    private void onActtionButtonEditar(ActionEvent event) {
        if (mangaSeleccionado != null) {
            try {
                // LLAMA A LA SEGUNDA PANTALLA
                App.setRoot("secondary");
                // COJE OBJETO DEL CONTROLADOR
                SecondaryController secondaryController = (SecondaryController)App.fxmlLoader.getController();
                // CON ESE OBJETO LE DAMOS EL MANGA
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
        // ASEGURAMOS QUE HAYA UN MANGA SELECCIONADO
        if(mangaSeleccionado != null){
            // ALERT PARA CONFIRMAR SI DESEA BORRAR EL REGISTRO
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar");
            alert.setHeaderText("¿Desea suprimir el siguiente registro?");
            alert.setContentText(mangaSeleccionado.getNombre() + " "
                    + mangaSeleccionado.getAutor());
            Optional<ButtonType> result = alert.showAndWait();
            // DETECTA SI HA PULSADO A "ACEPTAR" Y LO BORRA
            if (result.get() == ButtonType.OK){
                App.em.getTransaction().begin();
                App.em.remove(mangaSeleccionado);
                App.em.getTransaction().commit();
                tableViewManga.getItems().remove(mangaSeleccionado);
                tableViewManga.getFocusModel().focus(null);
                tableViewManga.requestFocus();
            } else { // VUELVE A SELECCIONAR EL CONTENIDO DEL TABLEVIEW
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
        // SE ASEGURA QUE HAYA UN MANGA SELECCIONADO
        if (mangaSeleccionado != null) {
            // CAMBIA EL NOMBRE Y EL AUTOR DEL MANGA POR EL QUE ESCRIBIMOS EN EL TEXTFIELD
            mangaSeleccionado.setNombre(textFieldNombre.getText());
            mangaSeleccionado.setAutor(textFieldAutor.getText());
            
            // VUELCA EL OBJETO EN LA BASE DE DATOS
            // EMPIEZA CON UNA TRANSACCIÓN, PASA EL OBJETO Y CIERRA LA TRANSACCIÓN
            App.em.getTransaction().begin();
            App.em.merge(mangaSeleccionado);
            App.em.getTransaction().commit();
            
            // ACTUALIZA EL TABLEVIEW
            int numFilaSeleccionada = tableViewManga.getSelectionModel().getSelectedIndex();
            tableViewManga.getItems().set(numFilaSeleccionada, mangaSeleccionado);
            TablePosition pos = new TablePosition(tableViewManga, numFilaSeleccionada, null);
            tableViewManga.getFocusModel().focus(pos);
            tableViewManga.requestFocus();
        }
    }

    @FXML
    private void onActtionButtonBuscar(ActionEvent event) {
        // PREGUNTAMOS QUE EL TEXTFIELD NO ESTÉ VACÍO
        if (!textFieldBuscar.getText().isEmpty()) {
            // SI EL CHECKBOX ESTÁ SELECCIONADO BUSCARA POR LOS NOMBRES QUE COINCIDAN EXACTAMENTE
            // CON EL CONTENIDO DEL TEXTFIELD
            if (checkBoxCoincide.isSelected()) {
                Query queryMangaFindByNombre = App.em.createNamedQuery("Manga.findByNombre");
                queryMangaFindByNombre.setParameter("nombre", textFieldBuscar.getText());
                List<Manga> listManga = queryMangaFindByNombre.getResultList();
                tableViewManga.setItems(FXCollections.observableArrayList(listManga));
            } else {
                // SI NO ESTÁ SELECCIONADO, BUSCARÁ POR LOS NOMBRES QUE COINCIDAN EN ALGUNA LETRA
                // YA SEA EN MAYÚSCULA O EN MINÚSCULA
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
