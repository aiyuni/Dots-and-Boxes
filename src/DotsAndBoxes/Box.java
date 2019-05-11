package DotsAndBoxes;

import java.util.ArrayList;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Represents a square in the board. A square contains 4 lines and 4 points. 
 * Contains logic such as checking if the box/lines within the box are filled.
 */
public class Box {
    
    private Line topLine;
    private Line rightLine;
    private Line bottomLine;
    private Line leftLine;
    
    private ArrayList<Line> lines = new ArrayList<Line>();
    private ArrayList<Point2D> boxPoint2Ds = new ArrayList<Point2D>();
    private ArrayList<Circle> boxDots = new ArrayList<Circle>();
    
    private Board board;
    private Text text;
    
    boolean player1 = true;
    boolean hasFilled = false;
    boolean hasText = false;
   
    public Box(Point2D topLeft, Point2D topRight, Point2D bottomLeft, Point2D bottomRight, Board board) {
        
        boxPoint2Ds.add(topLeft);
        boxPoint2Ds.add(topRight);
        boxPoint2Ds.add(bottomLeft);
        boxPoint2Ds.add(bottomRight);
        
        topLine = new Line(topLeft.getX(), topLeft.getY(), topRight.getX(), topRight.getY());
        rightLine = new Line(topRight.getX(), topRight.getY(), bottomRight.getX(), bottomRight.getY());
        bottomLine = new Line(bottomLeft.getX(), bottomLeft.getY(), bottomRight.getX(), bottomLeft.getY());
        leftLine = new Line (topLeft.getX(), topLeft.getY(), bottomLeft.getX(), bottomLeft.getY());
        this.board = board;
        
        initializeLineStates();
    }

    private void initializeLineStates() {
        
        lines.add(topLine);
        lines.add(bottomLine);
        lines.add(rightLine);
        lines.add(leftLine);
        
        topLine.setVisible(false);
        topLine.setFill(Color.BLACK);
        rightLine.setVisible(false);
        rightLine.setFill(Color.BLACK);
        bottomLine.setVisible(false);
        bottomLine.setFill(Color.BLACK);
        leftLine.setVisible(false);
        leftLine.setFill(Color.BLACK);
        
        for (int i = 0; i < lines.size(); i++) {
            board.getPane().getChildren().add(lines.get(i));
        }
        for (int i = 0; i < boxPoint2Ds.size(); i++) {
            Circle circle = new Circle(boxPoint2Ds.get(i).getX(), boxPoint2Ds.get(i).getY(), 5);
            boxDots.add(circle);
            board.getPane().getChildren().add(circle);
        }
        board.addCircles(boxDots);
        board.addLines(lines);
    }
    
    
    public void findAndSetLineColor(Line line, boolean player1) {
        
        if (hasFilled) {
            //box is filled already...
            return;
        }
        System.out.println("line being passed has coordinates: " + line.toString());
        for (int i = 0; i < lines.size(); i++) {
            if (lineCompare(line, lines.get(i))) {
                if (player1) {
                    lines.get(i).setVisible(true);
                    lines.get(i).setStroke(Color.RED);
                    this.player1 = true;
                    
                }
                else {
                    lines.get(i).setVisible(true);
                    lines.get(i).setStroke(Color.BLUE);
                    this.player1 = false;
                    
                }
            }
        }
    }
    
    /**
     * Compares two lines and see if they are the same lines.
     * @param line1
     * @param line2
     * @return
     */
    private boolean lineCompare(Line line1, Line line2) {
        if (line1.getStartX() == line2.getStartX() && line1.getStartY() == line2.getStartY() &&
                line1.getEndX() ==line2.getEndX() && line2.getEndY() == line2.getEndY()) {
            return true;
        }
        return false;
    }
    
    public boolean checkIfLineMatchAndFilled(Line line) {
        for (int i = 0; i < lines.size(); i++) {
            if (lineCompare(line, lines.get(i))) {
                if (line.isVisible()) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Checks if this box is completely filled.
     * @return
     */
    public boolean checkForBoxFilled() {
        if (hasFilled) {
            return false;  //this is key.  It returns false if it is already filled, because this method should only return true if it is first not filled.
        }
        for (int i = 0; i < lines.size(); i++) {
            if (!lines.get(i).isVisible()) {
                return false;
            }
        }
        
        //box is filled, check by who:
        if (player1) {
            board.incrementPlayerOneScore();
        }
        else {
            board.incrementPlayerTwoScore();
        }
        
         //adds the text inside the box
        if (player1) {
            text = new Text("1");
            text.setFill(Color.RED);
        }
        else {
            text = new Text("2");
            text.setFill(Color.BLUE);
        }
        text.setX(topLine.getStartX() + 20);
        text.setY(topLine.getStartY() + 35);
        text.setFont(Font.font(25));
        board.getPane().getChildren().add(text);
        hasText = true;
        hasFilled = true;
        return true;
    }
    
    public ArrayList<Line> getLines(){
        return lines;
    }
    
    
}
