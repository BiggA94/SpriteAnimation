package test.application.animation.classes;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import application.animation.classes.SpriteHandler;
import application.animation.util.SpriteMap;
import application.animation.util.ZFAnimation;
import javafx.geometry.Rectangle2D;

public class SpriteHandlerTest {
	private int offsetX = 1;
	private int offsetY = 1;
	/**
	 * Pictures per Row
	 */
	private int rowCount = 2;
	/**
	 * Pictures per Column
	 */
	private int columnCount = 8;
	/**
	 * Width/Height of Line that Separates the images
	 */
	private int separationWidth = 1;
	/**
	 * Height of the Part of the Image, that should be used
	 */
	private int heigth = 99;
	/**
	 * Width of the Part of the Image, that should be used
	 */
	private int width = 393;

	private int widhtPerPic = 48;
	private int heightPerPic = 48;
	public SpriteHandler<ZFAnimation> handler;

	@Before
	public void initializeTest() {
		handler = new SpriteHandler<ZFAnimation>(offsetX, offsetY, rowCount, columnCount, separationWidth, width, heigth);
	}

	@Test
	public void testNum0() {
		Rectangle2D viewPort = handler.getViewPort(0);
		assertEquals(offsetX, viewPort.getMinX(), 0);
		assertEquals(offsetY, viewPort.getMinY(), 0);
		assertEquals(48, viewPort.getWidth(), 0);
		assertEquals(48, viewPort.getHeight(), 0);
		testNumX(0);
	}

	@Test
	public void testNum1() {
		Rectangle2D viewPort = handler.getViewPort(1);
		assertEquals(offsetX + widhtPerPic + separationWidth, viewPort.getMinX(), 0);
		assertEquals(offsetY, viewPort.getMinY(), 0);
		assertEquals(widhtPerPic, viewPort.getWidth(), 0);
		assertEquals(heightPerPic, viewPort.getHeight(), 0);
		testNumX(1);
	}

	@Test
	public void testNum2() {
		Rectangle2D viewPort = handler.getViewPort(2);
		assertEquals(offsetX + 2 * (widhtPerPic + separationWidth), viewPort.getMinX(), 0);
		assertEquals(offsetY, viewPort.getMinY(), 0);
		assertEquals(widhtPerPic, viewPort.getWidth(), 0);
		assertEquals(heightPerPic, viewPort.getHeight(), 0);
		testNumX(2);
	}

	@Test
	public void testNum8() {
		Rectangle2D viewPort = handler.getViewPort(8);
		assertEquals(offsetX, viewPort.getMinX(), 0);
		assertEquals(offsetY + heightPerPic + separationWidth, viewPort.getMinY(), 0);
		assertEquals(widhtPerPic, viewPort.getWidth(), 0);
		assertEquals(heightPerPic, viewPort.getHeight(), 0);
		testNumX(8);
	}

	@Test
	public void testNum9() {
		Rectangle2D viewPort = handler.getViewPort(9);
		assertEquals(offsetX + widhtPerPic + separationWidth, viewPort.getMinX(), 0);
		assertEquals(offsetY + heightPerPic + separationWidth, viewPort.getMinY(), 0);
		assertEquals(widhtPerPic, viewPort.getWidth(), 0);
		assertEquals(heightPerPic, viewPort.getHeight(), 0);
		testNumX(9);
	}

	@Test
	public void testNum7() {
		Rectangle2D viewPort = handler.getViewPort(7);
		assertEquals(offsetX + 7 * (widhtPerPic + separationWidth), viewPort.getMinX(), 0);
		assertEquals(offsetY, viewPort.getMinY(), 0);
		assertEquals(widhtPerPic, viewPort.getWidth(), 0);
		assertEquals(heightPerPic, viewPort.getHeight(), 0);
		testNumX(7);
	}

	@Test
	public void testNumFrom0To16() {
		for (int i = 0; i < 16; i++) {
			testNumX(i);
		}
	}

	public void testNumX(int x) {
		Rectangle2D viewPort = handler.getViewPort(x);
		assertEquals(offsetX + (x % columnCount) * (widhtPerPic + separationWidth), viewPort.getMinX(), 0);
		assertEquals(offsetY + (x / columnCount) * (heightPerPic + separationWidth), viewPort.getMinY(), 0);
		assertEquals(widhtPerPic, viewPort.getWidth(), 0);
		assertEquals(heightPerPic, viewPort.getHeight(), 0);
	}

	@Test
	public void testGetNext0To1() {
		Rectangle2D viewPort = handler.getViewPort(0);

		this.handler.getSpriteMap().addEntry(ZFAnimation.PORT_DOWN, 0, 1, 2, 3, 4, 5, 6, 7);

		Rectangle2D nextViewPort = this.handler.getNextViewPort(ZFAnimation.PORT_DOWN);
		assertEquals(handler.getViewPort(1), nextViewPort);
	}

	@Test
	public void testGetNext7To0() {

		this.handler.getSpriteMap().addEntry(ZFAnimation.PORT_DOWN, 0, 1, 2, 3, 4, 5, 6, 7);

		this.handler.getViewPort(7);

		Rectangle2D nextViewPort = this.handler.getNextViewPort(ZFAnimation.PORT_DOWN);

		assertEquals(handler.getViewPort(0), nextViewPort);
	}

	@Test
	public void testGetNext1To1() {

		this.handler.getSpriteMap().addEntry(ZFAnimation.PORT_DOWN, 1);

		for (int i = 0; i < 10; i++) {
			// Test multiple times, in order to test the cycle

			Rectangle2D nextViewPort = this.handler.getNextViewPort(ZFAnimation.PORT_DOWN);

			assertEquals(handler.getViewPort(1), nextViewPort);

		}
	}

	@Test
	public void testGetNextWithoutViewportBefore() {
		this.handler.getSpriteMap().addEntry(ZFAnimation.PORT_DOWN, 0, 1, 2);

		Rectangle2D nextViewPort = this.handler.getNextViewPort(ZFAnimation.PORT_DOWN);

		assertEquals(this.handler.getViewPort(0), nextViewPort);
	}
	
	@Test
	public void testGetNextWithoutAnimationDefined(){
		Rectangle2D nextViewPort = this.handler.getNextViewPort(ZFAnimation.PORT_DOWN);
		assertEquals(handler.getViewPort(0), nextViewPort);		
	}
	
	@Test
	public void testGetextWithKeyNotInAnimationSet(){
		this.handler.getSpriteMap().addEntry(ZFAnimation.PORT_DOWN, 7,2,4);

		Rectangle2D nextViewPort = this.handler.getNextViewPort(ZFAnimation.PORT_UP);

		assertEquals(this.handler.getViewPort(7), nextViewPort);		
	}
}
