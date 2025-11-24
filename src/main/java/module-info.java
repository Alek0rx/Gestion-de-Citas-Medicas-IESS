module gestion.gestion_citas_medicas {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.base;
    requires java.desktop;
    //requires gestion.gestion_citas_medicas;
    opens gestion.gestion_citas_medicas.ClasesNormales to javafx.base;

    opens gestion.gestion_citas_medicas to javafx.fxml;
    exports gestion.gestion_citas_medicas;

    opens gestion.gestion_citas_medicas.Controladores to javafx.fxml;
    exports gestion.gestion_citas_medicas.Controladores;
}