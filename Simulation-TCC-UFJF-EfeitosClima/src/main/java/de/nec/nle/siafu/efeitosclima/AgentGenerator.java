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

import static de.nec.nle.siafu.efeitosclima.Constants.AVG_SLEEP_TIME;
import static de.nec.nle.siafu.efeitosclima.Constants.AVG_WORK_START;
import static de.nec.nle.siafu.efeitosclima.Constants.AVG_WORK_TIME;
import static de.nec.nle.siafu.efeitosclima.Constants.GENDER_TYPES;
import static de.nec.nle.siafu.efeitosclima.Constants.HALF_HOUR_BLUR;
import static de.nec.nle.siafu.efeitosclima.Constants.IMC_TYPES;
import static de.nec.nle.siafu.efeitosclima.Constants.MAX_AGE;
import static de.nec.nle.siafu.efeitosclima.Constants.MENTAL_TYPES;
import static de.nec.nle.siafu.efeitosclima.Constants.MIN_AGE;
import static de.nec.nle.siafu.efeitosclima.Constants.PROB_HAS_CAR;
import static de.nec.nle.siafu.efeitosclima.Constants.RESISTENCY_TYPES;
import static de.nec.nle.siafu.efeitosclima.Constants.SLEEP_TYPES;
import static de.nec.nle.siafu.efeitosclima.Constants.TWO_HOUR_BLUR;
import static de.nec.nle.siafu.efeitosclima.Constants.WORKAHOLIC_TYPES;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.ACTIVITY;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.AGE;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.GENDER;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.HAS_CAR;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.HOME;
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
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.DATE;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.WORKAHOLIC;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.WORK_END;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.WORK_START;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.TEMPERATURE_RESISTANCE;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.HUMIDITY_RESISTANCE;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.DENSITYAIR_RESISTANCE;

import java.util.ArrayList;
import java.util.Random;

import de.nec.nle.siafu.efeitosclima.Constants.Activity;
import de.nec.nle.siafu.exceptions.PlaceNotFoundException;
import de.nec.nle.siafu.model.Agent;
import de.nec.nle.siafu.model.Place;
import de.nec.nle.siafu.model.Position;
import de.nec.nle.siafu.model.World;
import de.nec.nle.siafu.types.BooleanType;
import de.nec.nle.siafu.types.EasyTime;
import de.nec.nle.siafu.types.IntegerNumber;
import de.nec.nle.siafu.types.Text;
import de.nec.nle.siafu.types.TimePeriod;

/**
 * This class creates an agent that's suitable for the Santos Dumont-MG simulation, by
 * setting randomized values for the whole population.
 * 
 * @author Miquel Martin
 * 
 */
final class AgentGenerator {

	/**
	 * A random number generator.
	 */
	private static Random rand = new Random();

	/**
	 * Keep this class from being instantiated.
	 */
	private AgentGenerator() {
	}

	/**
	 * Create a population made up of <code>size</code> random agents.
	 * 
	 * @param size the population size
	 * @param world the world object of the whole simulation
	 * @return an ArrayList with the collection of agents
	 */
	public static ArrayList<Agent> createRandomPopulation(final int size,
			final World world) {
		ArrayList<Agent> population = new ArrayList<Agent>(size);

		for (int i = 0; i < size; i++) {
			population.add(createRandomAgent(world));
		}

		return population;
	}

	/**
	 * Create a random agent that fits the Santos Dumont-MG simulation.
	 * @param world the world of the simulation
	 * @return the new agent
	 */
	public static Agent createRandomAgent(final World world) {
		
		Position anywhere = world.getPlaces().get(0).getPos();

		Agent a = new Agent(anywhere, "HumanGreen", world);
		
		/* Not used in this simulation for not being necessary
		boolean hasCar = false;
		if (rand.nextFloat() < PROB_HAS_CAR) {
			hasCar = true;
		}*/
		
		int age = MIN_AGE + rand.nextInt(MAX_AGE - MIN_AGE);
		EasyTime workStart = new EasyTime(AVG_WORK_START, 0);
		EasyTime sleepEnd = new EasyTime(workStart).shift(-1, 0);
		EasyTime sleepStart =
				new EasyTime(sleepEnd).shift(-AVG_SLEEP_TIME, 0);

		workStart.blur(TWO_HOUR_BLUR);
		sleepEnd.blur(HALF_HOUR_BLUR);
		sleepStart.blur(TWO_HOUR_BLUR);

		a.set(AGE, new IntegerNumber(age));
		a.set(ACTIVITY, Activity.ASLEEP);
		a.set(DATE,new EasyTime("1:1"));
		a.set(DENSITYAIR_RESISTANCE, getRandomType(RESISTENCY_TYPES));
		a.set(GENDER, getRandomType(GENDER_TYPES));
		a.set(HAS_CAR, new BooleanType(false));
		a.set(HUMIDITY_RESISTANCE, getRandomType(RESISTENCY_TYPES));
		a.set(IMC, getRandomType(IMC_TYPES));
		a.set(LEFTHOME,new EasyTime(0,0));
		a.set(LEFTWORK,new EasyTime(0,0));
		a.set(MENTAL_STATE, getRandomType(MENTAL_TYPES));
		a.set(NIGHT_SLEEP, getRandomType(SLEEP_TYPES));
		a.set(REACHHOME,new EasyTime(0,0));
		a.set(REACHWORK,new EasyTime(0,0));
		a.set(SLEEP_PERIOD, new TimePeriod(sleepStart, sleepEnd));
		a.set(SPEED,new IntegerNumber(0));
		a.set(TEMPERATURE_RESISTANCE, getRandomType(RESISTENCY_TYPES));
		a.set(WORK_START, workStart);
		a.set(WORK_END, new EasyTime(workStart).shift(AVG_WORK_TIME, 0));
		a.set(WORKAHOLIC, getRandomType(WORKAHOLIC_TYPES));
		
		try {
			a.set(HOME, world.getRandomPlaceOfType("Homes"));
		} catch (PlaceNotFoundException e) {
			throw new RuntimeException(
					"Can't find any homes Places. Did u create them?");
		}

		try {
			a.set(OFFICE, world.getRandomPlaceOfType("Offices"));
		} catch (PlaceNotFoundException e) {
			throw new RuntimeException(
					"Can't find any offices. Did u create them?");
		}
		//SaveToCSV.getInstance().saveAgentDetails(a);
		a.setPos(((Place) a.get(HOME)).getPos());
		return a;
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
