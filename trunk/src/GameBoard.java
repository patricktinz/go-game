package gogame;

import java.util.*;
import static gogame.Stone.*;

public class GameBoard 
{
    private Stone[] stone;
    private int[][] boardState;
    private int numStones;
    private int numBoardState;
    private int dimension;
    private int maxStones;
    private HashSet<Integer> history;

    public GameBoard(int dimension)
    {
        this.dimension = dimension;
        maxStones = dimension * dimension;
        boardState = new int[maxStones][maxStones];         // x and y are the same
        stone = new Stone[maxStones];               
        numStones = 0;
        numBoardState = 0;
        history = new HashSet<>();

        // init board with empty stones
        for(int i = 0; i < maxStones; i++)
        {
            int liberty = 4;
            if(left(numStones) == -1)
                liberty--;
            if(right(numStones) == -1)
                liberty--;
            if(up(numStones) == -1)
                liberty--;
            if(down(numStones) == -1)
                liberty--;
            stone[numStones] = new Stone(liberty);
            numStones++;
            for(int j = 0; j < maxStones; j++)
            {
                if(i != j && isNextTo(i, j))
                {
                    boardState[i][j] = FRIEND;     
                    boardState[j][i] = FRIEND;
                }
                else
                {
                    boardState[i][j] = NONE;     
                    boardState[j][i] = NONE;
                }
            }
        }
        numStones = 0;
    }

    public GameBoard(GameBoard copy)
    {
        maxStones = copy.getMaxStones();
        dimension = copy.getDimension();
        numStones = copy.getNumStones();
        numBoardState = copy.getNumBoardState();
        stone = new Stone[maxStones];
        boardState = new int[maxStones][maxStones];
        history = copy.getHistory();

        for(int i = 0; i < maxStones; i++)
            stone[i] = new Stone(copy.getStone(i));
        

        for(int i = 0; i < maxStones; i++)
        {
            for(int j = 0; j < maxStones; j++)
                boardState[i][j] = copy.getBoardState(i, j);
        }
    }

    public void overwrite(GameBoard copy)
    {
        maxStones = copy.getMaxStones();
        dimension = copy.getDimension();
        numStones = copy.getNumStones();
        numBoardState = copy.getNumBoardState();
        stone = new Stone[maxStones];
        boardState = new int[maxStones][maxStones];
        history = copy.getHistory();

        for(int i = 0; i < maxStones; i++)
            stone[i] = new Stone(copy.getStone(i));
        

        for(int i = 0; i < maxStones; i++)
        {
            for(int j = 0; j < maxStones; j++)
                boardState[i][j] = copy.getBoardState(i, j);
        }
    }

    public GameAnalysis playTurn(int playerID, int newStone)
    {
        // check surrondings, combine chains, recalculate liberties, kill surrounded stones, calc valid turns
        HashSet<Integer> validTurns = new HashSet<>();

        integrateStone(newStone, playerID);

        int capturedStones = removeDeadStones(newStone);

        history.add(stateHashCode());   
        // calculate possible turns for next player
        calcValidTurns(validTurns, playerID);

        GameAnalysis gameAnalysis = new GameAnalysis();

        if(validTurns.isEmpty()) 
            gameAnalysis.setPlayerStatus(1);
        else
            gameAnalysis.setPlayerStatus(0);

        gameAnalysis.setValidTurns(validTurns);
        gameAnalysis.setCapturedStones(capturedStones);

        return gameAnalysis;
    }

    // method to simulate a turn for ko-rule
    private void simulateTurn(int playerId, int newStone)
    {
        integrateStone(newStone, playerId);
        removeDeadStones(newStone);
    }

    public int stateHashCode()
    {
        return Arrays.hashCode(stone);
    }
    
    public HashSet<Integer> getValidTurns(int playerId)
    {
        HashSet<Integer> validTurns = new HashSet<>();

        if(playerId == BLACK)
            playerId = WHITE;
        else
            playerId = BLACK;

        calcValidTurns(validTurns, playerId);     

        return validTurns;
    }

