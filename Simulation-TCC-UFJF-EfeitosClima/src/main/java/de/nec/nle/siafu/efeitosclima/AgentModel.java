/*
 * Copyright NEC Europe Ltd. 2006-2007
 * 
 * This file is part of the context simulator called Siafu.
 * 
 * Siafu is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * Siafu is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.nec.nle.siafu.efeitosclima;

import static de.nec.nle.siafu.efeitosclima.Constants.AVG_WORK_TIME;
import static de.nec.nle.siafu.efeitosclima.Constants.MENTAL_TYPES;
import static de.nec.nle.siafu.efeitosclima.Constants.ONE_HOUR_BLUR;
import static de.nec.nle.siafu.efeitosclima.Constants.POPULATION;
import static de.nec.nle.siafu.efeitosclima.Constants.SMALL_WANDER;
import static de.nec.nle.siafu.efeitosclima.Constants.SLEEP_TYPES;
import static de.nec.nle.siafu.efeitosclima.Constants.WORKAHOLIC_FACTOR;
import static de.nec.nle.siafu.efeitosclima.Constants.WORKAHOLIC_TYPES;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.ACTIVITY;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.DENSITYAIR;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.DENSITYAIR_RESISTANCE;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.HAS_CAR;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.HOME;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.HUMIDITY;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.HUMIDITY_RESISTANCE;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.IMC;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.LEFTHOME;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.LEFTWORK;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.MENTAL_STATE;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.NIGHT_SLEEP;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.OFFICE;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.REACHHOME;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.REACHWORK;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.SLEEP_PERIOD;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.SPEED;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.TEMPERATURE;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.TEMPERATURE_RESISTANCE;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.DATE;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.WORKAHOLIC;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.WORK_END;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.WORK_START;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Random;

import de.nec.nle.siafu.behaviormodels.BaseAgentModel;
import de.nec.nle.siafu.efeitosclima.Constants.Activity;
import de.nec.nle.siafu.exceptions.InfoUndefinedException;
import de.nec.nle.siafu.exceptions.UnknownContextException;
import de.nec.nle.siafu.model.Agent;
import de.nec.nle.siafu.model.Place;
import de.nec.nle.siafu.model.World;
import de.nec.nle.siafu.types.BooleanType;
import de.nec.nle.siafu.types.EasyTime;
import de.nec.nle.siafu.types.IntegerNumber;
import de.nec.nle.siafu.types.Text;
import de.nec.nle.siafu.types.TimePeriod;

/**
 * Defines the behavior of the agent population. Essentially, they wake up in
 * the morning at home (red spots), and go to work (blue spots) and then
 * either go home and stay, or go to an entertainment place (yellow spots) or
 * one and then the other. Eventually (hopefully before they have to go work
 * again) they go to sleep home, and the cycle resumes.
 * 
 * @author Miquel Martin
 * 
 */
public class AgentModel extends BaseAgentModel {

	/**
	 * A random number generator.
	 */
	private static Random rand = new Random();
	
	/**
	 * Cars move in between 1 and 1+SPEED_RANGE speed.
	 */
	private static final int SPEED_RANGE = 3;

	/**
	 * An agent that owns a car will only use it to cover distances of this
	 * amount.
	 */
	private static final int MIN_DIST_4_CAR = 100;

	/** A random number generator. */
	private static final Random RAND = new Random();


	/**
	 * A constructor that simply calls the super constructor (BaseAgentModel).
	 * 
	 * @param world the simulated world
	 */
	public AgentModel(final World world) {
		super(world);
	}

	/**
	 * Create POPULATION agents randomly, using the PersonGenerator class.
	 * 
	 * @return the list of agents.
	 */
	public ArrayList<Agent> createAgents() {
		System.out.println("Creating " + POPULATION + " people");

		ArrayList<Agent> people =
				AgentGenerator.createRandomPopulation(POPULATION, world);
		
		return people;
	}

