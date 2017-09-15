package gogame;

public class GoGame 
{
    private final GoIO_Interface GoIOInterface;
    private GameBoard GameBoard;
    private Player_Interface player1;
    private Player_Interface player2;
    private GameAnalysis player1AnalysisData;
    private GameAnalysis player2AnalysisData;
    private int currentTurn;
    private boolean configured;
    private int capturedStonesPlayer1;
    private int capturedStonesPlayer2;
    private Memory memory;
    
    // constructor
    public GoGame() 
    {
        GoIOInterface = GoMenu.getInstance();
        GameBoard = null;
        player1 = null;
        player2 = null;
        player1AnalysisData = new GameAnalysis();
        player2AnalysisData = new GameAnalysis();
        currentTurn = 0;
        configured = false;
        capturedStonesPlayer1 = 0;
        capturedStonesPlayer2 = 0;
        memory = null;
    }

    public static void main(String args[])
    {
        GoGame game = new GoGame();
        game.startGoGame();
    }

    // start the game
    public void startGoGame() 
    {
        while (true) 
        {
            // print main menuStart and save menuStart choice in choice_menu
            int choice_menu = GoIOInterface.menuStart();

            // start the game
            if (choice_menu == 1) 
            {
                if (!configured) 
                {
                    getConfiguration();
                    configured = true;
                }
                memory = new Memory();
                configured = false;
                gameLoop();
            }            
        }
    }

    // configuration: dimension = 9
    private void getConfiguration() 
    {
        // set board dimension
        final int DIMENSION = 9;
        GameBoard = new GameBoard(DIMENSION);
        player1AnalysisData.initValidTurns(DIMENSION);
        player2AnalysisData.initValidTurns(DIMENSION);
        // pass board to IO
        GoIOInterface.setBoard(GameBoard);

        // get starting player
        int startingPlayer = GoIOInterface.menuStartPlayer();

        // set players
        if (startingPlayer == 1) 
        {
            player1 = new Human(1);
            player2 = new Computer(2);
        }
        else {
            player1 = new Computer(1);
            player2 = new Human(2);
        }

        // pass board to both players
        player1.setBoard(GameBoard);
        player2.setBoard(GameBoard);
        // initialize variables
        setCapturedStonesPlayer1(0);
        setCapturedStonesPlayer2(0);
        currentTurn = 0;
    }


    private void gameLoop() 
    {
        Boolean runGame = true;
        int currentPlayer = 1;
        save();

        while (runGame) 
        {
            switch (currentPlayer) 
            {
                case 1:
                    GoIOInterface.printTurn(currentTurn);
                    GoIOInterface.printCapturedStones(getCapturedStonesPlayer1(), getCapturedStonesPlayer2());
                    GoIOInterface.printBoard();
                    currentPlayer = makeTurn(currentPlayer);
                    break;
                case 2:
                    GoIOInterface.printTurn(currentTurn);
                    GoIOInterface.printCapturedStones(getCapturedStonesPlayer1(), getCapturedStonesPlayer2());
                    GoIOInterface.printBoard();
                    currentPlayer = makeTurn(currentPlayer);
                    break;
                case -3:
                    // both passed or no more valid turns
                    endGoGame();
                    runGame = false;
                    break;
                case -2:
                    // player2 give up
                    endGameGiveUp(2);
                    runGame = false;
                    break;
                case -1:
                    // player1 give up
                    endGameGiveUp(1);
                    runGame = false;
                    break;
            }
        }
    }

