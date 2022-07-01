package cz.vse.fimed.UI;
import cz.vse.fimed.dbapi.JDBCDao;
import cz.vse.fimed.profile.Doctor;
import cz.vse.fimed.profile.Pacient;
import cz.vse.fimed.profile.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.IOException;

public class Start extends Application {

    /**
     * Metoda je volána poté, co je systém připraven na spuštění aplikace.
     * <p>
     * Metoda předvídá potenciální zobrazení výjimky a program je díky ní schopen ji zpracovat.
     *
     * @param stage primární Stage, do které lze pak nastavit scénu aplikace
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("registration.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle("FiMed App");
        stage.setMaximized(true);
        stage.show();

    }

    /**
     * Spouštěcí metoda aplikace. Vyhodnotí parametry, se kterými byla aplikace
     * spuštěna, a spustí grafickou hru.
     * <p>
     *
     * @param args parametry aplikace z příkazové řádky
     */
    public static void main(String[] args) throws SQLException {
        launch();
    }

}