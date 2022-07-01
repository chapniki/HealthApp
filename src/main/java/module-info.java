module cz.vse.fimed {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens cz.vse.fimed.UI to javafx.fxml;
    exports cz.vse.fimed.UI;
}
