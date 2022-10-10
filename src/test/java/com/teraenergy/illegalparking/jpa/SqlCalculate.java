package com.teraenergy.illegalparking.jpa;

import com.teraenergy.illegalparking.ApplicationTests;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.entity.calculate.domain.Calculate;
import com.teraenergy.illegalparking.model.entity.point.domain.Point;
import com.teraenergy.illegalparking.model.entity.product.domain.Product;
import com.teraenergy.illegalparking.model.entity.product.enums.Brand;
import com.teraenergy.illegalparking.model.entity.point.enums.PointType;
import com.teraenergy.illegalparking.model.entity.calculate.service.CalculateService;
import com.teraenergy.illegalparking.model.entity.point.service.PointService;
import com.teraenergy.illegalparking.model.entity.product.service.ProductService;
import com.teraenergy.illegalparking.model.entity.report.domain.Report;
import com.teraenergy.illegalparking.model.entity.report.service.ReportService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import org.apache.commons.compress.utils.Lists;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Date : 2022-09-27
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@ActiveProfiles(value = "debug")
@SpringBootTest(classes = ApplicationTests.class)
@RunWith(SpringRunner.class)
@Transactional
public class SqlCalculate {

    @Autowired
    private CalculateService calculateService;

    @Autowired
    private UserService userService;

    @Autowired
    private PointService pointService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ReportService reportService;

    @Test
    public void insert(){
        try {
            insertByProduct();
            insertByPoint();
            insertByCalculate();
        } catch (TeraException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void insertByProduct() throws TeraException {
        List<Product> products = Lists.newArrayList();

        User adminUser = null;
        adminUser = userService.get(1);
        User user = userService.get(2);

        Product product = new Product();
        product.setUser(adminUser);
        product.setPointValue(500L);
        product.setRegDt(LocalDateTime.now());
        product.setName("아메리카노");
        product.setBrand(Brand.STARBUGS);
        
        Product product2 = new Product();
        product2.setUser(adminUser);
        product2.setPointValue(500L);
        product2.setRegDt(LocalDateTime.now());
        product2.setName("아이스크림");
        product2.setBrand(Brand.BASKINROBBINS);
        products.add(product);
        products.add(product2);
        productService.sets(products);
    }

    @Test
    public void insertByPoint(){

        List<Point> points = Lists.newArrayList();

        Report report = reportService.get(1);

        Point point = new Point();
        point.setValue(1000L);
        point.setResidualValue(1000L);
        point.setUseValue(1000L);
        point.setPointType(PointType.PLUS);
        point.setNote("");
        point.setIsPointLimit(false);
        point.setIsTimeLimit(false);
        point.setStartDate(LocalDate.now().minusDays(10));
        point.setStopDate(LocalDate.now().plusDays(10));
        points.add(point);

        Product product = productService.get(1);
        Point point2 = new Point();
        point2.setValue(1000L);
        point2.setResidualValue(1000L);
        point2.setUseValue(1000L);
        point2.setPointType(PointType.MINUS);
        point2.setProduct(product);
        point2.setNote("");
        points.add(point2);

        pointService.sets(points);
    }

    @Test
    public void insertByCalculate() throws TeraException {
        Point point = pointService.get(1);
        User user = userService.get(1);
        Calculate calculate = new Calculate();
        calculate.setIsDel(false);
        calculate.setRegDt(LocalDateTime.now());
        calculate.setUser(user);
        calculate.setPoint(point);
        calculate.setBeforePointValue(10000L);
        calculate.setCurrentPointValue(11000L);

        calculateService.set(calculate);
    }

}
