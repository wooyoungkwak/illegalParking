package com.teraenergy.illegalparking.controller.calculate;

import com.teraenergy.illegalparking.controller.ExtendsController;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.entity.calculate.domain.Calculate;
import com.teraenergy.illegalparking.model.entity.product.domain.Product;
import com.teraenergy.illegalparking.model.entity.calculate.enums.CalculateFilterColumn;
import com.teraenergy.illegalparking.model.entity.calculate.enums.CalculateOrderColumn;
import com.teraenergy.illegalparking.model.entity.product.enums.ProductFilterColumn;
import com.teraenergy.illegalparking.model.entity.product.enums.ProductOrderColumn;
import com.teraenergy.illegalparking.model.entity.calculate.service.CalculateService;
import com.teraenergy.illegalparking.model.entity.product.service.ProductService;
import com.teraenergy.illegalparking.util.CHashMap;
import com.teraenergy.illegalparking.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Controller
public class CalculateController extends ExtendsController {

    private final CalculateService calculateService;

    private final ProductService productService;

    private String subTitle = "결재";

    @RequestMapping("/calculate")
    public RedirectView calculate() {
        return new RedirectView("/calculate/calculateList");
    }

    @GetMapping("/calculate/calculateList")
    public String calculateList(Model model, HttpServletRequest request) throws TeraException {
        RequestUtil requestUtil = new RequestUtil(request);
        requestUtil.setParameterToModel(model);
        CHashMap parameterMap = requestUtil.getParameterMap();

        Integer pageNumber = parameterMap.getAsInt("pageNumber");
        if (pageNumber == null) {
            pageNumber = 1;
            model.addAttribute("pageNumber", pageNumber);
        }

        String orderColumnStr = parameterMap.getAsString("orderColumn");
        CalculateOrderColumn orderColumn;
        if (orderColumnStr == null) {
            orderColumn = CalculateOrderColumn.calculateSeq;
        } else {
            orderColumn = CalculateOrderColumn.valueOf(orderColumnStr);
        }

        String filterColumnStr = parameterMap.getAsString("filterColumn");
        CalculateFilterColumn filterColumn;
        if (filterColumnStr == null) {
            filterColumn = CalculateFilterColumn.user;
        } else {
            filterColumn = CalculateFilterColumn.valueOf(filterColumnStr);
        }

        String searchStr = parameterMap.getAsString("searchStr");
        if (searchStr == null) {
            searchStr = "";
        }

        String orderDirectionStr = parameterMap.getAsString("orderDirection");
        Sort.Direction direction;
        if (orderDirectionStr == null) {
            direction = Sort.Direction.ASC;
        } else {
            direction = Sort.Direction.valueOf(orderDirectionStr);
        }


        Integer pageSize = parameterMap.getAsInt("pageSize");
        if (pageSize == null) {
            pageSize = 10;
        }

        Page<Calculate> pages = calculateService.gets(pageNumber, pageSize, filterColumn, searchStr, orderColumn, direction);

        boolean isBeginOver = false;
        boolean isEndOver = false;

        int totalPages = pages.getTotalPages();

        if (totalPages > 3 && (totalPages - pageNumber) > 2) {
            isEndOver = true;
        }

        if (totalPages > 3 && pageNumber > 1) {
            isBeginOver = true;
        }

        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("isBeginOver", isBeginOver);
        model.addAttribute("isEndOver", isEndOver);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("calculates", pages.getContent());
        model.addAttribute("subTitle", subTitle);
        return getPath("/calculateList");
    }

    @GetMapping("/calculate/productList")
    public String productList(Model model, HttpServletRequest request) throws TeraException {
        RequestUtil requestUtil = new RequestUtil(request);
        requestUtil.setParameterToModel(model);
        CHashMap parameterMap = requestUtil.getParameterMap();

        Integer pageNumber = parameterMap.getAsInt("pageNumber");
        if (pageNumber == null) {
            pageNumber = 1;
            model.addAttribute("pageNumber", pageNumber);
        }

        String orderColumnStr = parameterMap.getAsString("orderColumn");
        ProductOrderColumn orderColumn;
        if (orderColumnStr == null) {
            orderColumn = ProductOrderColumn.productSeq;
        } else {
            orderColumn = ProductOrderColumn.valueOf(orderColumnStr);
        }

        String filterColumnStr = parameterMap.getAsString("filterColumn");
        ProductFilterColumn filterColumn;
        if (filterColumnStr == null) {
            filterColumn = ProductFilterColumn.name;
        } else {
            filterColumn = ProductFilterColumn.valueOf(filterColumnStr);
        }

        String searchStr = parameterMap.getAsString("searchStr");
        String searchStr2 = parameterMap.getAsString("searchStr2");
        String search;
        if (filterColumn.equals(ProductFilterColumn.brand)) {
            search = searchStr2;
        } else {
            if (searchStr == null ) {
                search = "";
            } else {
                search = searchStr.trim();
            }
        }

        String orderDirectionStr = parameterMap.getAsString("orderDirection");
        Sort.Direction direction;
        if (orderDirectionStr == null) {
            direction = Sort.Direction.ASC;
        } else {
            direction = Sort.Direction.valueOf(orderDirectionStr);
        }

        Integer pageSize = parameterMap.getAsInt("pageSize");
        if (pageSize == null) {
            pageSize = 10;
        }

        Page<Product> pages = productService.gets(pageNumber, pageSize, filterColumn, search, orderColumn, direction);

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
        model.addAttribute("products", pages.getContent());
        model.addAttribute("subTitle", subTitle);
        return getPath("/productList");
    }

    @GetMapping("/calculate/productAdd")
    public String productAdd(Model model, HttpServletRequest request) throws TeraException {
        RequestUtil requestUtil = new RequestUtil(request);
        requestUtil.setParameterToModel(model);

        model.addAttribute("subTitle", subTitle);
        return getPath("/productAdd");
    }

}
