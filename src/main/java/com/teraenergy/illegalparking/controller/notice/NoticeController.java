package com.teraenergy.illegalparking.controller.notice;

import com.teraenergy.illegalparking.controller.ExtendsController;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.entity.notice.domain.Notice;
import com.teraenergy.illegalparking.model.entity.notice.enums.NoticeFilterColumn;
import com.teraenergy.illegalparking.model.entity.notice.service.NoticeService;
import com.teraenergy.illegalparking.util.CHashMap;
import com.teraenergy.illegalparking.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

/**
 * Date : 2022-10-17
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Controller
public class NoticeController extends ExtendsController {

    private final NoticeService noticeService;

    private String subTitle = "공지사항";

    @GetMapping("/notice")
    public RedirectView notice() {
        return new RedirectView("/notice/noticeList");
    }

    @GetMapping("/notice/noticeList")
    public String noticeList(Model model, HttpServletRequest request) throws TeraException {

        RequestUtil requestUtil = new RequestUtil(request);
        requestUtil.setParameterToModel(model);
        CHashMap paramMap = requestUtil.getParameterMap();

        Integer pageNumber = paramMap.getAsInt("pageNumber");
        if (pageNumber == null) {
            pageNumber = 1;
            model.addAttribute("pageNumber", pageNumber);
        }

        String filterColumnStr = paramMap.getAsString("filterColumn");
        NoticeFilterColumn filterColumn;
        if(filterColumnStr == null) {
            filterColumn = NoticeFilterColumn.SUBJECT;
        } else  {
            filterColumn = NoticeFilterColumn.valueOf(filterColumnStr);
        }

        String search = paramMap.getAsString("searchStr");
        if (search == null) {
            search = "";
        }

        Integer pageSize = paramMap.getAsInt("pageSize");
        if ( pageSize == null) {
            pageSize = 10;
        }


        Page<Notice> pages = noticeService.gets(pageNumber, pageSize, filterColumn, search);

        boolean isBeginOver = false;
        boolean isEndOver = false;

        int totalPages = pages.getTotalPages();

        if (totalPages > 3 && ( totalPages - pageNumber ) > 2 ) {
            isEndOver = true;
        }

        if (totalPages > 3 && pageNumber > 1) {
            isBeginOver = true;
        }

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("isBeginOver", isBeginOver);
        model.addAttribute("isEndOver", isEndOver);
        model.addAttribute("notices", pages.getContent());
        model.addAttribute("subTitle", subTitle);

        return getPath("/noticeList");
    }
}
