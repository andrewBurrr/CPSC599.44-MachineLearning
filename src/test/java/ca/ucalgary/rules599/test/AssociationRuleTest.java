package ca.ucalgary.rules599.test;

import ca.ucalgary.rules599.model.*;
import ca.ucalgary.rules599.rules.AssociationRule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class AssociationRuleTest {
    static AccidentData item1,item2,item3,item4,item5,item6;
    static Person person1,person2,person3,person4,person5,person6;
    static Collision collision1,collision2,collision3,collision4,collision5,collision6;
    static Vehicle vehicle1,vehicle2,vehicle3,vehicle4,vehicle5,vehicle6;
    @Before
    public void setup() {
        person1 = Person.builder().P_ID("1").P_SEX("M").P_AGE("75").P_PSN("11").P_ISEV("2").P_SAFE("NN").P_USER("1").build();
        person2 = Person.builder().P_ID("1").P_SEX("F").P_AGE("21").P_PSN("11").P_ISEV("2").P_SAFE("2").P_USER("1").build();
        person3 = Person.builder().P_ID("1").P_SEX("F").P_AGE("34").P_PSN("11").P_ISEV("2").P_SAFE("2").P_USER("1").build();
        person4 = Person.builder().P_ID("1").P_SEX("F").P_AGE("50").P_PSN("11").P_ISEV("2").P_SAFE("2").P_USER("1").build();
        person5 = Person.builder().P_ID("1").P_SEX("M").P_AGE("63").P_PSN("11").P_ISEV("1").P_SAFE("NN").P_USER("1").build();
        person6 = Person.builder().P_ID("1").P_SEX("F").P_AGE("26").P_PSN("11").P_ISEV("2").P_SAFE("2").P_USER("1").build();
        vehicle1= Vehicle.builder().V_ID("1").V_TYPE("1").V_YEAR("UUUU").build();
        vehicle2= Vehicle.builder().V_ID("2").V_TYPE("1").V_YEAR("UUUU").build();
        vehicle3= Vehicle.builder().V_ID("1").V_TYPE("1").V_YEAR("2016").build();
        vehicle4= Vehicle.builder().V_ID("1").V_TYPE("1").V_YEAR("UUUU").build();
        vehicle5= Vehicle.builder().V_ID("2").V_TYPE("1").V_YEAR("UUUU").build();
        vehicle6= Vehicle.builder().V_ID("1").V_TYPE("1").V_YEAR("UUUU").build();
        collision1= Collision.builder().C_YEAR("2017").C_MNTH("1").C_WDAY("1").C_HOUR("10").C_SEV("2").C_VEHS("1").C_CONF("2").C_RCFG("3").C_WTHR("1").C_RSUR("3").C_RALN("2").C_TRAF("18").build();
        collision2= Collision.builder().C_YEAR("2017").C_MNTH("1").C_WDAY("1").C_HOUR("12").C_SEV("2").C_VEHS("1").C_CONF("4").C_RCFG("UU").C_WTHR("1").C_RSUR("5").C_RALN("1").C_TRAF("UU").build();
        collision3= Collision.builder().C_YEAR("2017").C_MNTH("1").C_WDAY("1").C_HOUR("0").C_SEV("2").C_VEHS("1").C_CONF("3").C_RCFG("UU").C_WTHR("7").C_RSUR("3").C_RALN("1").C_TRAF("UU").build();
        collision4= Collision.builder().C_YEAR("2017").C_MNTH("1").C_WDAY("1").C_HOUR("17").C_SEV("2").C_VEHS("2").C_CONF("21").C_RCFG("UU").C_WTHR("1").C_RSUR("1").C_RALN("1").C_TRAF("18").build();
        collision5= Collision.builder().C_YEAR("2017").C_MNTH("1").C_WDAY("1").C_HOUR("17").C_SEV("2").C_VEHS("2").C_CONF("21").C_RCFG("UU").C_WTHR("1").C_RSUR("1").C_RALN("1").C_TRAF("18").build();
        collision6= Collision.builder().C_YEAR("2017").C_MNTH("1").C_WDAY("1").C_HOUR("22").C_SEV("2").C_VEHS("1").C_CONF("4").C_RCFG("5").C_WTHR("1").C_RSUR("3").C_RALN("1").C_TRAF("18").build();

        item3 = AccidentData.builder().collision(collision3).person(person3).vehicle(vehicle3).build();
        item1 = AccidentData.builder().collision(collision1).person(person1).vehicle(vehicle1).build();
        item2 = AccidentData.builder().collision(collision2).person(person2).vehicle(vehicle2).build();
        item4 = AccidentData.builder().collision(collision4).person(person4).vehicle(vehicle4).build();
        item5 = AccidentData.builder().collision(collision5).person(person5).vehicle(vehicle5).build();
        item6 = AccidentData.builder().collision(collision6).person(person6).vehicle(vehicle6).build();
    }

    @Test
    public final void testConstructor() {
        ItemSet<AccidentData> body = new ItemSet<>();
        body.add(item1);
        body.add(item2);
        ItemSet<AccidentData> head = new ItemSet<>();
        head.add(item3);
        head.add(item4);
        double support = 0.5;
        AssociationRule<AccidentData> associationRule = new AssociationRule<>(body, head, support);
        assertEquals(body, associationRule.getBody());
        assertEquals(head, associationRule.getConsequence());
        assertEquals(support, associationRule.getSupport(), 0);
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testConstructorThrowsExceptionWhenBodyIsNull() {
        new AssociationRule<>(null, new ItemSet<>(), 0.5);
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testConstructorThrowsExceptionWhenHeadIsNull() {
        new AssociationRule<>(new ItemSet<>(), null, 0.5);
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testConstructorThrowsExceptionWhenSupportIsLessThanZero() {
        new AssociationRule<>(new ItemSet<>(), new ItemSet<>(), -1);
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testConstructorThrowsExceptionWhenSupportIsGreaterThanOne() {
        new AssociationRule<>(new ItemSet<>(), new ItemSet<>(), 1.1);
    }


    @Test
    public final void testCoversWithArrayParameter() {
        AccidentData[] items = {
                item1,item3,item4,item5,item6};
        ItemSet<AccidentData> body = new ItemSet<>();
        body.add(item1);
        body.add(item2);
        ItemSet<AccidentData> head = new ItemSet<>();
        head.add(item3);
        AssociationRule<AccidentData> associationRule = new AssociationRule<>(new ItemSet<>(), head,
                0.5);
        assertTrue(associationRule.covers(items));
        associationRule = new AssociationRule<>(body, head, 0.5);
        assertFalse(associationRule.covers(items));
        items[1] = item2;
        assertTrue(associationRule.covers(items));
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testCoversWithArrayParameterThrowsException() {
        AccidentData[] items = null;
        new AssociationRule<>(new ItemSet<>(), new ItemSet<>(), 0.5).covers(items);
    }


    @Test
    public final void testCoversWithIterableParameter() {
        Collection<AccidentData> items = new LinkedList<>();
        items.add(item1);
        items.add(item3);
        items.add(item4);
        items.add(item5);
        items.add(item6);
        ItemSet<AccidentData> body = new ItemSet<>();
        body.add(item1);
        body.add(item2);
        ItemSet<AccidentData> head = new ItemSet<>();
        head.add(item3);
        AssociationRule<AccidentData> associationRule = new AssociationRule<>(new ItemSet<>(), head,
                0.5);
        assertTrue(associationRule.covers(items));
        associationRule = new AssociationRule<>(body, head, 0.5);
        assertFalse(associationRule.covers(items));
        items.add(item2);
        assertTrue(associationRule.covers(items));
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testCoversWithIterableParameterThrowsException() {
        Iterable<AccidentData> iterable = null;
        new AssociationRule<>(new ItemSet<>(), new ItemSet<>(), 0.5).covers(iterable);
    }


    @Test
    public final void testCoversWithIteratorParameter() {
        Collection<AccidentData> items = new LinkedList<>();
        items.add(item1);
        items.add(item3);
        items.add(item4);
        items.add(item5);
        items.add(item6);
        ItemSet<AccidentData> body = new ItemSet<>();
        body.add(item1);
        body.add(item2);
        ItemSet<AccidentData> head = new ItemSet<>();
        head.add(item3);
        AssociationRule<AccidentData> associationRule = new AssociationRule<>(new ItemSet<>(), head,
                0.5);
        assertTrue(associationRule.covers(items.iterator()));
        associationRule = new AssociationRule<>(body, head, 0.5);
        assertFalse(associationRule.covers(items.iterator()));
        items.add(item2);
        assertTrue(associationRule.covers(items.iterator()));
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testCoversWithIteratorParameterThrowsException() {
        Iterator<AccidentData> iterator = null;
        new AssociationRule<>(new ItemSet<>(), new ItemSet<>(), 0.5).covers(iterator);
    }


    @Test
    public final void testCompareTo() {
        ItemSet<AccidentData> body = new ItemSet<>();
        body.add(item1);
        ItemSet<AccidentData> head = new ItemSet<>();
        head.add(item2);
        AssociationRule<AccidentData> associationRule1 = new AssociationRule<>(body, head, 0.5);
        AssociationRule<AccidentData> associationRule2 = new AssociationRule<>(body, head, 0.5);
        assertEquals(0, associationRule1.compareTo(associationRule2));
        associationRule2 = new AssociationRule<>(body, head, 0.6);
        assertEquals(-1, associationRule1.compareTo(associationRule2));
        associationRule2 = new AssociationRule<>(body, head, 0.4);
        assertEquals(1, associationRule1.compareTo(associationRule2));
    }


    @Test
    public final void testClone() {
        ItemSet<AccidentData> body = new ItemSet<>();
        body.add(item1);
        ItemSet<AccidentData> head = new ItemSet<>();
        head.add(item2);
        double support = 0.5;
        AssociationRule<AccidentData> associationRule1 = new AssociationRule<>(body, head, support);
        AssociationRule<AccidentData> associationRule2 = associationRule1.clone();
        assertEquals(body.size(), associationRule2.getBody().size());
        assertEquals(item1, associationRule2.getBody().first());
        assertEquals(head.size(), associationRule2.getConsequence().size());
        assertEquals(item2, associationRule2.getConsequence().first());
        assertEquals(support, associationRule2.getSupport(), 0);
    }


    @Test
    public final void testToString() {
        ItemSet<AccidentData> body = new ItemSet<>();
        body.add(item1);
        body.add(item2);
        ItemSet<AccidentData> head = new ItemSet<>();
        head.add(item3);
        head.add(item4);
        AssociationRule<AccidentData> associationRule = new AssociationRule<>(body, head, 0.5);
        Assert.assertEquals("[" + item1.toString() + ", " + item2.toString() + "] -> [" + item3.toString() + ", " + item4.toString() + "]", associationRule.toString());
    }


    @Test
    public final void testHashCode() {
        ItemSet<AccidentData> body1 = new ItemSet<>();
        body1.add(item1);
        ItemSet<AccidentData> body2 = new ItemSet<>();
        body2.add(item2);
        ItemSet<AccidentData> head1 = new ItemSet<>();
        head1.add(item3);
        ItemSet<AccidentData> head2 = new ItemSet<>();
        head2.add(item4);
        AssociationRule<AccidentData> associationRule1 = new AssociationRule<>(body1, head1, 0.5);
        AssociationRule<AccidentData> associationRule2 = new AssociationRule<>(body1, head1, 0.5);
        assertEquals(associationRule1.hashCode(), associationRule1.hashCode());
        assertEquals(associationRule1.hashCode(), associationRule2.hashCode());
        associationRule2 = new AssociationRule<>(body2, head1, 0.5);
        assertNotEquals(associationRule1.hashCode(), associationRule2.hashCode());
        associationRule2 = new AssociationRule<>(body1, head2, 0.5);
        assertNotEquals(associationRule1.hashCode(), associationRule2.hashCode());
    }


    @Test
    public final void testEquals() {
        ItemSet<AccidentData> body1 = new ItemSet<>();
        body1.add(item1);
        ItemSet<AccidentData> body2 = new ItemSet<>();
        body2.add(item2);
        ItemSet<AccidentData> head1 = new ItemSet<>();
        head1.add(item3);
        ItemSet<AccidentData> head2 = new ItemSet<>();
        head2.add(item4);
        AssociationRule associationRule1 = new AssociationRule<>(body1, head1, 0.5);
        AssociationRule associationRule2 = new AssociationRule<>(body1, head1, 0.5);
        assertFalse(associationRule1.equals(null));
        assertFalse(associationRule1.equals(new Object()));
        assertTrue(associationRule1.equals(associationRule1));
        assertTrue(associationRule1.equals(associationRule2));
        associationRule2 = new AssociationRule<>(body2, head1, 0.5);
        assertFalse(associationRule1.equals(associationRule2));
        associationRule2 = new AssociationRule<>(body1, head2, 0.5);
        assertFalse(associationRule1.equals(associationRule2));
    }

}