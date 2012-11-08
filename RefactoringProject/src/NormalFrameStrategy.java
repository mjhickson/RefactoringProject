
/**
 * NormalFrameStrategy.java
 * 
 * This class is meant to handle PinsetterEvents for Lane.
 * This strategy should be used for frames 1-9.
 * 
 * @author - Michael Yeaple
 */

public class NormalFrameStrategy implements LaneStrategy {

	/**
	 * Default constructor for the NormalFrameStrategy.
	 * Should be empty.
	 */
	public NormalFrameStrategy() {
		
	}

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
	public void handlePinsetterEvent(PinsetterEvent pse, Lane curLane, Bowler curBowler, int frameNum) {

		// If the number of pins downed is 0 or greater, it's a real throw.
		if (pse.pinsDownOnThisThrow() >=  0) {
			curLane.markScore(curBowler, frameNum + 1, pse.getThrowNumber(), pse.pinsDownOnThisThrow());

				/* 
				 * In either the case that the number of pins downed is 10 (it's a strike)
				 * or the bowler just took their second throw, they don't get to throw again.
				 */
				if (pse.pinsDownOnThisThrow() == 10 || pse.getThrowNumber() == 2) {
					curLane.setCanThrowAgain(false);
				}
		} else {	//Since the number of pins downed is not 0 or greater, this must be a reset.
		}
	}

}
