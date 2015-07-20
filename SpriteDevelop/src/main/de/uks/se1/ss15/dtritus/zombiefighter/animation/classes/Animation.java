package de.uks.se1.ss15.dtritus.zombiefighter.animation.classes;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;

import de.uks.se1.ss15.dtritus.zombiefighter.animation.util.ZFAnimation;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public abstract class Animation {
	LinkedHashMap<String, SingleAnimation> singleAnimations = new LinkedHashMap<>();

	private ImageView imageView;

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	protected abstract void initAnimations(LinkedHashMap<String, SingleAnimation> animations, ImageView parentNode,
			Bounds bounds) throws InstantiationException, IllegalAccessException, IllegalArgumentException,
					InvocationTargetException, NoSuchMethodException, SecurityException;

	public Animation(ImageView imageView, Bounds bounds) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		this.setImageView(imageView);
		initAnimations(this.singleAnimations, imageView, bounds);
	}

	public Animation(Pane parentNode, Bounds bounds) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		this(new ImageView(), bounds);

		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> {
				parentNode.getChildren().add(this.getImageView());
			});
		} else {
			parentNode.getChildren().add(this.getImageView());
			System.out.println(parentNode.getChildren());
		}
		

		this.getImageView().setFitWidth(bounds.getWidth());
		this.getImageView().setFitHeight(bounds.getHeight());

		this.getImageView().setTranslateX(bounds.getMinX());
		this.getImageView().setTranslateY(bounds.getMinY());

		// this.getImageView().setViewport(this.sprite.getViewPort(0));

		while (!((Pane) parentNode).getChildren().contains(this.getImageView())) {
			// wait
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
//		initAnimations(this.singleAnimations, this.getImageView(), bounds);
	}

	public static Animation createAnimation(Class<? extends Animation> classToCreate, Pane parentNode, Bounds bounds)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		return classToCreate.getConstructor(Pane.class, Bounds.class).newInstance(parentNode, bounds);
	}

	public static Animation createAnimation(Class<? extends Animation> classToCreate, ImageView imageView,
			Bounds bounds) throws InstantiationException, IllegalAccessException, IllegalArgumentException,
					InvocationTargetException, NoSuchMethodException, SecurityException {
		return classToCreate.getConstructor(ImageView.class, Bounds.class).newInstance(imageView, bounds);
	}

	public void play() throws InterruptedException {
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for (String key : singleAnimations.keySet()) {
					try {
						singleAnimations.get(key).playAndWait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}, "Animation");
		thread.start();
	}
}
