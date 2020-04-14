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
    static AccidentAttribute item1,item2,item3,item4,item5,item6;

    @Before
    public void setup() {

        item3 = AccidentAttribute.builder().name("1999").build();
        item1 = AccidentAttribute.builder().name("2000").build();
        item2 = AccidentAttribute.builder().name("2001").build();
        item4 = AccidentAttribute.builder().name("2002").build();
        item5 = AccidentAttribute.builder().name("2003").build();
        item6 = AccidentAttribute.builder().name("2004").build();
    }

    @Test
    public final void testConstructor() {
        ItemSet<AccidentAttribute> body = new ItemSet<>();
        body.add(item1);
        body.add(item2);
        ItemSet<AccidentAttribute> head = new ItemSet<>();
        head.add(item3);
        head.add(item4);
        double support = 0.5;
        AssociationRule<AccidentAttribute> associationRule = new AssociationRule<>(body, head, support);
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
        AccidentAttribute[] items = {
                item1,item3,item4,item5,item6};
        ItemSet<AccidentAttribute> body = new ItemSet<>();
        body.add(item1);
        body.add(item2);
        ItemSet<AccidentAttribute> head = new ItemSet<>();
        head.add(item3);
        AssociationRule<AccidentAttribute> associationRule = new AssociationRule<>(new ItemSet<>(), head,
                0.5);
        assertTrue(associationRule.covers(items));
        associationRule = new AssociationRule<>(body, head, 0.5);
        assertFalse(associationRule.covers(items));
        items[1] = item2;
        assertTrue(associationRule.covers(items));
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testCoversWithArrayParameterThrowsException() {
        AccidentAttribute[] items = null;
        new AssociationRule<>(new ItemSet<>(), new ItemSet<>(), 0.5).covers(items);
    }


    @Test
    public final void testCoversWithIterableParameter() {
        Collection<AccidentAttribute> items = new LinkedList<>();
        items.add(item1);
        items.add(item3);
        items.add(item4);
        items.add(item5);
        items.add(item6);
        ItemSet<AccidentAttribute> body = new ItemSet<>();
        body.add(item1);
        body.add(item2);
        ItemSet<AccidentAttribute> head = new ItemSet<>();
        head.add(item3);
        AssociationRule<AccidentAttribute> associationRule = new AssociationRule<>(new ItemSet<>(), head,
                0.5);
        assertTrue(associationRule.covers(items));
        associationRule = new AssociationRule<>(body, head, 0.5);
        assertFalse(associationRule.covers(items));
        items.add(item2);
        assertTrue(associationRule.covers(items));
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testCoversWithIterableParameterThrowsException() {
        Iterable<AccidentAttribute> iterable = null;
        new AssociationRule<>(new ItemSet<>(), new ItemSet<>(), 0.5).covers(iterable);
    }


    @Test
    public final void testCoversWithIteratorParameter() {
        Collection<AccidentAttribute> items = new LinkedList<>();
        items.add(item1);
        items.add(item3);
        items.add(item4);
        items.add(item5);
        items.add(item6);
        ItemSet<AccidentAttribute> body = new ItemSet<>();
        body.add(item1);
        body.add(item2);
        ItemSet<AccidentAttribute> head = new ItemSet<>();
        head.add(item3);
        AssociationRule<AccidentAttribute> associationRule = new AssociationRule<>(new ItemSet<>(), head,
                0.5);
        assertTrue(associationRule.covers(items.iterator()));
        associationRule = new AssociationRule<>(body, head, 0.5);
        assertFalse(associationRule.covers(items.iterator()));
        items.add(item2);
        assertTrue(associationRule.covers(items.iterator()));
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testCoversWithIteratorParameterThrowsException() {
        Iterator<AccidentAttribute> iterator = null;
        new AssociationRule<>(new ItemSet<>(), new ItemSet<>(), 0.5).covers(iterator);
    }


    @Test
    public final void testCompareTo() {
        ItemSet<AccidentAttribute> body = new ItemSet<>();
        body.add(item1);
        ItemSet<AccidentAttribute> head = new ItemSet<>();
        head.add(item2);
        AssociationRule<AccidentAttribute> associationRule1 = new AssociationRule<>(body, head, 0.5);
        AssociationRule<AccidentAttribute> associationRule2 = new AssociationRule<>(body, head, 0.5);
        assertEquals(0, associationRule1.compareTo(associationRule2));
        associationRule2 = new AssociationRule<>(body, head, 0.6);
        assertEquals(-1, associationRule1.compareTo(associationRule2));
        associationRule2 = new AssociationRule<>(body, head, 0.4);
        assertEquals(1, associationRule1.compareTo(associationRule2));
    }


    @Test
    public final void testClone() {
        ItemSet<AccidentAttribute> body = new ItemSet<>();
        body.add(item1);
        ItemSet<AccidentAttribute> head = new ItemSet<>();
        head.add(item2);
        double support = 0.5;
        AssociationRule<AccidentAttribute> associationRule1 = new AssociationRule<>(body, head, support);
        AssociationRule<AccidentAttribute> associationRule2 = associationRule1.clone();
        assertEquals(body.size(), associationRule2.getBody().size());
        assertEquals(item1, associationRule2.getBody().first());
        assertEquals(head.size(), associationRule2.getConsequence().size());
        assertEquals(item2, associationRule2.getConsequence().first());
        assertEquals(support, associationRule2.getSupport(), 0);
    }


    @Test
    public final void testToString() {
        ItemSet<AccidentAttribute> body = new ItemSet<>();
        body.add(item1);
        body.add(item2);
        ItemSet<AccidentAttribute> head = new ItemSet<>();
        head.add(item3);
        head.add(item4);
        AssociationRule<AccidentAttribute> associationRule = new AssociationRule<>(body, head, 0.5);
        Assert.assertEquals("[" + item1.toString() + ", " + item2.toString() + "] -> [" + item3.toString() + ", " + item4.toString() + "]", associationRule.toString());
    }


    @Test
    public final void testHashCode() {
        ItemSet<AccidentAttribute> body1 = new ItemSet<>();
        body1.add(item1);
        ItemSet<AccidentAttribute> body2 = new ItemSet<>();
        body2.add(item2);
        ItemSet<AccidentAttribute> head1 = new ItemSet<>();
        head1.add(item3);
        ItemSet<AccidentAttribute> head2 = new ItemSet<>();
        head2.add(item4);
        AssociationRule<AccidentAttribute> associationRule1 = new AssociationRule<>(body1, head1, 0.5);
        AssociationRule<AccidentAttribute> associationRule2 = new AssociationRule<>(body1, head1, 0.5);
        assertEquals(associationRule1.hashCode(), associationRule1.hashCode());
        assertEquals(associationRule1.hashCode(), associationRule2.hashCode());
        associationRule2 = new AssociationRule<>(body2, head1, 0.5);
        assertNotEquals(associationRule1.hashCode(), associationRule2.hashCode());
        associationRule2 = new AssociationRule<>(body1, head2, 0.5);
        assertNotEquals(associationRule1.hashCode(), associationRule2.hashCode());
    }


    @Test
    public final void testEquals() {
        ItemSet<AccidentAttribute> body1 = new ItemSet<>();
        body1.add(item1);
        ItemSet<AccidentAttribute> body2 = new ItemSet<>();
        body2.add(item2);
        ItemSet<AccidentAttribute> head1 = new ItemSet<>();
        head1.add(item3);
        ItemSet<AccidentAttribute> head2 = new ItemSet<>();
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