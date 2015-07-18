package test.application.animation.classes;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

import application.animation.util.SpriteMap;
import application.animation.util.ZFAnimation;

public class SpriteMapTest {
	SpriteMap<ZFAnimation, Integer> map = new SpriteMap<ZFAnimation, Integer>();

	@Test
	public void testAddEntry() {
		map = new SpriteMap<ZFAnimation, Integer>();

		assertNull(map.getList(ZFAnimation.WALK_DOWN));
		map.addEntry(ZFAnimation.WALK_DOWN, 1,3,5,7);
		LinkedList<Integer> res = new LinkedList<Integer>();
		res.add(1);
		res.add(3);
		res.add(5);
		res.add(7);
		
		assertEquals(res,map.getList(ZFAnimation.WALK_DOWN));
	}
	
	@Test
	public void testRemoveEntry(){
		// Add 1,3,5,7 to Direction.DOWN
		testAddEntry();
		
		// Remove 3
		assertTrue(map.removeEntry(ZFAnimation.WALK_DOWN, 1));
		
		// Check result
		LinkedList<Integer> res = new LinkedList<Integer>();
		res.add(1);
		res.add(5);
		res.add(7);
		assertEquals(res, map.getList(ZFAnimation.WALK_DOWN));
	}
	
	@Test
	public void testWrongRemoveEntry(){
		// Add 1,3,5,7 to Direction.DOWN
		testAddEntry();
		
		// Remove from Direction.LEFT, where no entry was added
		assertFalse(map.removeEntry(ZFAnimation.WALK_LEFT, 1));
		
		// Check that the initial List is still there
		LinkedList<Integer> res = new LinkedList<Integer>();
		res.add(1);
		res.add(3);
		res.add(5);
		res.add(7);
		assertEquals(res, map.getList(ZFAnimation.WALK_DOWN));
	}

}
