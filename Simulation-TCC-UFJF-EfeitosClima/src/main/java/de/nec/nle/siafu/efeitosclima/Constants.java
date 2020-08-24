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

import java.util.ArrayList;

import de.nec.nle.siafu.types.FlatData;
import de.nec.nle.siafu.types.IntegerNumber;
import de.nec.nle.siafu.types.Publishable;
import de.nec.nle.siafu.types.Text;

/**
 * A list of the constants used by this simulation. None of this is strictly
 * needed, but it makes referring to certain values easier and less error
 * prone.
 * 
 * @author Miquel Martin
 */
public class Constants {
	/**
	 * Population size, that is, how many agents should inhabit this
	 * simulation.
	 */
	public static final int POPULATION = 40;

	/** A small maximum distance to wander off a main point when wanderng. */
	public static final int SMALL_WANDER = 80;

	/** A big distance to wander off a main point when wanderng. */
	public static final int BIG_WANDER = 200;

	/** Probability that the agent has a car. */
	public static final float PROB_HAS_CAR = 0.3f;

	/** Minimum age of an agent. */
	public static final int MIN_AGE = 20;

	/** Maximum age of an agent. */
	public static final int MAX_AGE = 60;

	/** Average hour of the day for work start. */
	public static final int AVG_WORK_START = 7;

	/** Average number of hours an agent works. */
	public static final int AVG_WORK_TIME = 8;

	/** Average number of hours an agent sleeps. */
	public static final int AVG_SLEEP_TIME = 8;

	/** 120min time blur. */
	public static final int TWO_HOUR_BLUR = 120;

	/** 60min time blur. */
	public static final int ONE_HOUR_BLUR = 60;

	/** 30min time blur. */
	public static final int HALF_HOUR_BLUR = 30;

	/** Hours would someone overwork per point in the party animal index. */
	public static final int WORKAHOLIC_FACTOR = 1;

	/** Hours would someone go party per point in the party animal index. */
	public static final int PARTY_ANIMAL_FACTOR = 1;

	/**
	 * The names of the fields in each agent object.
	 */
	static class Fields {

		/** The agent's current activity. */
		public static final String ACTIVITY = "Activity";
		
		/** Agent's age. */
		public static final String AGE = "Age";
		
		/** Agent's gender. */
		public static final String DATE = "Current Day";
		
		/** The current density of air of the agent's premise. */
		public static final String DENSITYAIR = "DensityAir";
		
		/** The current density of air of the agent's premise. */
		public static final String DENSITYAIR_RESISTANCE = "DensityAir_Resistance";
		
		/** Agent's gender. */
		public static final String GENDER = "Gender";

		/** Whether the agent has a car. */
		public static final String HAS_CAR = "Owns a car";

		/** The agent's home. */
		public static final String HOME = "Home";
		
		/** Agent's gender. */
		public static final String HOUR = "Current hour";
		
		/** The current humidity of the agent's premise. */
		public static final String HUMIDITY = "Humidity";
		
		/** The current humidity of the agent's premise. */
		public static final String HUMIDITY_RESISTANCE = "Humidity_Resistance";
		
		/** The Agent's simplified Body mass index . */
		public static final String IMC = "IMC result";

		/** Agent's language. */
		public static final String LANGUAGE = "Language";

		/** The hour and minute the agent left his work. */
		public static final String LEFTHOME = "Time left home";

		/** The hour and minute the agent left his work. */
		public static final String LEFTWORK = "Time left work";
		
		/** Agent's current mental state. */
		public static final String MENTAL_STATE = "Mental state";

		/** if the agent had a comfortable night sleep. */
		public static final String NIGHT_SLEEP = "Night sleep";
		
		/** The agent's office. */
		public static final String OFFICE = "Office";
		
		/** The hour and minute the agent reached his home. */
		public static final String REACHHOME = "Time reached home";
		
		/** The hour and minute the agent reached his work. */
		public static final String REACHWORK = "Time reached work";

		/** Start and finish sleeping period of the agent. */
		public static final String SLEEP_PERIOD = "Sleep period";
		
		/** Agent's current speed. */
		public static final String SPEED = "Speed";
		
		/** The agent's current temperature. */
		public static final String TEMPERATURE = "Temperature";
		
		/** The agent's current temperature. */
		public static final String TEMPERATURE_RESISTANCE = "Temperature_Resistance";

		/** Finish working time of the agent. */
		public static final String WORK_END = "Work end";

		/** Start working time of the agent. */
		public static final String WORK_START = "Work start";

		/** How much of a workaholic the agent is. */
		public static final String WORKAHOLIC = "Workaholism";
	}

	/* FIXME the activity doesn't show the actual description */
	/**
	 * List of possible activies. This is implemented as an enum because it
	 * helps us in switch statements. Like the rest of the constants in this
	 * class, they could also have been coded directly in the model
	 */
	enum Activity implements Publishable {
		/** The agent's asleep. */
		ASLEEP("Asleep"),
		/** The agent's at work, presumably working. */
		WORKING("Working"),
		/** The agen'ts on the way to work. */
		GOING_TO_WORK("Going to work"),
		/** The agent's at home, doing homey things. */
		AT_HOME("At home"),
		/** The agent's on the way home. */
		GOING_HOME("Going home");
		/** Human readable desription of the activity. */
		private String description;

