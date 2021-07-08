package com.api.hsm;

import hec.heclib.dss.DSSErrorMessage;
import hec.heclib.dss.HecDataManager;
import hec.heclib.dss.HecTimeSeries;
import hec.heclib.util.HecTime;
import hec.io.TimeSeriesContainer;

public class MAin {

	
	
	
	public static void main(String[] args) {
		//System.load("F:\\Workspaces\\TestHEC\\lib\\javaHeclib.dll");
		int version = 3;
		
		HecDataManager dataManager = new HecDataManager();
		int status = HecDataManager.setLogFile("F:/Workspaces/apigeo/TestHEC/log/MyProgramName"+version);
		if (status != 0) System.out.println("Error opening log file");
		HecDataManager.setDefaultDSSFileName("F:/Workspaces/apigeo/TestHEC/MyDSS_file.dss");
		
		//HecDataManager.set
		//TimeSeriesContainer ts = new TimeSeriesContainer();
		HecTimeSeries dssTimeSeriesWrite = new HecTimeSeries();		    		   
	    status = dssTimeSeriesWrite.setDSSFileName("F:/Workspaces/apigeo/TestHEC/MyDSS_file"+version+".dss");			
	    if (status < 0) {
	    	logStatus(dataManager);
	    }
		try {
			TimeSeriesContainer tsc,tsc2 = null;													
			String path = "/MOROCCO FLOOD HAZARD/BGE_EL_MELLAH/ET/01Nov1974/1DAY/EL MELLAH HMS/";						
			HecTime start = new HecTime("10Nov1974","2400");
			/*tsc = createTimeSeriesContainer(path, new double[] {0.1f,2.1f,1.1f,4.1f,3.1f,6.1f,5.1f,8.1f,7.1f,9.1f} , start );			
			status = dssTimeSeriesWrite.write(tsc);
			dssTimeSeriesWrite.done();
			if (status < 0) {
		    	logStatus(dataManager);
		    }*/					
			tsc2 = createTimeSeriesContainer(path, new double[] {0.2f,2.2f,1.2f,4.2f,3.2f,6.2f,5.2f,8.2f,7.2f,9.2f},  start  );
			status = dssTimeSeriesWrite.write(tsc2);
			if (status < 0) {
		    	logStatus(dataManager);
		    }
			dssTimeSeriesWrite.done();
			
			//
			dssTimeSeriesWrite.close();		    
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		HecDataManager.closeAllFiles();
		HecDataManager.closeLogFile();
		
	}
	
	
	private static void logStatus(HecDataManager dataManager){
		
		// This means the open failed; could be lots of reasons, such
		// as a permission issue, a bad DSS file name, etc. You cannot continue.
		DSSErrorMessage error = dataManager.getLastError();
		// return proecessError(error); It is recommended that you write a function to handle errors
		error.printMessage();
		return;
			
	}
	private static TimeSeriesContainer createTimeSeriesContainer( String path, double[] flows, HecTime start ) {
		//if (tsc == null) {
		TimeSeriesContainer	tsc = new TimeSeriesContainer();			
		tsc.fullName = path;
		tsc.units ="mm";
	    tsc.type = "PER-CUM";			
		tsc.interval = 60*24;
		//}		
		tsc.setStartTime(start);
		//double[] flows1 = new double[] {0.1f,2.1f,1.1f,4.1f,3.1f,6.1f,5.1f,8.1f,7.1f,9.1f};		
		int[] times = new int[flows.length] ;			
		for (int j=0 ; j<flows.length; j++) {
			 times[j] = start.value();
		     start.add(tsc.interval);
		 }
		tsc.values = flows;		
	    tsc.numberValues = tsc.numberValues+flows.length;
	    
	    return tsc;
		
	}
}

