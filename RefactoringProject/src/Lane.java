/**
 * Lane.java
 * 
 * This class is representative of a lane and its responsibilities.
 * It has a pinsetter, and parties can be assigned to it.
 * The parties then play bowling games on the lane.
 * When a game is completed, it is responsible for asking the party
 * if they want to play another game or quit.
 * If they are done, a score report is emailed to them
 * and they are offered and option to print their scores.
 * 
 * @author - unknown, Maddison Hickson, Michael Yeaple
 * 
 */

/* $Id$
 *
 * Revisions:
 *   $Log: Lane.java,v $
 *   Revision 1.52  2003/02/20 20:27:45  ???
 *   Fouls disables.
 *
 *   Revision 1.51  2003/02/20 20:01:32  ???
 *   Added things.
 *
 *   Revision 1.50  2003/02/20 19:53:52  ???
 *   Added foul support.  Still need to update laneview and test this.
 *
 *   Revision 1.49  2003/02/20 11:18:22  ???
 *   Works beautifully.
 *
 *   Revision 1.48  2003/02/20 04:10:58  ???
 *   Score reporting code should be good.
 *
 *   Revision 1.47  2003/02/17 00:25:28  ???
 *   Added disbale controls for View objects.
 *
 *   Revision 1.46  2003/02/17 00:20:47  ???
 *   fix for event when game ends
 *
 *   Revision 1.43  2003/02/17 00:09:42  ???
 *   fix for event when game ends
 *
 *   Revision 1.42  2003/02/17 00:03:34  ???
 *   Bug fixed
 *
 *   Revision 1.41  2003/02/16 23:59:49  ???
 *   Reporting of sorts.
 *
 *   Revision 1.40  2003/02/16 23:44:33  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.39  2003/02/16 23:43:08  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.38  2003/02/16 23:41:05  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.37  2003/02/16 23:00:26  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.36  2003/02/16 21:31:04  ???
 *   Score logging.
 *
 *   Revision 1.35  2003/02/09 21:38:00  ???
 *   Added lots of comments
 *
 *   Revision 1.34  2003/02/06 00:27:46  ???
 *   Fixed a race condition
 *
 *   Revision 1.33  2003/02/05 11:16:34  ???
 *   Boom-Shacka-Lacka!!!
 *
 *   Revision 1.32  2003/02/05 01:15:19  ???
 *   Real close now.  Honest.
 *
 *   Revision 1.31  2003/02/04 22:02:04  ???
 *   Still not quite working...
 *
 *   Revision 1.30  2003/02/04 13:33:04  ???
 *   Lane may very well work now.
 *
 *   Revision 1.29  2003/02/02 23:57:27  ???
 *   fix on pinsetter hack
 *
 *   Revision 1.28  2003/02/02 23:49:48  ???
 *   Pinsetter generates an event when all pins are reset
 *
 *   Revision 1.27  2003/02/02 23:26:32  ???
 *   ControlDesk now runs its own thread and polls for free lanes to assign queue members to
 *
 *   Revision 1.26  2003/02/02 23:11:42  ???
 *   parties can now play more than 1 game on a lane, and lanes are properly released after games
 *
 *   Revision 1.25  2003/02/02 22:52:19  ???
 *   Lane compiles
 *
 *   Revision 1.24  2003/02/02 22:50:10  ???
 *   Lane compiles
 *
 *   Revision 1.23  2003/02/02 22:47:34  ???
 *   More observering.
 *
 *   Revision 1.22  2003/02/02 22:15:40  ???
 *   Add accessor for pinsetter.
 *
 *   Revision 1.21  2003/02/02 21:59:20  ???
 *   added conditions for the party choosing to play another game
 *
 *   Revision 1.20  2003/02/02 21:51:54  ???
 *   LaneEvent may very well be observer method.
 *
 *   Revision 1.19  2003/02/02 20:28:59  ???
 *   fixed sleep thread bug in lane
 *
 *   Revision 1.18  2003/02/02 18:18:51  ???
 *   more changes. just need to fix scoring.
 *
 *   Revision 1.17  2003/02/02 17:47:02  ???
 *   Things are pretty close to working now...
 *
 *   Revision 1.16  2003/01/30 22:09:32  ???
 *   Worked on scoring.
 *
 *   Revision 1.15  2003/01/30 21:45:08  ???
 *   Fixed speling of received in Lane.
 *
 *   Revision 1.14  2003/01/30 21:29:30  ???
 *   Fixed some MVC stuff
 *
 *   Revision 1.13  2003/01/30 03:45:26  ???
 *   *** empty log message ***
 *
 *   Revision 1.12  2003/01/26 23:16:10  ???
 *   Improved thread handeling in lane/controldesk
 *
 *   Revision 1.11  2003/01/26 22:34:44  ???
 *   Total rewrite of lane and pinsetter for R2's observer model
 *   Added Lane/Pinsetter Observer
 *   Rewrite of scoring algorythm in lane
 *
 *   Revision 1.10  2003/01/26 20:44:05  ???
 *   small changes
 *
 * 
 */

