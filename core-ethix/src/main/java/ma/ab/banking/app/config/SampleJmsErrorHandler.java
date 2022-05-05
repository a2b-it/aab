/**
 * 
 */
package ma.ab.banking.app.config;

import org.springframework.stereotype.Service;
import org.springframework.util.ErrorHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * @author a.bouabidi
 *
 */
@Service
@Slf4j
public class SampleJmsErrorHandler implements ErrorHandler {

	@Override
	public void handleError(Throwable t) {
		log.warn("In default jms error handler... {}", t.getMessage());
        log.error("Error Message : {}", t.getMessage());

	}

}
