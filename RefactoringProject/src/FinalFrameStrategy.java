
/**
 *  NormalFrameStrategy.java
 * 
 * This class is meant to handle PinsetterEvents for Lane.
 * This strategy should be used for frames 1-9.
 * 
 * @author Michael Yeaple
 */

public class FinalFrameStrategy implements LaneStrategy {

	/**
	 * Default constructor for the FinalFrameStrategy.
	 * Should be empty.
	 */
	public FinalFrameStrategy() {
		
	}

	/**
	 * Method for handling PinsetterEvents sent from Lane.
	 * Specifically, this will mark the score and set whether
	 * the bowler can throw again or not.
	 * 
	 * @param pse - The PinsetterEvent to be handled.
	 * @param curLane - The Lane that the PinsetterEvent comes from.
	 */
	public void handlePinsetterEvent(PinsetterEvent pse, Lane curLane) {
		
	}

}
