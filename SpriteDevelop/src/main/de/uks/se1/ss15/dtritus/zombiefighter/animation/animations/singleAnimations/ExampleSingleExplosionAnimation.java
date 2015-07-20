package de.uks.se1.ss15.dtritus.zombiefighter.animation.animations.singleAnimations;

import java.net.URL;

import de.uks.se1.ss15.dtritus.zombiefighter.animation.classes.SingleAnimation;
import de.uks.se1.ss15.dtritus.zombiefighter.animation.classes.SpriteAnimation;
import de.uks.se1.ss15.dtritus.zombiefighter.animation.classes.SpriteHandler;
import de.uks.se1.ss15.dtritus.zombiefighter.animation.util.ZFAnimation;
import de.uks.se1.ss15.dtritus.zombiefighter.test.animation.demo.Main;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class ExampleSingleExplosionAnimation extends SingleAnimation<ZFAnimation> {

	public ExampleSingleExplosionAnimation(Pane parentNode, Bounds tileSize) {
		super(parentNode, tileSize);
	}

	public ExampleSingleExplosionAnimation(ImageView imageView, Bounds tileSize) {
		super(imageView, tileSize);
	}

	@Override
	protected void initAnimation(SpriteAnimation<ZFAnimation> spriteAnimation) {
		spriteAnimation.addEntry(ZFAnimation.DEFAULT, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
	}

	@Override
	protected SpriteHandler<ZFAnimation> initSpriteHandler() {
		SpriteHandler<ZFAnimation> anim = new SpriteHandler<>(0, 0, 2, 5, 0, 0, 1355, 532);
		return anim;
	}

	@Override
	protected URL initImageView() {
		return Main.class.getResource("animation/ressources/singleAnimations/Oexplosion_Sprite.png");
	}	

	@Override
	protected SpriteAnimation initSpriteAnimation(SpriteAnimation spriteAnimation) {
		return spriteAnimation;
	}

}
