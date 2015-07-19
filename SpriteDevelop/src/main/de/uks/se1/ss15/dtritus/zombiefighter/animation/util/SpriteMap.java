package de.uks.se1.ss15.dtritus.zombiefighter.animation.util;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class SpriteMap<Tkey, Tval> extends LinkedHashMap<Tkey, LinkedList<Tval>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 691591452913340372L;

	public void addEntry(Tkey key, Tval... num) {
		for (Tval i : num) {
			this.addEntry(key, i);
		}
	}

	public void addEntry(Tkey direction, Tval num) {
		LinkedList<Tval> list;
		// Get the LinkedList from the HashMap
		if (!this.containsKey(direction)) {
			list = new LinkedList<Tval>();
			this.put(direction, list);
		} else {
			list = (LinkedList<Tval>) this.get(direction);
		}
		// Add the Integer to the LinkedList
		list.add(num);
	}

	public boolean removeEntry(Tkey direction, int index) {
		LinkedList<Tval> intList;
		// Get the LinkedList from the HashMap
		if (this.containsKey(direction)) {
			intList = this.get(direction);
			if(index < intList.size()-1){
				intList.remove(index);
				return true;
			}else{
				throw new IndexOutOfBoundsException();
			}
		}
		return false;
	}

	public LinkedList<Tval> getList(Tkey key) {
		return this.get(key);
	}

}
