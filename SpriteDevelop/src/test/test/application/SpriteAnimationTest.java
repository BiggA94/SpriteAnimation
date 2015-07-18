package test.application;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import application.animation.SpriteAnimation;
import application.animation.SpriteHandler;
import application.animation.util.SpriteMap;
import application.animation.util.ZFAnimation;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SpriteAnimationTest extends ApplicationTest {

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
	public void testSetImageView() {
		ImageView imageView = new ImageView();

		assertNotEquals(imageView, spriteAnimation.getImageView());

		spriteAnimation.setImageView(imageView);

		assertEquals(imageView, spriteAnimation.getImageView());
	}

	@Test
	public void testMove() throws InterruptedException {
		int ViewportNumToMove = 4;

		spriteAnimation.addEntry(ZFAnimation.WALK_DOWN, ViewportNumToMove);

		assertEquals(spriteAnimation.getList(ZFAnimation.WALK_DOWN).getFirst(), ViewportNumToMove, 0);

		spriteAnimation.moveAndWait(ZFAnimation.WALK_DOWN);

		System.err.println(spriteAnimation.getImageView().getViewport());
		assertEquals(spriteHandler.getViewPort(ViewportNumToMove), spriteAnimation.getImageView().getViewport());
	}

	/*
	 * //@Test public void testMoveCylcic() { spriteAnimation.addEntry(1, 0, 1,
	 * 2, 3, 4, 5, 6, 7);
	 * 
	 * // spriteAnimation.setCylceCount(10);
	 * 
	 * spriteAnimation.moveAndWait(1);
	 * 
	 * assertEquals(this.spriteHandler.getViewPort(7),
	 * spriteAnimation.getImageView().getViewport()); }
	 */

	@Override
	public void start(Stage stage) throws Exception {
		/*
		 * root = new GridPane(); stage.setScene(new Scene(root)); stage.show();
		 */
	}
}
