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

import static de.nec.nle.siafu.efeitosclima.Constants.CLIMATE_TYPES;
import static de.nec.nle.siafu.efeitosclima.Constants.MIXED_CLIMATE_TYPES;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;

import de.nec.nle.siafu.behaviormodels.BaseContextModel;
import de.nec.nle.siafu.model.Overlay;
import de.nec.nle.siafu.model.Position;
import de.nec.nle.siafu.model.World;
import de.nec.nle.siafu.types.EasyTime;
import de.nec.nle.siafu.types.Publishable;
import de.nec.nle.siafu.types.Text;

/**
 * The model that controls the evolution of context in the simulation. 
 * 
 * @author João Pedro de Souza Jardim da Costa
 * 
 */
public class ContextModel extends BaseContextModel {


	/** boolean to lock the amount of times each overlay is changed per day to 1 each. */
	boolean firstTimeDay = true;


	/**
	 * A random number generator.
	 */
	private static Random rand = new Random();
	
	/** The moment that the climate will change, which is 00:00. */
	EasyTime midnight = new EasyTime(0, 0);
	
	/**
	 * Constructor for the ContextModel.
	 * 
	 * @param world the simulated world
	 */
	public ContextModel(final World world) {
		super(world);
	}

	/**
	 * A hook to create new overlays. We use only the ones provided by the
	 * images.
	 * 
	 * @param oList the list of already existing overlays
	 */
	@Override
	public void createOverlays(final ArrayList<Overlay> oList) {
		Overlay newTemperaOverlay = createDiscreteOverlay("Temperature",getRandomType(CLIMATE_TYPES));
		Overlay newDensAirOverlay = createDiscreteOverlay("DensityAir",getRandomType(CLIMATE_TYPES));
		Overlay newHumidOverlay = createDiscreteOverlay("Humidity",getRandomType(CLIMATE_TYPES));
		oList.add(newTemperaOverlay);
		oList.add(newDensAirOverlay);
		oList.add(newHumidOverlay);
	}

	/**
	 * Hook to modify the overlays at simulation time. Nothing is done in this
	 * case.
	 * 
	 * @param overlays the available overlays
	 */
	@Override
	public void doIteration(final Map<String, Overlay> overlays) {

		Calendar time = world.getTime();
		EasyTime now =
				new EasyTime(time.get(Calendar.HOUR_OF_DAY), time
						.get(Calendar.MINUTE));
		
		
		if(now.equals(midnight) && firstTimeDay)
		{
			
			Overlay newTemperaOverlay = createDiscreteOverlay("Temperature",getRandomType(CLIMATE_TYPES));
			Overlay newDensAirOverlay = createDiscreteOverlay("DensityAir",getRandomType(CLIMATE_TYPES));
			Overlay newHumidOverlay = createDiscreteOverlay("Humidity",getRandomType(CLIMATE_TYPES));
			
			firstTimeDay = false;

			overlays.replace("Temperature", newTemperaOverlay);
			overlays.replace("DensityAir", newDensAirOverlay);
			overlays.replace("Humidity", newHumidOverlay);

		}
		else if(!now.equals(midnight) && !firstTimeDay)
		{
			firstTimeDay = true;
		}
		
	}

	/**
	 * Return a random element from the given array list.
	 * 
	 * @param types the ArrayList containing the types
	 * @return the randomly chosen type
	 */
	private static int[][] getRandomType(final ArrayList<int[][]> types) {
		return types.get(rand.nextInt(types.size()));
	}
	
	private static Overlay createDiscreteOverlay(String key, int[][] inputImage)
	{
		Overlay newOverlay = new Overlay(key,inputImage) {
			@Override
			public Publishable getValue(Position pos) {
				Text threshold;
				if(value[pos.getCol()][pos.getRow()]<=3355443)
				{
					threshold = new Text("very low");
				}
				else if(value[pos.getCol()][pos.getRow()]<=6710886)
				{
					threshold = new Text("low");
				}
				else if(value[pos.getCol()][pos.getRow()]<=10066329)
				{
					threshold = new Text("comfortable");
				}
				else if(value[pos.getCol()][pos.getRow()]<=13421772)
				{
					threshold = new Text("high");
				}
				else
				{
					threshold = new Text("very high");
				}
				return threshold;
			}
		};
		return newOverlay;
	}
}
