package com.github.artemdvn.longmap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.artemdvn.longmap.LongMap;

/**
 * Unit tests for @LongMap.
 */
public class LongMapTest {
    private LongMap<String> mapToTest;    

    @Before
	public void setUp() {
        mapToTest = new LongMap<String>();
    }
    
    @Test
    public void sizeTest() {
    	Assert.assertEquals(0, mapToTest.size());
    	mapToTest.put(-25L, "Minus twenty five");
    	Assert.assertEquals(1, mapToTest.size());
    	mapToTest.put(1L, "One");
    	Assert.assertEquals(2, mapToTest.size());
    	mapToTest.put(1L, "Another one");
    	Assert.assertEquals(2, mapToTest.size());
        mapToTest.put(5L, "Five");
    	Assert.assertEquals(3, mapToTest.size());
    }

    @Test
    public void putTest() {
    	mapToTest.put(-25L, "Minus twenty five");
    	mapToTest.put(1L, "One");
    	mapToTest.put(1L, "Another one");
    	mapToTest.put(5L, "Five");
    	mapToTest.put(566L, "566");
    	mapToTest.put(7L, null);
        
    	Assert.assertEquals(5, mapToTest.size());
        Assert.assertEquals("Minus twenty five", mapToTest.get(-25L));
        Assert.assertNotEquals("One", mapToTest.get(1L));
        Assert.assertEquals("Another one", mapToTest.get(1L));
        Assert.assertEquals("Five", mapToTest.get(5L));
        Assert.assertNull(mapToTest.get(7L));
        Assert.assertNull(mapToTest.get(111L));
    }
    
    @Test
    public void getTest() {
    	mapToTest.put(-25L, "Minus twenty five");
    	mapToTest.put(1L, "One");
    	mapToTest.put(566L, "566");
    	mapToTest.put(7L, null);
        
    	Assert.assertEquals("Minus twenty five", mapToTest.get(-25L));
        Assert.assertEquals("One", mapToTest.get(1L));
        Assert.assertNull(mapToTest.get(7L));
        Assert.assertNull(mapToTest.get(111L));
        Assert.assertNull(mapToTest.get(-888L));
        Assert.assertNotNull(mapToTest.get(566L));
    }

    @Test
    public void removeTest() {
    	mapToTest.put(-25L, "Minus twenty five");
    	mapToTest.put(1L, "One");
    	mapToTest.put(566L, "566");
    	mapToTest.put(7L, null);
    	
        Assert.assertEquals(4, mapToTest.size());
        Assert.assertEquals("566", mapToTest.get(566L));

        mapToTest.remove(566L);
        Assert.assertEquals(3, mapToTest.size());
        Assert.assertNull(mapToTest.get(566L));
        
        mapToTest.remove(1212L);
        Assert.assertEquals(3, mapToTest.size());
        Assert.assertNull(mapToTest.get(1212L));
    }

    @Test
    public void isEmptyTest() {
        Assert.assertTrue(mapToTest.isEmpty());
        mapToTest.put(1L, "One");
        Assert.assertFalse(mapToTest.isEmpty());
        mapToTest.clear();
        Assert.assertTrue(mapToTest.isEmpty());
    }

    @Test
    public void clearTest() {
    	mapToTest.put(1L, "One");
    	mapToTest.put(7L, "Seven");
        Assert.assertEquals(2, mapToTest.size());

        mapToTest.clear();
        Assert.assertEquals(0, mapToTest.size());
    }

    @Test
    public void containsKeyTest() {
    	mapToTest.put(1L, "One");
    	mapToTest.put(7L, "Seven");
        
    	Assert.assertTrue(mapToTest.containsKey(7L));
        Assert.assertFalse(mapToTest.containsKey(-47L));
    }

    @Test
    public void containsValueTest() {
    	mapToTest.put(-25L, "Minus twenty five");
    	mapToTest.put(1L, "One");
    	mapToTest.put(7L, null);
        
    	Assert.assertTrue(mapToTest.containsValue("One"));
        Assert.assertTrue(mapToTest.containsValue(null));
        Assert.assertFalse(mapToTest.containsValue("566"));
    }
    
    @Test
    public void keysTest() {
    	mapToTest.put(1L, "One");
    	mapToTest.put(7L, "Seven");
        
    	Assert.assertEquals(2, mapToTest.keys().length);
        Assert.assertEquals(1L, mapToTest.keys()[0]);
    }
    
    @Test
    public void valuesTest() {
    	LongMap<Boolean> mapToTestBoolean = new LongMap<Boolean>();
    	mapToTestBoolean.put(15L, true);
    	
    	Assert.assertEquals(1, mapToTestBoolean.values().length);
        Assert.assertTrue(mapToTestBoolean.values()[0]);
        
        LongMap<String> mapToTestString = new LongMap<String>();
        mapToTestString.put(-25L, "Minus twenty five");
        mapToTestString.put(1L, "One");
        mapToTestString.put(15L, "One");
    	
    	Assert.assertEquals(3, mapToTestString.values().length);
        Assert.assertNotNull(mapToTestString.values()[0]);
    }    

}
