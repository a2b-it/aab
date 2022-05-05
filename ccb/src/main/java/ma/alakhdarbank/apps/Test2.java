/**
 * 
 */
package ma.alakhdarbank.apps;

import java.util.List;

/**
 * @author a.bouabidi
 *
 */
public class Test2 extends Test {

	void add(List<? super Double> list) {
		list.add(Math.PI);
	}

	

}
