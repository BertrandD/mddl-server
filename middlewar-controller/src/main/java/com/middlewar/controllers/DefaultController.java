package com.middlewar.controllers;

import com.middlewar.api.util.response.MetaHolder;
import com.middlewar.api.util.response.Response;
import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.model.Account;
import com.middlewar.core.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author LEBOC Philippe
 */
@RestController
@RequestMapping(produces = "application/json")
public class DefaultController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping(value = "/", method = GET)
    public Response index() {
        return new Response();
    }

    @RequestMapping(value = "/resetworld", method = GET)
    public Response resetWorld(@AuthenticationPrincipal Account pAccount) {
        return new Response();
    }

    @RequestMapping(value = "/reload", method = GET)
    public Response reload() {
        //BuildingData.getInstance().load();
        ItemData.getInstance().load();
        return new Response();
    }

    @RequestMapping(value = "/time", method = GET)
    public Response getTime() {
        return new Response(new MetaHolder("time", TimeUtil.getCurrentTime()));
    }

    @RequestMapping(value = ERROR_PATH)
    public Response error(HttpServletRequest request) {
        final RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return new Response(errorAttributes.getErrorAttributes(requestAttributes, false).get("message").toString());
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
