package com.teraenergy.illegalparking.controller.report;

import com.teraenergy.illegalparking.controller.ExtendsController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

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
    
    @RequestMapping("/report")
    public RedirectView report() {
        return new RedirectView("/report/reportList");
    }

    @RequestMapping("/report/reportList")
    public ModelAndView reportList(HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(getPath("/reportList"));
        modelAndView.addObject("mainTitle", mainTitle);
        modelAndView.addObject("subTitle", subTitle);


        // 전체 개수 /

        int rowNumber = 10;
        modelAndView.addObject("rowNumber", rowNumber);

        int pageNumber = 1;
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

}
