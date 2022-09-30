package com.teraenergy.illegalparking.controller.report;

import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.controller.ExtendsController;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.entity.parking.domain.Parking;
import com.teraenergy.illegalparking.model.entity.report.domain.Report;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportFilterColumn;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportOrderColumn;
import com.teraenergy.illegalparking.model.entity.report.service.ReportService;
import com.teraenergy.illegalparking.util.CHashMap;
import com.teraenergy.illegalparking.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

@RequiredArgsConstructor
@Controller
public class ReportController extends ExtendsController {


    private final ReportService reportService;

    private String subTitle = "신고";

    @GetMapping(value = "/report")
    public RedirectView report() {
        return new RedirectView("/report/reportList");
    }

    @GetMapping(value = "/report/reportList")
    public String reportList(Model model, HttpServletRequest request) throws TeraException {
        RequestUtil requestUtil = new RequestUtil(request);
        requestUtil.setParameterToModel(model);
        CHashMap paramMap = requestUtil.getParameterMap();

        Integer pageNumber = paramMap.getAsInt("pageNumber");
        if (pageNumber == null) {
            pageNumber = 1;
            model.addAttribute("pageNumber", pageNumber);
        }

        String orderColumnStr = paramMap.getAsString("orderColumn");
        ReportOrderColumn orderColumn;
        if (orderColumnStr == null) {
            orderColumn = ReportOrderColumn.REPORT_SEQ;
        } else {
            orderColumn = ReportOrderColumn.valueOf(orderColumnStr);
        }

        String filterColumnStr = paramMap.getAsString("filterColumn");
        ReportFilterColumn filterColumn;
        if (filterColumnStr == null) {
            filterColumn = ReportFilterColumn.ADDR;
        } else {
            filterColumn = ReportFilterColumn.valueOf(filterColumnStr);
        }

        String search = "";
        String searchStr = paramMap.getAsString("searchStr");
        String searchStr2 = paramMap.getAsString("searchStr2");
        if (filterColumn.equals(ReportFilterColumn.RESULT)) {
            search = searchStr2;
        } else {
            if (searchStr == null) {
                searchStr = "";
            }
            search = searchStr.trim();
        }

        String orderDirectionStr = paramMap.getAsString("orderDirection");
        Sort.Direction direction;
        if (orderDirectionStr == null) {
            direction = Sort.Direction.ASC;
        } else {
            direction = Sort.Direction.valueOf(orderDirectionStr);
        }

        Integer pageSize = paramMap.getAsInt("pageSize");
        if (pageSize == null) {
            pageSize = 10;
            model.addAttribute("pageSize", pageSize);
        }

        Page<Report> pages = reportService.gets(pageNumber, pageSize, filterColumn, search, orderColumn, direction);

        boolean isBeginOver = false;
        boolean isEndOver = false;

        int totalPages = pages.getTotalPages();

        if (totalPages > 3 && (totalPages - pageNumber) > 2) {
            isEndOver = true;
        }

        if (totalPages > 3 && pageNumber > 1) {
            isBeginOver = true;
        }

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("isBeginOver", isBeginOver);
        model.addAttribute("isEndOver", isEndOver);
        model.addAttribute("reports", pages.getContent());
        model.addAttribute("mainTitle", mainTitle);
        model.addAttribute("subTitle", subTitle);
        return getPath("/reportList");
    }

}
