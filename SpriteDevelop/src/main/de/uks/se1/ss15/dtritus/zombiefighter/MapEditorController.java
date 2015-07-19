package de.uks.se1.ss15.dtritus.zombiefighter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import de.uks.se1.ss15.dtritus.zombiefighter.Main;
import de.uks.se1.ss15.dtritus.zombiefighter.animation.classes.Zombie;
import de.uks.se1.ss15.dtritus.zombiefighter.animation.zombies.BloatedZombOneWalking;
import de.uks.se1.ss15.dtritus.zombiefighter.animation.zombies.DefaultZombie;
import de.uks.se1.ss15.dtritus.zombiefighter.global.Mediator;
import de.uks.se1.ss15.dtritus.zombiefighter.global.runnables.MapEditorStatusUpdater;
import de.uks.se1.ss15.dtritus.zombiefighter.global.runnables.Restarter;
import de.uks.se1.ss15.dtritus.zombiefighter.mapEditor.model.DefenseTile;
import de.uks.se1.ss15.dtritus.zombiefighter.mapEditor.model.Map;
import de.uks.se1.ss15.dtritus.zombiefighter.mapEditor.model.MapEditor;
import de.uks.se1.ss15.dtritus.zombiefighter.mapEditor.model.NoneTile;
import de.uks.se1.ss15.dtritus.zombiefighter.mapEditor.model.Tile;
import de.uks.se1.ss15.dtritus.zombiefighter.mapEditor.model.WalkableTile;
import de.uks.se1.ss15.dtritus.zombiefighter.mapEditor.model.util.TilePO;
import de.uks.se1.ss15.dtritus.zombiefighter.mapEditor.model.util.TileSet;
import de.uks.se1.ss15.dtritus.zombiefighter.networking.ServerMessageHandler;
import de.uks.se1.ss15.dtritus.zombiefighter.test.mapEditor.controller.MapEditorControllerTest;
import javafx.beans.property.adapter.JavaBeanStringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.layout.StackPane;

public class MapEditorController {

	@FXML
	private GridPane mapEditorGridBox;

	@FXML
	StackPane mapEditorStackPane;

	@FXML
	private HBox mapEditorHBox;

	@FXML
	private GridPane miniMap;

	@FXML
	private VBox rightEditorBox;

	@FXML
	private HBox editorButtonBox;

	@FXML
	private HBox walkableBox;

	@FXML
	private HBox noneBox;

	@FXML
	private HBox defenseBox;

	@FXML
	private Button saveButton;

	@FXML
	private Button loginButton;

	@FXML
	private Button uploadButton;

	@FXML
	private TextField mapname;

	@FXML
	private HBox statusBox;

	@FXML
	private Label statusLabel;

	@FXML
	private Circle statusColor;

	private String oldMapName = "";

	private MapEditor mapEditor;

	private String version = "1.0";

	private String owner = "SE";

	private String imgPath = "";

	private PopOver badName;

	@FXML
	Button zombieValidation;

	private static MapEditorController instance;

	private Stage currentStage;

	public void setStage(Stage value) {
		this.currentStage = value;
	}

	// Thread that hides the status
	Thread statusUpdater;

	// Fix height and width of the popup
	private static final int DOWNLOAD_POPUP_WINDOW_HEIGHT = 169;
	private static final int DOWNLOAD_POPUP_WINDOW_WIDTH = 439;
	private static final int LOGIN_POPUP_WINDOW_HEIGHT = 110;
	private static final int LOGIN_POPUP_WINDOW_WIDTH = 493;

	// System dependent variables
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static final String PATH_SEPARATOR = System.getProperty("file.separator");
	private static final String TEMP_PATH = System.getProperty("java.io.tmpdir");

	@FXML
	public void initialize() {
		// Check connection
		boolean serverIsReachable = ServerMessageHandler.checkServer("se1.cs.uni-kassel.de", 5000);
		// Disable login button, if server is not reachable
		if (!serverIsReachable) {
			this.loginButton.setDisable(true);
		}
		newEmptyMap();
		// Hide upload button
		uploadButton.setVisible(false);

		instance = this;
	}

