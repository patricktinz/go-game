import gogame.GameBoard;
import gogame.GoMenu;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.BeforeClass;

public class GoMenuTest
{
    private static GoMenu goMenu;
    private static GameBoard testboard;
    
    @BeforeClass
    public static void setUp()
    {
        testboard= new GameBoard(9);
        goMenu= GoMenu.getInstance();
        goMenu.setBoard(testboard);
    }
    
    @Test
    public void testprintTurn()
    {
        int a=0, b=2, c=64, d=256, e=1024;
        
        ByteArrayOutputStream output_a = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output_a));
        goMenu.printTurn(a);
        assertEquals("Zug wurde falsch augegeben", "###       Zug "+a+"      ###" + System.lineSeparator(), output_a.toString());
        
        ByteArrayOutputStream output_b = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output_b));
        goMenu.printTurn(b);
        assertEquals("Zug wurde falsch augegeben", "###       Zug "+b+"      ###" + System.lineSeparator(), output_b.toString());
        
        ByteArrayOutputStream output_c = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output_c));
        goMenu.printTurn(c);
        assertEquals("Zug wurde falsch augegeben", "###       Zug "+c+"      ###" + System.lineSeparator(), output_c.toString());
        
        ByteArrayOutputStream output_d = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output_d));
        goMenu.printTurn(d);
        assertEquals("Zug wurde falsch augegeben", "###       Zug "+d+"      ###" + System.lineSeparator(), output_d.toString());
        
        ByteArrayOutputStream output_e = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output_e));
        goMenu.printTurn(e);
        assertEquals("Zug wurde falsch augegeben", "###       Zug "+e+"      ###" + System.lineSeparator(), output_e.toString());
    }
    
    @Test
    public void testmenuPlayerInput()
    {
        String a="a1", b="1", c="2", d="3", e="j9";
        
        // Black
        ByteArrayInputStream input_ab = new ByteArrayInputStream(a.getBytes());
        System.setIn(input_ab);
        int i1=goMenu.menuPlayerInput(1);
        assertEquals(0, i1);
        
        ByteArrayInputStream input_bb = new ByteArrayInputStream(b.getBytes());
        System.setIn(input_bb);
        int i2=goMenu.menuPlayerInput(1);
        assertEquals(-1, i2);
        
        ByteArrayInputStream input_cb = new ByteArrayInputStream(c.getBytes());
        System.setIn(input_cb);
        int i3=goMenu.menuPlayerInput(1);
        assertEquals(-2, i3);
        
        ByteArrayInputStream input_db = new ByteArrayInputStream(d.getBytes());
        System.setIn(input_db);
        int i4=goMenu.menuPlayerInput(1);
        assertEquals(-3, i4);
        
        ByteArrayInputStream input_eb = new ByteArrayInputStream(e.getBytes());
        System.setIn(input_eb);
        int i5=goMenu.menuPlayerInput(1);
        assertEquals(80, i5);

        // White
        ByteArrayInputStream input_aw = new ByteArrayInputStream(a.getBytes());
        System.setIn(input_aw);
        int i6=goMenu.menuPlayerInput(0);
        assertEquals(0, i6);
        
        ByteArrayInputStream input_bw = new ByteArrayInputStream(b.getBytes());
        System.setIn(input_bw);
        int i7=goMenu.menuPlayerInput(0);
        assertEquals(-1, i7);
        
        ByteArrayInputStream input_cw = new ByteArrayInputStream(c.getBytes());
        System.setIn(input_cw);
        int i8=goMenu.menuPlayerInput(0);
        assertEquals(-2, i8);
        
        ByteArrayInputStream input_dw = new ByteArrayInputStream(d.getBytes());
        System.setIn(input_dw);
        int i9=goMenu.menuPlayerInput(0);
        assertEquals(-3, i9);
    }
    
    @Test
    public void testmenuStartPlayer()
    {
        String a="1", b="2";
       
        ByteArrayInputStream input_a = new ByteArrayInputStream(a.getBytes());
        System.setIn(input_a);
        int i1 = goMenu.menuStartPlayer();
        assertEquals(1, i1);
        
        ByteArrayInputStream input_b = new ByteArrayInputStream(b.getBytes());
        System.setIn(input_b);
        int i2 = goMenu.menuStartPlayer();
        assertEquals(2, i2);
    }
}
