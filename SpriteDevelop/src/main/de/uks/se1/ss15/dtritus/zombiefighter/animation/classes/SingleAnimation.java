package de.uks.se1.ss15.dtritus.zombiefighter.animation.classes;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import de.uks.se1.ss15.dtritus.zombiefighter.animation.util.ZFAnimation;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public abstract class SingleAnimation<Tkey extends ZFAnimation> {
	Bounds bounds;
	private SpriteAnimation<Tkey> spriteAnimation;
	private SpriteHandler<Tkey> spriteHandler;

	private ImageView imageView;

	public ImageView getImageView() {
		if (this.imageView == null)
			this.imageView = new ImageView();
		return imageView;
	};

	public void initSingleAnimation(Bounds bounds) {
		/*
		 * Image image = new Image(initImageView().toString()); //
		 * ,tileSize.getWidth(),tileSize.getHeight(),false,false);
		 * this.getImageView().setImage(image);
		 */

		this.bounds = bounds;

		spriteHandler = initSpriteHandler();

		spriteAnimation = new SpriteAnimation<>(spriteHandler);
		spriteAnimation.setCylceCount(1);
		spriteAnimation.setMoveDuration(Duration.seconds(1));
		spriteAnimation.setImageView(getImageView());

		initSpriteAnimation(spriteAnimation);

		initAnimation(spriteAnimation);
	}

	/**
	 * Add Some Properties to the SpriteAnimation
	 * 
	 * @param spriteAnimation
	 * @return
	 */
	protected abstract SpriteAnimation initSpriteAnimation(SpriteAnimation spriteAnimation);

	public SingleAnimation(Pane parentNode, Bounds tileSize) {
		this(new ImageView(), tileSize);
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> {
				parentNode.getChildren().add(this.getImageView());
			});
		} else {
			parentNode.getChildren().add(this.getImageView());
		}

		this.getImageView().setMouseTransparent(true);

		this.getImageView().setFitWidth(tileSize.getWidth());
		this.getImageView().setFitHeight(tileSize.getHeight());

		this.getImageView().setTranslateX(tileSize.getMinX());
		this.getImageView().setTranslateY(tileSize.getMinY());

		this.getImageView().setViewport(this.spriteHandler.getViewPort(0));

		while (!parentNode.getChildren().contains(this.getImageView())) {
			// wait
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

	public SingleAnimation(ImageView imageView, Bounds tileSize) {
		this.imageView = imageView;

		initSingleAnimation(tileSize);

		this.getImageView().setViewport(this.spriteHandler.getViewPort(0));

	}

	public void play(Tkey key) {
		Image image = new Image(initImageView().toString());
		System.out.println(image);
		this.getImageView().setImage(image);
		this.spriteAnimation.animate(key);
	}

	public void playAndWait(Tkey key) throws InterruptedException {
		Image image = new Image(initImageView().toString());
		System.out.println(image);
		this.getImageView().setImage(image);
		this.spriteAnimation.animateAndWait(key);
	}

	public void play() {
		play((Tkey) ZFAnimation.DEFAULT);
	}
	
	public void playAndWait() throws InterruptedException {
		playAndWait((Tkey) ZFAnimation.DEFAULT);
	}

	protected abstract URL initImageView();

	/**
	 * Initialize the SpriteHandler
	 * 
	 * @return
	 */
	protected abstract SpriteHandler<Tkey> initSpriteHandler();

	/**
	 * Initialize the SpriteAnimation (add key and animation)
	 * 
	 * @param spriteAnimation
	 */
	protected abstract void initAnimation(SpriteAnimation<Tkey> spriteAnimation);

	public static SingleAnimation createSingleAnimation(Class<? extends SingleAnimation> classToCreate, Pane parentNode,
			Bounds bounds) throws InstantiationException, IllegalAccessException, IllegalArgumentException,
					InvocationTargetException, NoSuchMethodException, SecurityException {
		System.out.println("SingleAnimation.createSingleAnimation(Pane)");
		return classToCreate.getConstructor(Pane.class, Bounds.class).newInstance(parentNode, bounds);
	}

	public static SingleAnimation createSingleAnimation(Class<? extends SingleAnimation> classToCreate,
			ImageView imageView, Bounds bounds) throws InstantiationException, IllegalAccessException,
					IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		System.out.println("SingleAnimation.createSingleAnimation(ImageView)");
		return classToCreate.getConstructor(ImageView.class, Bounds.class).newInstance(imageView, bounds);
	}
}
