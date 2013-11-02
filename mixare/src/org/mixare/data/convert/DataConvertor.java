/*
 * Copyright (C) 2012- Peer internet solutions & Finalist IT Group
 * 
 * This file is part of mixare.
 * 
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details. 
 * 
 * You should have received a copy of the GNU General Public License along with 
 * this program. If not, see <http://www.gnu.org/licenses/>
 */
package org.mixare.data.convert;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.mixare.data.DataSource;
import org.mixare.lib.marker.Marker;
import org.mixare.lib.reality.PhysicalPlace;

/**
 * This class is responsible for converting raw data to marker data
 * The class will first check which processor is needed before it handles the data
 * After that it will convert the data to the format the processor wants. I.E. JSON / XML
 * @author A. Egal
 * @author Xu Duangui - edited the type of data processors for MIXARE. 
 */
public class DataConvertor {
	//------------------------
	//
	// VARIABLES USED FOR DataConvertor
	//
	//------------------------
	
	//------------------------
	// PRIAVTE VARIABLES
	//------------------------
	// Might not need to use the list anymore as there is only one processors now. 
	/** List to store the dataProcessors */
	private List<DataProcessor> dataProcessors = new ArrayList<DataProcessor>();
	
	/** An instance of data convertor  */
	private static DataConvertor instance;
	
	/**
	 * This method will get the data converter instance and 
	 * add the data processors to the list.
	 * 
	 * @return the instance of the data convertor 
	 * */
	public static DataConvertor getInstance(){
		if(instance == null){
			instance = new DataConvertor();
			instance.addDefaultDataProcessors();
		}
		return instance;
	}
	
	/**
	 * This method will clear the data converter list. And 
	 * add the data processors to the list.
	 * */
	public void clearDataProcessors() {
		dataProcessors.clear();
		addDefaultDataProcessors();
	}
	/**
	 * This method add the data processors to the list.
	 * */
	public void addDataProcessor(DataProcessor dataProcessor){
		dataProcessors.add(dataProcessor);
	}
	/**
	 * This method remove the data processors to the list.
	 * */
	public void removeDataProcessor(DataProcessor dataProcessor){
		dataProcessors.remove(dataProcessor);
	}
	
	/**
	 * This method load the data from the datasource. This will call the 
	 * MixareDataProcessor class to create the markers from the data source. And
	 * add them in to a marker list. 
	 * 
	 * @param url - the url of the data source 
	 * @param rawResult - the result of the source 
	 * @param ds -  the instance of data source
	 * 
	 * @return a list of markers.
	 * 
	 * @exception JSONException will be thrown if there is an error in reading the source.
	 * */
	public List<Marker> load(String url, String rawResult, DataSource ds){
		DataProcessor dataProcessor = searchForMatchingDataProcessors(url, rawResult, ds.getType());
		if(dataProcessor == null){
			dataProcessor = new MixareDataProcessor(); //using this as default if nothing is found.
		}
		try {
			return dataProcessor.load(rawResult, ds.getTaskId(), ds.getColor());
		} catch (JSONException e) {
		}
		return null;
	}
	/**
	 * This method search for the data processors that matches the current data source.
	 * 
	 * @param url - the url of the data source 
	 * @param rawResult - the result of the source 
	 * @param type -  the TYPE of data source
	 * 
	 * @return the data processor of the current data source
	 * */
	private DataProcessor searchForMatchingDataProcessors(String url, String rawResult, DataSource.TYPE type){
		for(DataProcessor dp : dataProcessors){
			if(dp.matchesRequiredType(type.name())){
				//checking if url matches any dataprocessor identifiers
				for(String urlIdentifier : dp.getUrlMatch()){
					if(url.toLowerCase().contains(urlIdentifier.toLowerCase())){
						return dp;
					}
				}
				//checking if data matches any dataprocessor identifiers
				for(String dataIdentifier : dp.getDataMatch()){
					if(rawResult.contains(dataIdentifier)){
						return dp;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * This method add the default processors for the types of data source used. 
	 * */
	private void addDefaultDataProcessors(){
		// Currently there is only one processor for WIFIN. 
		dataProcessors.add(new MixareDataProcessor());
		
	}
}
