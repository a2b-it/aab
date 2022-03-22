/**
 * 
 */
package ma.ab.banking.app.services;

import ma.ab.banking.app.rest.dto.TranSetting;

/**
 * @author a.bouabidi
 *
 */
public interface ExecOneLegXapi {
	
	
	
	
	int executeTran (String ref);
	
	int getOrder ();
	
	void setSetting (TranSetting setting);
	
	TranSetting getSetting ();
	
	boolean isInit();
	 

}

