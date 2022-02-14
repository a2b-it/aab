package ma.akhdarbank.apps.clients;

import ma.akhdarbank.apps.excp.DWJCallException;

public interface ApiAuthClient {
	
	public String auth() throws DWJCallException;

}
