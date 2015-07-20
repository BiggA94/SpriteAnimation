package de.uks.se1.ss15.dtritus.zombiefighter.animation.animations;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import de.uks.se1.ss15.dtritus.zombiefighter.animation.animations.singleAnimations.ExampleSingleExplosionAnimation;
import de.uks.se1.ss15.dtritus.zombiefighter.animation.classes.Animation;
import de.uks.se1.ss15.dtritus.zombiefighter.animation.classes.SingleAnimation;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Explosion extends Animation {

	public Explosion(ImageView imageView, Bounds bounds) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		super(imageView, bounds);
		// TODO Auto-generated constructor stub
	}

	public Explosion(Pane parentNode, Bounds bounds) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		super(parentNode, bounds);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initAnimations(LinkedHashMap<String, SingleAnimation> animations, ImageView imageView, Bounds bounds)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		animations.put("Explosion",
				SingleAnimation.createSingleAnimation(ExampleSingleExplosionAnimation.class, imageView, bounds));
		animations.put("Explosion2",
				SingleAnimation.createSingleAnimation(ExampleSingleExplosionAnimation.class, imageView, bounds));

	}

}
