package com.teraenergy.illegalparking.controller.calculate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.model.dto.calculate.service.ProductDtoService;
import com.teraenergy.illegalparking.model.entity.calculate.domain.Product;
import com.teraenergy.illegalparking.model.entity.calculate.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
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
    public JsonNode setProduct(@RequestBody String body) throws JsonProcessingException {
        Product product = objectMapper.convertValue(body, Product.class);
        product = productService.set(product);
        String jsonStr = objectMapper.writeValueAsString(product);
        return objectMapper.readTree(jsonStr);
    }

    @PostMapping("/calculate/product/sets")
    @ResponseBody
    public JsonNode setsProduct(@RequestBody String body) throws JsonProcessingException {
        List<Product> products = objectMapper.convertValue(body, List.class);
        products = productService.sets(products);
        String jsonStr = objectMapper.writeValueAsString(products);
        return objectMapper.readTree(jsonStr);
    }


    @PostMapping("/calculate/product/modify")
    @ResponseBody
    public JsonNode modifyProduct(@RequestBody String body) throws JsonProcessingException {
        Product product = objectMapper.convertValue(body, Product.class);
        product = productService.set(product);
        String jsonStr = objectMapper.writeValueAsString(product);
        return objectMapper.readTree(jsonStr);
    }

    @PostMapping("/calculate/product/remove")
    @ResponseBody
    public JsonNode removeProduct(@RequestBody String body) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(body);
        Integer productSeq = jsonNode.get("productSeq").asInt();
        long num = productService.remove(productSeq);

        HashMap<String, Object> result = Maps.newHashMap();
        result.put("num", num);
        String jsonStr = objectMapper.writeValueAsString(result);
        return objectMapper.readTree(jsonStr);
    }

}


