package application.animation.classes;

import java.lang.reflect.InvocationTargetException;

import application.animation.util.ZFAnimation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public abstract class Zombie {
	protected SpriteHandler<ZFAnimation> sprite;
	protected SpriteAnimation<ZFAnimation> anim;
	private ImageView imgView;
	protected Rectangle2D tileSize;

	private void initZombie() {
		initImageView();

		initSpriteHandler();

		anim = new SpriteAnimation<ZFAnimation>(sprite);
		anim.setCylceCount(1);
		// anim.setAnimationRate(0.00151);
		anim.setMoveDuration(Duration.seconds(1));
		anim.setImageView(getImagView());

		initAnimations();
	}

	protected abstract void initImageView();

	protected abstract void initAnimations();

	protected abstract void initSpriteHandler();

	public Zombie(ImageView imgView, Rectangle2D tileSize) {
		this.setImagView(imgView);
		this.tileSize = tileSize;

		initZombie();
	}

	public Zombie(Pane parentNode, Rectangle2D tileSize) {
		this(new ImageView(), tileSize);
		parentNode.getChildren().add(this.getImagView());
	}

	public void move(double toX, double toY) {
		this.move(getImagView().getTranslateX(), getImagView().getTranslateY(), toX, toY);
	}

	public void move(double fromX, double fromY, double toX, double toY) {
		WalkAnimation walk = new WalkAnimation(anim, getImagView(), tileSize);
		walk.move(fromX, fromY, toX, toY);
	}

	public void die() {
		anim.die(ZFAnimation.DIE);
	}

	/**
	 * @return the imgView
	 */
	public ImageView getImagView() {
		return imgView;
	}

	/**
	 * @param imgView the imgView to set
	 */
	public void setImagView(ImageView imgView) {
		this.imgView = imgView;
	}

	public static Zombie createZombie(Class<? extends Zombie> newClass, Node parentNode, Rectangle2D tileSize) {
		try {
			return newClass.getConstructor(Pane.class, Rectangle2D.class).newInstance(parentNode, tileSize);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	public void destroyZombie(){
		Parent parent = this.getImagView().getParent();
		if(parent instanceof Pane){
			((Pane)parent).getChildren().remove(this.getImagView());
		}
	}
	
	public static void destroyZombie(ImageView imageView){
		Parent parent = imageView.getParent();
		if(parent instanceof Pane){
			((Pane)parent).getChildren().remove(imageView);
		}
	}
}
