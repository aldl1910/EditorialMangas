package es.antoniodominguez.editorialmangas;

import es.antoniodominguez.editorialmangas.entities.Manga;
import es.antoniodominguez.editorialmangas.entities.Editorial;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.util.StringConverter;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javax.persistence.Query;
import javax.persistence.RollbackException;
import javafx.scene.image.Image;
import java.io.File;
import javafx.stage.FileChooser;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import java.util.Optional;


public class SecondaryController{
    private Manga manga;
    private static final String PUBLICANDO = "P";
    private static final String PAUSADO = "S";
    private static final String CANCELADO = "C";
    private static final String FINALIZADO = "F";
    
    private static final String CARPETA_FOTOS = "Fotos";
     
    @FXML
    private BorderPane rootSecondary;
    @FXML
    private Label CheckBoxLider;
    @FXML
    private Button ButtonGuardarSecondary;
    @FXML
    private Button ButtonCancelarSecondary;
    @FXML
    private TextField textFieldNombreSecondary;
    @FXML
    private Button ButtonExaminarSecondary;
    @FXML
    private Button ButtonSuprimirSecondary;
    @FXML
    private ToggleGroup estadoManga;
    @FXML
    private TextField textFieldAutorSecondary;
    @FXML
    private TextField textFieldISBMSecondary;
    @FXML
    private TextField textFieldVolumenSecondary;
    @FXML
    private TextField textFieldCapitulosSecondary;
    @FXML
    private TextField textFieldNumPaginasSecondary;
    @FXML
    private TextField textFieldEmailEditorialSecondary;
    @FXML
    private TextField textFieldIdiomaSecondary;
    @FXML
    private TextField textFieldPrecioSecondary;
    @FXML
    private CheckBox CheckBox18;
    @FXML
    private RadioButton RadioButtonPublicando;
    @FXML
    private RadioButton RadioButtonPausado;
    @FXML
    private RadioButton RadioButtonCancelado;
    @FXML
    private RadioButton RadioButtonFinalizado;
    @FXML
    private DatePicker DatePickerPublicacion;
    @FXML
    private ComboBox<Editorial> ComboBoxEditorial;
    @FXML
    private ImageView imageViewLogo;
    
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
    
      
    