import java.util.Vector;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Date;

public class Lane extends Thread implements PinsetterObserver {	
	private Bowler currentThrower;	// The bowler currently throwing the ball.
	private Party party;
	private Iterator bowlerIterator;
	
	private Pinsetter setter;
	private HashMap scores;
	private Vector subscribers;

	private boolean canThrowAgain; // True if the bowler is allowed another throw.
	private boolean gameIsHalted;
	private boolean partyAssigned;
	private boolean gameFinished;
	
	private int gameNumber;
	private int ball;
	private int bowlIndex;
	private int frameNumber;

	private int[] curScores;
	private int[][] cumulScores;
	private int[][] finalScores;

	private LaneStrategy laneStrat;
	

	/** Lane()
	 * 
	 * Constructs a new lane and starts its thread.
	 * 
	 */
	public Lane() { 
		setter = new Pinsetter();
		scores = new HashMap();
		subscribers = new Vector();

		gameIsHalted = false;
		partyAssigned = false;

		gameNumber = 0;

		setter.subscribe( this );
		
		this.start();
	}

	/** run()
	 * 
	 * Entry point for execution of this lane.
	 * 
	 */
	public void run() {
		
		while (true) {
			
			/*
			 *  If there is a party assigned to this lane and
			 *  they are not finished with their game, continue
			 *  playing.
			 */
			if (partyAssigned && !gameFinished) {
				
				// If the game is halted, cease execution for 10 milliseconds.
				while (gameIsHalted) {
					try {
						sleep(10);
					} catch (Exception e) {}
				}

				bowlTurn(); // Bowl a turn.
			}
			
			try {
				sleep(10);
			} catch (Exception e) {}
		}
	}
	
	/** bowlTurn()
	 * 
	 * This method simulates one bowler's turn occurring.
	 * 
	 */
	private void bowlTurn(){
		switchLaneStrategy(); // Switch to the appropriate LaneStrategy.
		
		// If we have another bowler, let them throw.
		if (bowlerIterator.hasNext()) {
			currentThrower = (Bowler)bowlerIterator.next();

			canThrowAgain = true;
			ball = 0;
			while (canThrowAgain) {
				setter.ballThrown(); // Simulate the ball hitting the Pinsetter.
				ball++;
			}
			
			/*
			 * If we are on the tenth frame, add the 
			 * bowler's score to the score history file.
			 */
			if (frameNumber == 9){
				addBowlerScore();
			}
			
			setter.reset(); // Reset the pins for the next bowler.
			bowlIndex++;
			
		} else { // All bowlers have thrown for this frame.
			frameNumber++;
			resetBowlerIterator();
			bowlIndex = 0;
			
			// If we are past the tenth frame, the game is complete. 
			if (frameNumber > 9) {
				gameFinished = true;
				gameNumber++;
				finishGame();
			}
		}
	}
	
	/** switchLaneStrategy()
	 * 
	 * Switches to the appropriate lane strategy when called upon.
	 * 
	 */
	private void switchLaneStrategy(){
		if(frameNumber == 9){ // If we're on the 10th frame, we need a FinalFrameStrategy.
			laneStrat = new FinalFrameStrategy();
		} else { // If we're on any other frame, we need a NormalFrameStrategy.
			laneStrat = new NormalFrameStrategy();
		}
	}
	
	/** receivePinsetterEvent() 
	 * 
	 * Receives a PinsetterEvent and pass it to the current strategy to be
	 * handled appropriately.
	 * 
	 * @param pse - The PinsetterEvent that has been received.
	 */
	public void receivePinsetterEvent(PinsetterEvent pse) {
		laneStrat.handlePinsetterEvent(pse, this, currentThrower, frameNumber);
	}
	
