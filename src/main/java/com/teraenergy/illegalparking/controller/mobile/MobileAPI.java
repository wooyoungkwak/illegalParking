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
import com.teraenergy.illegalparking.model.entity.mycar.domain.MyCar;
import com.teraenergy.illegalparking.model.entity.mycar.service.MyCarService;
import com.teraenergy.illegalparking.model.entity.notice.domain.Notice;
import com.teraenergy.illegalparking.model.entity.notice.service.NoticeService;
import com.teraenergy.illegalparking.model.entity.parking.service.ParkingService;
import com.teraenergy.illegalparking.model.entity.pm.service.PmService;
import com.teraenergy.illegalparking.model.entity.point.enums.PointType;
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
import com.teraenergy.illegalparking.sms.Sms;
import com.teraenergy.illegalparking.util.JsonUtil;
import com.teraenergy.illegalparking.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.mobile.device.Device;
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
    private final IllegalEventService illegalEventService;
    private final ReportService reportService;
    private final LawDongService lawDongService;
    private final IllegalZoneMapperService illegalZoneMapperService;
    private final CommentService commentService;
    private final MyCarService myCarService;
    private final ParkingService parkingService;
    private final PmService pmService;

    /**
     * ????????? ????????? ??????
     */
    @PostMapping("/api/login")
    @ResponseBody
    public Object login(@RequestBody String body) throws TeraException {
        boolean result = false;
        UserDto userDto = null;
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        String username = jsonNode.get("userName").asText();
        String password = jsonNode.get("password").asText();

        result = userService.isUserByUserNameAndPasswordMobile(username, password);

        if (result) {
            User user = userService.get(username);
            userDto = userDtoService.get(user);
        } else {
            throw new TeraException(TeraExceptionCode.USER_PASSWORD_IS_NOT_EXIST);
        }

        return userDto;
    }

    /**
     * ????????? ?????? ??????
     */
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
        user.setPhoneNumber(phoneNumber.trim());
        user.setPhotoName(photoName);

        try {
            // ????????? ?????? ??????
            if (userService.isUserByPhoneNumber(user.getPhoneNumber())) {
                throw new TeraException(TeraExceptionCode.USER_IS_EXIST);
            }

            // ????????? ????????? ??????
            if (userService.isUserByDuplicate(user.getUsername())) {
                throw new TeraException(TeraExceptionCode.USER_IS_EXIST);
            }

            userService.set(user);
            return "";
        } catch (TeraException e) {
            e.printStackTrace();
            if (e.getCode() == TeraExceptionCode.USER_IS_EXIST.getCode()) {
                throw new TeraException(TeraExceptionCode.USER_IS_EXIST);
            } else {
                throw new TeraException(TeraExceptionCode.USER_FAIL_RESiSTER);
            }
        }
    }

    /**
     * ????????? ?????? ?????? ????????????
     */
    @PostMapping("/api/user/isExist")
    @ResponseBody
    public Object isExist(@RequestBody String body) throws TeraException {

        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        String userName = jsonNode.get("userName").asText();

        HashMap<String, Object> resultMap = Maps.newHashMap();

        try {
            if (userService.isUserByDuplicate(userName)) {
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

    /**
     * ????????? ?????? ??????
     */
    @PostMapping("/api/user/profile/change")
    @ResponseBody
    public Object changeProfile(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer userSeq = jsonNode.get("userSeq").asInt();
            String photoName = jsonNode.get("photoName").asText();

            User user = userService.get(userSeq);
            user.setDecryptPassword();
            user.setPhotoName(photoName);

            userService.set(user);
            return "complete ... ";
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.USER_INSERT_FAIL);
        }
    }

    /**
     * ???????????? ????????????
     */
    @PostMapping("/api/user/password/check")
    @ResponseBody
    public Object checkPassword(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer userSeq = jsonNode.get("userSeq").asInt();
            String password = jsonNode.get("password").asText();

            User user = userService.get(userSeq);
            user.setDecryptPassword();
            if (user.getPassword().equals(password)) {
                return "complete .. ";
            } else {
                throw new TeraException(TeraExceptionCode.USER_FAIL_PASSWORD);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.USER_FAIL_PASSWORD);
        }
    }

    /**
     * ???????????? ?????? ??????
     */
    @PostMapping("/api/user/password/change")
    @ResponseBody
    public Object changePassword(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer userSeq = jsonNode.get("userSeq").asInt();
            String password = jsonNode.get("password").asText();

            User user = userService.get(userSeq);
            user.setPassword(password);
            userService.set(user);

            return "complete .. ";
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.USER_INSERT_FAIL);
        }

    }

    /**
     * ??? ????????? ?????? ????????????
     */
    @PostMapping("/api/myPage/get")
    @ResponseBody
    public Object getMyPage(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer userSeq = jsonNode.get("userSeq").asInt();

            HashMap<String, Object> resultMap = Maps.newHashMap();

            List<MyCar> myCars = myCarService.gets(userSeq);
            if (myCars.size() > 0) {
                resultMap.put("carNum", myCars.get(0).getCarNum());
                resultMap.put("carLevel", myCars.get(0).getCarGrade());
                resultMap.put("carName", myCars.get(0).getCarName());
                resultMap.put("isAlarm", myCars.get(0).isAlarm());
            } else {
                resultMap.put("carNum", "");
                resultMap.put("carLevel", "");
                resultMap.put("carName", "");
                resultMap.put("isAlarm", false);
            }

            List<Receipt> receipts = receiptService.gets(userSeq);
            Calculate calculate = calculateService.getAtLast(userSeq);
            List<Notice> notices = noticeService.getsAtFive();

            List<Map<String, Object>> noticeMap = Lists.newArrayList();
            for (Notice notice : notices) {
                HashMap<String, Object> map = Maps.newHashMap();
                map.put("noticeType", notice.getNoticeType().getValue());
                map.put("subject", notice.getSubject());
                map.put("content", notice.getContent());
                map.put("regDt", StringUtil.convertDatetimeToString(notice.getRegDt(), "yyyy-MM-dd HH:mm"));

                noticeMap.add(map);
            }

            resultMap.put("reportCount", receipts == null ? 0 : receipts.size());
            resultMap.put("currentPoint", calculate == null ? 0 : calculate.getCurrentPointValue());
            resultMap.put("notices", noticeMap);

            return resultMap;
        } catch (Exception e) {
            throw new TeraException(TeraExceptionCode.MYPAGE_GET_FAIL);
        }
    }

    /**
     * ?????? ?????? ?????? ????????? ????????????
     */
    @PostMapping("/api/notice/gets")
    @ResponseBody
    public Object getsNotice(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer userSeq = jsonNode.get("userSeq").asInt();
            Integer offset = jsonNode.get("offset").asInt();
            Integer count = jsonNode.get("count").asInt();

            User user = userService.get(userSeq);

            if (user == null) {
                throw new TeraException(TeraExceptionCode.USER_IS_NOT_EXIST);
            }

            List<Notice> notices = noticeService.getsAtFive(offset, count);

            List<Map<String, Object>> noticeMap = Lists.newArrayList();
            for (Notice notice : notices) {
                HashMap<String, Object> map = Maps.newHashMap();
                map.put("noticeType", notice.getNoticeType().getValue());
                map.put("subject", notice.getSubject());
                map.put("content", notice.getContent());
                map.put("regDt", StringUtil.convertDatetimeToString(notice.getRegDt(), "yyyy-MM-dd HH:mm"));
                noticeMap.add(map);
            }
            return noticeMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.NOTICE_GET_FAIL);
        }
    }

    /**
     * ????????? ????????? ?????? ????????????
     */
    @PostMapping("/api/point/gets")
    @ResponseBody
    public Object getsPoint(@RequestBody String body) throws TeraException {
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
                map.put("regDt", StringUtil.convertDatetimeToString(calculate.getRegDt(), "yyyy-MM-dd HH:mm"));
                resultMap.add(map);
            }
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.POINT_GET_FAIL);
        }
    }

    /**
     * ?????? ????????? ??????
     */
    @PostMapping("/api/product/gets")
    @ResponseBody
    public Object getsProduct(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer userSeq = jsonNode.get("userSeq").asInt();

            User user = userService.get(userSeq);
            if (user == null) {
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

    /**
     * ?????? ?????? ?????? ??????
     */
    @PostMapping("/api/calculate/set")
    @ResponseBody
    public Object setCalculate(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer userSeq = jsonNode.get("userSeq").asInt();

            User user = userService.get(userSeq);
            if (user == null) {
                throw new TeraException(TeraExceptionCode.USER_IS_NOT_EXIST);
            }

            Calculate calculate = new Calculate();
            Integer productSeq = jsonNode.get("productSeq").asInt();

            // ?????? ????????? = ?????? ????????? - ?????? ?????????
            long balancePointValue = jsonNode.get("balancePointValue").asLong();

            // ?????? ?????? ??? ?????? ???
            Product product = productService.get(productSeq);
            calculate.setEventPointValue(product.getPointValue());
            calculate.setProductName(product.getName());

            // ?????? ??????
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
     * ?????? ?????? ?????? ??????
     */
    @PostMapping("/api/car/alarmHistory")
    @ResponseBody
    public Object alarmHistoryCar(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer userSeq = jsonNode.get("userSeq").asInt();
            String carNum = jsonNode.get("carNum").asText();
            List<Receipt> receipts = receiptService.gets(carNum);
            List<HashMap<String, Object>> resultMap = Lists.newArrayList();

            for (Receipt receipt : receipts) {
                HashMap<String, Object> map = Maps.newHashMap();
                map.put("addr", receipt.getAddr());
                String timePattern = "yyyy-MM-dd HH:mm";
                if (receipt.getSecondRegDt() == null) {
                    map.put("regDt", StringUtil.convertDatetimeToString(receipt.getRegDt(), timePattern));
                } else {
                    map.put("regDt", StringUtil.convertDatetimeToString(receipt.getSecondRegDt(), timePattern));
                }
                map.put("stateType", receipt.getReceiptStateType().getValue());
                map.put("fileName", receipt.getFileName());
                resultMap.add(map);
            }

            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.CAR_ALARM_HISTORY_GET_FAIL);
        }
    }

    /**
     * ?????? ??????
     */
    @PostMapping("/api/car/set")
    @ResponseBody
    public Object setCar(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer userSeq = jsonNode.get("userSeq").asInt();
            String carNum = jsonNode.get("carNum").asText();
            String carName = jsonNode.get("carName").asText();
            String carGrade = jsonNode.get("carGrade").asText();

            MyCar mycar = new MyCar();
            mycar.setUserSeq(userSeq);
            mycar.setCarName(carName);
            mycar.setCarNum(carNum);
            mycar.setCarGrade(carGrade);

            if (myCarService.isExist(carNum)) {
                throw new TeraException(TeraExceptionCode.CAR_EXIST);
            }

            mycar = myCarService.set(mycar);

            return "complete ... ";
        } catch (TeraException e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.valueOf(e.getCode()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.CAR_SET_FAIL);
        }

    }


    /**
     * ?????? ?????? ????????? ?????? ??????
     */
    @PostMapping("/api/car/modify")
    @ResponseBody
    public Object modifyCar(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer userSeq = jsonNode.get("userSeq").asInt();
            String carNum = jsonNode.get("carNum").asText();
            boolean isAlarm = jsonNode.get("isAlarm").booleanValue();

            MyCar myCar = myCarService.get(userSeq, carNum);
            myCar.setIsAlarm(isAlarm);
            myCarService.set(myCar);
            return "complete ... ";
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.CAR_SET_FAIL);
        }
    }

    /**
     * ?????? ??????
     */
    @PostMapping("/api/receipt/set")
    @ResponseBody
    public Object setReceipt(@RequestBody String body) throws TeraException {
        try {
            if ( body.length() == 0) {
                throw new TeraException(TeraExceptionCode.UNKNOWN);
            }

            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            double latitude = jsonNode.get("latitude").asDouble();      // ??????
            double longitude = jsonNode.get("longitude").asDouble();    // ??????
            String addr = jsonNode.get("addr").asText();
            String temp[] = addr.split(" ");

            // ??? ??????
            LawDong lawDong = lawDongService.getFromLnmadr(temp[0] + " " + temp[1] + " " + temp[2]);

            String carNum = jsonNode.get("carNum").asText();    // ?????? ??????
            String regDtStr = jsonNode.get("regDt").asText();   // ?????? ?????? (??????)
            LocalDateTime regDt = StringUtil.convertStringToDateTime(regDtStr, "yyyy-MM-dd HH:mm:ss"); // ?????? ?????? (LocalDateTime ??????)

            // ?????? ????????? ?????? ( mybatis ??? ???????????? ????????? illegal_event ???????????? ?????? ?????? ?????????)
            IllegalZone illegalZone = illegalZoneMapperService.get(lawDong.getCode(), latitude, longitude);

            // ?????????
            User user = userService.get(jsonNode.get("userSeq").asInt());

            // 1. ?????? ????????? ?????? ?????? ( ?????? ????????? ?????? ??????? )
            if (illegalZone == null) {
                Receipt receipt_etc = new Receipt();
                receipt_etc.setAddr(addr);
                receipt_etc.setCarNum(carNum);
                receipt_etc.setFileName(jsonNode.get("fileName").asText());
                receipt_etc.setRegDt(regDt);
                receipt_etc.setUser(user);
                receipt_etc.setCode(lawDong.getCode());
                receipt_etc.setReceiptStateType(ReceiptStateType.EXCEPTION);

                receipt_etc = receiptService.set(receipt_etc);
                _comment(receipt_etc.getReceiptSeq(), TeraExceptionCode.ILLEGAL_PARKING_NOT_AREA.getMessage());
                throw new TeraException(TeraExceptionCode.ILLEGAL_PARKING_NOT_AREA);
            }

            IllegalEvent illegalEvent = illegalEventService.get(illegalZone.getEventSeq());
            illegalZone.setIllegalEvent(illegalEvent);

            // 2. ?????? ?????? ?????? 1??? ?????? 10????????? ?????? ????????? ??? ??????
            // 2.1 ?????? ????????? ?????? ?????? ?????? ??? ??????
            Receipt receipt;
            switch (illegalZone.getIllegalEvent().getIllegalType()) {
                case ILLEGAL:
                    if (LocalDateTime.now().plusMinutes(11).isAfter(regDt)) {
                        receipt = new Receipt();
                        receipt.setAddr(addr);
                        receipt.setCarNum(carNum);
                        receipt.setFileName(jsonNode.get("fileName").asText());
                        receipt.setRegDt(regDt);
                        receipt.setUser(user);
                        receipt.setCode(lawDong.getCode());
                        receipt.setReceiptStateType(ReceiptStateType.FORGET);
                        receiptService.set(receipt);

                        _comment(receipt.getReceiptSeq(), TeraExceptionCode.REPORT_OVER_TIME.getMessage());
                        throw new TeraException(TeraExceptionCode.REPORT_OVER_TIME);
                    }
                    break;
                case FIVE_MINUTE:
                    if (LocalDateTime.now().plusMinutes(16).isAfter(regDt)) {
                        receipt = new Receipt();
                        receipt.setAddr(addr);
                        receipt.setCarNum(carNum);
                        receipt.setFileName(jsonNode.get("fileName").asText());
                        receipt.setRegDt(regDt);
                        receipt.setUser(user);
                        receipt.setCode(lawDong.getCode());
                        receipt.setReceiptStateType(ReceiptStateType.FORGET);
                        receiptService.set(receipt);

                        _comment(receipt.getReceiptSeq(), TeraExceptionCode.REPORT_OVER_TIME.getMessage());
                        throw new TeraException(TeraExceptionCode.REPORT_OVER_TIME);
                    }
                    break;
            }

            // 2.2 ???????????? ?????? 1??? ?????? 10????????? ?????? ????????? ??? ??????
            Receipt oldReceipt = receiptService.getByLastOccur(user.getUserSeq(), carNum, regDt, illegalZone.getIllegalEvent().getIllegalType());
            if (oldReceipt != null) {
                oldReceipt.setReceiptStateType(ReceiptStateType.NOTHING);
                receiptService.set(oldReceipt);
                throw new TeraException(TeraExceptionCode.REPORT_OVER_TIME);
            }

            receipt = receiptService.getByCarNumAndBetweenNow(user.getUserSeq(), carNum, LocalDateTime.now());

            if (receipt == null) {
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
                receipt.setSecondRegDt(regDt);
            }

            // 3. ?????? ????????? ?????? ?????? ( ?????? ?????? ??? ?????? ? )
            // 3-1. ????????? ?????? ??????
            String dateStr = regDtStr.split(" ")[0];
            if (!illegalZone.getIllegalEvent().isUsedFirst()) {
                LocalDateTime fs = StringUtil.convertStringToDateTime(dateStr + " " + illegalZone.getIllegalEvent().getFirstStartTime(), "yyyy-MM-dd HH:mm");
                LocalDateTime fe = StringUtil.convertStringToDateTime(dateStr + " " + illegalZone.getIllegalEvent().getFirstEndTime(), "yyyy-MM-dd HH:mm");
                if (fs.isBefore(regDt) && fe.isAfter(regDt)) {
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
            // 3-2. ????????? ?????? ??????
            if (!illegalZone.getIllegalEvent().isUsedSecond()) {
                LocalDateTime ss = StringUtil.convertStringToDateTime(dateStr + " " + illegalZone.getIllegalEvent().getSecondStartTime(), "yyyy-MM-dd HH:mm");
                LocalDateTime se = StringUtil.convertStringToDateTime(dateStr + " " + illegalZone.getIllegalEvent().getSecondEndTime(), "yyyy-MM-dd HH:mm");

                if (ss.isBefore(regDt) && se.isAfter(regDt)) {
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

            // 4. 1 ??? ?????? ( ?????? 5??? ??? ) ????????? ???????????? ??????
            if (receiptService.isExistByIllegalType(user.getUserSeq(), carNum, regDt, lawDong.getCode(), illegalEvent.getIllegalType())) {
                switch (illegalEvent.getIllegalType()) {
                    case ILLEGAL:
                        _comment(receipt.getReceiptSeq(), TeraExceptionCode.REPORT_OCCUR_ONE.getMessage());
                        throw new TeraException(TeraExceptionCode.REPORT_OCCUR_ONE);
                    case FIVE_MINUTE:
                        _comment(receipt.getReceiptSeq(), TeraExceptionCode.REPORT_OCCUR_FIVE.getMessage());
                        throw new TeraException(TeraExceptionCode.REPORT_OCCUR_FIVE);
                }
            }

            // 5. ?????? ????????? ?????? ????????? ?????? ?????? ?????? (?????? ????????? ?????????????)
            if (reportService.isExist(carNum, regDt, illegalEvent.getIllegalType())) {
                // ?????? ????????? ?????? ????????? ?????? ????????? ????????? ?????? ?????? (NOTHING)
                receipt = new Receipt();
                receipt.setAddr(addr);
                receipt.setCarNum(carNum);
                receipt.setRegDt(regDt);
                receipt.setUser(user);
                receipt.setCode(lawDong.getCode());
                receipt.setIllegalZone(illegalZone);
                receipt.setFileName(jsonNode.get("fileName").asText());
                receipt.setReceiptStateType(ReceiptStateType.NOTHING);
                receipt = receiptService.set(receipt);
                _comment(receipt.getReceiptSeq(), TeraExceptionCode.ILLEGAL_PARKING_EXIST_REPORT_CAR_NUM.getMessage());
                throw new TeraException(TeraExceptionCode.ILLEGAL_PARKING_EXIST_REPORT_CAR_NUM);
            }

            // 6. ????????? ?????? ?????? ?????? ( ????????? ?????? ?????? ????????? ? )
            // ?????? ??? ??? ????????? ?????? ?????? ??? ??????
            // * ?????? ?????? : ?????? ?????? ?????? ?????? ?????? ???????????? 11??? ????????? (?????? 16??? ?????????)
            // * TODO : ????????? ????????? ?????? ?????? .... ( ?????? ??? ?????? )

            String message = "??????????????? ????????? ?????? ???????????????.\n 1????????? ?????? ???????????? ??????????????? ????????????. ";
            if (receiptService.isExist(user.getUserSeq(), carNum, regDt, lawDong.getCode(), illegalEvent.getIllegalType())) {
                // ???????????? ???????????? 11??? (16???) ????????? ???????????? ????????? ??????????????? ??????????
                receipt.setReceiptStateType(ReceiptStateType.REPORT);
                receipt = receiptService.set(receipt);

                // ?????? ??????
                Report report = new Report();
                report.setReceipt(receipt);
                report.setReportStateType(ReportStateType.COMPLETE);
                reportService.set(report);

                _deleteCommentByOneMinute(receipt.getReceiptSeq());

                message = "??????????????? ????????? ?????? ???????????? ?????????????????? ??????????????????. ";
            } else {
                receipt.setReceiptStateType(ReceiptStateType.OCCUR);
                receipt = receiptService.set(receipt);
                message = "??????????????? ????????? ?????? ???????????????.\n 1????????? ?????? ???????????? ??????????????? ????????????. ";
            }

            /** ?????? ????????? ... */
            MyCar car = myCarService.getByAlarm(carNum);
            if (car != null) {
                User carUser = userService.get(car.getUserSeq());
                String phoneNumber = carUser.getPhoneNumber();
                Sms.sendSMS(phoneNumber, message);
            }

            return receipt.getReceiptStateType().getValue() + "???(???) ?????? ???????????????.";
        } catch (TeraException e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.valueOf(e.getCode()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.UNKNOWN);
        }
    }

    /**
     * ?????? ?????? ?????? ????????????
     */
    @PostMapping("/api/receipt/gets")
    @ResponseBody
    public Object getsReceipt(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        Integer userSeq = jsonNode.get("userSeq").asInt();
        List<Receipt> receipts = receiptService.gets(userSeq);

        List<HashMap<String, Object>> resutMap = Lists.newArrayList();
        String timePattern = "yyyy-MM-dd HH:mm";
        for (Receipt receipt : receipts) {
            HashMap<String, Object> map = Maps.newHashMap();
            map.put("carNum", receipt.getCarNum());
            map.put("addr", receipt.getAddr());
            map.put("firstRegDt", StringUtil.convertDatetimeToString(receipt.getRegDt(), timePattern));
            if (receipt.getSecondRegDt() != null) {
                map.put("secondRegDt", StringUtil.convertDatetimeToString(receipt.getSecondRegDt(), timePattern));
            }
            map.put("reportState", receipt.getReceiptStateType().getValue());
            map.put("firstFileName", receipt.getFileName());
            if (receipt.getSecondFileName() != null) {
                map.put("secondFileName", receipt.getSecondFileName());
            }
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

    // 1??? ?????? ????????? ???????????????. ??????
    public void _deleteCommentByOneMinute(Integer receiptSeq) {
        Comment comment = commentService.getByOneMinute(receiptSeq);
        if ( comment == null) {
            return;
        }
        comment.setDel(true);
        comment.setDelDt(LocalDateTime.now());
        commentService.set(comment);
    }


    @PostMapping("/api/parking/gets")
    @ResponseBody
    public Object getsParking(Device device, @RequestBody String body) throws TeraException {
        try {
            if (device.isMobile()) {
                JsonNode jsonNode = JsonUtil.toJsonNode(body);
                List<String> codes = com.google.common.collect.Lists.newArrayList();
                JsonNode codesArrNode = jsonNode.get("codes");
                if (codesArrNode.isArray()) {
                    for (JsonNode obj : codesArrNode) {
                        codes.add(obj.asText());
                    }
                }
                return parkingService.gets(codes);
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.UNKNOWN);
        }
    }

    @PostMapping("/api/pm/gets")
    @ResponseBody
    public Object getsPm(Device device, @RequestBody String body) throws TeraException {
        try {
            if (device.isMobile()) {
                JsonNode jsonNode = JsonUtil.toJsonNode(body);
                List<String> codes = com.google.common.collect.Lists.newArrayList();
                JsonNode codesArrNode = jsonNode.get("codes");
                if (codesArrNode.isArray()) {
                    for (JsonNode obj : codesArrNode) {
                        codes.add(obj.asText());
                    }
                }
                return pmService.gets(codes);
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.UNKNOWN);
        }
    }

}
