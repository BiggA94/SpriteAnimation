package de.uks.se1.ss15.dtritus.zombiefighter.animation.classes;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import de.uks.se1.ss15.dtritus.zombiefighter.animation.util.ZFAnimation;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public abstract class Zombie {
	protected SpriteHandler<ZFAnimation> sprite;
	protected SpriteAnimation<ZFAnimation> anim;
	private volatile ImageView imageView;
	protected Rectangle2D tileSize;
	private Duration duration = Duration.seconds(1);
	private WalkAnimation walkAnimation;

	private void initZombie() {
		Image image = new Image(initImageView().toString()); // ,tileSize.getWidth(),tileSize.getHeight(),false,false);
		this.getImageView().setImage(image);

		initSpriteHandler();

		anim = new SpriteAnimation<ZFAnimation>(sprite);
		anim.setCylceCount(1);
		// anim.setAnimationRate(0.00151);
		anim.setMoveDuration(Duration.seconds(1));
		anim.setImageView(getImageView());

		initAnimations(anim);
	}

	protected abstract URL initImageView();

	protected abstract void initAnimations(SpriteAnimation<ZFAnimation> anim);

	protected abstract void initSpriteHandler();

	public Zombie(ImageView imgView, Rectangle2D tileSize) {
		this.setImageView(imgView);
		this.tileSize = tileSize;

		initZombie();

	}

	public Zombie(Pane parentNode, Rectangle2D tileSize) {
		this(new ImageView(), tileSize);

		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> {
				parentNode.getChildren().add(this.getImageView());
			});
		} else {
			parentNode.getChildren().add(this.getImageView());
		}

		this.getImageView().setFitWidth(tileSize.getWidth());
		this.getImageView().setFitHeight(tileSize.getHeight());

		this.getImageView().setTranslateX(tileSize.getMinX());
		this.getImageView().setTranslateY(tileSize.getMinY());

		this.getImageView().setViewport(this.sprite.getViewPort(0));

		while (!parentNode.getChildren().contains(this.getImageView())) {
			// wait
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

	public void move(double toX, double toY) {
		this.move(getImageView().getTranslateX(), getImageView().getTranslateY(), toX, toY);
	}

	public void move(double fromX, double fromY, double toX, double toY) {
		this.getWalkAnimation().move(fromX, fromY, toX, toY);
	}

	/**
	 * Waits for the Animation to be finished
	 * 
	 * @param toX
	 * @param toY
	 * @throws InterruptedException 
	 */
	public void moveAndWait(double toX, double toY) throws InterruptedException {
		this.moveAndWait(getImageView().getTranslateX(), getImageView().getTranslateY(), toX, toY);
	}

	public void moveAndWait(double fromX, double fromY, double toX, double toY) throws InterruptedException {
		this.getWalkAnimation().moveAndWait(fromX, fromY, toX, toY);
	}

	public void die() {
		anim.destroy(ZFAnimation.DIE);
	}
	
	public void dieAndWait() {
		anim.destroyAndWait(ZFAnimation.DIE);
	}

	/**
	 * @return the imageView
	 */
	public ImageView getImageView() {
		return imageView;
	}

	/**
	 * @param imageView
	 *            the imageView to set
	 */
	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	public static Zombie createZombie(Class<? extends Zombie> newClass, Node parentNode, Rectangle2D tileSize) {
		Zombie ret = null;
		try {
			ret = newClass.getConstructor(Pane.class, Rectangle2D.class).newInstance(parentNode, tileSize);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return ret;

	}

	public void destroyZombie() {
		destroyZombie(this.getImageView());
	}

	public static void destroyZombie(ImageView imageView) {
		Parent parent = imageView.getParent();
		if (parent instanceof Pane) {
			if (Platform.isFxApplicationThread()) {
				((Pane) parent).getChildren().remove(imageView);
			} else {
				Platform.runLater(() -> {
					((Pane) parent).getChildren().remove(imageView);
				});
			}
		}
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
		this.anim.setMoveDuration(this.getDuration());
	}

	public WalkAnimation getWalkAnimation() {
		if (walkAnimation == null)
			walkAnimation = new WalkAnimation(anim, getImageView(), tileSize, this.getDuration());
		// Update WalkSpeed
		walkAnimation.setDuration(duration);
		return walkAnimation;
	}
}