		/**
		 * Get the description of the activity.
		 * 
		 * @return a string describing the activity
		 */
		public String toString() {
			return description;
		}

		/**
		 * Build an instance of Activity which keeps a human readable
		 * description for when it's flattened.
		 * 
		 * @param description the humanreadable description of the activity
		 */
		private Activity(final String description) {
			this.description = description;
		}

		/**
		 * Flatten the description of the activity.
		 * 
		 * @return a flatenned text with the description of the activity
		 */
		public FlatData flatten() {
			return new Text(description).flatten();
		}
	}

	/** A list with the possible genders, to ensure uniform spelling/form. */
	public static final ArrayList<Text> GENDER_TYPES = new ArrayList<Text>();

	/** How is the agent's current mental state. */
	public static final ArrayList<Text> MENTAL_TYPES =
			new ArrayList<Text>();

	/** Simplified types of Body mass index. */
	public static final ArrayList<Text> IMC_TYPES =
			new ArrayList<Text>();
	
	/** How the agent's body responds to the climate is. */
	public static final ArrayList<Text> RESISTENCY_TYPES =
			new ArrayList<Text>();

	/** How was the agent's last  night sleep. */
	public static final ArrayList<Text> SLEEP_TYPES =
			new ArrayList<Text>();
	
	/** How much of a workaholic the agent is. */
	public static final ArrayList<Text> WORKAHOLIC_TYPES =
			new ArrayList<Text>();

	/** How the agent's body responds to the climate it is in. */
	public static final ArrayList<int[][]> CLIMATE_TYPES =
			new ArrayList<int[][]>();
			


	/** How the agent's body responds to the climate it is in. */
	public static final ArrayList<int[][]> MIXED_CLIMATE_TYPES =
			new ArrayList<int[][]>();

	
	static {

		int [][] veryLow = new int[1249][899];
		int [][] low = new int[1249][899];
		int [][] comfortable = new int[1249][899];
		int [][] high = new int[1249][899];
		int [][] veryHigh = new int[1249][899];

		int [][] mixedLow = new int[1249][899];
		int [][] mixedComfortable = new int[1249][899];
		int [][] mixedHigh = new int[1249][899];
		
		for(int i = 0; i < 1249; i++) {       
		    for(int j = 0; j < 899; j++) { 
		    	veryLow[i][j] = 3355443;
		    	low[i][j] = 6710886;
		    	comfortable[i][j] = 10066329;
		    	high[i][j] = 13421772;
		    	veryHigh[i][j]= 16777215;
		    	
		    	if(j<899/2)
		    	{
		    		mixedLow[i][j] = 6710886;
		    		mixedComfortable[i][j] = 10066329;
			    	mixedHigh[i][j] = 13421772;
		    	}
		    	else
		    	{
			    	if(i>1249/2)
			    	{
			    		mixedLow[i][j] = 10066329;
			    		mixedComfortable[i][j] = 13421772;
				    	mixedHigh[i][j] = 16777215;
			    	}
			    	else
			    	{
			    		mixedLow[i][j] = 3355443;
			    		mixedComfortable[i][j] = 6710886;
				    	mixedHigh[i][j] = 10066329;
			    	}
		    	}
		    	
		    }
		}
		
		GENDER_TYPES.add(new Text("Male"));
		GENDER_TYPES.add(new Text("Female"));
		
		IMC_TYPES.add(new Text("Obese"));
		IMC_TYPES.add(new Text("Fit"));
		IMC_TYPES.add(new Text("Skinny"));

		RESISTENCY_TYPES.add(new Text("indifferent climate"));
		RESISTENCY_TYPES.add(new Text("strong comfortable"));
		RESISTENCY_TYPES.add(new Text("weak comfortable"));
		RESISTENCY_TYPES.add(new Text("strong high"));
		RESISTENCY_TYPES.add(new Text("weak high"));
		RESISTENCY_TYPES.add(new Text("strong low"));
		RESISTENCY_TYPES.add(new Text("weak low"));

		WORKAHOLIC_TYPES.add(new Text("Slacker"));
		WORKAHOLIC_TYPES.add(new Text("Easygoing"));
		WORKAHOLIC_TYPES.add(new Text("Average"));
		WORKAHOLIC_TYPES.add(new Text("Hardworker"));
		WORKAHOLIC_TYPES.add(new Text("Terminal"));


		SLEEP_TYPES.add(new Text("Comfortable"));
		SLEEP_TYPES.add(new Text("Uncomfortable"));
		SLEEP_TYPES.add(new Text("Indifferent"));

		MENTAL_TYPES.add(new Text("Happy"));
		MENTAL_TYPES.add(new Text("Sad"));
		MENTAL_TYPES.add(new Text("Indifferent"));
		
		CLIMATE_TYPES.add(veryLow);
		CLIMATE_TYPES.add(low);
		CLIMATE_TYPES.add(comfortable);
		CLIMATE_TYPES.add(high);
		CLIMATE_TYPES.add(veryHigh);

		MIXED_CLIMATE_TYPES.add(mixedLow);
		MIXED_CLIMATE_TYPES.add(mixedComfortable);
		MIXED_CLIMATE_TYPES.add(mixedHigh);
	}
}
