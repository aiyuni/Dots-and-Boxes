package DotsAndBoxes;

import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * This class simply stores some info about the game and displays it to the user.   
 *
 */
public class GameStatus {
    
    public static GameStatus statusInstance = null;
    
    private Board board;
    private VBox container = new VBox();
    private Text playerOneInfo= new Text("Player 1: It's your turn." + " Score: 0");  //a delay is caused due to game score being updated after player text is changed. 
    private Text playerTwoInfo = new Text("Player 2: Not your turn." + " Score: 0"); 
    
    /**
     * Private constructor because this is better as a singleton.
     * @param board
     */
    private GameStatus(Board board) {
        this.board = board;
        playerOneInfo.setFont(Font.font(20));
        playerTwoInfo.setFont(Font.font(20));
        container.getChildren().addAll(playerOneInfo, playerTwoInfo);
        board.getRoot().setRight(container);
    }
    
    /**
     * Lazy initialization...
     * @param board
     * @return
     */
    public static GameStatus getInstance(Board board) {
        if (statusInstance == null) {
            statusInstance = new GameStatus(board);
        }
        return statusInstance;
    }
    
    public void setPlayerTurn(boolean playerOne) {
        if (playerOne) {
            playerOneInfo.setText("Player 1: It's your turn."  + " Score: " + board.getPlayerScores()[0]);
            playerTwoInfo.setText("Player 2: Not your turn." + " Score: " + board.getPlayerScores()[1]);
        }
        else {
            playerTwoInfo.setText("Player 2: It's your turn." + " Score: " + board.getPlayerScores()[1]);
            playerOneInfo.setText("Player 1: Not your turn." + " Score: " + board.getPlayerScores()[0]);
        }
    }
    
    public void setGameDrawn() {
        playerOneInfo.setText("Player 1: Game drawn...");
        playerTwoInfo.setText("Player 2: Game drawn...");
    }
    
    public void setGameWin(boolean playerOne) {
        if (playerOne) {
            playerOneInfo.setText("Player 1: You win :D");
            playerTwoInfo.setText("Player 2: You lose :( ");
        }
        else {
            playerTwoInfo.setText("Player 2: You win :D");
            playerOneInfo.setText("Player 1: You lose :( ");
        }
    }
}

