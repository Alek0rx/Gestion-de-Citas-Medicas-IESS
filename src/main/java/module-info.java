module gestion.gestion_citas_medicas {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens gestion.gestion_citas_medicas to javafx.fxml;
    exports gestion.gestion_citas_medicas;
}