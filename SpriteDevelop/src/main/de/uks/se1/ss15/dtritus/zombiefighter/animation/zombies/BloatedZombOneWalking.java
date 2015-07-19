package de.uks.se1.ss15.dtritus.zombiefighter.animation.zombies;

import java.net.URL;

import de.uks.se1.ss15.dtritus.zombiefighter.Main;
import de.uks.se1.ss15.dtritus.zombiefighter.animation.classes.SpriteAnimation;
import de.uks.se1.ss15.dtritus.zombiefighter.animation.classes.SpriteHandler;
import de.uks.se1.ss15.dtritus.zombiefighter.animation.classes.Zombie;
import de.uks.se1.ss15.dtritus.zombiefighter.animation.util.ZFAnimation;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;

public class BloatedZombOneWalking extends Zombie {

	public BloatedZombOneWalking(Pane parentNode, Rectangle2D tileSize) {
		super(parentNode, tileSize);
	}

	@Override
	protected URL initImageView() {
		return Main.class.getResource("animation/ressources/bloatedzombonewalking.png");
	}

	@Override
	protected void initAnimations(SpriteAnimation<ZFAnimation> anim) {
		anim.addEntry(ZFAnimation.WALK_DOWN, 0, 1, 2, 3, 4, 5, 6, 7);
	}

	@Override
	protected void initSpriteHandler() {
		sprite = new SpriteHandler<ZFAnimation>(1, 1, 1, 8, 1, 1, 393, 50);
	}
}
