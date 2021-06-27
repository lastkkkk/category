package com.example.category.Controller;

import com.example.category.Bean.Predict;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/predict")
public class PredictController {

    public static final String PREDICT_URL = "http://localhost:5000";

    @Resource
    private RestTemplate restTemplate;

    @RequestMapping(value = "/predict_one",method = RequestMethod.POST,produces = {"text/plain;charset=UTF-8"})
    public String predict_one(@RequestBody Predict sentence){
        Map<String,List<String>> list =new HashMap<String,List<String>>();
        String s = sentence.getSentence();
        List<String> list1 = Arrays.asList(s.split(","));
        list.put("sentence",list1);
        System.out.println(list);
        String result = restTemplate.postForEntity(PREDICT_URL+"/predict",list,String.class).getBody();
        return result;
    }

}