    public void setManga(Manga manga) {
        App.em.getTransaction().begin();
        this.manga = manga;
        mostrarDatos();
    }
//    
    private void  mostrarDatos(){
        textFieldNombreSecondary.setText(manga.getNombre());
        textFieldAutorSecondary.setText(manga.getAutor());
        textFieldISBMSecondary.setText(manga.getIsbn());
        textFieldEmailEditorialSecondary.setText(manga.getEmailEditorial());
        textFieldIdiomaSecondary.setText(manga.getIdioma());
        
        if (manga.getVolumen() != null){
            textFieldVolumenSecondary.setText(String.valueOf(manga.getVolumen()));
        }
        if (manga.getCapitulos() != null){
            textFieldCapitulosSecondary.setText(String.valueOf(manga.getCapitulos()));
        }
        if (manga.getNumpaginas() != null){
            textFieldNumPaginasSecondary.setText(String.valueOf(manga.getNumpaginas()));
        }
        if (manga.getPrecio() != null){
            textFieldPrecioSecondary.setText(String.valueOf(manga.getPrecio()));
        }
        if (manga.getRestriccionEdad() != null){
            CheckBox18.setSelected(manga.getRestriccionEdad());
        }
        if (manga.getEstado() != null){
            switch (manga.getEstado()){
                case PUBLICANDO:
                    RadioButtonPublicando.setSelected(true);
                    break;
                case PAUSADO:
                    RadioButtonPausado.setSelected(true);
                    break;
                case CANCELADO:
                    RadioButtonCancelado.setSelected(true);
                    break;
                case FINALIZADO:
                    RadioButtonFinalizado.setSelected(true);
                    break;
            }
        }
        if (manga.getFechaPublicaion() != null) {
            Date date = manga.getFechaPublicaion();
            Instant instant = date.toInstant();
            ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
            LocalDate localDate = zdt.toLocalDate();
            DatePickerPublicacion.setValue(localDate);
        }
        Query queryEditorialFindAll = App.em.createNamedQuery("Editorial.findAll");
        List<Editorial> listEditorial = queryEditorialFindAll.getResultList();
        
        ComboBoxEditorial.setItems(FXCollections.observableList(listEditorial));
        if (manga.getEditorial() != null) {
            ComboBoxEditorial.setValue(manga.getEditorial());
        }
        ComboBoxEditorial.setCellFactory((ListView<Editorial> l) -> new ListCell<Editorial>() {
            @Override
            protected void updateItem(Editorial editorial, boolean empty) {
                super.updateItem(editorial, empty);
                if (editorial == null || empty){
                    setText("");
                } else {
                    setText(editorial.getId() + "-" + editorial.getNombre());
                }
            }
        });
        
        ComboBoxEditorial.setConverter(new StringConverter<Editorial>(){
            @Override
            public String toString(Editorial editorial) {
                if (editorial == null) {
                    return null;
                } else {
                    return editorial.getId() + "-" + editorial.getNombre();
                }
            }
            @Override
            public Editorial fromString(String userId) {
                return null;
            }
        });
        
        if (manga.getLogo() != null) {
            String imageFileName = manga.getLogo();
            File file = new File(CARPETA_FOTOS + "/" + imageFileName);
            if (file.exists()) {
                Image image = new Image (file.toURI().toString());
                imageViewLogo.setImage(image);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "No se encuentra la imagen");
                alert.showAndWait();
            }
        }
    }
    @FXML
    private void onActtionButtonGuardar(ActionEvent event) {
        int numFilaSeleccionada;
        boolean errorFormato = false;
        
        manga.setNombre(textFieldNombreSecondary.getText());
        manga.setAutor(textFieldAutorSecondary.getText());
        manga.setIsbn(textFieldISBMSecondary.getText());
        manga.setEmailEditorial(textFieldEmailEditorialSecondary.getText());
        manga.setIdioma(textFieldIdiomaSecondary.getText());
        
        if (!textFieldVolumenSecondary.getText().isEmpty()){
            try{
                manga.setVolumen(Short.valueOf(textFieldVolumenSecondary.getText()));
            } catch (NumberFormatException ex) {
                errorFormato = true;
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Número de volúmenes no válido");
                alert.showAndWait();
                textFieldVolumenSecondary.requestFocus();
            }
        }
        if (!textFieldCapitulosSecondary.getText().isEmpty()){
            try{
                manga.setCapitulos(Short.valueOf(textFieldCapitulosSecondary.getText()));
            } catch (NumberFormatException ex) {
                errorFormato = true;
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Número de capítulos no válido");
                alert.showAndWait();
                textFieldCapitulosSecondary.requestFocus();
            }
        }
        if (!textFieldNumPaginasSecondary.getText().isEmpty()){
            try{
                manga.setNumpaginas(Short.valueOf(textFieldNumPaginasSecondary.getText()));
            } catch (NumberFormatException ex) {
                errorFormato = true;
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Número de páginas no válido");
                alert.showAndWait();
                textFieldNumPaginasSecondary.requestFocus();
            }
        }
        if (!textFieldPrecioSecondary.getText().isEmpty()){
            try{
                manga.setPrecio(BigDecimal.valueOf(Double.valueOf(textFieldPrecioSecondary.getText()).doubleValue()));
            } catch (NumberFormatException ex) {
                errorFormato = true;
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Número de precio no válido");
                alert.showAndWait();
                textFieldPrecioSecondary.requestFocus();
            }
        }
        
        manga.setRestriccionEdad(CheckBox18.isSelected());
        
        if (RadioButtonPublicando.isSelected()){
            manga.setEstado(PUBLICANDO);
        } else if (RadioButtonPausado.isSelected()){
            manga.setEstado(PAUSADO);
        } else if (RadioButtonCancelado.isSelected()){
            manga.setEstado(CANCELADO);
        } else if (RadioButtonFinalizado.isSelected()){
            manga.setEstado(FINALIZADO);
        }
        
        if (DatePickerPublicacion.getValue() != null) {
            LocalDate LocalDate = DatePickerPublicacion.getValue();
            ZonedDateTime zonedDateTime =  LocalDate.atStartOfDay(ZoneId.systemDefault());
            Instant instant = zonedDateTime.toInstant();
            Date date = Date.from(instant);
            manga.setFechaPublicaion(date);
        } else {
            manga.setFechaPublicaion(null);
        }
        
        manga.setEditorial(ComboBoxEditorial.getValue());
        
        if (!errorFormato) {
            try {
                if (manga.getId() == null) {
                    System.out.println("Guardando nuevo manga en BD");
                    App.em.persist(manga);
                } else {
                    System.out.println("Actualizando manga en BD");
                    App.em.merge(manga);
                }
                App.em.getTransaction().commit();
                
                App.setRoot("primary");
            } catch (RollbackException ex) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("No se han podido guardar los cambios. " + 
                        "Compruebe que los datos cumplen los requisitos");
                alert.setContentText(ex.getLocalizedMessage());
                alert.showAndWait();
            } catch (IOException ex) {
                Logger.getLogger(SecondaryController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    @FXML
    private void onActtionButtonCancelar(ActionEvent event) {
        App.em.getTransaction().rollback();
        
        try {
            App.setRoot("primary");
        } catch  (IOException ex){
            Logger.getLogger(SecondaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onActtionButtonExaminar(ActionEvent event) {
        File carpetaFotos = new File(CARPETA_FOTOS);
        if(!carpetaFotos.exists()){
            carpetaFotos.mkdir();
        }
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sleccionar imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes (jpg ,png)", "*.jpg", "*.png"),
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );
        
        File file = fileChooser.showOpenDialog(rootSecondary.getScene().getWindow());
        if (file != null){
            try {
                Files.copy(file.toPath(), new File (CARPETA_FOTOS + "/" + file.getName()).toPath());
                manga.setLogo(file.getName());
                Image image = new Image(file.toURI().toString());
                imageViewLogo.setImage(image);
            } catch (FileAlreadyExistsException ex){
                Alert alert = new Alert(Alert.AlertType.WARNING, "Nombre de archivo duplicado");
                alert.showAndWait();
            } catch (IOException ex){
                Alert alert = new Alert(Alert.AlertType.WARNING, "No se ha podido guardar la imagen");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void onActtionButtonSuprimir(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar supresión de imagen");
        alert.setHeaderText("¿Desea SUPRIMIR el archivo asociado a al imagen, \n" 
            + "quitar la foto pero MANTENER el archivo, \no CANCELAR la operación");
        alert.setContentText("Elija la opción deseada:");
        
        ButtonType buttonTypeEliminar = new ButtonType("Suprimir");
        ButtonType buttonTypeMantener = new ButtonType("Mantener");
        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alert.getButtonTypes().setAll(buttonTypeEliminar, buttonTypeMantener, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeEliminar){
            String imageFileName = manga.getLogo();
            File file = new File(CARPETA_FOTOS + "/" + imageFileName);
            if (file.exists()){
                file.delete();
            }
            manga.setLogo(null);
            imageViewLogo.setImage(null);
        } else if (result.get() == buttonTypeMantener) {
            manga.setLogo(null);
            imageViewLogo.setImage(null);
        }
    }

}