    private int removeDeadStones(int currentJunc) 
    {
        int capturedStones = 0;
        int liberties = 1;
        LinkedList<Integer> enemyStones = new LinkedList<>();
        LinkedList<Integer> stonesToRemove = new LinkedList<>();
        ListIterator listIterator;

        setAllVisited(false);

        findCloseEnemyStones(currentJunc, enemyStones);

        while(!enemyStones.isEmpty())
        {
            setAllVisited(false);
            liberties = calculateLiberties(enemyStones.getFirst()); // calculate liberties also visits all stones in chain

            if(liberties == 0)
            {
                setAllVisited(false);
                addChainToList(enemyStones.getFirst(), stonesToRemove);
            }

            // remove all visited stones from enemyStones as they belong to the same chain
            listIterator = enemyStones.listIterator();
            while(listIterator.hasNext())
            {
                if(stone[(Integer)listIterator.next()].isVisited())
                    listIterator.remove();
            }
        }

        listIterator = stonesToRemove.listIterator();
        while(listIterator.hasNext())
        {
            removeStone((Integer) listIterator.next());
            listIterator.remove();
            capturedStones++;
        }

        return capturedStones;
    }

    // checks all surrounded Stones and adds them to validTurns
    private void checkSurroundedStones(LinkedList<Integer> surroundedStones, HashSet<Integer> validTurns, int nextPlayer) 
    {
        int currentJunc;
        int liberties;
        ListIterator listIterator;

        while(surroundedStones.isEmpty() == false) 
        {
            currentJunc = surroundedStones.getFirst();

            stone[currentJunc].setColor(nextPlayer);
            integrateStone(currentJunc, nextPlayer);

            setAllVisited(false);
            liberties = calculateLiberties(currentJunc); // also visits chain

            if (liberties > 0)
                validTurns.add(currentJunc);
            else 
            {   // check if new stone would capture enemy stones
                LinkedList<Integer> enemyStones = new LinkedList<>();
                liberties = 1;

                setAllVisited(false);
                findCloseEnemyStones(currentJunc, enemyStones);    // also visits enemy stones

                while (!enemyStones.isEmpty() && liberties != 0) 
                {
                    setAllVisited(false);
                    liberties = calculateLiberties(enemyStones.getFirst());

                    if (liberties == 0) 
                        validTurns.add(currentJunc);
                    

                    // remove all visited stones from enemyStones as they belong to the same chain
                    listIterator = enemyStones.listIterator();
                    while (listIterator.hasNext()) 
                    {
                        if (stone[(Integer) listIterator.next()].isVisited())
                            listIterator.remove();
                    }
                }
            }
            surroundedStones.remove();
            removeStone(currentJunc);
        }
    }
    private boolean isNextTo(int v, int w)
    {
        boolean result = false;

        if(left(v) == w)
            result = true;

        if(right(v) == w)
            result = true;

        if(up(v) == w)
            result = true;

        if(down(v) == w)
            result = true;

        return result;
    }

    private void integrateStone(int index, int color)
    {   // integrateStone can assume index points to a valid empty space
        int x = index % dimension;
        int y = index / dimension;

        stone[index].setColor(color);

        // assume stone is not surrounded by anything
        stone[index].setDegree(0);
        stone[index].setNumLiberties(4);


        // check junction left
        if(x != 0)
            addBoardState(index, left(index));
        else
            stone[index].decrementLiberties();

        // check junction right
        if(x != (dimension - 1))
            addBoardState(index, right(index));
        else
            stone[index].decrementLiberties();

        // check junction up
        if(y != 0)
            addBoardState(index, up(index));
        else
            stone[index].decrementLiberties();

        // check junction down
        if(y != dimension - 1)
            addBoardState(index, down(index));
        else
            stone[index].decrementLiberties();
    }

    private int left(int index)
    {
        int x = index % dimension;

        if(x != 0)
            return index - 1;
        else
            return -1;
    }

    private int right(int index)
    {
        int x = index % dimension;

        if(x != dimension - 1)
            return index + 1;
        else
            return -1;
    }

    private int up(int index)
    {
        int y = index / dimension;

        if(y != 0)
            return index - dimension;
        else
            return -1;
    }

    private int down(int index)
    {
        int y = index / dimension;

        if(y != dimension - 1)
            return index + dimension;
        else return -1;
    }

    private void addBoardState(int i, int j)
    {
        int value;

        if(stone[j].getColor() != FREE) 
        {
            stone[i].decrementLiberties();
            stone[i].incrementDegree();
        }

        if(stone[i].getColor() != FREE) 
        {
            stone[j].decrementLiberties();
            stone[j].incrementDegree();
        }

        if(stone[i].getColor() == stone[j].getColor())
            value = FRIEND;
        else if(stone[j].getColor() == FREE || stone[i].getColor() == FREE)
            value = NONE;
        else
            value = ENEMY;

        boardState[i][j] = value;
        boardState[j][i] = value;              
    }

