package com.teraenergy.illegalparking.controller.report;

import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.controller.ExtendsController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@Controller
public class ReportController extends ExtendsController {

    private String subTitle = "신고";
    
    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public RedirectView report() {
        return new RedirectView("/report/reportList");
    }

    @RequestMapping(value = "/report/reportList", method = RequestMethod.GET)
    public ModelAndView reportList(HttpServletRequest request) {

        HashMap<String, Object> param = _getParam(request);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(getPath("/reportList"));
        modelAndView.addObject("mainTitle", mainTitle);
        modelAndView.addObject("subTitle", subTitle);

        // 임시
        int total = 100;

        int rowNumber = 10;
        modelAndView.addObject("rowNumber", rowNumber);

        int pageNumber = 1;
        String strPageNumber = (String) param.get("pageNumber");
        if ( strPageNumber != null && strPageNumber.length() > 0) {
            pageNumber = Integer.parseInt(strPageNumber);
        }

        int begin = 1;
        int end = 5;

        boolean isBeginOver = false;
        boolean isEndOver = false;

        modelAndView.addObject("pageNumber", pageNumber);
        modelAndView.addObject("begin", begin);
        modelAndView.addObject("end", end);
        modelAndView.addObject("isBeginOver", isBeginOver);
        modelAndView.addObject("isEndOver", isEndOver);
        return modelAndView;
    }

    private HashMap<String, Object> _getParam(HttpServletRequest request) {
        HashMap<String, Object> parameterMap = Maps.newHashMap();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            String value = request.getParameter(name);
            parameterMap.put(name, value);
        }
        return parameterMap;
    }

}
