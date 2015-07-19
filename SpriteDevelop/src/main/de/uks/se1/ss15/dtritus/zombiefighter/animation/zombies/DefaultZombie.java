package de.uks.se1.ss15.dtritus.zombiefighter.animation.zombies;

import java.net.URL;

import de.uks.se1.ss15.dtritus.zombiefighter.Main;
import de.uks.se1.ss15.dtritus.zombiefighter.animation.classes.SpriteAnimation;
import de.uks.se1.ss15.dtritus.zombiefighter.animation.classes.SpriteHandler;
import de.uks.se1.ss15.dtritus.zombiefighter.animation.classes.Zombie;
import de.uks.se1.ss15.dtritus.zombiefighter.animation.util.ZFAnimation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class DefaultZombie extends Zombie {

	public DefaultZombie(ImageView imgView, Rectangle2D tileSize) {
		super(imgView, tileSize);
	}

	public DefaultZombie(Pane parentNode, Rectangle2D tileSize) {
		super(parentNode, tileSize);
	}

	@Override
	protected URL initImageView() {
		return Main.class.getResource("animation/ressources/zombie-animation.png");
	}

	@Override
	protected void initAnimations(SpriteAnimation<ZFAnimation> anim) {
		anim.addEntry(ZFAnimation.WALK_RIGHT, 0, 1, 2, 3, 4, 5);
		anim.addEntry(ZFAnimation.DIE, 5, 6, 7, 8, 9, 10);
		this.getWalkAnimation().setStandardAnimation(90);
	}

	@Override
	protected void initSpriteHandler() {
		sprite = new SpriteHandler<ZFAnimation>(0, 0, 2, 6, 5,0, 738, 160);
	}

}
