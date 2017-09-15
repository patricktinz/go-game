package gogame;

import java.util.Scanner;

public class GoMenu implements GoIO_Interface 
{
    private static final String TITLE = "Wombat Go";
    private static final String VERSION = "1.0";
    int size=0;
    private GameBoard myboard; 

    // Singleton GoMenu
    private static GoMenu instance = null;

    private GoMenu() 
    {
    }

    public static GoMenu getInstance() 
    {
        if (instance == null) 
            instance = new GoMenu();
        return instance;
    }

    @Override
    public void setBoard(GameBoard board)
    {
        myboard=board;
    }

    @Override
    public void printTurn(int turn)
    {
        System.out.println("###       Zug "+turn+ "      ###");        
    }

    @Override
    public void printCapturedStones(int p1stones, int p2stones)
    {
        System.out.println("### Gefangene Steine ###");       
        System.out.println("Schwarz: "+p1stones+"    Weiss: "+p2stones);
    }

    @Override
    public void printBoard()
    {        
        String str;
        System.out.println();
        size=myboard.getDimension();
        if(size>9)
            System.out.print("    ");
        else 
            System.out.print("    ");
        for(int j = 65; j < (size+65); j++)
        {
            char column = (char) j;
            if(column == 'I')
                column = 'J';
            
            str=" "+column+" ";
            System.out.print(str);
        }

        System.out.println();
        for(int i = size - 1; i >= 0 ; i--) 
        {
            if(i < 9) 
                System.out.print(" "+(i + 1) + "  ");
            else 
                System.out.print(i + 1 + "  ");

            for(int j = i * size; j < size * i + size; j++) 
            {
                String stone;
                if (myboard.getStone(j).getColor() == 1) 
                    stone = "B";
                
                else if (myboard.getStone(j).getColor() == 2) 
                    stone = "W";
                else 
                    stone = "-";
                         
                str=" "+stone+" ";
                System.out.print(str);
            }
            System.out.println();
        }
        System.out.println();
    }

    @Override
    public int menuPlayerInput(int id)
    {
        size = myboard.getDimension();
        String yourmove = "";
        if (id == 1) 
            yourmove = "Schwarz";
        else if (id == 2)
            yourmove = "Weiss";
        
        int input = -1;
        int x = 0;
        int y = 0;
        while(input < 0)
        {
            System.out.println();
            System.out.println("(1) Undo \n(2) Aufgeben \n(3) Passen \noder Koordinate (z.B.: A1)");
            
            System.out.println("Eingabe (" + yourmove + "): ");
            String inputstring = (new Scanner(System.in)).next();
            
            if(inputstring.contains("J") || inputstring.contains("j"))
            {
                String row = inputstring.substring(1);
                String column = "I";
                inputstring = column + row;
            } 
            // undo
            if(inputstring.equals("1")) 
                return -1;
            // give up
            if(inputstring.equals("2")) 
                return -2;
            // pass
            if(inputstring.equals("3"))
                return -3;
            
            if (inputstring.length() < 2 || inputstring.length() > 3) 
                System.out.println("Bitte eine gueltige Koordinate eingeben (z.B.: A1)!");
            else 
            {
                int stringlength = inputstring.length();
                char[] inputchars = new char[stringlength];
                for (int i = 0; i < stringlength; i++) 
                    inputchars[i] = inputstring.charAt(i);
                
                boolean valid = true;

                for (int j = 0; j < stringlength && valid; j++) 
                {
                    if (j == 0) 
                    {
                        if (!(Character.isLetter(inputchars[j]))) 
                            valid = false;   
                    }
                    else if (j == 1) 
                    {
                        if (!(Character.isDigit(inputchars[j]))) 
                            valid = false;
                    }
                    else if (j == 2) 
                    {
                        if (!(Character.isDigit(inputchars[j])))
                            valid = false;                        
                    }
                    else
                        System.out.println("Bitte eine gueltige Koordinate eingeben (z.B.: A1)!!");
                }

                if (!valid)
                    System.out.println("Bitte eine gueltige Koordinate eingeben (z.B.: A1)!!!");
                else 
                {
                    // calculate X
                    x = (int) inputchars[0];

                    if (x >= 97) 
                        x = x - 97;
                    else
                        x = x - 65;
                    
                    // calculate Y
                    int a = (int) inputchars[1] - 48;
                    int b = 0;
                    if (stringlength == 3)
                        b = (int) inputchars[2] - 48;
                    
                    if (stringlength == 3)
                        y = (a*10) + b - 1;
                    else 
                        y = a - 1;
                    
                    if ((x >= 0 && x < size) && (y >= 0 && y < size)) 
                        input = x + (y * size);
                    else
                        System.out.println("Bitte eine gueltige Koordinate eingeben (z.B.: A1)!!!!");
                }
            }
        }
        return input;
    }

    @Override
    public int menuStart()
    {
        int default_return = 0;

        System.out.println();
        System.out.println("### Willkommen bei " + TITLE + " Version: " + VERSION + " ###");
        System.out.println("(1) Spiel starten");
        System.out.println("(2) Beenden");
        System.out.println("Eingabe > ");
        while (default_return == 0) 
        {
            String input_choice = (new Scanner(System.in)).next();
            if (input_choice.length()==1) 
            {
                int num = 0;
                char first = input_choice.charAt(0);
                if (Character.isDigit(first)) 
                    num = Integer.parseInt(input_choice);       // char to integer
                if (num == 1) 
                    return num;
                if(num == 2)
                    System.exit(0);
            }
            System.out.println("Keine gueltige Eingabe, bitte erneut versuchen: ");
        }
        return default_return;
    }

    @Override
    public void undoNotAllow()
    {
        System.out.println("!!!Undo hier nicht erlaubt!!!\n");
    }

    @Override 
    public void printGiveUpMessage(int color) 
    {
        switch(color)
        {
            case 1:
                System.out.println();
                System.out.print("### Weiss hat gewonnen ###\n");
                break;
            case 2:
                System.out.println();
                System.out.print("### Schwarz hat gewonnen ###\n");
                break;
        }        
    }

    @Override
    public void printEndOfGameMessage(double p1score, double p2score)
    {
        System.out.println();
        System.out.println("### Ende ###\n");
        System.out.println("Endstand: Schwarz " + p1score + " | " + " Weiss " + p2score);
        System.out.println("Danke fuers Spielen!\n");
    }

    @Override
    public void printBackToMenuStartMessage() 
    {
        System.out.println("ENTER druecken, um zum Hauptmenue zugelangen!");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    @Override
    public void printNoMoreValidTurnsMessage(int player)
    {        
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        switch(player) 
        {
            case(1):
                System.out.println("Schwarz hat keine gueltigen Zuege mehr!");
                break;
            case(2):
                System.out.println("Weiss hat keine gueltigen Zuege mehr!");
                break;
        }
        System.out.println("ENTER zum Bestaetigen...");
        scanner.nextLine();
    }

    @Override
    public void printNotValidTurnMessage() 
    {
        System.out.println("Keine gueltige Stelle, ENTER zum Bestaetigen...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    @Override
    public int menuStartPlayer() 
    {   
        System.out.println();
        System.out.print("Wer moechte beginnen: \n(1) Mensch \n(2) Computer \n");
        boolean run = true;

        while (run) 
        {
            System.out.println("Eingabe > ");
            String input_choice = (new Scanner(System.in)).next();
            System.out.println(); 
            switch(input_choice)
            {
                case "1":
                    return 1;
                case "2":
                    return 2;
                default:
                    System.out.println("!!!Ungueltige Eingabe!!!\n"); 
            }
        }
        return 0;
    }
}
