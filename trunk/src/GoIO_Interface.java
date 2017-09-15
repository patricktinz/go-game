package gogame;

public interface GoIO_Interface 
{
    public void setBoard(GameBoard board);
    public void printBoard();
    public void printTurn(int turn);
    public int menuStart();                     // 1 = game starts, 2 = exit
    public int menuStartPlayer();               // 1 = human starts, 2 = computer starts
    public int menuPlayerInput(int id);
    public void undoNotAllow();
    public void printCapturedStones(int player1Stones, int player2Stones);
    public void printGiveUpMessage(int color);
    public void printEndOfGameMessage(double player1Score, double player2Score);
    public void printBackToMenuStartMessage();
    public void printNoMoreValidTurnsMessage(int player);   // no more valid turns for player
    public void printNotValidTurnMessage();
}