	/**
	 * Perform an iteration by going through each of the agents. The exact
	 * behaviour is explained in this class' description. Note that agents who
	 * are being controlled by the GUI will not be affected.
	 * 
	 * @param agents the agents in the world (inluding those controlled
	 *            through the GUI
	 */
	public void doIteration(final Collection<Agent> agents) {
		for (Agent a : agents) {
			if (!a.isOnAuto()) {
				continue; // This guy's being managed by the user interface
			}

			Calendar time = world.getTime();
			EasyTime now =
					new EasyTime(time.get(Calendar.HOUR_OF_DAY), time
							.get(Calendar.MINUTE));
			EasyTime date =
					new EasyTime(time.get(Calendar.DAY_OF_MONTH), time
							.get(Calendar.MONTH));
			TimePeriod sleepPeriod = (TimePeriod) a.get(SLEEP_PERIOD);
			EasyTime workStart = (EasyTime) a.get(WORK_START);
			EasyTime workEnd = (EasyTime) a.get(WORK_END);

			
			switch ((Activity) a.get(ACTIVITY)) {
			
				case ASLEEP:
					//woke up
					if (!now.isIn(sleepPeriod)) {
						a.set(ACTIVITY, Activity.AT_HOME);
						
						a.set(NIGHT_SLEEP, getRandomType(SLEEP_TYPES));
						
						a.set(MENTAL_STATE, getRandomType(MENTAL_TYPES));
						
						setSpeedConformingClimate(a,now);
						a.set(DATE, date);
					}
	
					break;
	
				case AT_HOME:
					//leaving house
					if (now.isAfter(workStart) && now.isBefore(workEnd)) {
						
						a.set(MENTAL_STATE, getRandomType(MENTAL_TYPES));
						
						setSpeedConformingClimate(a,now);
						a.set(LEFTHOME, now);
						goWork(a);
					} 
					//going to sleep
					else if (now.isIn(sleepPeriod)) {
						
						
						a.set(ACTIVITY, Activity.ASLEEP);
					}
					//doing something at home
					else {
						beIdleAtHome(a);
					}
	
					break;
	
				case GOING_TO_WORK:
					//reached work
					if (a.isAtDestination()) {
						a.set(REACHWORK, now);
						a.set(ACTIVITY, Activity.WORKING);
						
						a.set(MENTAL_STATE, getRandomType(MENTAL_TYPES));
						setSpeedConformingClimate(a,now);
						
						setWorkEnd(a, workStart);
					}
	
					break;
	
				case WORKING:
					///leaving work
					if (!now.isBefore((EasyTime) a.get(WORK_END))) {
						
						a.set(MENTAL_STATE, getRandomType(MENTAL_TYPES));
						setSpeedConformingClimate(a,now);
						
						a.set(LEFTWORK,now);
						goHome(a);
					}
					//working
					else {
						beAtWork(a);
					}
	
					break;
	
				case GOING_HOME:
					///reached home
					if (a.isAtDestination()) {
						a.set(REACHHOME,now);
						saveCurrentAgentDetails(a,now);
						saveCurrentAgentFromTo(a);
						a.set(ACTIVITY, Activity.AT_HOME);
						
						a.set(MENTAL_STATE, getRandomType(MENTAL_TYPES));
					} 
	
					break;
					
				default:
					throw new RuntimeException("Unable to handle activity "
							+ (Activity) a.get(ACTIVITY));
			}
		}
	}

	/**
	 * Modifies the agent to behave like a car by setting the right speed and
	 * appearance).
	 * 
	 * @param a the agent to turn into a car
	 * @param turnToCar true if the agent should become a car, false if it
	 *            should look back like a person when looking like a car.
	 * @param appearance the name of the sprite that represents the car we
	 *            need
	 * @param speed the speed of a car. The speed of a person is 1 by default.
	 */
	protected void carify(final Agent a, final boolean turnToCar,
			final String appearance, final int speed) {
		if (turnToCar) {
			a.setImage(appearance);
			a.setSpeed(speed);
		} else {
			a.setImage(appearance);
		}
	}

