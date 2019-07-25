/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.petsitterproject.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.progmatic.petsitterproject.services.UserService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 *
 * @author progmatic
 */
@Component
public class PetSitterSuccessHandler 
  extends SimpleUrlAuthenticationSuccessHandler {
 
    private RequestCache requestCache = new HttpSessionRequestCache();
    private UserService userService;

    public PetSitterSuccessHandler(UserService userService) {
        this.userService = userService;
    }
    
    @Override
    public void onAuthenticationSuccess(
      HttpServletRequest request,
      HttpServletResponse response, 
      Authentication authentication) 
      throws ServletException, IOException {
        
        
        Map<String, Object> responseMap = new HashMap<>();
        
        responseMap.put("message", "Sikeres belépés!");
        responseMap.put("user", userService.getUserDTO());
        
        //jackson átalakítása a message-nek json formátummá
        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = response.getWriter();
        writer.append(om.writeValueAsString(responseMap));
        writer.flush();
        writer.close();
        
  
        SavedRequest savedRequest
          = requestCache.getRequest(request, response);
 
        if (savedRequest == null) {
            clearAuthenticationAttributes(request);
            return;
        }
        String targetUrlParam = getTargetUrlParameter();
        if (isAlwaysUseDefaultTargetUrl()
          || (targetUrlParam != null
          && StringUtils.hasText(request.getParameter(targetUrlParam)))) {
            requestCache.removeRequest(request, response);
            clearAuthenticationAttributes(request);
            return;
        }
 
        clearAuthenticationAttributes(request);
        
        
    }
 
    public void setRequestCache(RequestCache requestCache) {
        this.requestCache = requestCache;
    }
}