	private void newEmptyMap() {
		// if old map needs to be overwritten
		if (this.mapEditor != null && this.mapEditor.getMap() != null && this.mapEditor.getMap().getStart() != null) {
			this.mapEditor.getMap().getStart().setType(new NoneTile());
		}
		miniMap.setStyle("-fx-background-color: #000000;");
		noneBox.setStyle("-fx-background-color: #455A64;");
		walkableBox.setStyle("-fx-background-color: #ABCDEF;");
		defenseBox.setStyle("-fx-background-color: #455A64;");
		mapEditor = Mediator.getInstance().getMapEditor();
		mapEditor.setMiniMap(miniMap);
		mapEditor.withSelected(new WalkableTile());
		Map map = new Map();
		mapEditor.withMap(map);
		mapEditorGridBox.setStyle("-fx-background-color: #FFFFFF;");
		MapClickController mcc = new MapClickController();
		mcc.setMapEditor(mapEditor);
		mapEditorGridBox.setOnMouseClicked(mcc);
		// mapEditorGridBox.setStyle("-fx-background-image:
		// url('de/uks/se1/ss15/dtritus/zombiefighter/mapEditor/view/background.png');");
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				AnchorPane pane = (AnchorPane) mapEditorGridBox.lookup("#map_" + i + "_" + j);
				TileController tc = new TileController();
				Tile tile = new Tile().withRow(j).withColumn(i);
				tile.setGui(pane);
				map.withTile(tile);
				tc.initTc(tile, pane);
				tile.setTc(true);
				TileController2 tc2 = new TileController2();
				tc2.initTc(tile, pane);
				pane.setOnMouseClicked(tc);
				pane.setOnDragDetected(tc2);
				pane.setOnMouseDragEntered(tc);
				tile.addPropertyChangeListener(new TileListener());
				tile.setType(new NoneTile());
				map.addPropertyChangeListener(new StartEndListener());
			}
		}

		mapEditorHBox.setOnKeyPressed(new MapEditorShortcutController(noneBox, defenseBox, walkableBox));

		// Binding for TextField -> MapName
		try {
			JavaBeanStringProperty beanProperty = JavaBeanStringPropertyBuilder.create().bean(map)
					.name(Map.PROPERTY_MAPNAME).build();
			mapname.textProperty().bindBidirectional(beanProperty);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		mapEditorHBox.setOnKeyPressed(new MapEditorShortcutController(noneBox, defenseBox, walkableBox));
	}

	@FXML
	void cancelButton_Clicked() {
		if (badName != null && badName.isShowing()) {
			badName.hide(Duration.ZERO);
		}
		Mediator.runLater(new Restarter());
	}

	private static String mapName;

	public static String getHeaderName() {
		return mapName;
	}

	public static void setHeaderName(String name) {
		mapName = name;
	}

	@FXML
	void uploadButton_Clicked() throws IOException {
		if (mapname.getText() != null) {
			Mediator.getInstance().getZombieFighter().getServerMessageHandler().UploadMap(mapname.getText());
		} else {
			MapEditorController.showStatus("Failed to Upload map", "red");
			return;
		}
	}

	@FXML
	void loginButton_Clicked() {
		// Show login dialog
		Mediator.runLater(new Runnable() {
			@Override
			public void run() {
				// Load the save dialog popup fxml
				FXMLLoader loader = new FXMLLoader(Main.class.getResource("mapEditor/view/LoginDialog.fxml"));
				Pane pane;
				try {
					// Create new stage for the popup window
					pane = (Pane) loader.load();
					Stage popUpStage = new Stage();
					// popUpStage.initStyle(StageStyle.UNDECORATED);
					popUpStage.setTitle("Login");
					popUpStage.setWidth(LOGIN_POPUP_WINDOW_WIDTH);
					popUpStage.setHeight(LOGIN_POPUP_WINDOW_HEIGHT);
					popUpStage.setResizable(false);
					// Center popup window relative to the map editor
					Stage primaryStage = Mediator.getInstance().getPrimaryStage();
					popUpStage.setX(primaryStage.getX() + primaryStage.getWidth() / 2 - popUpStage.getWidth() / 2);
					popUpStage.setY(primaryStage.getY() + primaryStage.getHeight() / 2 - popUpStage.getHeight() / 2);
					// Make map editor unusable, while popup is shown
					popUpStage.initModality(Modality.WINDOW_MODAL);
					popUpStage.initOwner(Mediator.getInstance().getPrimaryStage());
					Scene scene = new Scene(pane);
					popUpStage.setScene(scene);

					// Overload needed variables to the contorller
					LoginDialogController controller = loader.getController();
					controller.setStage(popUpStage);
					controller.init();

					// Show popup window
					popUpStage.showAndWait();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	void downloadButton_Clicked() {
		// Show Download Dialog
		Mediator.runLater(new Runnable() {
			@Override
			public void run() {
				// Load the save dialog popup fxml
				FXMLLoader loader = new FXMLLoader(Main.class.getResource("mapEditor/view/DownloadDialog.fxml"));
				Pane pane;
				try {
					// Create new stage for the popup window
					pane = (Pane) loader.load();
					Stage popUpStage = new Stage();
					popUpStage.initStyle(StageStyle.UNDECORATED);
					popUpStage.setWidth(DOWNLOAD_POPUP_WINDOW_WIDTH);
					popUpStage.setHeight(DOWNLOAD_POPUP_WINDOW_HEIGHT);
					popUpStage.setResizable(false);
					// Center popup window relative to the map editor
					Stage primaryStage = Mediator.getInstance().getPrimaryStage();
					popUpStage.setX(primaryStage.getX() + primaryStage.getWidth() / 2 - popUpStage.getWidth() / 2);
					popUpStage.setY(primaryStage.getY() + primaryStage.getHeight() / 2 - popUpStage.getHeight() / 2);
					// Make map editor unusable, while popup is shown
					popUpStage.initModality(Modality.WINDOW_MODAL);
					popUpStage.initOwner(Mediator.getInstance().getPrimaryStage());
					Scene scene = new Scene(pane);
					popUpStage.setScene(scene);

					// Overload needed variables to the contorller
					DownloadDialogController controller = loader.getController();
					controller.setStage(popUpStage);
					controller.init();

					// Show popup window
					popUpStage.showAndWait();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	@FXML
	void saveButton_Clicked() {
		System.out.println("Save map");
		// Create new message box for error output
		HBox popOver = new HBox();
		popOver.setPadding(new Insets(20, 20, 20, 20));
		badName = new PopOver();
		badName.setContentNode(popOver);

		// Create error messages:
		// ErrorMsg: No walkable tiles set
		Text walkableFail = new Text();
		walkableFail.setText("There is no walkable path in your map!");
		walkableFail.setFont(new Font(18));
		// ErrorMsg: No Defense Tiles set
		Text defenseFail = new Text();
		defenseFail.setText("You have not set a single defense field, yet!");
		defenseFail.setFont(new Font(18));
		// ErrorMsg: Empty Map
		Text mapEmpty = new Text();
		mapEmpty.setText("The map is still empty!");
		mapEmpty.setFont(new Font(18));
		// ErrorMsg: No Background Image set
		Text noBackgroundImg = new Text();
		noBackgroundImg.setText("You must choose a background image first!");
		noBackgroundImg.setFont(new Font(18));

		// Fetch map tiles by type and create a list with their x y
		// coordinates
		StringBuilder noneTileList = new StringBuilder();
		StringBuilder walkableTileList = new StringBuilder();
		StringBuilder defenseTileList = new StringBuilder();
		for (Tile currentTile : mapEditor.getMap().getTile()) {
			if (currentTile.getType() instanceof NoneTile) {
				noneTileList.append(currentTile.getColumn() + ";" + currentTile.getRow() + LINE_SEPARATOR);
			}
			if (currentTile.getType() instanceof DefenseTile) {
				defenseTileList.append(currentTile.getColumn() + ";" + currentTile.getRow() + LINE_SEPARATOR);
			}
		}
		// Fetch walkable tiles from start to end
		if (mapEditor.getMap().getStart() != null && mapEditor.getMap().getEnd() != null) {
			for (Tile currentTile = mapEditor.getMap().getStart(); currentTile != null
					&& !currentTile.equals(mapEditor.getMap().getEnd()); currentTile = currentTile.getNext()) {
				walkableTileList.append(currentTile.getColumn() + "," + currentTile.getRow() + LINE_SEPARATOR);
			}
			walkableTileList.append(mapEditor.getMap().getEnd().getColumn() + "," + mapEditor.getMap().getEnd().getRow()
					+ LINE_SEPARATOR);
		}

		// Validate map
		if (walkableTileList.length() + defenseTileList.length() <= 0) {
			popOver.getChildren().add(mapEmpty);
			badName.show(saveButton);
			return;
		} else if (walkableTileList.length() <= 0) {
			popOver.getChildren().add(walkableFail);
			badName.show(saveButton);
			return;
		} else if (defenseTileList.length() <= 0) {
			popOver.getChildren().add(defenseFail);
			badName.show(saveButton);
			return;
		}

		// Fetch values from text fields
		String backgroundFilePath;
		if (this.imgPath.length() > 0) {
			backgroundFilePath = this.imgPath;
		} else {
			// Show Hint: no background image
			popOver.getChildren().add(noBackgroundImg);
			badName.show(saveButton);
			return;
		}

		// Increase map version
		double tmp_mapVersion = Double.parseDouble(this.version);
		if (oldMapName.equals(this.mapname.getText())) {
			tmp_mapVersion += 0.1;
		} else {
			tmp_mapVersion = 1.0;
		}

		// Format the version number
		DecimalFormatSymbols decimalSeparator = new DecimalFormatSymbols(Locale.GERMAN);
		decimalSeparator.setDecimalSeparator('.');
		decimalSeparator.setGroupingSeparator(' ');
		DecimalFormat decimalFormat = new DecimalFormat("#.0", decimalSeparator);
		// Set map properties
		String mapVersion = decimalFormat.format(tmp_mapVersion);
		String ownerName = this.owner;
		String mapName = mapname.getText();
		this.version = mapVersion;
		this.oldMapName = mapName;

		// Format map name
		if (!mapName.startsWith("map_")) {
			mapName = "map_" + mapName;
		}
		if (!mapName.endsWith(".zip")) {
			mapName = mapName + ".zip";
		}

		// Set name and path for zip file
		String workingDirectory = Mediator.getWorkingDirectory();
		File zipFile = new File(workingDirectory + mapName);

		if (zipFile != null) {
			// Add file extension, if it is missing
			if (!zipFile.getName().endsWith(".zip")) {
				zipFile = new File(zipFile.getAbsolutePath() + ".zip");
			}
			// Add name prefix, if it is missing
			if (!zipFile.getName().startsWith("map_")) {
				zipFile = new File(zipFile.getParent() + PATH_SEPARATOR + "map_" + zipFile.getName());
			}
			mapName = mapName.replace(".zip", "");

			// Build map file content and merge it with the needed data
			StringBuilder mapFileContent = new StringBuilder();
			mapFileContent.append("###" + LINE_SEPARATOR + "### " + mapName + LINE_SEPARATOR + "###" + LINE_SEPARATOR);
			mapFileContent.append(
					"# set the Background:" + LINE_SEPARATOR + mapName + ".jpg" + LINE_SEPARATOR + LINE_SEPARATOR);
			mapFileContent
					.append("# VERSION" + LINE_SEPARATOR + "VERSION: " + mapVersion + LINE_SEPARATOR + LINE_SEPARATOR);
			mapFileContent.append("# OWNER" + LINE_SEPARATOR + "OWNER: " + ownerName + LINE_SEPARATOR + LINE_SEPARATOR);
			mapFileContent.append("# mark player context with one of the following collors:" + LINE_SEPARATOR);
			mapFileContent.append("# GRAY WHITE BLUE RED YELLOW MAGENTA CYAN GREEN:" + LINE_SEPARATOR);
			mapFileContent.append("# of if you do not want to use it for this map:" + LINE_SEPARATOR);
			mapFileContent.append("# just set it to:" + LINE_SEPARATOR + "# OFF" + LINE_SEPARATOR);
			mapFileContent.append("SET_ALPHA_BACKGROUND_COLOR:OFF" + LINE_SEPARATOR);
			mapFileContent.append("# Next lines represent cells" + LINE_SEPARATOR);
			mapFileContent.append("# where no defense can be built." + LINE_SEPARATOR);
			mapFileContent.append("# Just like a holy land." + LINE_SEPARATOR + "#" + LINE_SEPARATOR);
			mapFileContent.append("# x y coorinates are seperated by" + LINE_SEPARATOR);
			mapFileContent.append("# semicolon" + LINE_SEPARATOR);
			mapFileContent.append(noneTileList);
			mapFileContent
					.append(LINE_SEPARATOR + LINE_SEPARATOR + "# This is the way where zombies walk." + LINE_SEPARATOR);
			mapFileContent.append("#" + LINE_SEPARATOR + "# x y coordinates are seperated by" + LINE_SEPARATOR
					+ "# comma." + LINE_SEPARATOR);
			mapFileContent.append(walkableTileList);
			// Forward map file content to an inputstream
			InputStream mapFileStream = new ByteArrayInputStream(mapFileContent.toString().getBytes());

			// Create ZipFile
			try {
				int length;
				byte[] buffer = new byte[2014];
				InputStream imageStream = new FileInputStream(backgroundFilePath);
				FileOutputStream fos = new FileOutputStream(zipFile);
				ZipOutputStream zos = new ZipOutputStream(fos);
				ZipEntry mapEntry = new ZipEntry(mapName + ".map");
				ZipEntry imageEntry = new ZipEntry(mapName + ".jpg");

				// Write map file to zip
				zos.putNextEntry(mapEntry);
				while ((length = mapFileStream.read(buffer)) > 0) {
					zos.write(buffer, 0, length);
				}
				mapFileStream.close();
				zos.closeEntry();
				// Write image file to zip
				zos.putNextEntry(imageEntry);
				while ((length = imageStream.read(buffer)) > 0) {
					zos.write(buffer, 0, length);
				}
				imageStream.close();
				zos.closeEntry();
				zos.close();

				// Show status indicator
				showStatus("Map successfully saved", "light green");

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// Show status indicator
				MapEditorController.showStatus("Failed to save map", "red");
			} catch (IOException ioe) {
				ioe.printStackTrace();
				// Show status indicator
				MapEditorController.showStatus("Failed to save map", "red");
			}

		}
	}

	@FXML
	void loadButton_Clicked() throws ZipException, IOException, URISyntaxException {
		File chosenFile;
		if (Mediator.testSignal != null) { // If unit test, bypass file dialog
			chosenFile = new File(MapEditorControllerTest.class.getResource("map_testfx.zip").toURI());
		} else {
			// Show Open-File Dialog
			Stage stage = Mediator.getInstance().getPrimaryStage();
			FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialDirectory(new File(Mediator.getWorkingDirectory()));
			fileChooser.setTitle("Open Map");
			fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("ZIP", "*.zip"));
			chosenFile = fileChooser.showOpenDialog(stage);
		}
		if (chosenFile != null) {
			loadMap(chosenFile);
		}
	}

	public static void loadMap(File file) {
		if (file != null) { // If a file was chosen
			// Set mapName
			Mediator.runLater(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					instance.mapname.setText(file.getName().replace(".zip", ""));
					instance.oldMapName = instance.mapname.getText();
				}

			});
			// Extract files from zip
			ZipFile zipFile;
			try {
				zipFile = new ZipFile(file);
				Enumeration<? extends ZipEntry> entries = zipFile.entries();
				ZipEntry entry;
				InputStream is;
				while (entries.hasMoreElements()) {
					entry = (ZipEntry) entries.nextElement();
					is = zipFile.getInputStream(entry);
					if (entry.getName().endsWith(".map")) { // Handle map file
						BufferedReader reader = new BufferedReader(new InputStreamReader(is));
						StringBuilder mapContent = new StringBuilder();
						// Read file to string
						String line;
						while ((line = reader.readLine()) != null) {
							mapContent.append(line);
							mapContent.append(LINE_SEPARATOR);
						}
						/*
						 * Extract needed infos out of map content string
						 */
						boolean expectNoneTileData = false;
						boolean expectWalkableTileData = false;
						StringBuilder walkableTiles = new StringBuilder();
						StringBuilder noneTiles = new StringBuilder();
						String[] lines = mapContent.toString().split(LINE_SEPARATOR);
						for (String currentLine : lines) {
							if (currentLine.startsWith("VERSION:")) {
								instance.version = currentLine.replace("VERSION: ", "");
							} else if (currentLine.startsWith("OWNER:")) {
								instance.owner = currentLine.replace("OWNER: ", "");
							} else if (currentLine.startsWith("# Next lines represent")) {
								expectNoneTileData = true;
								expectWalkableTileData = false;
							} else if (currentLine.startsWith("# This is the way where zombies walk")) {
								expectNoneTileData = false;
								expectWalkableTileData = true;
							}
							if (expectNoneTileData) {
								// The pattern (int;int) that the current line
								// needs
								// to be in.
								Pattern dataPattern = Pattern.compile("(\\d*)([;,])(\\d*)");
								Matcher patternMatcher = dataPattern.matcher(currentLine);
								// True if the current line matches the pattern.
								Boolean lineMatches = patternMatcher.find();
								if (lineMatches) {
									noneTiles.append(currentLine + LINE_SEPARATOR);
								}
							}
							if (expectWalkableTileData) {
								// The pattern (int;int) that the current line
								// needs
								// to be in.
								Pattern dataPattern = Pattern.compile("(\\d*)([;,])(\\d*)");
								Matcher patternMatcher = dataPattern.matcher(currentLine);
								// True if the current line matches the pattern.
								Boolean lineMatches = patternMatcher.find();
								if (lineMatches) {
									walkableTiles.append(currentLine + LINE_SEPARATOR);
								}
							}
						}
						/*
						 * Build Map
						 */
						// Clear map
						instance.newEmptyMap();
						// Make all tiles to defense tiles
						for (Tile currentTile : instance.mapEditor.getMap().getTile()) {
							currentTile.setType(new DefenseTile());
						}
						// Set none tiles
						if (noneTiles.length() > 0) {
							String[] noneTilesLines = noneTiles.toString().split(LINE_SEPARATOR);
							for (String currentLine : noneTilesLines) {
								int x = Integer.parseInt(currentLine.split("[;,]")[0]);
								int y = Integer.parseInt(currentLine.split("[;,]")[1]);
								TilePO nonePO = instance.mapEditor.getMap().getTile().hasTilePO().hasColumn(x)
										.hasRow(y);
								for (Tile matchedTile : nonePO.allMatches()) {
									matchedTile.setType(new NoneTile());
								}
							}
						}
						// Set walkable tiles
						if (walkableTiles.length() > 0) {
							String[] walkableTilesLines = walkableTiles.toString().split(LINE_SEPARATOR);
							boolean firstTile = true;
							for (String currentLine : walkableTilesLines) {
								int x = Integer.parseInt(currentLine.split("[;,]")[0]);
								int y = Integer.parseInt(currentLine.split("[;,]")[1]);
								TilePO walkablePO = instance.mapEditor.getMap().getTile().hasTilePO().hasColumn(x)
										.hasRow(y);
								for (Tile matchedTile : walkablePO.allMatches()) {
									if (firstTile) { // StartTile
										matchedTile.setType(new WalkableTile());
										matchedTile.getMap().setStart(matchedTile);
										Tile end = matchedTile.getMap().getTile().hasColumn(15)
												.hasRow(matchedTile.getColumn()).first();
										matchedTile.getMap().setEnd(end);
										end.setType(new WalkableTile());
										matchedTile.setWaypoint(0);
										matchedTile.getMap().getMapEditor().setSelectedTile(matchedTile);
										firstTile = false;
									} else if (matchedTile.getMap().getMapEditor().getSelectedTile() != null) {
										Tile source = matchedTile.getMap().getMapEditor().getSelectedTile();
										// Check if a new tile for cross
										// functionality is needed, except if
										// its the end tile
										if (matchedTile.getType() instanceof WalkableTile
												&& !matchedTile.equals(matchedTile.getMap().getEnd())) {
											AnchorPane pane = matchedTile.getGui();
											matchedTile = new Tile().withColumn(matchedTile.getColumn())
													.withRow(matchedTile.getRow()).withMap(matchedTile.getMap());
											matchedTile.setGui(pane);
											matchedTile.addPropertyChangeListener(new TileListener());
										}
										if (source.connectable(matchedTile)) {
											matchedTile.setType(new WalkableTile());
											matchedTile.setPrevious(source);
											matchedTile.setWaypoint(source.getWaypoint() + 1);
											// For now, end can't be selected
											if (matchedTile.equals(matchedTile.getMap().getEnd())) {
												matchedTile.getMap().getMapEditor().setSelectedTile(null);
											} else {
												matchedTile.getMap().getMapEditor().setSelectedTile(matchedTile);
											}
										}
									} else if (!matchedTile.equals(matchedTile.getMap().getEnd())) { // Select
																										// tile,
																										// but
																										// can't
																										// select
																										// end
										// Find highest tile
										int highest = -1;
										for (Tile forTile : matchedTile.getMap().getTile().hasRow(x).hasColumn(y)) {
											if (forTile.getWaypoint() > highest) {
												highest = forTile.getWaypoint();
											}
										}
										// Can only select walkables
										if (highest != -1) {
											matchedTile.getMap().getMapEditor().setSelectedTile(
													matchedTile.getMap().getTile().hasWaypoint(highest).first());
										}
									}
								}
							}
						}
					} else if (entry.getName().endsWith(".jpg")) { // Handle
																	// image
																	// file
						// Extract JPG to TMP Path
						StringBuilder absolutePath = new StringBuilder();
						absolutePath.append(TEMP_PATH);
						if (!TEMP_PATH.endsWith(PATH_SEPARATOR)) {
							absolutePath.append(PATH_SEPARATOR);
						}
						absolutePath.append(entry.getName());
						FileOutputStream fos = new FileOutputStream(absolutePath.toString());
						int len;
						byte[] buffer = new byte[2048];
						while ((len = is.read(buffer)) > 0) {
							fos.write(buffer, 0, len);
						}
						fos.close();
						// Set image path
						instance.imgPath = absolutePath.toString();

						// Open file
						File imgFile = new File(instance.imgPath);

						// Set background img
						instance.mapEditorGridBox.setStyle("-fx-background-image: url('" + imgFile.toURI() + "');"
								+ "-fx-background-size: 100% 100%;");
					} else {
						// Unknown file found in zip
						Mediator.getInstance().showDialog("There are unexpected files in the zip file.");
						// Show status indicator
						MapEditorController.showStatus("Failed to load map", "red");
						return;
					}
					is.close();
				}
				zipFile.close();
			} catch (ZipException e) {
				// Zip is corrupt
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Zip corrupt!");
				alert.setHeaderText(null);
				alert.setContentText("Error while loading the zip file!");
				alert.showAndWait();
				// Show status indicator
				MapEditorController.showStatus("Failed to load map", "red");
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// Show status indicator
				MapEditorController.showStatus("Failed to load map", "red");
			}
		}
		// Show status indicator
		MapEditorController.showStatus("Successfully load map", "light green");
	}

	@FXML
	void walkableTileClicked() {
		walkableBox.setStyle("-fx-background-color: #ABCDEF;");
		noneBox.setStyle("-fx-background-color: #455A64;");
		defenseBox.setStyle("-fx-background-color: #455A64;");
		mapEditor.setSelected(new WalkableTile());
		mapEditor.setSelectedTile(null);
	}

	@FXML
	void noneTileClicked() {
		noneBox.setStyle("-fx-background-color: #ABCDEF;");
		walkableBox.setStyle("-fx-background-color: #455A64;");
		defenseBox.setStyle("-fx-background-color: #455A64;");
		mapEditor.setSelected(new NoneTile());
		mapEditor.setSelectedTile(null);
	}

	@FXML
	void defenseTileClicked() {
		defenseBox.setStyle("-fx-background-color: #ABCDEF;");
		noneBox.setStyle("-fx-background-color: #455A64;");
		walkableBox.setStyle("-fx-background-color: #455A64;");
		mapEditor.setSelected(new DefenseTile());
		mapEditor.setSelectedTile(null);

	}

	@FXML
	void mapTileClicked() {
		// Not Used
	}

	@FXML
	void bgiButton_clicked() {
		File choosenFile;
		// Show Open-File Dialog
		Stage stage = Mediator.getInstance().getPrimaryStage();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Background Image");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg"));
		choosenFile = fileChooser.showOpenDialog(stage);
		if (choosenFile != null) { // If a file was choosen
			mapEditorGridBox.setStyle(
					"-fx-background-image: url('" + choosenFile.toURI() + "');" + "-fx-background-size: 100% 100%;");
			this.imgPath = choosenFile.getAbsolutePath();
		}
	}

	void validationPopOver(Node target, String msg) {
		HBox content = new HBox();
		content.setPadding(new Insets(20, 20, 20, 20));

		Text text = new Text();
		text.setText(msg);
		text.setFont(new Font(18));

		content.getChildren().add(text);

		PopOver popOver = new PopOver();
		popOver.setArrowLocation(ArrowLocation.TOP_CENTER);
		popOver.setContentNode(content);
		popOver.show(target);
	}

	@FXML
	void zombieValidation_Clicked() {
		removeZombie();
		startZombieMove();
	}

	private void showValidationError() {

		Map map = Mediator.getInstance().getMapEditor().getMap();

		Tile startField = map.getStart();
		Tile endField = map.getEnd();
		// Check StartPoint with the EndPoint
		if (startField == null || endField == null) {
			validationPopOver(startField.getGui(), "There is no start and end point, yet!");
			return;
		} else if (startField.getRow() + startField.getColumn() != endField.getColumn() + endField.getRow()) {
			validationPopOver(endField.getGui(), "The end point is not at the correct position to his start point!");
			return;
		} else {
			TileSet x = new TileSet();
			Tile buffer1 = startField;
			Tile buffer2 = startField.getNext();

			if (startField.getWaypoint() != 0) {
				validationPopOver(buffer1.getGui(), "There is no waypoint set!");
				return;
			} else if (buffer2 == null) {
				validationPopOver(buffer1.getGui(), "There is no way to walk, yet!");
				return;
			}

			while (buffer2 != null) {
				if (buffer2.getWaypoint() != buffer1.getWaypoint() + 1) {
					validationPopOver(buffer2.getGui(), "The waypoint is not right!");
					return;
				}

				if (!buffer1.connectable(buffer2)) {
					Tile targetTile = buffer1;

					int tx = buffer2.getRow();
					int ty = buffer2.getColumn();
					int sx = buffer1.getRow();
					int sy = buffer1.getColumn();

					for (int i = sy; i < ty; i++) {
						for (int j = sx; j < tx; j++) {
							Tile t = map.getTile().hasRow(i).hasColumn(j).first();
							if (t.getType() instanceof DefenseTile) {
								targetTile = t;
								break;
							}
						}
					}

					validationPopOver(targetTile.getGui(), "The tiles aren´t connected!");
					return;
				}

				x.with(buffer1);
				buffer1 = buffer1.getNext();
				buffer2 = buffer2.getNext();
			}

			x.with(buffer1);
			if (!buffer1.equals(map.getEnd())) {
				validationPopOver(buffer1.getGui(), "The tiles aren´t connected to the end!");
				return;
			}

			if (map.getTile().size() - map.getTile().hasWaypoint(-1).size() != x.size()) {
				validationPopOver(x.first().getGui(), "There are some missing tiles!");
				return;
			}
		}

	}

	private Zombie zombie;

	private Thread zombieWalkerThread;

	private void removeZombie() {
		if (zombie != null) {
			if(zombieWalkerThread != null){
				zombieWalkerThread.interrupt();
			}
			zombie.destroyZombie();
			zombie = null;
		}
	}

	private void startZombieMove() {
		zombieWalkerThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Map map = Mediator.getInstance().getMapEditor().getMap();

				Tile startField = map.getStart();
				Tile endField = map.getEnd();

				// Remove old zombie
				removeZombie();

				// Start the walk of the zombie
				if (startField != null) {
					Rectangle2D tileSize;
					if (mapEditorGridBox.getChildrenUnmodifiable().get(0) instanceof AnchorPane) {
						AnchorPane pane = startField.getGui();
						tileSize = new Rectangle2D(pane.getBoundsInParent().getMinX(),
								pane.getBoundsInParent().getMinY(), pane.getWidth(), pane.getHeight());
					} else {
						System.err.println("Children isn't a AnchorPane!!!");
						// Fallback
						tileSize = new Rectangle2D(100, 100, 100, 100);
					}
					Zombie zombie = Zombie.createZombie(BloatedZombOneWalking.class, mapEditorStackPane,
							tileSize);
					MapEditorController.this.zombie = zombie;

					zombie.setDuration(Duration.millis(500));
					zombie.getImageView().setEffect(new DropShadow());

					Tile currentField = startField;
					while (currentField.getNext() != null && !Thread.interrupted()) {
						currentField = currentField.getNext();
						Bounds coords = currentField.getGui().getBoundsInParent();
						try {
							zombie.moveAndWait(coords.getMinX(), coords.getMinY());
						} catch (InterruptedException e) {
							//e.printStackTrace();
							Mediator.printDebugln("ZombieWalk aborted");
							zombieWalkerThread.interrupt();
						}
					}
					if(Thread.interrupted()){
						System.err.println("interrupted");
						return;
					}
					if (currentField == endField) {
						// if run was succesfully, remove zombie
						removeZombie();
					} else {
						zombie.dieAndWait();
					}

				}

				// After the walk, show the errormessage
				Mediator.runLater(() -> {
					showValidationError();
				});
			}
		}, "ZombieWalk");
		zombieWalkerThread.start();
	}

	public static boolean mapValidation() {
		Tile startfield = Mediator.getInstance().getMapEditor().getMap().getStart();
		Tile endField = Mediator.getInstance().getMapEditor().getMap().getEnd();
		// Check Startpoint with the EndPoint
		if (startfield == null || endField == null) {
			return false;
		} else if (startfield.getRow() + startfield.getColumn() != endField.getColumn() + endField.getRow()) {
			return false;
		} else {
			Tile buffer1 = startfield;
			Tile buffer2 = startfield.getNext();
			if (startfield.getWaypoint() != 0) {
				return false;
			} else if (buffer2 == null) {
				return false;
			}
			while (buffer2 != null) {
				if (buffer2.getWaypoint() != buffer1.getWaypoint() + 1) {
					return false;
				}
				if (!buffer1.connectable(buffer2)) {
					return false;
				}
				buffer1 = buffer1.getNext();
				buffer2 = buffer2.getNext();
			}

			if (!buffer1.equals(Mediator.getInstance().getMapEditor().getMap().getEnd())) {
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void hideLoginButton() {
		instance.loginButton.setText("Download");
		instance.uploadButton.setVisible(true);
		instance.loginButton.setOnMouseClicked(new EventHandler() {

			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				instance.downloadButton_Clicked();
			}

		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void showLoginButton() {
		instance.loginButton.setText("Login");
		instance.uploadButton.setVisible(false);
		instance.loginButton.setOnMouseClicked(new EventHandler() {

			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				instance.loginButton_Clicked();
			}

		});
	}

	public static void showStatus(String statusText, String statusColor) {
		if (!instance.statusBox.isVisible()) {
			instance.statusBox.setVisible(true);
			instance.statusLabel.setText(statusText);
			instance.statusColor.setStyle("-fx-fill: " + statusColor);

			// Start thread that waits 5 seconds and then hides the status
			Runnable MapEditorStatusUpdater = new MapEditorStatusUpdater(new Date());
			instance.statusUpdater = new Thread(MapEditorStatusUpdater);
			instance.statusUpdater.start();
		} else {
			// Interrupts current showing status
			instance.statusUpdater.interrupt();
			instance.statusBox.setVisible(false);
			// Shows new status
			showStatus(statusText, statusColor);
		}
	}

	public static void hideStatus() {
		instance.statusBox.setVisible(false);
	}
}