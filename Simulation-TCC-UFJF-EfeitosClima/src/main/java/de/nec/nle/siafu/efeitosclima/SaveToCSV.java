package de.nec.nle.siafu.efeitosclima;

import static de.nec.nle.siafu.efeitosclima.Constants.Fields.AGE;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.GENDER;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.HUMIDITY;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.HUMIDITY_RESISTANCE;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.IMC;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.LEFTHOME;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.LEFTWORK;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.REACHHOME;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.REACHWORK;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.SPEED;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.TEMPERATURE;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.TEMPERATURE_RESISTANCE;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.DATE;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.DENSITYAIR;
import static de.nec.nle.siafu.efeitosclima.Constants.Fields.DENSITYAIR_RESISTANCE;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import de.nec.nle.siafu.exceptions.PositionOutOfTheMapException;
import de.nec.nle.siafu.exceptions.UnknownContextException;
import de.nec.nle.siafu.model.Agent;
import de.nec.nle.siafu.model.Position;
import de.nec.nle.siafu.model.World;
import de.nec.nle.siafu.types.EasyTime;

public class SaveToCSV {
	
	public static final SaveToCSV instance = new SaveToCSV();
	
	public SaveToCSV()
	{
	    try {
	    	
	        File overallClimateFile = new File("E:\\faculdade\\Siafu-master2\\SiafuContext\\climate_details2.csv");
	        if (overallClimateFile.createNewFile()) {
	          System.out.println("File created: " + overallClimateFile.getName());
	        } else {
	          System.out.println("File already exists.");
	        }

	        File fromToFile = new File("E:\\faculdade\\Siafu-master2\\SiafuContext\\from_to2.csv");
	        if (fromToFile.createNewFile()) {
	          System.out.println("File created: " + fromToFile.getName());
	        } else {
	          System.out.println("File already exists.");
	        }

	        File agentsFile = new File("E:\\faculdade\\Siafu-master2\\SiafuContext\\agents2.csv");
	        if (agentsFile.createNewFile()) {
	          System.out.println("File created: " + agentsFile.getName());
	        } else {
	          System.out.println("File already exists.");
	        }
	        
	      } catch (IOException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	      }
	    
	}
	
	public static SaveToCSV getInstance()
	{
		return instance;
	}
	
	public void saveAgentDetails(Agent agent)
	{
		try {
		FileWriter myWriter = new FileWriter("E:\\faculdade\\Siafu-master2\\SiafuContext\\agents2.csv",true);
		
		String output = agent.getName().toString()+"2,"
		+agent.get(AGE).toString()+","
		+agent.get(GENDER).toString()+","
		+agent.get(IMC).toString()+","
		+agent.get(DENSITYAIR_RESISTANCE).toString()+","
		+agent.get(HUMIDITY_RESISTANCE).toString()+","
		+agent.get(TEMPERATURE_RESISTANCE)+"\n";
		  
		myWriter.write(output);
		myWriter.close();
	      
		System.out.println("Successfully wrote to agents2.csv.");
		
	    } catch (IOException|PositionOutOfTheMapException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
		}
	}
	
	public void updateFile(Agent agent, String hour,World world)
	{
		try {
		      FileWriter myWriter = new FileWriter("E:\\faculdade\\Siafu-master2\\SiafuContext\\climate_details2.csv",true);
		      
		      
		      
		      String output = agent.getName().toString()+"2,"
		    		  + agent.get(AGE).toString()+","
		    		  + hour+"##"
		    		  + agent.get(DATE).toString()+","
		    		  + agent.get(GENDER).toString()+","
		    		  + agent.get(IMC).toString()+","
		    		  + agent.get(SPEED).toString()+","
		    		  + world.getOverlays().get(DENSITYAIR)
		    		  .getValue(new Position(0, 0))+","
		    		  
		    		  + world.getOverlays().get(HUMIDITY)
		    		  .getValue(new Position(0, 0))+","
		    		  
		    		  + world.getOverlays().get(TEMPERATURE)
		    		  .getValue(new Position(0, 0))+"\n";
		      
		      myWriter.write(output);
		      myWriter.close();
		      
		      System.out.println("Successfully wrote to climate_details2.csv.");
		    } catch (IOException|PositionOutOfTheMapException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
			} 
	}
	
	public void updateFileMixed(Agent agent, String hour)
	{
		try {
		      FileWriter myWriter = new FileWriter("E:\\faculdade\\Siafu-master2\\SiafuContext\\climate_details2.csv",true);
		      
		      String output = agent.getName().toString()+"2_Mixed,"
		    		  + agent.get(AGE).toString()+","
		    		  + hour+"##"
		    		  + agent.get(DATE).toString()+","
		    		  + agent.get(GENDER).toString()+","
		    		  + agent.get(IMC).toString()+","
		    		  + agent.get(SPEED).toString()+","
		    		  + agent.getContext(DENSITYAIR).toString()+","
		    		  + agent.getContext(HUMIDITY).toString()+","
		    		  + agent.getContext(TEMPERATURE).toString()+"\n";
		      
		      myWriter.write(output);
		      myWriter.close();
		      
		      System.out.println("Successfully wrote to climate_details2.csv.");
		    } catch (IOException|UnknownContextException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
			}
	}
	
	public void updateFromTo(Agent agent)
	{
		try {
		      FileWriter myWriter = new FileWriter("E:\\faculdade\\Siafu-master2\\SiafuContext\\from_to2.csv",true);

		      EasyTime leftHome = (EasyTime)agent.get(LEFTHOME);
		      EasyTime reachWork = (EasyTime)agent.get(REACHWORK);
		      
		      EasyTime leftWork = (EasyTime)agent.get(LEFTWORK);
		      EasyTime reachHome = (EasyTime)agent.get(REACHHOME);

		      int secondsHomeToWork= reachWork.getTimeInSeconds()-leftHome.getTimeInSeconds();
		      int secondsWorkToHome= reachHome.getTimeInSeconds()-leftWork.getTimeInSeconds();
		      
		      String output = agent.getName().toString()+"2,"
		    		  + agent.get(DATE).toString()+","
		    		  + secondsHomeToWork+","
		    		  + secondsWorkToHome+"\n";
		      
		      myWriter.write(output);
		      myWriter.close();
		      
		      System.out.println("Successfully wrote to from_to2.csv.");
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
			}
	}

	public void updateFromToMixedClimate(Agent agent)
	{
		try {
		      FileWriter myWriter = new FileWriter("E:\\faculdade\\Siafu-master2\\SiafuContext\\from_to2.csv",true);

		      EasyTime leftHome = (EasyTime)agent.get(LEFTHOME);
		      EasyTime reachWork = (EasyTime)agent.get(REACHWORK);
		      
		      EasyTime leftWork = (EasyTime)agent.get(LEFTWORK);
		      EasyTime reachHome = (EasyTime)agent.get(REACHHOME);

		      int secondsHomeToWork= reachWork.getTimeInSeconds()-leftHome.getTimeInSeconds();
		      int secondsWorkToHome= reachHome.getTimeInSeconds()-leftWork.getTimeInSeconds();
		      
		      String output = agent.getName().toString()+"2_Mixed,"
		    		  + agent.get(DATE).toString()+","
		    		  + secondsHomeToWork+","
		    		  + secondsWorkToHome+"\n";
		      
		      myWriter.write(output);
		      myWriter.close();
		      
		      System.out.println("Successfully wrote to from_to2.csv.");
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
			}
	}
}
