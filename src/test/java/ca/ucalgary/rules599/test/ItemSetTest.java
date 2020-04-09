package ca.ucalgary.rules599.test;

import ca.ucalgary.rules599.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class ItemSetTest {
    static Person person1,person2,person3;
    static Collision collision1,collision2,collision3;
    static Vehicle vehicle1,vehicle2,vehicle3;



    @Before
    public void setup() {
        person1 = Person.builder().P_ID("1").P_SEX("M").P_AGE("75").P_PSN("11").P_ISEV("2").P_SAFE("NN").P_USER("1").build();
        person2 = Person.builder().P_ID("1").P_SEX("F").P_AGE("21").P_PSN("11").P_ISEV("2").P_SAFE("2").P_USER("1").build();
        person3 = Person.builder().P_ID("1").P_SEX("F").P_AGE("34").P_PSN("11").P_ISEV("2").P_SAFE("2").P_USER("1").build();

        vehicle1= Vehicle.builder().V_ID("1").V_TYPE("1").V_YEAR("UUUU").build();
        vehicle2= Vehicle.builder().V_ID("2").V_TYPE("1").V_YEAR("UUUU").build();
        vehicle3= Vehicle.builder().V_ID("1").V_TYPE("1").V_YEAR("2016").build();


        collision1= Collision.builder().C_YEAR("2017").C_MNTH("1").C_WDAY("1").C_HOUR("10").C_SEV("2").C_VEHS("1").C_CONF("2").C_RCFG("3").C_WTHR("1").C_RSUR("3").C_RALN("2").C_TRAF("18").build();
        collision2= Collision.builder().C_YEAR("2017").C_MNTH("1").C_WDAY("1").C_HOUR("12").C_SEV("2").C_VEHS("1").C_CONF("4").C_RCFG("UU").C_WTHR("1").C_RSUR("5").C_RALN("1").C_TRAF("UU").build();
        collision3= Collision.builder().C_YEAR("2017").C_MNTH("1").C_WDAY("1").C_HOUR("0").C_SEV("2").C_VEHS("1").C_CONF("3").C_RCFG("UU").C_WTHR("7").C_RSUR("3").C_RALN("1").C_TRAF("UU").build();



    }
    @Test
    public final void testDefaultConstructor() {
        ItemSet<AccidentData> itemSet = new ItemSet<>();
        assertEquals(0, itemSet.getSupport(), 0);
        assertTrue(itemSet.isEmpty());
    }


    @Test
    public final void testConstructorWithItemSetArgument() {
        AccidentData item = AccidentData.builder().collision(collision1).person(person1).vehicle(vehicle1).build();
        ItemSet<AccidentData> itemSet1 = new ItemSet<>();
        itemSet1.setSupport(0.5);
        itemSet1.add(item);
        ItemSet<AccidentData> itemSet2 = new ItemSet<>(itemSet1);
        assertEquals(itemSet1.getSupport(), itemSet2.getSupport(), 0);
        assertEquals(itemSet1.size(), itemSet2.size());
        assertEquals(item, itemSet2.first());
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testConstructorWithItemSetArgumentThrowsException() {
        new ItemSet<>(null);
    }


    @Test
    public final void testSetSupport() {
        double support = 0.5;
        ItemSet<AccidentData> itemSet = new ItemSet<>();
        itemSet.setSupport(support);
        assertEquals(support, itemSet.getSupport(), 0);
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testSetSupportThrowsExceptionWhenSupportIsLessThanZero() {
        ItemSet<AccidentData> itemSet = new ItemSet<>();
        itemSet.setSupport(-1);
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testSetSupportThrowsExceptionWhenSupportIsGreaterThanOne() {
        ItemSet<AccidentData> itemSet = new ItemSet<>();
        itemSet.setSupport(1.1);
    }


    @Test
    public final void testAdd() {
        AccidentData item1 = AccidentData.builder().collision(collision1).person(person1).vehicle(vehicle1).build();
        AccidentData item2 = AccidentData.builder().collision(collision1).person(person1).vehicle(vehicle1).build();
        AccidentData item3 = AccidentData.builder().collision(collision2).person(person2).vehicle(vehicle2).build();
        ItemSet<AccidentData> itemSet = new ItemSet<>();
        assertTrue(itemSet.isEmpty());
        itemSet.add(item1);
        itemSet.add(item2);
        itemSet.add(item3);
        itemSet.add(item3);
        assertEquals(2, itemSet.size());
        Iterator<AccidentData> iterator = itemSet.iterator();
        assertEquals(item2, iterator.next());
        assertEquals(item3, iterator.next());
        assertFalse(iterator.hasNext());
    }


    @Test
    public final void testRemove() {
        AccidentData item1 = AccidentData.builder().collision(collision3).person(person3).vehicle(vehicle3).build();
        AccidentData item2 = AccidentData.builder().collision(collision1).person(person1).vehicle(vehicle1).build();
        AccidentData item3 = AccidentData.builder().collision(collision2).person(person2).vehicle(vehicle2).build();
        ItemSet<AccidentData> itemSet = new ItemSet<>();
        itemSet.add(item1);
        itemSet.add(item2);
        itemSet.add(item3);
        assertEquals(3, itemSet.size());
        itemSet.remove(item3);
        itemSet.remove(item3);
        assertEquals(2, itemSet.size());
        Iterator<AccidentData> iterator = itemSet.iterator();
        assertEquals(item1, iterator.next());
        assertEquals(item2, iterator.next());
        assertFalse(iterator.hasNext());
    }


    @Test
    public final void testContains() {
        AccidentData item1 = AccidentData.builder().collision(collision1).person(person1).vehicle(vehicle1).build();
        AccidentData item2 = AccidentData.builder().collision(collision2).person(person2).vehicle(vehicle2).build();
        ItemSet<AccidentData> itemSet = new ItemSet<>();
        itemSet.add(item1);
        assertEquals(1, itemSet.size());
        assertTrue(itemSet.contains(item1));
        assertFalse(itemSet.contains(item2));
        itemSet.clear();
        assertTrue(itemSet.isEmpty());
        assertFalse(itemSet.contains(item1));
        assertFalse(itemSet.contains(item2));
    }


    @Test
    public final void testCompareTo() {
        ItemSet<AccidentData> itemSet1 = new ItemSet<>();
        itemSet1.setSupport(0.5);
        ItemSet<AccidentData> itemSet2 = new ItemSet<>();
        itemSet2.setSupport(0.5);
        assertEquals(0, itemSet1.compareTo(itemSet2));
        itemSet1.setSupport(0.3);
        assertEquals(-1, itemSet1.compareTo(itemSet2));
        itemSet1.setSupport(0.6);
        assertEquals(1, itemSet1.compareTo(itemSet2));
    }


    @Test
    public final void testClone() {
        AccidentData item = AccidentData.builder().collision(collision1).person(person1).vehicle(vehicle1).build();
        ItemSet<AccidentData> itemSet1 = new ItemSet<>();
        itemSet1.setSupport(0.5);
        itemSet1.add(item);
        ItemSet<AccidentData> itemSet2 = itemSet1.clone();
        assertEquals(itemSet1.getSupport(), itemSet2.getSupport(), 0);
        assertEquals(itemSet1.size(), itemSet2.size());
        assertEquals(item, itemSet2.first());
    }


    @Test
    public final void testToString() {
        AccidentData item1 = AccidentData.builder().collision(collision1).person(person1).vehicle(vehicle1).build();
        AccidentData item2 = AccidentData.builder().collision(collision2).person(person2).vehicle(vehicle2).build();
        ItemSet<AccidentData> itemSet = new ItemSet<>();
        itemSet.add(item1);
        itemSet.add(item2);
        assertEquals("[" + item1.toString() + ", " + item2.toString() + "]", itemSet.toString());
    }


    @Test
    public final void testHashCode() {
        ItemSet<AccidentData> itemSet1 = new ItemSet<>();
        ItemSet<AccidentData> itemSet2 = new ItemSet<>();
        assertEquals(itemSet1.hashCode(), itemSet1.hashCode());
        assertEquals(itemSet1.hashCode(), itemSet2.hashCode());
        itemSet1.add(AccidentData.builder().collision(collision1).person(person1).vehicle(vehicle1).build());
        assertNotEquals(itemSet1.hashCode(), itemSet2.hashCode());
        itemSet2.add(AccidentData.builder().collision(collision1).person(person1).vehicle(vehicle1).build());
        assertEquals(itemSet1.hashCode(), itemSet2.hashCode());
        itemSet1.add(AccidentData.builder().collision(collision2).person(person2).vehicle(vehicle2).build());
        assertNotEquals(itemSet1.hashCode(), itemSet2.hashCode());
    }


    @Test
    public final void testEquals() {
        ItemSet<AccidentData> itemSet1 = new ItemSet<>();
        ItemSet<AccidentData> itemSet2 = new ItemSet<>();
        assertFalse(itemSet1.equals(null));
        assertFalse(itemSet1.equals(new Object()));
        assertTrue(itemSet1.equals(itemSet1));
        assertTrue(itemSet1.equals(itemSet2));
        itemSet1.add(AccidentData.builder().collision(collision1).person(person1).vehicle(vehicle1).build());
        assertFalse(itemSet1.equals(itemSet2));
        itemSet2.add(AccidentData.builder().collision(collision1).person(person1).vehicle(vehicle1).build());
        assertTrue(itemSet1.equals(itemSet2));
        itemSet1.add(AccidentData.builder().collision(collision2).person(person2).vehicle(vehicle2).build());
        assertFalse(itemSet1.equals(itemSet2));
    }

}