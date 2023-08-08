module com.example.mypuzzlefx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;

    opens com.example.mypuzzlefx to javafx.fxml;
    exports com.example.mypuzzlefx;
}