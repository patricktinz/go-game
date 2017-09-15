package gogame;

public class Stone 
{
    public static final int INVALID = 0;
    public static final int FREE = 0;
    public static final int BLACK = 1;
    public static final int WHITE = 2;
    public static final int FRIEND = 1;
    public static final int ENEMY = 0;
    public static final int NONE = -1;
    private int numLiberties;
    private int degree;
    private int color;
    private boolean visited;

    public Stone(int lib) 
    {
        this.color = FREE;
        this.visited = false;
        this.degree = 0;
        this.numLiberties = lib;
    }

    public Stone(int lib, boolean valid) 
    {
        if(valid == false)
            this.color = INVALID;
        else
        {
            this.color = FREE;
            this.visited = false;
            this.degree = 0;
            this.numLiberties = lib;
        }
    }

    public Stone(Stone copy)
    {
        color = copy.getColor();
        visited = copy.isVisited();
        degree = copy.getDegree();
        numLiberties = copy.getNumLiberties();
    }
 
    @Override
    public int hashCode()
    {
        Integer hashColor = color;
        return hashColor.hashCode();
    }

    public void setNumLiberties(int numLiberties) {
        this.numLiberties = numLiberties;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public int getNumLiberties() {
        return numLiberties;
    }

    public int getDegree() {
        return degree;
    }

    public int getColor() {
        return color;
    }

    public boolean isAlive() {
        if(color != FREE) 
            return true; 
        else 
            return false;
    }

    public boolean isVisited() {
        return visited;
    }

    public void incrementDegree(){
        degree++;
    }
    public void incrementLiberties(){
        numLiberties++;
    }

    public void decrementDegree(){
        degree--;
    }

    public void decrementLiberties(){
        numLiberties--;
    }
}
