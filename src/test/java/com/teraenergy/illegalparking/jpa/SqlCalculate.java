package com.teraenergy.illegalparking.jpa;

import com.teraenergy.illegalparking.ApplicationTests;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.entity.calculate.domain.Calculate;
import com.teraenergy.illegalparking.model.entity.calculate.domain.Point;
import com.teraenergy.illegalparking.model.entity.calculate.domain.Product;
import com.teraenergy.illegalparking.model.entity.calculate.enums.Brand;
import com.teraenergy.illegalparking.model.entity.calculate.enums.PointType;
import com.teraenergy.illegalparking.model.entity.calculate.service.CalculateService;
import com.teraenergy.illegalparking.model.entity.calculate.service.PointService;
import com.teraenergy.illegalparking.model.entity.calculate.service.ProductService;
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

import javax.transaction.Transactional;
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
    public void insertProduct(){
        List<Product> products = Lists.newArrayList();

        User adminUser = null;
        try {
            adminUser = userService.get(1);
            User user = userService.get(2);
        } catch (TeraException e) {
            throw new RuntimeException(e);
        }

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
    public void insertPoint(){

        List<Point> points = Lists.newArrayList();

        Report report = reportService.get(1);
        Point point = new Point();
        point.setValue(1000L);
        point.setPointType(PointType.PLUS);
        point.setReport(report);
        point.setNote("");
        point.setUserSeq(2);
        points.add(point);

        Product product = productService.get(1);
        Point point2 = new Point();
        point2.setValue(1000L);
        point2.setPointType(PointType.MINUS);
        point2.setProduct(product);
        point2.setNote("");
        point2.setUserSeq(2);
        points.add(point2);

        pointService.sets(points);
    }

    @Test
    public void insertCalculate() throws TeraException {
        Point point = pointService.get(2);
        User user = userService.get(2);
        Calculate calculate = new Calculate();
        calculate.setIsDel(false);
        calculate.setRegDt(LocalDateTime.now());
        calculate.setUser(user);
        calculate.setPoint(point);
        calculate.setBeforePointValue(10000L);
        calculate.setCurrentPointValue(11000L);

        calculateService.save(calculate);
    }

}
