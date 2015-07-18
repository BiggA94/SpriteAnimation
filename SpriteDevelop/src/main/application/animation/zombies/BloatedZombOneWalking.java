package application.animation.zombies;

import application.Main;
import application.animation.classes.SpriteHandler;
import application.animation.classes.Zombie;
import application.animation.util.ZFAnimation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class BloatedZombOneWalking extends Zombie {
	
	public BloatedZombOneWalking(Pane parentNode, Rectangle2D tileSize) {
		super(parentNode, tileSize);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initImageView() {		
		getImagView().setImage(new Image(Main.class.getResourceAsStream("bloatedzombonewalking.png")));
	}

	@Override
	protected void initAnimations() {
		anim.addEntry(ZFAnimation.WALK_DOWN, 0, 1, 2, 3, 4, 5, 6, 7);
		anim.addEntry(ZFAnimation.DIE, 0,1,2,3,4,5,6,7);
	}

	@Override
	protected void initSpriteHandler() {
		sprite = new SpriteHandler<ZFAnimation>(1, 1, 1, 8, 1, 393, 50);
	}
}