	/** setCanThrowAgain()
	 * 
	 * Sets canThrowAgain to the input boolean.
	 * 
	 * @param throwAgain - A boolean value for whether a person can throw again or not.
	 */
	public void setCanThrowAgain(boolean throwAgain){
		canThrowAgain = throwAgain;
	}
	
	/** resetBowlerIterator()
	 * 
	 * Sets the current bowler iterator back to the first bowler.
	 * 
	 */
	private void resetBowlerIterator() {
		bowlerIterator = (party.getMembers()).iterator();
	}
	
	/** addBowlerScore()
	 * 
	 * Add a bowler's score to the score history file.
	 * 
	 */
	private void addBowlerScore(){
		finalScores[bowlIndex][gameNumber] = cumulScores[bowlIndex][9];
		
		try{
			Date date = new Date();
			String dateString = "" + date.getHours() + ":" + date.getMinutes() + " " + date.getMonth() + "/" + date.getDay() + "/" + (date.getYear() + 1900);
			ScoreHistoryFile.addScore(currentThrower.getNick(), dateString, new Integer(cumulScores[bowlIndex][9]).toString());
		} catch (Exception e) {
			System.err.println("Exception in addScore. "+ e );
		} 
	}

	/** resetScores()
	 * 
	 * Resets the scoring mechanism. This must be called before scoring starts.
	 * 
	 */
	private void resetScores() {
		Iterator bowlIt = (party.getMembers()).iterator();

		while ( bowlIt.hasNext() ) {
			int[] toPut = new int[25];
			for ( int i = 0; i != 25; i++){
				toPut[i] = -1;
			}
			scores.put( bowlIt.next(), toPut );
		}
		
		gameFinished = false;
		frameNumber = 0;
	}
	
	/** finishGame()
	 * 
	 * Finish a game for a party.
	 * 
	 */
	private void finishGame(){
		// Prompt to see if the party should play another game.
		EndGamePrompt egp = new EndGamePrompt( ((Bowler) party.getMembers().get(0)).getNickName() + "'s Party" );
		int result = egp.getResult(); // Get the result of the prompt.
		egp.distroy(); // Destroy the prompt.
		egp = null;
		
		// TODO: send record of scores to control desk
		if (result == 1) {	// They chose to play another game.
			
			// Reset the scores and bowler iterator so they may start again.
			resetScores();
			resetBowlerIterator();
			
		} else if (result == 2) { // They chose not to play another game.
			
			// Create an end game report for the party.
			Vector printVector; // This holds the names of the bowlers who want a printed report.
			EndGameReport egr = new EndGameReport( ((Bowler)party.getMembers().get(0)).getNickName() + "'s Party", party);
			printVector = egr.getResult();
			
			// Get an iterator for the party then get rid of the party.
			Iterator scoreIt = party.getMembers().iterator();
			party = null;
			partyAssigned = false;
			
			publish(lanePublish());
			
			int myIndex = 0;
			while (scoreIt.hasNext()){ // For each bowler...
				
				// Create the score report.
				Bowler thisBowler = (Bowler)scoreIt.next();
				ScoreReport sr = new ScoreReport( thisBowler, finalScores[myIndex++], gameNumber );
				
				// Email the score report.
				sr.sendEmail(thisBowler.getEmail());
				
				// Print the score report for each bowler that wants a printed report.
				Iterator printIt = printVector.iterator();
				while (printIt.hasNext()){
					
					// If the bowler wanted a printed report...
					if (thisBowler.getNick() == (String)printIt.next()){
						//Then print it!
						System.out.println("Printing " + thisBowler.getNick());
						sr.sendPrintout();
					}
				}
			}
		}
	}
		
	/** assignParty()
	 * 
	 * Assigns a party to this lane.
	 * 
	 * @param theParty - The party to be assigned to this lane.
	 */
	public void assignParty( Party theParty ) {
		party = theParty;
		resetBowlerIterator();
		partyAssigned = true;
		
		curScores = new int[party.getMembers().size()];
		cumulScores = new int[party.getMembers().size()][10];
		finalScores = new int[party.getMembers().size()][128]; //Hardcoding a max of 128 games, bite me.
		gameNumber = 0;
		
		resetScores();
	}

