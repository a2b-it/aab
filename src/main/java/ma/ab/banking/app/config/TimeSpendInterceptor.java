/**
 * 
 */
package ma.ab.banking.app.config;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

/**
 * @author a.bouabidi
 *
 */
@Intercepts(
// Intercept the executor.query method, not the cache
@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class,
		ResultHandler.class }))
//@ Signature (type = StatementHandler.class, method = "prepare", args = {Connection.class}))		
@Slf4j
public class TimeSpendInterceptor implements Interceptor {
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		// The parameters of invocation come from the intercepting method. Here, the
		// first parameter of query method is statement object
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		String id = mappedStatement.getId();
		//Connection connection = (Connection) invocation.getArgs () [0];
		//connection.
		Object proceed = null;
		try {
			proceed = invocation.proceed();
		} finally {
			stopWatch.stop();
			log.info("sqlid:" + id + "execution time is:" + stopWatch.getTotalTimeSeconds() + "Ms");			
		}
		return proceed;
	}

	@Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
    }
}
