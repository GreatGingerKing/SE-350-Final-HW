package shop.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VideoTEST  {


    @Test
    public void testConstructorAndAttributes() {
        String title1 = "XX";
        String director1 = "XY";
        String title2 = " XX ";
        String director2 = " XY ";
        int year = 2002;

        VideoObj v1 = new VideoObj(title1, year, director1);
        assertSame(title1, v1.title());
        assertEquals(year, v1.year());
        assertSame(director1, v1.director());

        VideoObj v2 = new VideoObj(title2, year, director2);
        assertEquals(title1, v2.title());
        assertEquals(director1, v2.director());
    }
    @Test
    public void testConstructorExceptionYear() {
        try {
            new VideoObj("X", 1800, "Y");
            fail("Year should be too low.");
        } catch (IllegalArgumentException e) { }
        try {
            new VideoObj("X", 5000, "Y");
            fail("Year should be too high.");
        } catch (IllegalArgumentException e) { }
        try {
            new VideoObj("X", 1801, "Y");
            new VideoObj("X", 4999, "Y");
        } catch (IllegalArgumentException e) {
            fail("Years Should be working correctly.");
        }
    }

    @Test
    public void testConstructorExceptionTitle() {
        try {
            new VideoObj(null, 2002, "Y");
            fail("Title shouldn't be able to be null");
        } catch (IllegalArgumentException e) { }
        try {
            new VideoObj("", 2002, "Y");
            fail("Title needs to be non-empty");
        } catch (IllegalArgumentException e) { }
        try {
            new VideoObj(" ", 2002, "Y");
            fail("Title should be non-empty");
        } catch (IllegalArgumentException e) { }
    }

    @Test
    public void testConstructorExceptionDirector() {
        try{
            new VideoObj("To Kill a MockingBird", 1987, null);
            fail("Director should not be null.");
        }
        catch(IllegalArgumentException e){}
        try{
            new VideoObj("Terminator", 2004, "");
            fail("Director needs to be non-empty");
        }
        catch(IllegalArgumentException e){}
        try{
            new VideoObj("Liar Liar", 1997, "Adam Sandler");
        }
        catch(IllegalArgumentException e) {fail("Should be working correctly.");}
    }

    @Test
    public void testgets(){
        VideoObj t1 = new VideoObj("Stuff",2000, "Things");
        assertEquals(t1.director(),"Things");
        assertEquals(t1.title(),"Stuff");
        assertEquals(t1.year(),2000);
    }
    @Test
    public void testHashCode() {
        assertEquals
                (-875826552,
                        new VideoObj("None", 2009, "Zebra").hashCode());
        assertEquals
                (-1391078111,
                        new VideoObj("Blah", 1954, "Cante").hashCode());

    }

    @Test
    public void testEquals() {
        VideoObj t1 = new VideoObj("Stuff",2000, "Things");
        assertEquals(t1,t1);

        VideoObj t2 = new VideoObj("Stuff",2000, "Things");
        assertEquals(t1,t2);


        VideoObj t3 = new VideoObj("Stuff",2000, "MORESTUFF");
        assertNotEquals(t1,t3);

        VideoObj t4 = new VideoObj("Junk",2000, "Things");
        assertNotEquals(t1,t4);

        VideoObj t5 = new VideoObj("Stuff",2001, "Things");
        assertNotEquals(t1,t5);

        assertNotEquals(t1,new Integer(4));
        assertNotEquals(t1,null);

    }

    @Test
    public void testCompareTo() {

        VideoObj t1 = new VideoObj("Stuff",2000, "Things");
        assertEquals(t1.compareTo(t1),0);

        VideoObj t2 = new VideoObj("Things", 1995, "Things");
        assertTrue(t1.compareTo(t2) < 0);
        assertTrue(t2.compareTo(t1) > 0);

        VideoObj t3 = new VideoObj("Stuff", 1999, "Things");
        assertTrue(t1.compareTo(t3) > 0);
        assertTrue(t3.compareTo(t1) < 0);

        VideoObj t4 = new VideoObj("Stuff", 2000, "Junk");
        assertTrue(t1.compareTo(t4) > 0);
        assertTrue(t4.compareTo(t1) < 0);

    }

    @Test
    public void testToString() {

        VideoObj t1 = new VideoObj("Indiana Jones", 1993, "Lucas");
        VideoObj t2 = new VideoObj("A   ", 2157, " Hello    ");
        assertEquals(t1.toString(),"Indiana Jones (1993) : Lucas");
        assertEquals(t2.toString(), "A (2157) : Hello");
    }
}
