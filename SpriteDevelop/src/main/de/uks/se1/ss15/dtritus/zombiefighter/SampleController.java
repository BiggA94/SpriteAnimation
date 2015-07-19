package de.uks.se1.ss15.dtritus.zombiefighter;

import java.net.URL;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

import de.uks.se1.ss15.dtritus.zombiefighter.animation.classes.Zombie;
import de.uks.se1.ss15.dtritus.zombiefighter.animation.util.ZFAnimation;
import de.uks.se1.ss15.dtritus.zombiefighter.animation.zombies.BloatedZombOneWalking;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class SampleController implements Initializable {
	@FXML
	GridPane grid;
	@FXML
	ImageView imgView_Sprite;
	@FXML
	StackPane root;

	LinkedList<Zombie> zombies = new LinkedList<>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Add Panes to Grid
		int rowCount = grid.getRowConstraints().size();
		int columnCount = grid.getColumnConstraints().size();

		int rowIndex = 0, columnIndex = 0;

		while (rowIndex < rowCount) {
			AnchorPane pane = new AnchorPane();
			pane.setId("map_" + columnIndex + "_" + rowIndex);
			// pane.setStyle("-fx-background-color: #CCFF99;");
			pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					// SampleController.this.move();
					AnchorPane pane = (AnchorPane) event.getSource();
					zombies.getFirst().move(pane.getBoundsInParent().getMinX(), pane.getBoundsInParent().getMinY());
				}
			});
			System.out.println(pane);
			// GridPane.setColumnIndex(pane, columnIndex);
			// GridPane.setRowIndex(pane, rowIndex);

			grid.add(pane, columnIndex, rowIndex);

			columnIndex++;
			if (columnIndex >= columnCount) {
				columnIndex = 0;
				rowIndex++;
			}
		}

		this.createNewZombie();
		this.zombies.getFirst().move(0, 0);
	}

	@FXML
	public void root_clicked(MouseEvent event) throws InterruptedException {

	}

	@FXML
	public void move() {
		for (Zombie zombie : zombies) {
			zombie.move(Math.random() * root.getWidth(), Math.random() * root.getHeight());
		}
	}

	private void getPosition() {

	}

	@FXML
	public void kill(ActionEvent event) {
		try {
			zombies.getFirst().die();
			zombies.removeFirst();
		} catch (NoSuchElementException e) {

		}
	}

	@FXML
	public void createNewZombie() {
		Bounds coords = grid.getChildren().get(1).getBoundsInLocal();
		Rectangle2D tileSize = new Rectangle2D(0, 0, coords.getWidth(), coords.getHeight());
		Zombie zombie = Zombie.createZombie(BloatedZombOneWalking.class, root, tileSize);
		zombies.add(zombie);
	}
}
