package de.uks.se1.ss15.dtritus.zombiefighter.animation.classes;

import de.uks.se1.ss15.dtritus.zombiefighter.animation.util.ZFAnimation;
import javafx.animation.Animation.Status;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.util.Duration;

public class WalkAnimation {

	private static final double STANDARD_WALK_SPEED = 1;// 0.001;

	private Node nodeToMove;

	SpriteAnimation<ZFAnimation> spriteAnimation;

	private Rectangle2D nodeToMoveOn = new Rectangle2D(0, 0, 1, 1);

	/**
	 * @return the nodeToMoveOn
	 */
	public Rectangle2D getNodeToMoveOn() {
		return nodeToMoveOn;
	}

	/**
	 * @param nodeToMoveOn
	 *            the nodeToMoveOn to set
	 */
	public void setNodeToMoveOn(Rectangle2D nodeToMoveOn) {
		this.nodeToMoveOn = nodeToMoveOn;
	}

	/**
	 * 
	 * @param spriteAnimation
	 * @param nodeToMove
	 * @param nodeToMoveOn
	 *            A Cell, so that the animation knows, how big the cells are
	 * @param duration 
	 */
	public WalkAnimation(SpriteAnimation<ZFAnimation> spriteAnimation, Node nodeToMove, Rectangle2D nodeToMoveOn, Duration duration) {
		this.spriteAnimation = spriteAnimation;
		this.nodeToMove = nodeToMove;
		this.duration = duration;
		this.setNodeToMoveOn(nodeToMoveOn);
	}
	
	/**
	 * 
	 * @param spriteAnimation
	 * @param nodeToMove
	 * @param nodeToMoveOn
	 *            A Cell, so that the animation knows, how big the cells are
	 * @param duration 
	 */
	public WalkAnimation(SpriteAnimation<ZFAnimation> spriteAnimation, Node nodeToMove, Rectangle2D nodeToMoveOn) {
		this(spriteAnimation, nodeToMove,  nodeToMoveOn, Duration.seconds(1));
	}

	private Duration duration = Duration.seconds(1);

	private int standardAnimationAngle = 0;

	public int getStandardAnimation() {
		return standardAnimationAngle;
	}

	/**
	 * Set the Angle of the Standard Animation (Default 0 for Down)
	 * 
	 * @param standardAnimation
	 */
	public void setStandardAnimation(int standardAnimation) {
		this.standardAnimationAngle = standardAnimation;
	}

	/**
	 * @return the duration
	 */
	public Duration getDuration() {
		return duration;
	}

