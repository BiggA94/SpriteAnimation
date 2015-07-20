package de.uks.se1.ss15.dtritus.zombiefighter.test.animation.classes;

import static org.junit.Assert.*;

import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import de.uks.se1.ss15.dtritus.zombiefighter.animation.classes.SpriteAnimation;
import de.uks.se1.ss15.dtritus.zombiefighter.animation.classes.SpriteHandler;
import de.uks.se1.ss15.dtritus.zombiefighter.animation.util.SpriteMap;
import de.uks.se1.ss15.dtritus.zombiefighter.animation.util.ZFAnimation;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class SpriteAnimationTest {

	@BeforeClass
	public static void beforeClass() throws TimeoutException {
		FxToolkit.registerPrimaryStage();
		//FxToolkit.showStage();
	}

	private SpriteAnimation<ZFAnimation> spriteAnimation;
	private SpriteHandler<ZFAnimation> spriteHandler;
	private GridPane root;

	@Before
	public void initializeTest() {
		// FxToolkit.setupStage(stage -> {
		// stage.setScene(loadNewScene());
		// });
		// FxToolkit.showStage();
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

		spriteAnimation.animateAndWait(ZFAnimation.WALK_DOWN);

		assertEquals(spriteHandler.getViewPort(ViewportNumToMove), spriteAnimation.getImageView().getViewport());
	}

	@Test
	public void testDie() throws InterruptedException {
		int ViewportNumToMove = 4;

		spriteAnimation.addEntry(ZFAnimation.DIE, ViewportNumToMove);

		assertEquals(spriteAnimation.getList(ZFAnimation.DIE).getFirst(), ViewportNumToMove, 0);

		spriteAnimation.destroyAndWait(ZFAnimation.DIE);
		assertEquals(spriteHandler.getViewPort(ViewportNumToMove), spriteAnimation.getImageView().getViewport());
		assertNull(this.spriteAnimation.getImageView().getParent());
	}

}
