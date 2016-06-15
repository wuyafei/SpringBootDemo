package me.fly.spring.controller;

/**
 * Created by wuyafei on 16/4/7.
 */

import me.fly.spring.model.HelloRequest;
import me.fly.spring.model.HelloResponse;
import me.fly.spring.model.User;
import me.fly.spring.service.TestCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Value("${application.message:Hello World}")
    private String message;


    @RequestMapping("/welcome")
    public String welcome(Map<String, Object> model) {
        model.put("message", this.message);
        return "welcome";
    }

    @RequestMapping("/")
    public @ResponseBody String index(){
        return "Hello, wuyafei!";
    }

    @RequestMapping(value = "/hello", method = RequestMethod.POST)
    public @ResponseBody HelloResponse sayHello(@RequestBody HelloRequest helloRequest){
        HelloResponse helloResponse = new HelloResponse();
        List<HashMap> respMap = new ArrayList<HashMap>();
        HashMap map1 = new HashMap();
        map1.put("start", 100);
        map1.put("end", -1);
        map1.put("value", 2);
        System.out.println("Name: " + helloRequest.getName() + "  Age: " + helloRequest.getAge());
        if(helloRequest.getTieredPrice() != null) {
            for (HashMap map : helloRequest.getTieredPrice()) {
                Double start = (Double) map.get("start");
                Double end = (Double) map.get("end");
                Double val = (Double) map.get("value");
                System.out.println("start: " + start + "  end: " + end + "  value: " + val);
                // respMap.add(map);
            }
        }
        // respMap.add(map1);
        helloResponse.setSuccess(true);
        helloResponse.setStatus("0");
        helloResponse.setNum(0);
        helloResponse.setTieredPrice(respMap);
        return helloResponse;
    }

    @Autowired
    TestCacheService cacheService;

    @RequestMapping("/tc")
    @ResponseBody
    public String putCache(){
        User user1 = cacheService.findUser(1256254l,"alex","phil");
        cacheService.clearUserCache(1256254l,"alex","phil");
        User user2 = cacheService.findUser(1256254l,"alex","phil");
        return "ok";
    }

    @RequestMapping("/tc2")
    @ResponseBody
    public String testCache(){
        User user1 = cacheService.findUser2(1256l,"alex","phil");
        cacheService.clearSessionCache(1256l,"alex","phil");
        User user2 = cacheService.findUser2(1256l,"alex","phil");
        return "ok";
    }
}