	/**
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public void move(double toX, double toY) {
		this.move(this.nodeToMove.getTranslateX(), this.nodeToMove.getTranslateY(), toX, toY);
	}

	
	
	public void move(double fromX, double fromY, double toX, double toY) {
		finished = false;
		ZFAnimation direction = calculateDirection(fromX, fromY, toX, toY);

		if (!spriteAnimation.animationSets.containsKey(direction)) {
			nodeToMove.setRotate(getAngleFromDirection(direction));
		} else {
			nodeToMove.setRotate(0);
		}
		spriteAnimation.setMoveDuration(this.getDuration());
		spriteAnimation.setAnimationRate(this.calculateSpeed(fromX, fromY, toX, toY));

		translateTransition = new TranslateTransition();
		translateTransition.setFromX(fromX);
		translateTransition.setFromY(fromY);

		translateTransition.setToX(toX);
		translateTransition.setToY(toY);

		translateTransition.setNode(this.nodeToMove);
		translateTransition.setAutoReverse(true);
		translateTransition.setDuration(getDuration());
		translateTransition.setOnFinished(evt -> {
			finished = true;
		});

		// If the Zombie should Port to the Destination, just show the start and
		// the end of the Transition
		if (!this.isNormalMove(direction)) {
			translateTransition.setInterpolator(new Interpolator() {
				@Override
				protected double curve(double t) {
					return Math.round(t);
				}
			});
		}else{
			translateTransition.setInterpolator(Interpolator.LINEAR);
		}
		
		spriteAnimation.animate(direction);
		translateTransition.play();

	}

	private boolean isNormalMove(ZFAnimation anim) {
		if (anim == ZFAnimation.WALK_DOWN || anim == ZFAnimation.WALK_LEFT || anim == ZFAnimation.WALK_LOWERLEFT
				|| anim == ZFAnimation.WALK_LOWERRIGHT || anim == ZFAnimation.WALK_RIGHT || anim == ZFAnimation.WALK_UP
				|| anim == ZFAnimation.WALK_UPPERLEFT || anim == ZFAnimation.WALK_UPPERRIGHT) {
			return true;
		}
		return false;
	}

	private volatile boolean finished = false;

	public void moveAndWait(double toX, double toY) throws InterruptedException {
		this.moveAndWait(this.nodeToMove.getTranslateX(), this.nodeToMove.getTranslateY(), toX, toY);
	}

	public void moveAndWait(double fromX, double fromY, double toX, double toY) throws InterruptedException {
		move(fromX, fromY, toX, toY);
		int x = 0;
		while (!finished) {
			Thread.sleep(1);
		}
	}

	private int getAngleFromDirection(ZFAnimation key) {
		int angle = 0;
		switch (key) {
		case WALK_LOWERLEFT:
		case PORT_LOWERLEFT:
			angle = 45;
			break;
		case WALK_LEFT:
		case PORT_LEFT:
			angle = 90;
			break;
		case WALK_UPPERLEFT:
		case PORT_UPPERLEFT:
			angle = 135;
			break;
		case WALK_UP:
		case PORT_UP:
			angle = 180;
			break;
		case WALK_UPPERRIGHT:
		case PORT_UPPERRIGHT:
			angle = 225;
			break;
		case WALK_RIGHT:
		case PORT_RIGHT:
			angle = 270;
			break;
		case WALK_LOWERRIGHT:
		case PORT_LOWERRIGHT:
			angle = 315;
			break;

		}

		return angle + getStandardAnimation();
	}

	private double walkSpeed = 1;

	private TranslateTransition translateTransition;

	public double calculateSpeed(double fromX, double fromY, double toX, double toY) {
		double directionHorizontally = 0;
		directionHorizontally = toX - fromX;
		directionHorizontally /= this.getNodeToMoveOn().getWidth();

		double directionVertically = 0;
		directionVertically = toY - fromY;
		directionVertically /= this.getNodeToMoveOn().getHeight();

		double distance = Math.sqrt(Math.pow(directionVertically, 2) + Math.pow(directionHorizontally, 2));
		this.walkSpeed = (STANDARD_WALK_SPEED + STANDARD_WALK_SPEED * Math.sqrt(distance));
		return walkSpeed;
	}

	public ZFAnimation calculateDirection(double fromX, double fromY, double toX, double toY) {
		ZFAnimation direction = ZFAnimation.UNDEFINED;
		double directionHorizontally = 0;
		directionHorizontally = toX - fromX;
		directionHorizontally /= this.getNodeToMoveOn().getWidth();

		double directionVertically = 0;
		directionVertically = toY - fromY;
		directionVertically /= this.getNodeToMoveOn().getHeight();

		if (directionHorizontally > 0) {
			// Right
			if (directionVertically < 0) {
				// Up + Right
				if (Math.abs(directionHorizontally) + Math.abs(directionVertically) > 2) {
					// Port
					direction = ZFAnimation.PORT_UPPERRIGHT;
				} else {
					// Walk
					direction = ZFAnimation.WALK_UPPERRIGHT;
				}
			} else if (directionVertically > 0) {
				// Down + Right
				if (Math.abs(directionHorizontally) + Math.abs(directionVertically) > 2) {
					// Port
					direction = ZFAnimation.PORT_LOWERRIGHT;
				} else {
					// Walk
					direction = ZFAnimation.WALK_LOWERRIGHT;
				}
			} else if (directionVertically == 0) {
				// Right
				if (Math.abs(directionHorizontally) > 1) {
					direction = ZFAnimation.PORT_RIGHT;
				} else {
					direction = ZFAnimation.WALK_RIGHT;
				}
			}
		} else if (directionHorizontally < 0) {
			// Left
			if (directionVertically < 0) {
				// Up + Left
				if (Math.abs(directionHorizontally) + Math.abs(directionVertically) > 2) {
					// Port
					direction = ZFAnimation.PORT_UPPERLEFT;
				} else {
					// Walk
					direction = ZFAnimation.WALK_UPPERLEFT;
				}
			} else if (directionVertically > 0) {
				// Down + Left
				if (Math.abs(directionHorizontally) + Math.abs(directionVertically) > 2) {
					// Port
					direction = ZFAnimation.PORT_LOWERLEFT;
				} else {
					// Walk
					direction = ZFAnimation.WALK_LOWERLEFT;
				}
			} else if (directionVertically == 0) {
				// Left
				if (Math.abs(directionHorizontally) > 1) {
					direction = ZFAnimation.PORT_LEFT;
				} else {
					direction = ZFAnimation.WALK_LEFT;
				}
			}

		} else if (directionHorizontally == 0) {
			// Up or Down
			if (directionVertically < 0) {
				// Up
				if (Math.abs(directionVertically) > 1) {
					// Port
					direction = ZFAnimation.PORT_UP;
				} else {
					// Walk
					direction = ZFAnimation.WALK_UP;
				}
			} else if (directionVertically > 0) {
				// Down
				if (Math.abs(directionVertically) > 1) {
					// Port
					direction = ZFAnimation.PORT_DOWN;
				} else {
					// Walk
					direction = ZFAnimation.WALK_DOWN;
				}
			}
		}
		return direction;
	}
}
