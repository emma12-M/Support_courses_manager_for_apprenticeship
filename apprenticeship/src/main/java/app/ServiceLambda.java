package app;

import Models.Parent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repositories.ParentRepository;

public class ServiceLambda
        extends Application {

    @Override
    public void start(Stage stage)
            throws Exception {

    	System.out.println(
    		    getClass().getResource("/ui/views/login.fxml")
    		);
    	
    	 // ===== TEST : création d'un parent =====
      /*  ParentRepository repository =
                new ParentRepository();

        Parent parent =
                new Parent(
                        1,
                        "Emma",
                        "Smith",
                        "emma@gmail.com",
                        "1234"
                );

        repository.save(parent);*/
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

