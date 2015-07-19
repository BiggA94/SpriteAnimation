package de.uks.se1.ss15.dtritus.zombiefighter.test.animation.classes;

import static org.junit.Assert.*;

import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxToolkit;

import de.uks.se1.ss15.dtritus.zombiefighter.animation.classes.Zombie;
import de.uks.se1.ss15.dtritus.zombiefighter.animation.zombies.BloatedZombOneWalking;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class ZombieTest {

	@BeforeClass
	public static void beforeClass() throws TimeoutException {
		FxToolkit.registerPrimaryStage();
		FxToolkit.showStage();
	}

	@Before
	public void before() throws TimeoutException {
		FxToolkit.setupStage(stage -> {
			stage.setScene(loadNewScene());
		});
		// FxToolkit.showStage();

		zombie = Zombie.createZombie(BloatedZombOneWalking.class, pane, new Rectangle2D(0, 0, 10, 10));

		assertEquals(zombie.getImageView().getBoundsInParent().getMinX(), 0, 0);
		assertEquals(zombie.getImageView().getBoundsInParent().getMinY(), 0, 0);
		assertEquals(zombie.getImageView().getFitHeight(), 10, 0);
		assertEquals(zombie.getImageView().getFitWidth(), 10, 0);

	}

	private AnchorPane pane;
	private Zombie zombie;

	private Scene loadNewScene() {
		pane = new AnchorPane();

		Scene scene = new Scene(pane);

		return scene;
	}

	public volatile boolean finished = false;

	public void removeZombie() {
		if (this.zombie != null && this.zombie.getImageView().getParent() != null) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					zombie.destroyZombie();
					finished = true;
				}
			});
			while (!finished) {
				// wait
			}
			finished = false;
		}
	}

	@Test
	public void testZombieCreation() {
		// Remove The Zombie that is created for the other test
		removeZombie();

		assertEquals(0, pane.getChildren().size());
		Zombie zombie = Zombie.createZombie(BloatedZombOneWalking.class, pane, new Rectangle2D(11, 12, 10, 10));
		assertEquals(pane.getChildren().get(0), zombie.getImageView());

		assertEquals(zombie.getImageView().getBoundsInParent().getMinX(), 11, 0);
		assertEquals(zombie.getImageView().getBoundsInParent().getMinY(), 12, 0);
		assertEquals(zombie.getImageView().getFitHeight(), 10, 0);
		assertEquals(zombie.getImageView().getFitWidth(), 10, 0);
	}

	@Test
	public void testMove() throws InterruptedException {
		zombie.moveAndWait(10, 0);

		assertEquals(zombie.getImageView().getBoundsInParent().getMinX(), 10, 0);
		assertEquals(zombie.getImageView().getBoundsInParent().getMinY(), 0, 0);
	}

	@Test
	public void setDurationTest() {
		assertEquals(Duration.seconds(1), zombie.getDuration());
		zombie.setDuration(Duration.millis(10));
		assertEquals(Duration.millis(10), zombie.getDuration());
	}
	
	@Test
	public void testDie(){
		assertEquals(pane,zombie.getImageView().getParent());
		zombie.dieAndWait();
		assertNull(zombie.getImageView().getParent());
	}
}
