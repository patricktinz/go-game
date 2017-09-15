package gogame;

import java.util.HashSet;

public class GameAnalysis 
{
    private Integer playerStatus;
    private Integer capturedStones;
    private HashSet<Integer> validTurns;

    GameAnalysis()
    {
        playerStatus = 0; 
        capturedStones = 0;
    }

    public void initValidTurns(int dimension)
    {
        validTurns = new HashSet<>();

        for(int i = 0; i < (dimension*dimension); i++)
            validTurns.add(i);
    }

    public Integer getPlayerStatus() 
    {
        return playerStatus;
    }

    public void setPlayerStatus(Integer playerStatus) 
    {
        this.playerStatus = playerStatus;
    }

    public Integer getCapturedStones() {
        return capturedStones;
    }

    public void setCapturedStones(Integer capturedStones) {
        this.capturedStones = capturedStones;
    }

    public HashSet<Integer> getValidTurns() 
    {
        return validTurns;
    }

    public void setValidTurns(HashSet<Integer> validTurns) 
    {
        this.validTurns = validTurns;
    }
}
