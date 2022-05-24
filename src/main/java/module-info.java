module es.antoniodominguez.editorialmangas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.instrument;
    requires java.persistence;
    requires java.sql;
    requires java.base;
    
    opens es.antoniodominguez.editorialmangas.entities;
    opens es.antoniodominguez.editorialmangas to javafx.fxml;
    exports es.antoniodominguez.editorialmangas;
}
