package com.teraenergy.illegalparking.controller.mobile;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.exception.enums.TeraExceptionCode;
import com.teraenergy.illegalparking.model.dto.user.domain.UserDto;
import com.teraenergy.illegalparking.model.dto.user.service.UserDtoService;
import com.teraenergy.illegalparking.model.entity.calculate.domain.Calculate;
import com.teraenergy.illegalparking.model.entity.calculate.service.CalculateService;
import com.teraenergy.illegalparking.model.entity.comment.domain.Comment;
import com.teraenergy.illegalparking.model.entity.comment.service.CommentService;
import com.teraenergy.illegalparking.model.entity.illegalEvent.domain.IllegalEvent;
import com.teraenergy.illegalparking.model.entity.illegalEvent.service.IllegalEventService;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneMapperService;
import com.teraenergy.illegalparking.model.entity.lawdong.domain.LawDong;
import com.teraenergy.illegalparking.model.entity.lawdong.service.LawDongService;
import com.teraenergy.illegalparking.model.entity.notice.domain.Notice;
import com.teraenergy.illegalparking.model.entity.notice.service.NoticeService;
import com.teraenergy.illegalparking.model.entity.point.enums.PointType;
import com.teraenergy.illegalparking.model.entity.point.service.PointService;
import com.teraenergy.illegalparking.model.entity.product.domain.Product;
import com.teraenergy.illegalparking.model.entity.product.service.ProductService;
import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptStateType;
import com.teraenergy.illegalparking.model.entity.receipt.service.ReceiptService;
import com.teraenergy.illegalparking.model.entity.report.domain.Report;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportStateType;
import com.teraenergy.illegalparking.model.entity.report.service.ReportService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.enums.Role;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import com.teraenergy.illegalparking.util.JsonUtil;
import com.teraenergy.illegalparking.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Date : 2022-10-17
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Slf4j
@RequiredArgsConstructor
@Controller
public class MobileAPI {

    private final UserService userService;
    private final UserDtoService userDtoService;

    private final ReceiptService receiptService;
    private final CalculateService calculateService;
    private final NoticeService noticeService;
    private final ProductService productService;
    private final PointService pointService;

    private final IllegalEventService illegalEventService;

    private final ReportService reportService;
    private final LawDongService lawDongService;

    private final IllegalZoneMapperService illegalZoneMapperService;

    private final CommentService commentService;

    @PostMapping("/api/login")
    @ResponseBody
    public Object login(@RequestBody String body) throws TeraException {

        boolean result = false;
        UserDto userDto = null;
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        String username = jsonNode.get("userName").asText();
        String password = jsonNode.get("password").asText();

        result = userService.isUser(username, password);

        if (result) {
            User user = userService.get(username);
            userDto = userDtoService.get(user);
        } else {
            throw new TeraException(TeraExceptionCode.USER_IS_NOT_EXIST);
        }

        return userDto;
    }

