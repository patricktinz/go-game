package gogame;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class Computer implements Player_Interface
{
    private final Integer ID;
    private GameBoard board;
    private final String[] output = {"a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1", "j1", 
        "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2", "j2", 
        "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3", "j3", 
        "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4", "j4", 
        "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5", "j5", 
        "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6", "j6", 
        "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7", "j7", 
        "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8", "j8", 
        "a9", "b9", "c9", "d9", "e9", "f9", "g9", "h9", "j9"};   // Board a1 to j9, 0..80 fields 

    public Computer(int id)
    {
        this.ID = id;
    }

    @Override
    public GameAnalysis playTurn(HashSet<Integer> validTurns)
    {
        //Random random = new Random();
        int turn_index;
        int status = 0;
        GameAnalysis gameAnalysis = new GameAnalysis();
        
        gameAnalysis.setPlayerStatus(1);
        gameAnalysis.initValidTurns(board.getDimension());

        // play out the turn if there are validTurns left otherwise pass
        if (validTurns.size() > 0)
        {
            // choose a random index from set
            //turn_index = random.nextInt(validTurns.size());
                 
            turn_index = ki(validTurns);

            // retrieve actual turn from set
            Iterator it = validTurns.iterator();
            int turn = 0;
            for(int i = 0; i <= turn_index; i++)
                turn = (Integer) it.next();

            gameAnalysis = board.playTurn(ID, turn);
            gameAnalysis.setPlayerStatus(0);
            
            // Ouput of turns
            System.out.println("Eingabe Computer: " + output[turn] + "\n");
        }
        else 
        {
            gameAnalysis.setPlayerStatus(4);
	    gameAnalysis.setValidTurns(board.getValidTurns((ID % 2) + 1));
        }

        // check for successful turn or not
        if (gameAnalysis.getPlayerStatus() != 0)
            return gameAnalysis;       
        else
            gameAnalysis.setPlayerStatus(status);
        return gameAnalysis;
    }

    @Override
    public void setBoard(GameBoard board)
    {
        this.board = board;
    }
    Random random = new Random();
    public int start_point = (random.nextInt(100)%7)+72;  
    
    private int ki(HashSet<Integer> validTurns)
    {
        int turn_index;
        
        if(start_point >= 4)
        {
            turn_index = start_point;
            start_point = start_point - 9;
        }
        else
        {
            turn_index = (validTurns.size())/2;
            if(validTurns.size()-1 < 27)
            {                         
                    turn_index = random.nextInt(validTurns.size());                   
            }
        }
        return  turn_index;
    }
}
