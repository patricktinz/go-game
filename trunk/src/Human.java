package gogame;

import java.util.HashSet;

public class Human implements Player_Interface
{
    private final Integer id;
    private GameBoard board;
    private final GoIO_Interface goIO;

    public Human(int id) 
    {
        this.id = id;
        goIO = GoMenu.getInstance();
    }

    @Override
    public GameAnalysis playTurn(HashSet<Integer> validTurns)
    {
        GameAnalysis gameAnalysis = new GameAnalysis();

        gameAnalysis.initValidTurns(board.getDimension());

        // play out the turn if there are validTurns left otherwise pass
        if (validTurns.size() > 0) 
        {
            boolean isValid = false;
            Integer turn = 0;

            while (!isValid) 
            {
                turn = goIO.menuPlayerInput(id);

                if (turn < 0) 
                {
                    // undo
                    if (turn == -1) 
                    {
                        gameAnalysis.setPlayerStatus(3);
                        return gameAnalysis;
                    }
                    // pass
                    else if (turn == -3) 
                    {
                        
                        gameAnalysis.setPlayerStatus(1);
                        // calculate valid turns for enemey
                        gameAnalysis.setValidTurns(board.getValidTurns((id % 2) + 1));
                        return gameAnalysis;
                    }
                    // surrender
                    else if (turn == -2) 
                    {
                        gameAnalysis.setPlayerStatus(2);
                        return gameAnalysis;
                    }
                }

                if (validTurns.contains(turn)) 
                {
                    isValid = true;
                }
                else 
                {
                    goIO.printNotValidTurnMessage();
                }
            }

            // play out turn
            gameAnalysis = board.playTurn(id, turn);
            gameAnalysis.setPlayerStatus(0);
        }
        // no more remaining valid turns
        else 
        {
            gameAnalysis.setPlayerStatus(4);
            // calculate valid turns for enemey
            gameAnalysis.setValidTurns(board.getValidTurns((id % 2) + 1));
        }

        return gameAnalysis;
    }

    @Override
    public void setBoard(GameBoard board)
    {
        this.board = board;
    }
}