    private int makeTurn(int player) 
    {
        currentTurn++;
        // player1 turn
        if (player == 1) 
        {
            // get player1Status
            player1AnalysisData = player1.playTurn(player2AnalysisData.getValidTurns());

            switch (player1AnalysisData.getPlayerStatus()) 
            {
                // normal turn
                case 0:
                    setCapturedStonesPlayer1(getCapturedStonesPlayer1() + player1AnalysisData.getCapturedStones());
                    save();
                    return 2;
                // pass
                case 1:
                    // if both passed
                    if (player2AnalysisData.getPlayerStatus() == 1) 
                    {
                        // end of game
                        return -3;
                    }
                    save();
                    return 2;
                // give up
                case 2:
                    return -1;
                // undo
                case 3:
                    // undo not allowed in turn 1 and 2
                    if (currentTurn <= 2) 
                    {
                        GoIOInterface.undoNotAllow();
                        currentTurn--; 
                    }
                    else 
                    {
                        undo();
                    }
                    return 1;
                // no more valid turns
                case 4:
                    GoIOInterface.printNoMoreValidTurnsMessage(1);
                    save();
                    // end of game
                    return -3;
            }
        }

        // player2 turn
        else if (player == 2) 
        {
            // get player2Status
            player2AnalysisData = player2.playTurn(player1AnalysisData.getValidTurns());

            switch (player2AnalysisData.getPlayerStatus()) 
            {
                // normal turn
                case 0:
                    setCapturedStonesPlayer2(getCapturedStonesPlayer2() + player2AnalysisData.getCapturedStones());
                    save();
                    return 1;
                // pass
                case 1:
                    // if both passed
                    if (player1AnalysisData.getPlayerStatus() == 1) 
                    {
                        // end of game
                        return -3;
                    }
                    save();
                    return 1;
                // give up
                case 2:
                    return -2;
                // undo
                case 3:
                    // undo not allowed in turn 1 and 2
                    if (currentTurn <= 2) 
                    {
                        GoIOInterface.undoNotAllow();
                        currentTurn--;
                    }
                    else 
                    {
                        undo();
                    }
                    return 2;
                // no more valid turns
                case 4:
                    GoIOInterface.printNoMoreValidTurnsMessage(2);
                    save();
                    // end of game
                    return -3;
            }
        }

        return 0;
    }

    private void undo() 
    {
        Memory last = memory.getLastMemory();
        memory.restoreFromMemory(last);
        GameBoard.overwrite(last.getSavedBoard());
        player1AnalysisData.setValidTurns(GameBoard.getValidTurns(2));
        player2AnalysisData.setValidTurns(GameBoard.getValidTurns(1));
        GameBoard.getValidTurns(2);
        currentTurn = last.getSavedTurn();
        setCapturedStonesPlayer1(last.getSavedCapturedStonesForPlayer1());
        setCapturedStonesPlayer2(last.getSavedCapturedStonesForPlayer2());
    }

    private void save() 
    {
        // make deep copy of the current GameBoard and save that copy instead of the current board
        GameBoard GameBoardCopy = new GameBoard(GameBoard);
        memory.set(GameBoardCopy, currentTurn, getCapturedStonesPlayer1(), getCapturedStonesPlayer2());
        Memory memory_save = memory.storeInMemory();
        memory.addMemory(memory_save);
    }

    
    private void endGoGame() 
    {
        double player1score = getCapturedStonesPlayer1();
        double player2score = 6.5 + getCapturedStonesPlayer2();
        GoIOInterface.printEndOfGameMessage(player1score, player2score);
        GoIOInterface.printBackToMenuStartMessage();
    }

    private void endGameGiveUp(int player) 
    {
        GoIOInterface.printGiveUpMessage(player);
        endGoGame();
    }

    public int getCapturedStonesPlayer1() {
        return capturedStonesPlayer1;
    }

    public void setCapturedStonesPlayer1(int capturedStonesPlayer1) {
        this.capturedStonesPlayer1 = capturedStonesPlayer1;
    }

    public int getCapturedStonesPlayer2() {
        return capturedStonesPlayer2;
    }

   
    public void setCapturedStonesPlayer2(int capturedStonesPlayer2) {
        this.capturedStonesPlayer2 = capturedStonesPlayer2;
    }
}
