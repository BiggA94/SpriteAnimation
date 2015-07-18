package test.application;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import application.animation.classes.SpriteAnimation;
import application.animation.classes.SpriteHandler;
import application.animation.classes.WalkAnimation;
import application.animation.util.SpriteMap;
import application.animation.util.ZFAnimation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WalkAnimationTest extends ApplicationTest {
	@Override
	public void start(Stage stage) throws Exception {

		// root = new GridPane();
		// stage.setScene(new Scene(root));
		// stage.show();

	}

	private SpriteAnimation<ZFAnimation> spriteAnimation;
	private SpriteHandler<ZFAnimation> spriteHandler;
	private GridPane root;

	@Before
	public void initializeTest() {
		// Create Sprite Handler as Constructed in SpriteHandlerTest
		SpriteHandlerTest spriteHandlerTest = new SpriteHandlerTest();
		spriteHandlerTest.initializeTest();
		spriteHandler = spriteHandlerTest.handler;

		spriteHandler.setSpriteMap(new SpriteMap<ZFAnimation, Integer>());

		// Create SpriteAnimation
		spriteAnimation = new SpriteAnimation<ZFAnimation>(spriteHandler);
		spriteAnimation.setImageView(new ImageView());
		spriteAnimation.setCylceCount(1);
		spriteAnimation.setMoveDuration(Duration.millis(10));
	}

	@Test
	public void testGetDirection() {
		WalkAnimation walkAnimation = new WalkAnimation(spriteAnimation, spriteAnimation.getImageView(),
				new Rectangle2D(0, 0, 1, 1));

		assertEquals(ZFAnimation.UNDEFINED, walkAnimation.calculateDirection(0, 0, 0, 0));
		assertEquals(ZFAnimation.WALK_DOWN, walkAnimation.calculateDirection(0, 0, 0, 1));
		assertEquals(ZFAnimation.WALK_LEFT, walkAnimation.calculateDirection(1, 0, 0, 0));
		assertEquals(ZFAnimation.WALK_LOWERLEFT, walkAnimation.calculateDirection(1, 0, 0, 1));
		assertEquals(ZFAnimation.WALK_LOWERRIGHT, walkAnimation.calculateDirection(0, 0, 1, 1));
		assertEquals(ZFAnimation.WALK_RIGHT, walkAnimation.calculateDirection(0, 0, 1, 0));
		assertEquals(ZFAnimation.WALK_UP, walkAnimation.calculateDirection(0, 1, 0, 0));
		assertEquals(ZFAnimation.WALK_UPPERLEFT, walkAnimation.calculateDirection(1, 1, 0, 0));
		assertEquals(ZFAnimation.WALK_UPPERRIGHT, walkAnimation.calculateDirection(0, 1, 1, 0));

		assertEquals(ZFAnimation.PORT_DOWN, walkAnimation.calculateDirection(0, 0, 0, 2));
		assertEquals(ZFAnimation.PORT_LEFT, walkAnimation.calculateDirection(2, 0, 0, 0));
		assertEquals(ZFAnimation.PORT_LOWERLEFT, walkAnimation.calculateDirection(2, 0, 0, 2));
		assertEquals(ZFAnimation.PORT_LOWERRIGHT, walkAnimation.calculateDirection(0, 0, 2, 2));
		assertEquals(ZFAnimation.PORT_RIGHT, walkAnimation.calculateDirection(0, 0, 2, 0));
		assertEquals(ZFAnimation.PORT_UP, walkAnimation.calculateDirection(0, 2, 0, 0));
		assertEquals(ZFAnimation.PORT_UPPERLEFT, walkAnimation.calculateDirection(2, 2, 0, 0));
		assertEquals(ZFAnimation.PORT_UPPERRIGHT, walkAnimation.calculateDirection(0, 2, 2, 0));
	}

	@Test
	public void testGetDirectionWithBiggerBoxes() {
		WalkAnimation walkAnimation = new WalkAnimation(spriteAnimation, spriteAnimation.getImageView(),
				new Rectangle2D(0, 0, 10, 10));

		assertEquals(ZFAnimation.UNDEFINED, walkAnimation.calculateDirection(0, 0, 0, 0));
		assertEquals(ZFAnimation.WALK_DOWN, walkAnimation.calculateDirection(0, 0, 0, 10));
		assertEquals(ZFAnimation.WALK_LEFT, walkAnimation.calculateDirection(10, 0, 0, 0));
		assertEquals(ZFAnimation.WALK_LOWERLEFT, walkAnimation.calculateDirection(10, 0, 0, 10));
		assertEquals(ZFAnimation.WALK_LOWERRIGHT, walkAnimation.calculateDirection(0, 0, 10, 10));
		assertEquals(ZFAnimation.WALK_RIGHT, walkAnimation.calculateDirection(0, 0, 10, 0));
		assertEquals(ZFAnimation.WALK_UP, walkAnimation.calculateDirection(0, 10, 0, 0));
		assertEquals(ZFAnimation.WALK_UPPERLEFT, walkAnimation.calculateDirection(10, 10, 0, 0));
		assertEquals(ZFAnimation.WALK_UPPERRIGHT, walkAnimation.calculateDirection(0, 10, 10, 0));

		assertEquals(ZFAnimation.PORT_DOWN, walkAnimation.calculateDirection(0, 0, 0, 20));
		assertEquals(ZFAnimation.PORT_LEFT, walkAnimation.calculateDirection(20, 0, 0, 0));
		assertEquals(ZFAnimation.PORT_LOWERLEFT, walkAnimation.calculateDirection(20, 0, 0, 20));
		assertEquals(ZFAnimation.PORT_LOWERRIGHT, walkAnimation.calculateDirection(0, 0, 20, 20));
		assertEquals(ZFAnimation.PORT_RIGHT, walkAnimation.calculateDirection(0, 0, 20, 0));
		assertEquals(ZFAnimation.PORT_UP, walkAnimation.calculateDirection(0, 20, 0, 0));
		assertEquals(ZFAnimation.PORT_UPPERLEFT, walkAnimation.calculateDirection(20, 20, 0, 0));
		assertEquals(ZFAnimation.PORT_UPPERRIGHT, walkAnimation.calculateDirection(0, 20, 20, 0));
	}

	@Test
	public void testMove() {
		WalkAnimation walkAnimation = new WalkAnimation(spriteAnimation, spriteAnimation.getImageView(),
				new Rectangle2D(0, 0, 1, 1));		
		walkAnimation.moveAndWait(10, 10);
		assertEquals(10,this.spriteAnimation.getImageView().getTranslateX(),0);
		assertEquals(10,this.spriteAnimation.getImageView().getTranslateY(),0);
	}

}
