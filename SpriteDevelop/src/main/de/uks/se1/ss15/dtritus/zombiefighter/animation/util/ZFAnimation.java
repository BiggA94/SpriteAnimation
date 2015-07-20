package de.uks.se1.ss15.dtritus.zombiefighter.animation.util;

/**
 * @author Alexander
 *
 */
public enum ZFAnimation {
	// For a normal Walk
	WALK_UP,
	WALK_DOWN,
	WALK_RIGHT,
	WALK_LEFT,
	WALK_UPPERRIGHT,
	WALK_UPPERLEFT,
	WALK_LOWERRIGHT,
	WALK_LOWERLEFT,
	
	// For Teleportation	
	PORT_UP,
	PORT_DOWN,
	PORT_RIGHT,
	PORT_LEFT,
	PORT_UPPERRIGHT,
	PORT_UPPERLEFT,
	PORT_LOWERRIGHT,
	PORT_LOWERLEFT,
	
	// Default as fallback
	UNDEFINED,
	
	// Animation when a Zombie dies	
	DIE, 
	DEFAULT,

}
