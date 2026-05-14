package main.java.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServiceLambda
        extends Application {

    @Override
    public void start(Stage stage)
            throws Exception {

        FXMLLoader loader =
                new FXMLLoader(
                        getClass()
                        .getResource(
                                "/ui/views/login.fxml"
                        )
                );

        Scene scene =
                new Scene(
                        loader.load(),
                        600,
                        400
                );

        scene.getStylesheets().add(
                getClass()
                .getResource(
                        "/ui/styles/style.css"
                )
                .toExternalForm()
        );

        stage.setTitle(
                "Support Course Management"
        );

        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {

        launch(args);
    }
}