	/**
	 * Give a numeric value to a string from a sorted list of strings. In this
	 * case, the workaholic level. For instance, a slacker 
	 * have a level of 0, while terminal
	 * workaholics are whoping top level 4.
	 * 
	 * @param value the value to rate
	 * @param types the list of possible types to rate against
	 * @return the party animal index, between 0 and 4, both included
	 */
	protected int getIndex(final Text value, final ArrayList<Text> types) {
		for (int i = 0; i < types.size(); i++) {
			if (value.equals(types.get(i))) {
				return i;
			}
		}
		throw new RuntimeException("Uknown value: " + value);
	}

	/**
	 * Set the time at which the agent leaves work, which is the given start
	 * time plus a AVG_WORK_TIME plus number of hours depending on the
	 * workaholic factor, and blurred over one hour.
	 * 
	 * @param a the agent whose work end we want to set
	 * @param start the time at which the work starts
	 */
	protected void setWorkEnd(final Agent a, final EasyTime start) {
		int paIndex = getIndex((Text) a.get(WORKAHOLIC), WORKAHOLIC_TYPES);
		EasyTime workEnd =
				new EasyTime(start).shift(AVG_WORK_TIME + paIndex
						* WORKAHOLIC_FACTOR, 1);
		workEnd.blur(ONE_HOUR_BLUR);
		a.set(WORK_END, workEnd);
		return;
	}

	/**
	 * Go to the assigned office. If the place is far, and the agent has a car
	 * then he turns into a car to go there.
	 * 
	 * @param a the agent to sent to work
	 */
	protected void goWork(final Agent a) {
		a.set(ACTIVITY, Activity.GOING_TO_WORK);

		if ((((Place) a.get(OFFICE)).distanceFrom(a.getPos()) > MIN_DIST_4_CAR)
				&& ((BooleanType) a.get(HAS_CAR)).getValue()) {
			carify(a, true, "CarBlue", RAND.nextInt(SPEED_RANGE) + 1);
		} else {
			a.setImage("HumanBlue");
		}

		a.setDestination((Place) a.get(OFFICE));
	}


	/**
	 * Send the agent home.
	 * 
	 * @param a the agent to be sent home
	 */
	protected void goHome(final Agent a) {
		a.set(ACTIVITY, Activity.GOING_HOME);
		a.setImage("HumanMagenta");
		a.setDestination((Place) a.get(HOME));
	}

	/**
	 * Keeps the user wandering around home.
	 * 
	 * @param a the agent to keep at home
	 */
	protected void beIdleAtHome(final Agent a) {
		a.wanderAround((Place) a.get(HOME), SMALL_WANDER);
	}

	/**
	 * Do the activities related to being at work. In this case, wander around
	 * the work place with a radius of <code>SMALL_WANDER</code>.
	 * 
	 * @param p the agent that should be at work
	 * @throws InfoUndefinedException if the person doesn't exist
	 */
	protected void beAtWork(final Agent p) throws InfoUndefinedException {
		p.wanderAround((Place) p.get(OFFICE), SMALL_WANDER);
	}
	

	/**
	 * returns the speed modifier according to how comfortable was the agent's last night sleep
	 * 
	 * @param nightSleep how comfortable was the agent's last night sleep
	 */
	private int speedForSleep(String nightSleep)
	{
		int speed = 0;
		
		if(nightSleep.equals("Comfortable"))
		{
			speed++;
		}
		else if(nightSleep.equals("Uncomfortable"))
		{
			speed--;
		}
		
		return speed;
	}
	

	/**
	 * returns the speed modifier according to how comfortable was the agent's last night sleep
	 * 
	 * @param nightSleep how comfortable was the agent's last night sleep
	 */
	private int speedForIMC(String imcResult)
	{
		int speed = -1;
		
		if(imcResult.equals("Fit"))
		{
			speed++;
		}
		
		return speed;
	}
	
