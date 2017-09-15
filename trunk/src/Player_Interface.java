package gogame;

import java.util.HashSet;

public interface Player_Interface
{
    public GameAnalysis playTurn(HashSet<Integer> validTurns);
    public void setBoard(GameBoard board);
}

