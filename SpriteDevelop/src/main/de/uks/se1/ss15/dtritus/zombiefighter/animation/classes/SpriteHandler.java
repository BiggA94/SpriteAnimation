package de.uks.se1.ss15.dtritus.zombiefighter.animation.classes;

import java.util.LinkedList;

import de.uks.se1.ss15.dtritus.zombiefighter.animation.util.SpriteMap;
import javafx.geometry.Rectangle2D;

public class SpriteHandler<Tkey> {
	private int offsetX;
	private int offsetY;
	/**
	 * Pictures per Row
	 */
	private int rowCount;
	/**
	 * Pictures per Column
	 */
	private int columnCount;
	/**
	 * Width/Height of Line that Separates the images
	 */
	private int separationWidth;
	/**
	 * Height of the Part of the Image, that should be used
	 */
	private int heigth;
	/**
	 * Width of the Part of the Image, that should be used
	 */
	private int width;

	private SpriteMap<Tkey, Integer> spriteMap = new SpriteMap<>();
	private int separationHeight;

	public void setSpriteMap(SpriteMap<Tkey, Integer> spriteMap) {
		this.spriteMap = spriteMap;
	}

	public SpriteMap<Tkey, Integer> getSpriteMap() {
		return spriteMap;
	}

	public SpriteHandler(int offsetX, int offsetY, int rowCount, int columnCount, int separationWidth,
			int separationHeight, int width, int heigth) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.separationWidth = separationWidth;
		this.separationHeight = separationHeight;
		this.width = width;
		this.heigth = heigth;
	}

	// Save the last viewportNum that was used in order to being able to make a
	// animation
	int currentViewPortNum = -1;

	public Rectangle2D getViewPort(int num) {
		int x = 0, y = 0, width = 0, height = 0;
		width = this.width;
		width -= offsetX;
		width -= (num % columnCount + 1) * separationWidth;
		width /= columnCount;
		height = this.heigth;
		height -= offsetY;
		height -= (num / columnCount + 1) * separationHeight;
		height /= rowCount;
		x += offsetX;
		x += (num % columnCount) * (width);
		x += (num % columnCount) * (separationWidth);
		y += offsetY;
		y += (num / columnCount) * (height);
		y += (num / columnCount) * (separationWidth);

		currentViewPortNum = num;
		Rectangle2D viewPort = new Rectangle2D(x, y, width, height);
		// System.out.println(viewPort);
		return viewPort;
	}

	int lastIndex = -1;
	Tkey lastKey;

	public Rectangle2D getNextViewPort(Tkey key) {
		if (this.spriteMap != null && spriteMap.containsKey(key)) {
			// System.out.println("SpriteHandler.getNextViewPort()_key");

			LinkedList<Integer> list = spriteMap.getList(key);

			if (lastKey == null || lastKey != key) {
				// if a new Animation is played, start with the first Viewport
				// (reset lastIndex)
				lastIndex = -1;
				// return this.getViewPort(list.get(lastIndex));
			}
			// save key
			lastKey = key;

			// For a smooth Animation between two different Animations (Only for
			// cycles...)
			/*
			 * if (lastIndex < 0) { if (list != null) { // A there is a Viewport
			 * in the current Animation, that was // the last Viewport, start
			 * from there for a smooth // Animation lastIndex =
			 * list.indexOf(this.currentViewPortNum); } }
			 */

			if (list.size() == 1) {
				return this.getViewPort(list.get(0));
			}
			int nextIndex;
			if (lastIndex + 1 >= list.size()) {
				nextIndex = 0;
			} else {
				nextIndex = lastIndex + 1;
			}
			Integer integer = list.get(nextIndex);
			lastIndex = nextIndex;
			System.err.println(nextIndex);
			return this.getViewPort(integer);
		} else {
			// System.out.println("SpriteHandler.getNextViewPort()_nokey");
			// If the Animation key was not found
			if (this.getSpriteMap().size() > 0) {
				// Just take the first Animation and the Imageview must be
				// Rotated..
				return this.getNextViewPort((Tkey) this.getSpriteMap().keySet().toArray()[0]);
			} else {
				// If there is no Animation Defined, just iterate over all
				// Viewports
				this.lastIndex = lastIndex % (this.rowCount * this.columnCount - 1);
				return this.getViewPort(++this.lastIndex);
			}
		}
	}

	public int getMaxViewports() {
		return rowCount * columnCount;
	}

}
