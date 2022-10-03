package com.teraenergy.illegalparking.controller.calculate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.dto.calculate.domain.ProductDto;
import com.teraenergy.illegalparking.model.dto.calculate.service.ProductDtoService;
import com.teraenergy.illegalparking.model.entity.calculate.domain.Product;
import com.teraenergy.illegalparking.model.entity.calculate.enums.Brand;
import com.teraenergy.illegalparking.model.entity.calculate.service.ProductService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Date : 2022-09-27
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Controller
public class CalculateApi {

    private final ProductService productService;

    private final ProductDtoService productDtoService;

    private final UserService userService;

    private final ObjectMapper objectMapper;

    @PostMapping("/calculate/product/get")
    @ResponseBody
    public JsonNode getProduct(@RequestBody String body) throws JsonProcessingException {
        JsonNode bodyJson = objectMapper.readTree(body);
        Integer productSeq = bodyJson.get("productSeq").asInt();
        Product product = productService.get(productSeq);

        String jsonStr = objectMapper.writeValueAsString(productDtoService.get(product));
        return objectMapper.readTree(jsonStr);
    }

    @PostMapping("/calculate/product/gets")
    @ResponseBody
    public JsonNode getsProduct(@RequestBody String body) throws JsonProcessingException {
        JsonNode bodyJson = objectMapper.readTree(body);
        List<Integer> productSeqs = bodyJson.findValuesAsText("productSeqs").stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());
        List<Product> products = productService.gets(productSeqs);

        String jsonStr = objectMapper.writeValueAsString(productDtoService.gets(products));
        return objectMapper.readTree(jsonStr);
    }

    @PostMapping("/calculate/product/set")
    @ResponseBody
    public JsonNode setProduct(@RequestBody String body) throws JsonProcessingException, TeraException {
        JsonNode jsonNode = objectMapper.readTree(body);
        Product product = convertProduct(jsonNode);
        product = productService.set(product);

        HashMap<String, Object> result = Maps.newHashMap();
        result.put("success", true);
        String jsonStr = objectMapper.writeValueAsString(result);
        return objectMapper.readTree(jsonStr);
    }

    @PostMapping("/calculate/product/sets")
    @ResponseBody
    public JsonNode setsProduct(@RequestBody String body)  {
        boolean isSuccess = false;
        try {
            Iterator<JsonNode> iterator = objectMapper.readTree(body).elements();
            List<Product> products = Lists.newArrayList();
            while (iterator.hasNext()) {
                products.add(convertProduct(iterator.next()));
            }
            productService.sets(products);
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HashMap<String, Object> result = Maps.newHashMap();
            result.put("success", isSuccess);
            return convertResult(result);
        }

    }

    @PostMapping("/calculate/product/modify")
    @ResponseBody
    public JsonNode modifyProduct(@RequestBody String body) {
        boolean isSuccess = false;
        try {
            JsonNode jsonNode = objectMapper.readTree(body);
            Product product = convertProduct(jsonNode);
            productService.set(product);
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HashMap<String, Object> result = Maps.newHashMap();
            result.put("success", isSuccess);
            return convertResult(result);
        }
    }

    @PostMapping("/calculate/product/remove")
    @ResponseBody
    public JsonNode removeProduct(@RequestBody String body) {

        boolean isSuccess = false;
        long num = 0;
        try {
            JsonNode jsonNode = objectMapper.readTree(body);
            Integer productSeq = jsonNode.get("productSeq").asInt();
            num = productService.remove(productSeq);
            isSuccess = true;
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            HashMap<String, Object> result = Maps.newHashMap();
            result.put("num", num);
            result.put("success", isSuccess);
            return convertResult(result);
        }
    }

    private Product convertProduct(JsonNode jsonNode) throws TeraException {
        Product product  = new Product();
        if ( jsonNode.get("productSeq") != null ) {
            product.setProductSeq(jsonNode.get("productSeq").asInt());
        }
        product.setRegDt(LocalDateTime.now());
        product.setIsDel(false);
        product.setBrand(Brand.valueOf(jsonNode.get("brand").asText()));
        product.setName(jsonNode.get("name").asText());
        product.setPointValue(jsonNode.get("pointValue").asLong());
        Integer userSeq = jsonNode.get("userSeq").asInt();
        User user = userService.get(userSeq);
        product.setUser(user);
        return product;
    }

    private JsonNode convertResult(HashMap<String, Object> map) {
        try {
            String jsonStr = objectMapper.writeValueAsString(map);
            return objectMapper.readTree(jsonStr);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}


