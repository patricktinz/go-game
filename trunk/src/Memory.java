package gogame;

import java.util.Stack;

public class Memory 
{
    private GameBoard board;
    private int turn;
    private int capturedStonesPlayer1;
    private int capturedStonesPlayer2;
    private final Stack<Memory> savedMemory = new Stack<>();

    // Constructor
    public Memory() 
    {       
    }
    
    public Memory(GameBoard boardSave, int turnSave, int cap1, int cap2) 
    { 
        board = boardSave;
        turn = turnSave;
        capturedStonesPlayer1 = cap1;
        capturedStonesPlayer2 = cap2;
    }
   
    public void set(GameBoard newBoard, int newTurn, int newCapturedStonesForPlayer1, int newCapturedStonesForPlayer2) 
    { 
        this.board = newBoard;
        this.turn = newTurn;
        this.capturedStonesPlayer1 = newCapturedStonesForPlayer1;
        this.capturedStonesPlayer2 = newCapturedStonesForPlayer2;
    }

    public Memory storeInMemory() 
    { 
        return new Memory(board, turn, capturedStonesPlayer1, capturedStonesPlayer2); 
    }

    public void restoreFromMemory(Memory memory) 
    {
            board = memory.getSavedBoard();
            turn = memory.getSavedTurn();
            capturedStonesPlayer1 = memory.getSavedCapturedStonesForPlayer1();
            capturedStonesPlayer2 = memory.getSavedCapturedStonesForPlayer2();
    }

    public void addMemory(Memory memory) 
    {
        savedMemory.push(memory);
    }

    public Memory getLastMemory() 
    {
        if (!savedMemory.empty()) 
            savedMemory.pop();
        if (!savedMemory.empty()) 
            savedMemory.pop();
        return savedMemory.peek();
    }
    
    public GameBoard getSavedBoard() {
        return board;
    }
    
    public int getSavedTurn() {
        return turn;
    }
    
    public int getSavedCapturedStonesForPlayer1() {
        return capturedStonesPlayer1;
    }
    
    public int getSavedCapturedStonesForPlayer2() {
        return capturedStonesPlayer2;
    }
}

