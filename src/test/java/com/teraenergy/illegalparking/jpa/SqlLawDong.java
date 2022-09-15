package com.teraenergy.illegalparking.jpa;

import com.teraenergy.illegalparking.ApplicationTests;
import com.teraenergy.illegalparking.model.entity.lawdong.domain.LawDong;
import com.teraenergy.illegalparking.model.entity.lawdong.service.LawDongService;
import org.apache.commons.compress.utils.Lists;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@ActiveProfiles(value = "home")
@SpringBootTest(classes = ApplicationTests.class)
@RunWith(SpringRunner.class)
public class SqlLawDong {

    @Value("${file.staticLocation}")
    String staticPath;

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    LawDongService lawDongService;

    private String getCellData(XSSFCell cell) {
        String value = "";
        switch (cell.getCellType()) {
            case FORMULA:
                value = cell.getCellFormula();
                break;
            case NUMERIC:
                value = (long)cell.getNumericCellValue() + "";
                break;
            case STRING:
                value = cell.getStringCellValue() + "";
                break;
            case BLANK:
                value = cell.getBooleanCellValue() + "";
                break;
            case ERROR:
                value = cell.getErrorCellValue() + "";
                break;
        }

        return value;
    }


    @Test
    public void insert(){
        String fileName = "법정동코드_전체자료.xlsx";
        try {
            Resource resource = resourceLoader.getResource(staticPath + fileName);

            FileInputStream fis = new FileInputStream(resource.getFile());
            XSSFWorkbook book = new XSSFWorkbook(fis);
            XSSFSheet sheet = book.getSheetAt(0);
            List<LawDong> lawDongs = Lists.newArrayList();

            // 행수
            int rows = sheet.getPhysicalNumberOfRows();

            for (int rowindex = 0; rowindex < rows; rowindex++) {

                // 0행은 1행부터 값들의 이름을 작성된 것으로 제외
                if (rowindex == 0) {
                    continue;
                }

                // 행을읽는다
                XSSFRow row = sheet.getRow(rowindex);
                LawDong lawDong = new LawDong();

                lawDong.setCode(getCellData(row.getCell(0)));
                lawDong.setName(getCellData(row.getCell(1)));
                lawDong.setIsDel(getCellData(row.getCell(2)).trim().equals("존재") ? false : true);
                lawDongs.add(lawDong);
            }

            System.out.println("law_dong size = " + lawDongs.size());
//            lawDongService.sets(lawDongs);
            lawDongService.set(lawDongs.get(0));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void select(){
        LawDong lawDong = lawDongService.get("1");
        if (lawDong == null) {
            System.out.println(" =============> lawDong is null ");
        }
    }

}
