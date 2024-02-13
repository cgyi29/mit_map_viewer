package com.tmap.mit.map_viewer.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    //@ExceptionHandler(Exception.class)
    public Object handleException(Exception ex, WebRequest request) {
        log.error(String.valueOf(ex));
        if (isRestApiRequest(request)) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error: " + ex.getMessage());
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", "Internal server error");
        modelAndView.addObject("exception", ex.getMessage());
        modelAndView.setViewName("error"); // 'error' 뷰 페이지
        return modelAndView;

    }

    private boolean isRestApiRequest(WebRequest request) {
        String acceptHeader = request.getHeader("Accept");
        if (acceptHeader != null) {
            return acceptHeader.contains(MediaType.APPLICATION_JSON_VALUE)
                    || acceptHeader.contains(MediaType.APPLICATION_XML_VALUE);
        }
        return false;
    }

}
