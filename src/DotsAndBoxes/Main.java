package DotsAndBoxes;

import java.util.Optional;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Main extends Application {
    
    private FlowPane root = new FlowPane();
    private Button buttonVsAI = new Button("Play against AI");
    private Button buttonVsHuman = new Button ("Play against human");
    private Board board;
    private int size;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        buttonVsHuman.setOnAction((event) -> {
           TextInputDialog dialog = new TextInputDialog();
           
           dialog.setTitle("Set Playing Size");
           dialog.setHeaderText("Enter plot size: ");

           Optional<String> result = dialog.showAndWait();
           
           result.ifPresent(dimension -> {
               System.out.println("dimension is: " + dimension );
               size = Integer.parseInt(dimension);
           });
           board = new Board(primaryStage, false, size);
           
        });
        
        buttonVsAI.setOnAction((event) -> {
            TextInputDialog dialog = new TextInputDialog();
            
            dialog.setTitle("Set Playing Size");
            dialog.setHeaderText("Enter plot size: ");

            Optional<String> result = dialog.showAndWait();
            
            result.ifPresent(dimension -> {
                System.out.println("dimension is: " + dimension );
                size = Integer.parseInt(dimension);
            });
            board = new Board(primaryStage, true, size);
            
         });
        
        root.getChildren().addAll(buttonVsAI, buttonVsHuman);
        
        Scene scene = new Scene(root, 650, 650);
        primaryStage.setTitle("shitty game");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
        
    }
    
    public static void main (String[] args) {
        launch(args);
    }

}

