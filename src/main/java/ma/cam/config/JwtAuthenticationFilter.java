package ma.cam.config;

import static ma.cam.commun.Constants.HEADER_STRING;
import static ma.cam.commun.Constants.TOKEN_PREFIX;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);
        String username = null;
        String authToken = null;
        String message = null;
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            authToken = header.replace(TOKEN_PREFIX,"");
            try {
                username = jwtTokenUtil.getUsernameFromToken(authToken);
            } catch (IllegalArgumentException e) {
            	message = "an error occured during getting username from token";
            	jwtTokenUtil.handleTokenException(res, HttpStatus.UNAUTHORIZED, message);
                logger.error("an error occured during getting username from token", e);
                return;
            } catch (ExpiredJwtException e) {
            	message = "the token is expired and not valid anymore "+authToken;
            	jwtTokenUtil.handleTokenException(res, HttpStatus.UNAUTHORIZED, message);
                logger.warn(message, e);
                return;
            } catch(SignatureException e){
            	message = "Authentication Failed. Username or Password not valid.";
            	jwtTokenUtil.handleTokenException(res, HttpStatus.UNAUTHORIZED, message);
                logger.error(message);
                return;
            }
        } else {
            logger.warn("couldn't find bearer string, will ignore the header");
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(authToken, userDetails)) {
            	UsernamePasswordAuthenticationToken authentication = jwtTokenUtil.getAuthentication(authToken, SecurityContextHolder.getContext().getAuthentication(), userDetails);
            	//UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                logger.info("authenticated user " + username + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else{
            	message = "Operation Failed. Token not valid.";
            	jwtTokenUtil.handleTokenException(res, HttpStatus.UNAUTHORIZED, message);
                logger.error(message);
                return;
            }
        }

        chain.doFilter(req, res);
    }
}