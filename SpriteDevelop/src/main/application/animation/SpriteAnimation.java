package application.animation;

import java.util.LinkedList;

import application.animation.util.SpriteMap;
import application.animation.util.ZFAnimation;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SpriteAnimation<Tkey> {

	private final class SpriteTransition extends Transition {
		private Tkey key;

		{
			setCycleCount(getCylceCount());
			setRate(getAnimationRate());
			setCycleDuration((getMoveDuration().multiply(getAnimationRate())));
		}

		public SpriteTransition(Tkey key) {
			this.key = key;
		}

		@Override
		protected void interpolate(double frac) {
			// System.out.println("SpriteAnimation.SpriteTransition.interpolate()");
			imageView.setViewport(spriteHandler.getNextViewPort(key));
		}
	}

	private SpriteHandler<Tkey> spriteHandler;

	public SpriteAnimation(SpriteHandler<Tkey> spriteHandler) {
		this.spriteHandler = spriteHandler;
		this.animationSets = spriteHandler.getSpriteMap();
	}

	private ImageView imageView;

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

	/**
	 * Save regions of Sprite you want to use
	 */
	SpriteMap<Tkey, Integer> animationSets = new SpriteMap<Tkey, Integer>();

	/**
	 * @param direction
	 *            The key to save a animationSet as
	 * @param num
	 *            the Viewport numbers that are used
	 */
	public void addEntry(Tkey key, Integer... num) {
		animationSets.addEntry(key, num);
	}

	/**
	 * @return the animationRate
	 */
	public double getAnimationRate() {
		return animationRate;
	}

	private double animationRate = 1;

	/**
	 * @param animationRate
	 *            the animationRate to set
	 */
	public void setAnimationRate(double animationRate) {
		this.animationRate = animationRate;
	}

	/**
	 * @param direction
	 * @return LinkedList
	 */
	public LinkedList<Integer> getList(Tkey key) {
		return animationSets.getList(key);
	}

	private Duration moveDuration = Duration.seconds(2);

	public void setMoveDuration(Duration moveDuration) {
		this.moveDuration = moveDuration;
	}

	public Duration getMoveDuration() {
		return moveDuration;
	}

	private int cylceCount = Animation.INDEFINITE;

	/**
	 * @return the cylceCount
	 */
	public int getCylceCount() {
		return cylceCount;
	}

	/**
	 * @param cylceCount
	 *            the cylceCount to set
	 */
	public void setCylceCount(int cylceCount) {
		this.cylceCount = cylceCount;
	}

	public void move(Tkey key) {
		Transition transition = new SpriteTransition(key);
		transition.play();
	}

	public void die(Tkey key) {
		FadeTransition fadeTransition = new FadeTransition(getMoveDuration());
		fadeTransition.setNode(getImageView());
		fadeTransition.setFromValue(1);
		fadeTransition.setToValue(0);
		fadeTransition.setDuration(Duration.millis(getMoveDuration().toMillis() / 2));

		if (this.animationSets.containsKey(key)) {
			SpriteTransition transition = new SpriteTransition(key);
			fadeTransition.setDelay(Duration.millis(transition.getTotalDuration().toMillis()));
			transition.play();
		}

		fadeTransition.setOnFinished(evt -> {
			Zombie.destroyZombie(imageView);
		});

		fadeTransition.playFromStart();
	}

	protected volatile boolean finished = false;

	public void moveAndWait(Tkey key) {
		Transition transition = new SpriteTransition(key);
		transition.setOnFinished(evt -> {
			finished = true;
			System.out.println("finished");
		});
		transition.play();
		// TODO add timeout
		while (!finished) {
			// Wait for the animation to be finished
			// System.out.println("waiting.. " + finished);
		}
		finished = false;
	}
}
