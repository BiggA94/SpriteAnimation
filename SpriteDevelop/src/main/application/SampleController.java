package application;

import java.net.URL;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

import application.animation.Zombie;
import application.animation.util.ZFAnimation;
import application.animation.zombies.BloatedZombOneWalking;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;

public class SampleController implements Initializable {
	@FXML
	GridPane grid;
	@FXML
	ImageView imgView_Sprite;
	@FXML
	StackPane root;
	private Zombie zombie;
	
	LinkedList<Zombie> zombies = new LinkedList<>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	@FXML
	public void root_clicked(MouseEvent event) throws InterruptedException {

	}

	@FXML
	public void move(ActionEvent event) {
		for (Zombie zombie : zombies) {			
			zombie.move(Math.random() * root.getWidth(), Math.random() * root.getHeight());
		}
	}

	@FXML
	public void kill(ActionEvent event) {
		try{			
			zombies.getFirst().die();
			zombies.removeFirst();
		}catch(NoSuchElementException e){
			
		}
	}

	@FXML public void createNewZombie(ActionEvent event) {
		Rectangle2D tileSize = new Rectangle2D(0, 0, 10, 10);
		zombie = Zombie.createZombie(BloatedZombOneWalking.class, root, tileSize);//new BloatedZombOneWalking(imgView_Sprite, tileSize);
		zombies.add(zombie);
	}
}
