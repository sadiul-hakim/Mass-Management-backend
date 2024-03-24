package org.massmanagement.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class ResponseUtility {
    public static void commitResponse(HttpServletResponse response, Map<String,String> map, int status)  {

        try{
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(status);
            new ObjectMapper().writeValue(response.getOutputStream(), map);
        }catch (Exception ex){
            log.error("Error Occurred in ResponseUtility. Cause {}",ex.getMessage());
        }
    }
}