package DotsAndBoxes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This class represents the board and all the logic in the board itself. 
 *
 */
public class Board {
    
    /**Constants*/
    public static final int INITIAL_OFFSET = 30;  //the distance between the leftmost circle and the edge of the board
    public static final int TRANSPOSE_VALUE = 50;  //the distance between each circle
    
    /**Initialize global variables*/
    private BorderPane root;
    private Text titleBox;
    private GameStatus status;
    private Pane board;
    
    private boolean hasClicked = false;
    private boolean playerOneTurn = true;
    
    private int playerOneScore = 0;
    private int playerTwoScore = 0;
    
    /*The board has knowledge of every object being created in the board*/
    private List<Box> boxes = new ArrayList<Box>();
    private List<Point2D> points = new ArrayList<Point2D>();
    private List<Circle> circles = new ArrayList<Circle>();
    private TreeSet<Line> lines = new TreeSet<Line>(new Comparator<Line>() {
        @Override
        public int compare(Line line1, Line line2) {
            // TODO Auto-generated method stub
            if (line1.getStartX() == line2.getStartX() && line1.getEndX() == line2.getEndX() &&
                    line1.getStartY() == line2.getStartY() && line1.getEndY() == line2.getEndY()) {
                return 0;
            }

            return -1;
        } 
    });
    
    /*Defined globally to be used within event handlers. These represent the start and end points of a line.*/
    private Circle startCircle;
    private Circle endCircle;
    
