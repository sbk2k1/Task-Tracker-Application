package tech.sbk2k1.tasktracker.JWT;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tech.sbk2k1.tasktracker.model.ProjectDTO;
import tech.sbk2k1.tasktracker.services.Project.ProjectServices;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

  private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);
  @Autowired
  private JWTHelper jwtHelper;

  @Autowired
  private ProjectServices projectServices;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    // Authorization
    String requestHeader = request.getHeader("Authorization");
    logger.info(" Header :  {}", requestHeader);
    String username = null;
    String token = null;
    if (requestHeader != null && requestHeader.startsWith("Bearer")) {
      // Bearer Authentication is present
      token = requestHeader.substring(7);
      try {

        username = this.jwtHelper.getUsernameFromToken(token);

      } catch (IllegalArgumentException e) {
        logger.info("Illegal Argument while fetching the username !!");
        e.printStackTrace();
      } catch (ExpiredJwtException e) {
        logger.info("Given jwt token is expired !!");
        e.printStackTrace();
      } catch (MalformedJwtException e) {
        logger.info("Some changed has done in token !! Invalid Token");
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();

      }

    } else {
      logger.info("Invalid Header Value !! ");
    }

    // usernmane is not null and authentication is null
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

      // fetch user detail from username
      ProjectDTO project = this.projectServices.getProjectByUsername(username);
      Boolean validateToken = this.jwtHelper.validateToken(token, project);
      if (validateToken) {

        // set the authentication
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(project, null,
            null);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

      } else {
        logger.info("Validation fails !!");
      }

    }

    filterChain.doFilter(request, response);

  }
}