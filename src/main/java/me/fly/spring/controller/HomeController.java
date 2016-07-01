package me.fly.spring.controller;

/**
 * Created by wuyafei on 16/4/7.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import me.fly.spring.config.Servers;
import me.fly.spring.model.HelloRequest;
import me.fly.spring.model.HelloResponse;
import me.fly.spring.model.User;
import me.fly.spring.service.TestCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Controller
public class HomeController {

    @Value("${application.message:Hello World}")
    private String message;

    @Value("${lss.preset:yourpreset}")
    private String preset;

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

    @Autowired
    Servers servers;

    @RequestMapping("/servers")
    public @ResponseBody String servers(){
        StringBuilder sb = new StringBuilder();
        for (String s: servers.getSessions()) {
            sb.append(s);
            sb.append("\n");
        }
        sb.append(preset);
        return sb.toString();
    }

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
        User user2 = cacheService.findUser2(1256l,"alex","phil");
        if (user1 == user2) {
            return "ok";
        } else {
            return "fail";
        }
    }

    @Value("${migrate.session.path:classpath:migrate_sessions.json}")
    private String strMigrateSessions;
    private Set<String> migrateSessions;

    @Autowired
    private ApplicationContext context;

    @PostConstruct
    private void loadMigrateSessions() {
        try {
            migrateSessions = new HashSet<String>(new ObjectMapper()
                    .readValue(context.getResource(strMigrateSessions).getInputStream(), List.class));
        } catch (IOException e) {
            throw new RuntimeException("load migrating sessions from file failed");
        }
    }

    @RequestMapping("/migrate/{name:.+}")
    @ResponseBody
    public String migrate(@PathVariable String name){
        for (String s : migrateSessions) {
            System.out.println(s);
        }
        if (migrateSessions.contains(name))
            return "Yes";
        else
            return "No";
    }

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @RequestMapping("/testredis")
    @ResponseBody
    public String testredis(@RequestParam String key, @RequestParam String value) {
        redisTemplate.opsForValue().set(key, value);
        return redisTemplate.opsForValue().get(key);
    }

    @Autowired
    RedisTemplate<String, Object> objectRedisTemplate;

    @RequestMapping("/testredisobject")
    @ResponseBody
    public String testredisobject(@RequestParam String key) {
        User one = new User(1939834l, "lebron", "james");
        objectRedisTemplate.opsForValue().set(key, one);
        User another = (User) objectRedisTemplate.opsForValue().get(key);
        return another.getFirstName() + another.getLastName();
    }
}