    private void calcValidTurns(HashSet<Integer> validTurns, int playerID)
    {
        LinkedList<Integer> surroundedStones = new LinkedList<>();
        int nextPlayer;

        // change playerID because we want to calc possible turns for the next player
        if(playerID == BLACK)
            nextPlayer = WHITE;
        else
            nextPlayer = BLACK;

        findEmptyJunctions(surroundedStones, validTurns);   // empty junctions are added if they are next to another empty junction

        checkSurroundedStones(surroundedStones, validTurns, nextPlayer);    

        //check all validPlays for ko-rule
        Integer nextPlay = 0;
        Iterator iterator = validTurns.iterator();
        while(iterator.hasNext()){
            nextPlay = (Integer) iterator.next();
            GameBoard tmpBoard = new GameBoard(this);
            tmpBoard.simulateTurn(nextPlayer, nextPlay);
            if(history.contains(tmpBoard.stateHashCode()))
                iterator.remove();
        }
    }

    private void addChainToList(int visit, LinkedList<Integer> list)
    {
        LinkedList<Integer> visited = new LinkedList<>();

        stone[visit].setVisited(true);
        list.add(visit);
        visited.add(visit);

        while(visited.isEmpty() == false)
        {
            visit = visited.getFirst();
            visited.remove();

            int w = firstFriend(visit);
            while(w != -1){
                if(stone[w].isVisited() == false)
                {
                    stone[w].setVisited(true);
                    list.add(w);
                    visited.add(w);
                }
                w = nextFriend(visit, w);
            }
        }
    }

    private void findCloseEnemyStones(int visit, LinkedList<Integer> enemyStones) 
    {
        LinkedList<Integer> visited = new LinkedList<>();

        stone[visit].setVisited(true);
        visitEnemies(visit, enemyStones);   // checks enemies next to visit and adds them to list if they are unvisited
        visited.add(visit);

        while(visited.isEmpty() == false)
        {
            visit = visited.getFirst();
            visited.remove();

            int w = firstFriend(visit);
            while(w != -1){
                if(stone[w].isVisited() == false)
                {
                    stone[w].setVisited(true);
                    visitEnemies(w, enemyStones);
                    visited.add(w);
                }
                w = nextFriend(visit, w);
            }
        }
    }

    private void visitEnemies(int visit, LinkedList<Integer> enemyStones) 
    {
        if(left(visit) != -1)
        {
            if(stone[left(visit)].isAlive())
                if(stone[left(visit)].isVisited() == false && boardState[visit][left(visit)] == ENEMY)
                {
                    enemyStones.add(left(visit));
                    stone[left(visit)].setVisited(true);
                }
        }

        if(right(visit) != -1)
        {
            if(stone[right(visit)].isAlive())
                if(stone[right(visit)].isVisited() == false && boardState[visit][right(visit)] == ENEMY)
                {
                    enemyStones.add(right(visit));
                    stone[right(visit)].setVisited(true);
                }
        }

        if(up(visit) != -1)
        {
            if(stone[up(visit)].isAlive())
                if(stone[up(visit)].isVisited() == false && boardState[visit][up(visit)] == ENEMY)
                {
                    enemyStones.add(up(visit));
                    stone[up(visit)].setVisited(true);
                }
        }

        if(down(visit) != -1)
        {
            if(stone[down(visit)].isAlive())
                if(stone[down(visit)].isVisited() == false && boardState[visit][down(visit)] == ENEMY)
                {
                    enemyStones.add(down(visit));
                    stone[down(visit)].setVisited(true);
                }
        }
    }

    private int calculateLiberties(int visit) 
    {
        LinkedList<Integer> visited = new LinkedList<>();
        int liberties;

        stone[visit].setVisited(true);
        liberties = visitLiberties(visit);
        visited.add(visit);

        while(visited.isEmpty() == false)
        {
            visit = visited.getFirst();
            visited.remove();

            int w = firstFriend(visit);
            while(w != -1)
            {
                if(stone[w].isVisited() == false)
                {
                    stone[w].setVisited(true);
                    liberties += visitLiberties(w);
                    visited.add(w);
                }
                w = nextFriend(visit, w);
            }
        }

        return liberties;
    }

    private int visitLiberties(int visit) 
    {   // returns amount of unvisited liberties and visits them
        int numLiberties = 0;
        int liberty;

        liberty = left(visit);
        if(visitLiberty(liberty) == 1)
            numLiberties++;

        liberty = right(visit);
        if(visitLiberty(liberty) == 1)
            numLiberties++;

        liberty = up(visit);
        if(visitLiberty(liberty) == 1)
            numLiberties++;

        liberty = down(visit);
        if(visitLiberty(liberty) == 1)
            numLiberties++;

        return numLiberties;
    }