	/** markScore()
	 *
	 * Method that marks a bowlers score on the board.
	 * 
	 * @param Cur - The current bowler.
	 * @param frame - The frame that bowler is on.
	 * @param ball - The ball the bowler is on.
	 * @param score - The bowler's score.
	 */
	public void markScore( Bowler Cur, int frame, int ball, int score ){
		int[] curScore;
		int index =  ( (frame - 1) * 2 + ball);

		curScore = (int[]) scores.get(Cur);

	
		curScore[ index - 1] = score;
		scores.put(Cur, curScore);
		getScore( Cur, frame );
		publish( lanePublish() );
	}

	/** lanePublish()
	 *
	 * Method that creates and returns a newly created LaneEvent.
	 * 
	 * @return laneEvent - The new lane event.
	 */
	private LaneEvent lanePublish(  ) {
		LaneEvent laneEvent = new LaneEvent(party, bowlIndex, currentThrower, cumulScores, scores, frameNumber+1, curScores, ball, gameIsHalted);
		return laneEvent;
	}

	/** getScore()
	 *
	 * Method that calculates a bowler's score.
	 * 
	 * @param Cur - The bowler that is currently up.
	 * @param frame - The frame the current bowler is on.
	 * 
	 * @return totalScore - The bowler's total score.
	 */
	private int getScore( Bowler Cur, int frame) {
		int[] curScore;
		int totalScore = 0;
		int strikeballs = 0;
		curScore = (int[]) scores.get(Cur);
		for (int i = 0; i != 10; i++){
			cumulScores[bowlIndex][i] = 0;
		}
		int current = 2*(frame - 1)+ball-1;
		//Iterate through each ball until the current one.
		for (int i = 0; i != current+2; i++){
			//Spare:
			scoreSpare(curScore,i,current);
			//Strike:
			if( i < current && i%2 == 0 && curScore[i] == 10  && i < 18){
				strikeballs = ballsAfterStrike(curScore,i);
				if (strikeballs == 2){
					scoreStrike(curScore,i);
				} else {
					break;
				}
			}else {
				scoreEvenBall(curScore, i);
				scoreOddBall(curScore,i);
				scoreFinalFrame(curScore,i);
			}
		}
		return totalScore;
	}

	
	/** scoreSpare()
	 * 
	 * Checks whether a certain ball is a spare and if it is this method
	 * will calculate a score using the next ball if possible.
	 * 
	 * @param curScore
	 * @param ball
	 */
	private void scoreSpare(int[] curScore, int ball, int currentBall){
		if( ball%2 == 1 && curScore[ball - 1] + curScore[ball] == 10 && ball < currentBall - 1 && ball < 19){
			//This ball was a the second of a spare.  
			//Also, we're not on the current ball.
			//Add the next ball to the ith one in cumul.
			cumulScores[bowlIndex][(ball/2)] += curScore[ball+1] + curScore[ball]; 
			if (ball > 1) {
				//cumulScores[bowlIndex][i/2] += cumulScores[bowlIndex][i/2 -1];
			}
		}
	}
	
	/**
	 * 
	 * @param curScore
	 * @param ball
	 * @return
	 */
	private int ballsAfterStrike(int[] curScore, int ball){
		int strikeballs;
		strikeballs = 0;
		//This ball is the first ball, and was a strike.
		//If we can get 2 balls after it, good add them to cumul.
		if (curScore[ball+2] != -1) {
			strikeballs = 1;
			if(curScore[ball+3] != -1) {
				//Still got em.
				strikeballs = 2;
			} else if(curScore[ball+4] != -1) {
				//Ok, got it.
				strikeballs = 2;
			}
		}
		return strikeballs;
	}
	
	/**
	 * 
	 * @param curScore
	 * @param ball
	 */
	private void scoreStrike(int[] curScore, int ball){
		//Add up the strike.
		//Add the next two balls to the current cumulscore.
		cumulScores[bowlIndex][ball/2] += 10;
		if(curScore[ball+1] != -1) {
			cumulScores[bowlIndex][ball/2] += curScore[ball+1] + cumulScores[bowlIndex][(ball/2)-1];
			if (curScore[ball+2] != -1){
				if( curScore[ball+2] != -2){
					cumulScores[bowlIndex][(ball/2)] += curScore[ball+2];
				}
			} else {
				if( curScore[ball+3] != -2){
					cumulScores[bowlIndex][(ball/2)] += curScore[ball+3];
				}
			}
		} else {
			if ( ball/2 > 0 ){
				cumulScores[bowlIndex][ball/2] += curScore[ball+2] + cumulScores[bowlIndex][(ball/2)-1];
			} else {
				cumulScores[bowlIndex][ball/2] += curScore[ball+2];
			}
			if (curScore[ball+3] != -1){
				if( curScore[ball+3] != -2){
					cumulScores[bowlIndex][(ball/2)] += curScore[ball+3];
				}
			} else {
				cumulScores[bowlIndex][(ball/2)] += curScore[ball+4];
			}
		}
	}
	
