package es.antoniodominguez.editorialmangas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    public static EntityManager em;
    public static FXMLLoader fxmlLoader;
    @Override
    public void start(Stage stage) throws IOException {
        
        // CONEXIÓN CON LA BASE DE DATOS 
        try{
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("EditorialMangasPU");
            em = emf.createEntityManager();
        } catch(PersistenceException ex){
            // DETECTA SI LA BASE DE DATOS ESTÁ CONECTADA A LA HORA DE INICIAR EL PROGRAMA
            Logger.getLogger(App.class.getName()).log(Level.WARNING, ex.getMessage(),ex);
            Alert  alert =  new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Atención");
            alert.setHeaderText("No se ha podido abrir la base de datos\n"
                + "Compruebe que no se encuentra ya abierta la aplicación");
            alert.showAndWait();
        }
        
        scene = new Scene(loadFXML("primary"), 840, 630);
        stage.setScene(scene);
        stage.show();
        
    }
    
    @Override
    public void stop() throws Exception{
        em.close();
        try{
            DriverManager.getConnection("jdbc:derby:BDEditorialMangas;shutdown=true");
        }catch(SQLException ex){
            
        }
    }
    
    // CARGA EL FXML
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    // CREA UN OBJETO DE FXMLLOADER
    private static Parent loadFXML(String fxml) throws IOException {
        fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}