    /**
     * Constructor. Sets up the board and its objects.
     * @param stage
     * @param playAI
     * @param size
     */
    public Board(Stage stage, boolean playAI, int size) {
        
        board = new Pane();
        root = new BorderPane();

        int xCoordinate = 0 + INITIAL_OFFSET;
        int yCoordinate = 0 + INITIAL_OFFSET;
        
        Point2D topRightPoint;
        Point2D topLeftPoint;
        Point2D bottomRightPoint;
        Point2D bottomLeftPoint;
        
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1; j++) {
                topLeftPoint = new Point2D(xCoordinate, yCoordinate);
                topRightPoint = new Point2D(xCoordinate + TRANSPOSE_VALUE , yCoordinate);
                bottomLeftPoint = new Point2D(xCoordinate, yCoordinate + TRANSPOSE_VALUE);
                bottomRightPoint = new Point2D(xCoordinate + TRANSPOSE_VALUE, yCoordinate + TRANSPOSE_VALUE);
                points.add(topLeftPoint);
                points.add(topRightPoint);
                points.add(bottomLeftPoint);
                points.add(bottomRightPoint);
                
                Box box = new Box(topLeftPoint, topRightPoint, bottomLeftPoint, bottomRightPoint, this);
                boxes.add(box);
                
                xCoordinate += TRANSPOSE_VALUE;
            }
            xCoordinate = 0 + INITIAL_OFFSET;
            yCoordinate += TRANSPOSE_VALUE;
        }
        
        if (playAI) {
            setCircleEventHandlingAgainstAI();
        }
        else {
            setCircleEventHandling(); 
        }

        titleBox = new Text("Dots and Boxes - 2 player");
        titleBox.setFont(Font.font(30));
        root.setCenter(board);
        root.setTop(titleBox);
        status = GameStatus.getInstance(this);
        Scene scene = new Scene(root, 650, 350);
        stage.setScene(scene);
        stage.show();        
    }
   
    /**
     * Adds event handling to the dots shown in the board. Drives the main game logic. 
     */
    private void setCircleEventHandling() {
        
        for (int i = 0; i < circles.size(); i++) {
           circles.get(i).setOnMouseClicked((MouseEvent event) -> {  
               //If it is the 2nd press
               if (hasClicked) {
                   //System.out.println("pressed on circle 2ND TIME, boxes size: " + boxes.size());
                   endCircle = (Circle)event.getSource();
                   if (!checkValidMove(startCircle, endCircle)) {
                       System.out.println("invalid move");
                       hasClicked = false;
                       return;
                   }
                   Line line = circlesToLine(startCircle, endCircle);

                   for (int k = 0; k < boxes.size(); k++) {
                       boxes.get(k).findAndSetLineColor(line, playerOneTurn);
                   } 

                   if (checkForFilledBoxes()) {
                       status.setPlayerTurn(playerOneTurn); //stay the same turn
                       if (checkForWin()) {
                           //can add additional logic to remove all event handling                     
                           return;
                       }
                   }
                   else {  
                       playerOneTurn = !playerOneTurn;  //change turns
                       status.setPlayerTurn(playerOneTurn);
                   }                      
                   hasClicked = false;
                   
               }
               else {
                   //System.out.println("pressed on circle 1ST TIME");
                   startCircle = (Circle)event.getSource();
                   hasClicked = true;
               }
           });
        }
    }
    
    private void setCircleEventHandlingAgainstAI() {
        for (int i = 0; i < circles.size(); i++) {
            circles.get(i).setOnMouseClicked((MouseEvent event) -> {  
                //If it is the 2nd press
                if (hasClicked) {
                    System.out.println("pressed on circle 2ND TIME, boxes size: " + boxes.size());
                    endCircle = (Circle)event.getSource();
                    if (!checkValidMove(startCircle, endCircle)) {
                        System.out.println("invalid move");
                        hasClicked = false;
                        return;
                    }
                    Line line = circlesToLine(startCircle, endCircle);

                    for (int k = 0; k < boxes.size(); k++) {
                        boxes.get(k).findAndSetLineColor(line, playerOneTurn);
                    } 

                    if (checkForFilledBoxes()) {
                        status.setPlayerTurn(playerOneTurn); //stay the same turn
                        if (checkForWin()) {
                            //can add additional logic to remove all event handling                     
                            return;
                        }
                    }
                    else {  
                        playerOneTurn = !playerOneTurn;  //change turns
                        status.setPlayerTurn(playerOneTurn);
                    }                  
                    makeComputerMove();
                    hasClicked = false;
                    
                }
                else {
                    //System.out.println("pressed on circle 1ST TIME");
                    startCircle = (Circle)event.getSource();
                    hasClicked = true;
                }
            });
         }
    }
    
    /**
     * Checks if the line being "drawn" is valid or not
     * @param circle1  first point of the line
     * @param circle2  second point of the line
     * @return  true if valid, false otherwise
     */
    private boolean checkValidMove(Circle circle1, Circle circle2) {
        
        /*check if the 2 points are the same point...*/
        if (circle1.getCenterX() == circle2.getCenterX() && circle1.getCenterY() == circle2.getCenterY()) {
            return false;
        }
        /*check if the line is too long*/
        if (Math.abs(circle1.getCenterX() - circle2.getCenterX()) > TRANSPOSE_VALUE || 
                Math.abs(circle1.getCenterY() - circle2.getCenterY()) > TRANSPOSE_VALUE){
            return false;
        }
        /*check if the line is diagonal... diagonals not allowed*/
        if (circle1.getCenterX() != circle2.getCenterX() && circle1.getCenterY() != circle2.getCenterY()) {
            return false;
        }
        
        /*checks if the line is already 'drawn' Uses a helper method */
        Line line = circlesToLine(circle1, circle2);
        for (Line storedLine : lines) {
            if (storedLine.getStartX() == line.getStartX() && storedLine.getEndX() == line.getEndX() 
                    && storedLine.getStartY() == line.getStartY() && storedLine.getEndY() == line.getEndY()) {
                if (storedLine.isVisible()) {
                    System.out.println("Can't overwrite a line!");
                    System.out.println("total lines is: " + lines.size());
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Loops through all the boxes and see if there is a RECENTLY completed box. 
     * @return
     */
    private boolean checkForFilledBoxes() {

        boolean newFill = false;
        for (int i = 0; i < boxes.size(); i++) {
            if (boxes.get(i).checkForBoxFilled()) {
                newFill = true;
            }
        }
        return newFill;
    }
    
    /**
     * Checks if the game is over/if there is a winner
     * @return
     */
    private boolean checkForWin() {

        //if a box is not filled, game is not over yet, so no winner
        for (int i = 0; i < boxes.size(); i++) {
            if (!boxes.get(i).hasFilled) {
                return false;
            }
        }
        
        if (playerOneScore > playerTwoScore) {
            status.setGameWin(true);
            
        }
        else if (playerOneScore < playerTwoScore) {
            status.setGameWin(false);
        }
        else {
            status.setGameDrawn();
        }
        return true;
       
    }
    /**
     * Helper method that does 2 things: 
     * 1) decides which circle is the starting circle and ending circle
     * 2) creates a line from the 2 circles (aka "dots");
     * @param circle1
     * @param circle2
     * @return
     */
    private Line circlesToLine(Circle circle1, Circle circle2) {  
        Circle tempCircle;
        if (circle1.getCenterX() > circle2.getCenterX() || circle1.getCenterY() > circle2.getCenterY()) {
            tempCircle = circle1;
            circle1 = circle2;
            circle2 = tempCircle;
        }
        Line line = new Line (circle1.getCenterX(), circle1.getCenterY(), circle2.getCenterX(), circle2.getCenterY());
        return line;
    }

    /**Getters and setters*/
    public void addCircles(ArrayList<Circle> circles) {
        this.circles.addAll(circles);
    }
    
    public void addLines(ArrayList<Line> lines) {
        this.lines.addAll(lines);
    }
    
    public Pane getPane() {
        return board;
    }
    
    public BorderPane getRoot() {
        return root;
    }
    
    public int[] getPlayerScores() {
        int scores[] = {playerOneScore, playerTwoScore};
        return scores;
    }
    
    public void incrementPlayerOneScore() {
        playerOneScore++;
    }
    public void incrementPlayerTwoScore() {
        playerTwoScore++;
    }
    
    public void makeComputerMove() {
        
        TreeSet<Line> legalMoves = new TreeSet<Line>(new Comparator<Line>() {
            @Override
            public int compare(Line line1, Line line2) {
                // TODO Auto-generated method stub
                if (line1.getStartX() == line2.getStartX() && line1.getEndX() == line2.getEndX() &&
                        line1.getStartY() == line2.getStartY() && line1.getEndY() == line2.getEndY()) {
                    return 0;
                }
                if (line1.getStartX() == line2.getEndX() && line1.getEndX() == line2.getStartX() &&
                        line1.getStartY() == line2.getEndY() && line1.getEndY() == line2.getStartY()) {
                    return 0;
                }
                if (line2.isVisible()) {
                    System.out.println("line 2 is visible");
                    return 0;
                }
                return -1;
            } 
        });
        for (Line line : lines) {
            for (int i = 0; i < boxes.size(); i++) {
                if (!boxes.get(i).checkIfLineMatchAndFilled(line)) {
                    if (!line.isVisible()) {
                        legalMoves.add(line);
                    }
                }
            }
        }
        System.out.println("legalmoves size is: " + legalMoves.size());
        for (int i = 0; i < legalMoves.size(); i++) {
            
        }
        Random rand = new Random();
        int randomMove = rand.nextInt(legalMoves.size());
       // System.out.println("computer picked movie: " + legalMoves.get(randomMove));
        
        for (Line move : legalMoves) {
            randomMove--;
            if (randomMove == 0) {
                if (move.isVisible()) {
                    legalMoves.pollFirst().setVisible(true);
                    move.setStroke(Color.GOLD);
                }
                else {
                    move.setVisible(true);
                    move.setStroke(Color.PINK);
                }
            }
        }
    }

}
