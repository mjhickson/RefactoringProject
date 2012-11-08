/**
 * LaneStrategy.java
 * 
 * Defines the interface that each of the concrete lane strategies must adhere to.
 * The main purpose for this class is to define an interface to handle PinsetterEvents for Lane.
 * 
 * @author - Michael Yeaple
 */

public interface LaneStrategy {
	
	/** handlePinsetterEvent()
	 * 
	 * Method for handling PinsetterEvents sent from Lane.
	 * Specifically, this will mark the score and set whether
	 * the bowler can throw again or not.
	 * 
	 * @param pse - The PinsetterEvent to be handled.
	 * @param curLane - The Lane that the PinsetterEvent comes from.
	 * @param curBowler - The bowler that just threw the ball.
	 * @param frameNum - The frame that the bowler just threw for.
	 */
	public void handlePinsetterEvent(PinsetterEvent pse, Lane curLane, Bowler curBowler, int frameNum);
}