	/**
	 * returns the speed modifier according to the agent's current mental state
	 * 
	 * @param mentalState the agent's current mental state
	 */
	private int speedForMentalState(String mentalState)
	{
		int speed = 0;
		
		if(mentalState.equals("Happy"))
		{
			speed++;
		}
		else if(mentalState.equals("Sad"))
		{
			speed--;
		}
		
		return speed;
	}
	
	/**
	 * returns agent's speed according to an specific the climate 
	 * 
	 * @param climateSensation the agent's current climatic sensation, if he 
	 * is feeling it is comfortable, high or low.
	 * @param climateResistance how the agent's body respond to that climate
	 */
	private int speedForClimate(String climateSensation, String climateResistance)
	{
		String climateResponse = climateResistance.split(" ")[0];
		String climateType = climateResistance.split(" ")[1];
		int speed = 5;
		
		if(climateSensation.equals(climateType))
		{
			if(climateResponse.equals("strong"))
			{
				speed++;
			}
			else if(climateResponse.equals("weak"))
			{
				speed--;
			}
		}
		else if(!climateSensation.equals("comfortable"))
		{
			speed--;
		}
		
		return speed;
	}
	
	/**
	 * Return the agent's speed according to the overall climate
	 * 
	 * @param agent the agent in question
	 * @throws UnknownContextException if the overlay doesn't exist
	 */
	private void setSpeedConformingClimate(final Agent agent,EasyTime now)
	{
		int newSpeed=4,temperatureSpeed,humiditySpeed,
				densityairSpeed;
		int oldSpeed = Integer.parseInt(agent.get(SPEED).toString()) ;
		String temperature,humidity,densityair,
				temperatureResistance,humidityResistance,
				densityairResistance;
		try {
			
			temperature = agent.getContext(TEMPERATURE).toString();
			humidity = agent.getContext(HUMIDITY).toString();
			densityair = agent.getContext(DENSITYAIR).toString();
			
			temperature = temperature.replace("Text:", "");
			humidity = humidity.replace("Text:", "");
			densityair = densityair.replace("Text:", "");
			
			temperatureResistance = agent.get(TEMPERATURE_RESISTANCE).toString();
			humidityResistance = agent.get(HUMIDITY_RESISTANCE).toString();
			densityairResistance = agent.get(DENSITYAIR_RESISTANCE).toString();

			temperatureSpeed = speedForClimate(temperature,temperatureResistance);
			humiditySpeed = speedForClimate(humidity,humidityResistance);
			densityairSpeed =speedForClimate(densityair,densityairResistance);
			
			newSpeed = (temperatureSpeed + humiditySpeed + densityairSpeed + 2)/3;
			newSpeed = newSpeed + speedForMentalState(agent.get(MENTAL_STATE).toString())
			+ speedForSleep(agent.get(NIGHT_SLEEP).toString()) + speedForIMC(agent.get(IMC).toString());
			
			if(newSpeed!=oldSpeed)
			{
				agent.set(SPEED,new IntegerNumber(newSpeed));
				saveCurrentAgentDetails(agent, now);
			}
			
		} catch (UnknownContextException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//save to file the agent's details.
	private void saveCurrentAgentDetails(Agent a, EasyTime now)
	{
		//SaveToCSV.getInstance().updateFile(a,now.toString(),world);
		//SaveToCSV.getInstance().updateFileMixed(a,now.toString());
	}
	//save to file the amount of time it took to the agent to go from his home to his work and from his work to his home.
	private void saveCurrentAgentFromTo(Agent a)
	{
		//SaveToCSV.getInstance().updateFromTo(a);
		//SaveToCSV.getInstance().updateFromToMixedClimate(a);
	}
	/**
	 * Return a random element from the given array list.
	 * 
	 * @param types the ArrayList containing the types
	 * @return the randomly chosen type
	 */
	private static Text getRandomType(final ArrayList<Text> types) {
		return types.get(rand.nextInt(types.size()));
	}

}