    @PostMapping("/api/user/register")
    @ResponseBody
    public Object register(@RequestBody String body) throws TeraException {

        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        String userName = jsonNode.get("userName").asText();
        String password = jsonNode.get("password").asText();
        String name = jsonNode.get("name").asText();
        String phoneNumber = jsonNode.get("phoneNumber").asText();
        String photoName = jsonNode.get("photoName").asText();

        User user = new User();
        user.setUsername(userName);
        user.setPassword(password);
        user.setName(name);
        user.setRole(Role.USER);
        user.setUserCode(1L);
        user.setPhoneNumber(phoneNumber);
        user.setPhotoName(photoName);
        user.setEncyptPassword();

        try {
            userService.set(user);
            return "";
        } catch (TeraException e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.USER_FAIL_RESiSTER);
        }
    }

    @PostMapping("/api/user/isExist")
    @ResponseBody
    public Object isExist(@RequestBody String body) throws TeraException {

        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        String userName = jsonNode.get("userName").asText();

        HashMap<String, Object> resultMap = Maps.newHashMap();

        try {
            if (userService.isUser(userName)) {
                resultMap.put("isExist", true);
                resultMap.put("msg", TeraExceptionCode.USER_IS_NOT_EXIST.getMessage());
            } else {
                resultMap.put("isExist", false);
            }
            return resultMap;
        } catch (TeraException e) {
            throw new TeraException(TeraExceptionCode.UNKNOWN);
        }
    }


    @PostMapping("/api/myPage/get")
    public Object getMyPage(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer userSeq = jsonNode.get("userSeq").asInt();

            HashMap<String, Object> resultMap = Maps.newHashMap();

            // TODO : 차량정보
            resultMap.put("carNum", "123가1234");
            resultMap.put("carLevel", "소형");
            resultMap.put("carName", "모닝");

            List<Receipt> receipts = receiptService.gets(userSeq);
            Calculate calculate = calculateService.getAtLast(userSeq);
            List<Notice> notices = noticeService.getsAtFive();

            List<Map<String, Object>> noticeMap = Lists.newArrayList();
            for (Notice notice : notices) {
                HashMap<String, Object> map = Maps.newHashMap();
                map.put("subject", notice.getSubject());
                map.put("content", notice.getContent());
                map.put("regDt", StringUtil.covertDatetimeToString(notice.getRegDt(), "yyyy-MM-dd HH:mm"));

                noticeMap.add(map);
            }

            resultMap.put("reportCount", receipts.size());
            resultMap.put("currentPoint", calculate.getCurrentPointValue());
            resultMap.put("notices", noticeMap);

            return resultMap;
        } catch (Exception e) {
            throw new TeraException(TeraExceptionCode.MYPAGE_GET_FAIL);
        }
    }

    @PostMapping("/api/notice/get")
    public Object getNotice(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer userSeq = jsonNode.get("userSeq").asInt();
            Integer offest = jsonNode.get("offest").asInt();
            Integer count = jsonNode.get("count").asInt();

            User user = userService.get(userSeq);

            if (user == null) {
                throw new TeraException(TeraExceptionCode.USER_IS_NOT_EXIST);
            }

            List<Notice> notices = noticeService.getsAtFive(offest, count);

            List<Map<String, Object>> noticeMap = Lists.newArrayList();
            for (Notice notice : notices) {
                HashMap<String, Object> map = Maps.newHashMap();
                map.put("subject", notice.getSubject());
                map.put("content", notice.getContent());
                map.put("regDt", StringUtil.covertDatetimeToString(notice.getRegDt(), "yyyy-MM-dd HH:mm"));
                noticeMap.add(map);
            }
            return noticeMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.NOTICE_GET_FAIL);
        }
    }

    @PostMapping("/api/point/get")
    public Object getPoint(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer userSeq = jsonNode.get("userSeq").asInt();
            List<Calculate> calculates = calculateService.getsByUser(userSeq);
            List<HashMap<String, Object>> resultMap = Lists.newArrayList();
            for (Calculate calculate : calculates) {
                HashMap<String, Object> map = Maps.newHashMap();
                map.put("value", calculate.getEventPointValue());
                map.put("locationType", calculate.getLocationType() != null ? calculate.getLocationType().getValue() : null);
                map.put("productName", calculate.getProductName());
                map.put("pointType", calculate.getPointType());
                map.put("regDt", StringUtil.covertDatetimeToString(calculate.getRegDt(), "yyyy-MM-dd HH:mm"));
                resultMap.add(map);
            }
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.POINT_GET_FAIL);
        }
    }

    @PostMapping("/api/product/gets")
    public Object getsProduct(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer userSeq = jsonNode.get("userSeq").asInt();

            User user = userService.get(userSeq);
            if ( user == null) {
                throw new TeraException(TeraExceptionCode.USER_IS_NOT_EXIST);
            }

            List<Product> products = productService.gets();
            List<HashMap<String, Object>> resultMap = Lists.newArrayList();
            for (Product product : products) {
                HashMap<String, Object> map = Maps.newHashMap();
                map.put("productSeq", product.getProductSeq());
                map.put("brandType", product.getBrand().getValue());
                map.put("productName", product.getName());
                map.put("pointValue", product.getPointValue());
                map.put("thumbnail", product.getThumbnail());

                resultMap.add(map);
            }
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.POINT_GET_FAIL);
        }
    }

    // 제품 구매 신청
    @PostMapping("/api/calculate/set")
    public Object getProduct(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer userSeq = jsonNode.get("userSeq").asInt();

            User user = userService.get(userSeq);
            if ( user == null) {
                throw new TeraException(TeraExceptionCode.USER_IS_NOT_EXIST);
            }

            Calculate calculate = new Calculate();
            Integer productSeq = jsonNode.get("productSeq").asInt();

            // 잔액 포인트 = 현재 포인트 - 사용 포인트
            long balancePointValue = jsonNode.get("balancePointValue").asLong();

            // 제품 가격 및 제품 명
            Product product = productService.get(productSeq);
            calculate.setEventPointValue(product.getPointValue());
            calculate.setProductName(product.getName());

            // 현재 가격
            calculate.setCurrentPointValue(balancePointValue);
            calculate.setRegDt(LocalDateTime.now());
            calculate.setUserSeq(userSeq);
            calculate.setPointType(PointType.MINUS);

            calculateService.set(calculate);
            return "complete ... ";
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.POINT_GET_FAIL);
        }
    }




    /**
     * 신고 접수
     * id :
     * carNum :
     * addr :
     * latitude :
     * longitude :
     * fileName :
     */
    @PostMapping("/api/receipt/set")
    @ResponseBody
    public Object setReceipt(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        double latitude = jsonNode.get("latitude").asDouble();      // 위도
        double longitude = jsonNode.get("longitude").asDouble();    // 경도
        String addr = jsonNode.get("addr").asText();
        String temp[] = addr.split(" ");

        // 동 코드
        LawDong lawDong = lawDongService.getFromLnmadr(temp[0] + " " + temp[1] + " " + temp[2]);

        // 불법 주정차 구역 ( mybatis 로 가져오기 때문에 illegal_event 데이터는 따로 요청 해야함)
        IllegalZone illegalZone = illegalZoneMapperService.get(lawDong.getCode(), latitude, longitude);
        IllegalEvent illegalEvent = illegalEventService.get(illegalZone.getEventSeq());
        illegalZone.setIllegalEvent(illegalEvent);

        String carNum = jsonNode.get("carNum").asText();
        String regDtStr = jsonNode.get("regDt").asText();
        LocalDateTime regDt = StringUtil.convertStringToDateTime(regDtStr, "yyyy-MM-dd HH:mm");

        // 사용자
        User user = userService.get(jsonNode.get("userSeq").asInt());

        Receipt receipt = receiptService.getByCarNumAndBetweenNow(user.getUserSeq(), carNum, LocalDateTime.now());

        if ( receipt == null) {
            receipt = new Receipt();
            receipt.setAddr(addr);
            receipt.setCarNum(carNum);
            receipt.setFileName(jsonNode.get("fileName").asText());
            receipt.setRegDt(regDt);
            receipt.setUser(user);
            receipt.setCode(lawDong.getCode());
            receipt.setIllegalZone(illegalZone);
        } else {
            receipt.setSecondFileName(jsonNode.get("fileName").asText());
            receipt.setSecondRegDt(LocalDateTime.now());
        }

        // 1. 불법 주정자 지역 체크
        if (illegalZone == null) {
            receipt = new Receipt();
            receipt.setAddr(addr);
            receipt.setCarNum(carNum);
            receipt.setFileName(jsonNode.get("fileName").asText());
            receipt.setRegDt(regDt);
            receipt.setUser(user);
            receipt.setCode(lawDong.getCode());
            receipt.setReceiptStateType(ReceiptStateType.EXCEPTION);

            _comment(receipt.getReceiptSeq(), TeraExceptionCode.ILLEGAL_PARKING_NOT_AREA.getMessage());
            receipt = receiptService.set(receipt);
            throw new TeraException(TeraExceptionCode.ILLEGAL_PARKING_NOT_AREA);
        }

        // 2. 불법 주정차 시간 체크
        String dateStr = regDtStr.split(" ")[0];
        if (illegalZone.getIllegalEvent().isUsedFirst()) {
            LocalDateTime fs = StringUtil.convertStringToDateTime(dateStr + " " + illegalZone.getIllegalEvent().getFirstStartTime(), "yyyy-MM-dd HH:mm");
            LocalDateTime fe = StringUtil.convertStringToDateTime(dateStr + " " + illegalZone.getIllegalEvent().getFirstEndTime(), "yyyy-MM-dd HH:mm");
            if (fs.isAfter(regDt) && fe.isBefore(regDt)) {
                receipt = new Receipt();
                receipt.setAddr(addr);
                receipt.setCarNum(carNum);
                receipt.setFileName(jsonNode.get("fileName").asText());
                receipt.setRegDt(regDt);
                receipt.setUser(user);
                receipt.setCode(lawDong.getCode());
                receipt.setReceiptStateType(ReceiptStateType.EXCEPTION);

                receipt = receiptService.set(receipt);
                _comment(receipt.getReceiptSeq(), TeraExceptionCode.ILLEGAL_PARKING_NOT_CRACKDOWN_TIME.getMessage());
                throw new TeraException(TeraExceptionCode.ILLEGAL_PARKING_NOT_CRACKDOWN_TIME);
            }
        }

        if (illegalZone.getIllegalEvent().isUsedSecond()) {
            LocalDateTime ss = StringUtil.convertStringToDateTime(dateStr + " " + illegalZone.getIllegalEvent().getSecondStartTime(), "yyyy-MM-dd HH:mm");
            LocalDateTime se = StringUtil.convertStringToDateTime(dateStr + " " + illegalZone.getIllegalEvent().getSecondEndTime(), "yyyy-MM-dd HH:mm");

            if (ss.isAfter(regDt) && se.isBefore(regDt)) {
                receipt = new Receipt();
                receipt.setAddr(addr);
                receipt.setCarNum(carNum);
                receipt.setFileName(jsonNode.get("fileName").asText());
                receipt.setRegDt(regDt);
                receipt.setUser(user);
                receipt.setCode(lawDong.getCode());
                receipt.setReceiptStateType(ReceiptStateType.EXCEPTION);

                receipt = receiptService.set(receipt);
                _comment(receipt.getReceiptSeq(), TeraExceptionCode.ILLEGAL_PARKING_NOT_CRACKDOWN_TIME.getMessage());
                throw new TeraException(TeraExceptionCode.ILLEGAL_PARKING_NOT_CRACKDOWN_TIME);
            }
        }

        // 3. 이미 신고된 차량 여부 체크
        if (reportService.isExist(carNum)) {
            receipt.setReceiptStateType(ReceiptStateType.EXCEPTION);
            receipt = receiptService.set(receipt);
            _comment(receipt.getReceiptSeq(), TeraExceptionCode.ILLEGAL_PARKING_EXIST_REPORT_CAR_NUM.getMessage());
            throw new TeraException(TeraExceptionCode.ILLEGAL_PARKING_EXIST_REPORT_CAR_NUM);
        }

        // 4. 기존에 발생 여부 체크
        if (receiptService.isExist(user.getUserSeq(), carNum, regDt, lawDong.getCode())) {
            receipt.setReceiptStateType(ReceiptStateType.REPORT);
            receipt = receiptService.set(receipt);

            // 신고 접수
            Report report = new Report();
            report.setReceipt(receipt);
            report.setReportStateType(ReportStateType.COMPLETE);
            reportService.set(report);
        } else {
            receipt.setReceiptStateType(ReceiptStateType.OCCUR);
            receipt = receiptService.set(receipt);
        }

        return receipt.getReceiptStateType().getValue() + "가(이) 등록 되었습니다.";
    }

    @PostMapping("/api/receipt/gets")
    @ResponseBody
    public Object getReceiptByApi(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        Integer userSeq = jsonNode.get("userSeq").asInt();
        List<Receipt> receipts = receiptService.gets(userSeq);

        List<HashMap<String, Object>> resutMap = Lists.newArrayList();
        String timePattern = "yyyy-MM-dd HH:mm";
        for( Receipt receipt : receipts) {
            HashMap<String, Object> map = Maps.newHashMap();
            map.put("carNum", receipt.getCarNum());
            map.put("addr", receipt.getAddr());
            map.put("firstRegDt", StringUtil.covertDatetimeToString(receipt.getRegDt(),timePattern));
            if ( receipt.getSecondRegDt() != null) map.put("secondRegDt", StringUtil.covertDatetimeToString(receipt.getSecondRegDt(),timePattern));
            map.put("reportState", receipt.getReceiptStateType().getValue());
            List<Comment> comments = commentService.gets(receipt.getReceiptSeq());
            List<String> commentStrs = comments.stream().map(comment -> comment.getContent()).collect(Collectors.toList());
            map.put("comments", commentStrs);
            resutMap.add(map);
        }
        return resutMap;
    }

    public void _comment(Integer receiptSeq, String content) {
        Comment comment = new Comment();
        comment.setReceiptSeq(receiptSeq);
        comment.setRegDt(LocalDateTime.now());
        comment.setContent(content);
        commentService.set(comment);
    }
}