	/**
	 * 
	 * @param curScore
	 * @param ball
	 */
	private void scoreEvenBall(int[] curScore, int ball){
		if( ball%2 == 0 && ball < 18){
			if( ball%2 == 0 && ball < 18){
				if ( ball/2 == 0 ) {
					//First frame, first ball.  Set his cumul score to the first ball
					if(curScore[ball] != -2){	
						cumulScores[bowlIndex][ball/2] += curScore[ball];
					}
				} else if (ball/2 != 9){
					//add his last frame's cumul to this ball, make it this frame's cumul.
					if(curScore[ball] != -2){
						cumulScores[bowlIndex][ball/2] += cumulScores[bowlIndex][ball/2 - 1] + curScore[ball];
					} else {
						cumulScores[bowlIndex][ball/2] += cumulScores[bowlIndex][ball/2 - 1];
					}	
				}
			}
		}
	}
	
	/**
	 * 
	 * @param curScore
	 * @param ball
	 */
	private void scoreOddBall(int[] curScore, int ball){
		if (ball < 18){ 
			if(curScore[ball] != -1 && ball > 2){
				if(curScore[ball] != -2){
					cumulScores[bowlIndex][ball/2] += curScore[ball];
				}
			}
		}
	}
	
	/**
	 * 
	 * @param curScore
	 * @param ball
	 */
	private void scoreFinalFrame(int[] curScore, int ball){
		if (ball/2 == 9){
			if (ball == 18){
				cumulScores[bowlIndex][9] += cumulScores[bowlIndex][8];	
			}
			if(curScore[ball] != -2){
				cumulScores[bowlIndex][9] += curScore[ball];
			}
		} else if (ball/2 == 10) {
			if(curScore[ball] != -2){
				cumulScores[bowlIndex][9] += curScore[ball];
			}
		}
	}
	
	
	/** isPartyAssigned()
	 * 
	 * Checks to see if a party is currently assigned to this lane.
	 * 
	 * @return partyAssigned - True if a party is currently assigned. Otherwise, false.
	 */
	public boolean isPartyAssigned() {
		return partyAssigned;
	}
	
	/** isGameFinished()
	 * 
	 * @return gameFinished - True if the game is complete. Otherwise, false.
	 */
	public boolean isGameFinished() {
		return gameFinished;
	}

	/** subscribe()
	 * 
	 * Method that will add a subscriber
	 * 
	 * @param subscribe	Observer that is to be added
	 */

	public void subscribe( LaneObserver adding ) {
		subscribers.add( adding );
	}

	/** unsubscribe()
	 * 
	 * Method that unsubscribes an observer from this object
	 * 
	 * @param removing - The observer to be removed.
	 */
	
	public void unsubscribe( LaneObserver removing ) {
		subscribers.remove( removing );
	}

	/** publish()
	 *
	 * Method that publishes an event to subscribers
	 * 
	 * @param event	Event that is to be published
	 */

	public void publish( LaneEvent event ) {
		if( subscribers.size() > 0 ) {
			Iterator eventIterator = subscribers.iterator();
			
			while ( eventIterator.hasNext() ) {
				( (LaneObserver) eventIterator.next()).receiveLaneEvent( event );
			}
		}
	}

	/** getPinsetter()
	 * 
	 * Returns this Lane's Pinsetter
	 * 
	 * @return setter - A reference to this lane's Pinsetter
	 */

	public Pinsetter getPinsetter() {
		return setter;	
	}

	/**
	 * Pause the execution of this game
	 */
	public void pauseGame() {
		gameIsHalted = true;
		publish(lanePublish());
	}
	
	/**
	 * Resume the execution of this game
	 */
	public void unPauseGame() {
		gameIsHalted = false;
		publish(lanePublish());
	}

}
