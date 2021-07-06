package com.api.hsm;

import hec.heclib.dss.DSSErrorMessage;
import hec.heclib.dss.HecDataManager;
import hec.heclib.dss.HecTimeSeries;
import hec.heclib.util.HecTime;
import hec.io.TimeSeriesContainer;

public class MAin {

	
	
	
	public static void main(String[] args) {
		//System.load("F:\\Workspaces\\TestHEC\\lib\\javaHeclib.dll");
		int version = 8;
		
		HecDataManager dataManager = new HecDataManager();
		int status = HecDataManager.setLogFile("F:/Workspaces/TestHEC/log/MyProgramName"+version);
		if (status != 0) System.out.println("Error opening log file");
		HecDataManager.setDefaultDSSFileName("F:/Workspaces/TestHEC/MyDSS_file.dss");
		
		//HecDataManager.set
		//TimeSeriesContainer ts = new TimeSeriesContainer();
		
		try {
			TimeSeriesContainer tsc,tsc2 = null;													
			String path = "/MOROCCO FLOOD HAZARD/BGE_EL_MELLAH/ET/01JAN1977/1DAY/EL MELLAH HMS/";			
			//tsc.interval = 60;
			HecTime start = new HecTime("01Nov1974","0800");
			tsc = createTimeSeriesContainer(path, new double[] {0.1f,2.1f,1.1f,4.1f,3.1f,6.1f,5.1f,8.1f,7.1f,9.1f} , start );
			//start = tsc.getEndTime();
			//tsc2 = createTimeSeriesContainer(path, new double[] {0.2f,2.2f,1.2f,4.2f,3.2f,6.2f,5.2f,8.2f,7.2f,9.2f},  start  );
			//
		    
		    //
		    HecTimeSeries dssTimeSeriesWrite = new HecTimeSeries();		    		   
		    status = dssTimeSeriesWrite.setDSSFileName("F:/Workspaces/TestHEC/MyDSS_file"+version+".dss");			
			if (status < 0) {
			// This means the open failed; could be lots of reasons, such
			// as a permission issue, a bad DSS file name, etc. You cannot continue.
			DSSErrorMessage error = dataManager.getLastError();
			// return proecessError(error); It is recommended that you write a function to handle errors
			error.printMessage();
			return;
			};
			dssTimeSeriesWrite.write(tsc);
			//dssTimeSeriesWrite.write(tsc2);
			dssTimeSeriesWrite.done();
			
			//
		   
		    
		    		    
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		HecDataManager.closeAllFiles();
		HecDataManager.closeLogFile();
		
	}
	
	private static TimeSeriesContainer createTimeSeriesContainer(String path, double[] flows, HecTime start ) {
		TimeSeriesContainer tsc = new TimeSeriesContainer();			
		tsc.fullName = "/MOROCCO FLOOD HAZARD/BGE_EL_MELLAH/ET/01JAN1977/1DAY/EL MELLAH HMS/";		
		tsc.setStartTime(start);
		//double[] flows1 = new double[] {0.1f,2.1f,1.1f,4.1f,3.1f,6.1f,5.1f,8.1f,7.1f,9.1f};		
		/*int[] times = new int[flows.length] ;			
		for (int j=0 ; j<flows.length; j++) {
			 times[j] = start.value();
		     start.add(tsc.interval);
		 }*/
		//tsc.times = times;
	    //tsc.values = flows;
		tsc.setValues(flows);		
	    //tsc.numberValues = flows.length;
	    tsc.units ="mm";
	    tsc.type = "PER-CUM";
	    return tsc;
		
	}
}

