package com.teraenergy.illegalparking.controller.notice;

import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.exception.enums.TeraExceptionCode;
import com.teraenergy.illegalparking.model.entity.notice.domain.Notice;
import com.teraenergy.illegalparking.model.entity.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Date : 2022-10-17
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Controller
public class NoticeAPI {

    private final NoticeService noticeService;

    @PostMapping("/notice/set")
    public Object set(@RequestBody String body) throws TeraException {
        try {
            Notice notice = new Notice();


            return "";
        } catch (Exception e) {
            throw new TeraException(TeraExceptionCode.UNKNOWN);
        }

    }

}