    private int visitLiberty(int liberty) 
    {   // returns 1 if liberty is alive and unvisited
        if(liberty == -1)
            return 0;

        if(stone[liberty].isAlive() == false)       // isAlive == false: space is free
            if(stone[liberty].isVisited() == false) 
            {
                stone[liberty].setVisited(true);
                return 1;
            }
        return 0;
    }

    private void removeStone(int index) 
    {
        stone[index].setColor(FREE);

        if(left(index) != -1)
            removeBoardState(index, left(index));

        if(right(index) != -1)
            removeBoardState(index, right(index));

        if(up(index) != -1)
            removeBoardState(index, up(index));

        if(down(index) != -1)
            removeBoardState(index, down(index));
    }

    private void removeBoardState(int i, int j) 
    {
        int value;

        stone[j].incrementLiberties();
        stone[j].decrementDegree();

        if(stone[i].getColor() == stone[j].getColor())
            value = FRIEND;
        else if(stone[j].getColor() == FREE || stone[i].getColor() == FREE)
            value = NONE;
        else
            value = ENEMY;

        boardState[i][j] = value;
        boardState[j][i] = value;
    }

    // traverses all empty space and adds them to the corresponding Container
    private void findEmptyJunctions(LinkedList<Integer> surroundedStones, HashSet<Integer> validTurns){
        setAllVisited(false);
        int v = firstEmptyJunc(); 

        while(v != -1)
        {
            if(stone[v].isVisited() == false)
                emptyJunctions(surroundedStones, validTurns, v);
            v = nextEmptyJunc(v);
        }
    }

    private int firstEmptyJunc() 
    {
        int i;

        for(i = 0; i < maxStones; i++)
        {
            if(stone[i].isAlive() == false)
                break;
        }

        if(i == maxStones)
            return -1;
        else
            return i;
    }

    private int nextEmptyJunc(int v) 
    {
        int i;

        for(i = v + 1; i < maxStones; i++)
        {
            if(stone[i].isAlive() == false)
                break;
        }

        if(i == maxStones)
            return -1;
        else
            return i;
    }

    private void emptyJunctions(LinkedList<Integer> surroundedStones, HashSet<Integer> validTurns, int v) 
    {
        LinkedList<Integer> visited = new LinkedList<>();

        if(stone[v].isVisited() == false)
        {
            stone[v].setVisited(true);
            if(stone[v].getNumLiberties() > 0)
                validTurns.add(v);
            else 
            {
                surroundedStones.add(v);
                return;                 // IMPORTANT: no more empty junctions
            }
            visited.add(v);

            while(!visited.isEmpty())
            {
                v = visited.getFirst();
                visited.remove();

                int w = firstFriend(v);
                while(w != -1){
                    if(stone[w].isVisited() == false)
                    {
                        stone[w].setVisited(true);
                        validTurns.add(w);             
                        visited.add(w);
                    }
                    w = nextFriend(v, w);
                }
            }
        }
    }

    private int nextFriend(int v, int w) 
    {
        int i;

        for(i = w + 1; i < maxStones; i++)
        {
            if(boardState[v][i] == FRIEND)
                break;
        }

        if(i == maxStones)
            return -1;
        else
            return i;
    }

    private int firstFriend(int v) 
    {
        int i;

        for(i = 0; i < maxStones; i++)
        {
            if(boardState[v][i] == FRIEND)
                break;
        }

        if(i == maxStones)
            return -1;
        else
            return i;
    }

    private void setAllVisited(boolean bool) 
    {
        for(int i = 0; i < maxStones; i++)
            stone[i].setVisited(bool);
    }

    public Stone getStone(int i) {
        if(i >= 0 && i < stone.length)
            return stone[i];
        else
        {
            Stone invalid = new Stone(0, false);
            return invalid;
        }
    }

    public Stone[] getStone() {
        return stone;
    }

    public int getBoardState(int i, int j) {
        return boardState[i][j];
    }

    public int getNumStones() {
        return numStones;
    }

    public int getNumBoardState() {
        return numBoardState;
    }

    public int getDimension() {
        return dimension;
    }

    public int getMaxStones() {
        return maxStones;
    }

    public HashSet<Integer> getHistory() {
        return new HashSet<>(history);
    }
}
