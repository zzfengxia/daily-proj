package com.zz.dailytest;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zz.bean.Person;
import com.zz.bean.User;
import com.zz.utils.StringUtil;
import net.sf.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zz on 2016-04-13.
 * 描述：Json，Gson对象转换测试 <br/>
 */
public class TestJson {
    private User user;
    private List<User> userList;
    private Map<String, User> userMap;
    @Before
    public void setUp() {
        user = new User();
        user.setUsername("zzxia");
        user.setNickName("風俠");
        user.setEmail("1234@163.com");
        user.setContactPhone("18637262930");
        user.setBirthDay(new Date());

        userMap = new HashMap<String, User>();
        userMap.put("zzxia", user);
        userMap.put("zz", user);
    }

    /**
     * 对象转换成json字符串时自动调用对象的getXXX,isXXX(返回值为boolean)方法来生成json串的键值
     */
    @Test
    public void testAuto() {
        Person person = new Person();
        person.setUsername("Francis");

        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonStr = mapper.writeValueAsString(person);
            System.out.println(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用Gson实现对象的序列化和反序列化
     */
    @Test
    public void testGson() throws IllegalAccessException {
        Gson gson = new Gson();
        String gsonStr = gson.toJson(user);
        System.out.println("---------------user转换为Gson对象---------------");
        System.out.println(gsonStr);
        // 将泛型对象转换为gson对象
        Type type = new TypeToken<Map<String, User>>(){}.getType();
        String gsonMapStr = gson.toJson(userMap, type);
        System.out.println("---------------userMap转换为Gson对象---------------");
        System.out.println(gsonMapStr);
        // gson字符串转换为对象
        User newUser = gson.fromJson(gsonStr, User.class);
        System.out.println("---------------Gson对象转换为user---------------");
        // 使用反射遍历输出对象的所有属性的值
        for(Field f : newUser.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            String fName = f.getName();
            System.out.println(fName + "：" + f.get(newUser));
        }
        // gson的json对象转换为Map<String, User>的泛型
        Map<String, User> newUserMap = gson.fromJson(gsonMapStr, type);
        System.out.println("---------------Gson对象转换为泛型---------------");
        System.out.println("UserMap的size：" + newUserMap.size());
        User u = newUserMap.get("zzxia");
        // 使用反射遍历输出对象的所有属性的值
        for(Field f : u.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            String fName = f.getName();
            System.out.println(fName + "：" + f.get(newUser));
        }
    }

    /**
     * 使用Jackson实现对象的序列化和反序列化
     * @throws IOException
     */
    @Test
    public void testJackson() throws IOException, IllegalAccessException {
        ObjectMapper objMapper = new ObjectMapper();
        // 设置转换的日期格式
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        objMapper.setDateFormat(format);
        /** 序列化方法1 */
        String jacksonStr = objMapper.writeValueAsString(user);
        System.out.println("---------------user转换为Json对象①---------------");
        System.out.println(jacksonStr);

        System.out.println("---------------userMap转换为Json对象---------------");
        String jsonMapStr = objMapper.writeValueAsString(userMap);
        System.out.println(jsonMapStr);
        /**
         * 序列化方法2
         * 可以把序列化后的信息输出到文件或者IO流中
         */
        System.out.println("---------------user转换为Json对象②---------------");
        JsonGenerator generator = objMapper.getJsonFactory().createJsonGenerator(System.out, JsonEncoding.UTF8);
        generator.writeObject(user);
        System.out.println();

        /** 反序列化,普通的自定义对象 */
        User newUser = objMapper.readValue(jacksonStr, User.class);
        System.out.println("---------------Json对象转换为user---------------");
        // 使用反射遍历输出对象的所有属性的值
        for(Field f : newUser.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            String fName = f.getName();
            System.out.println(fName + "：" + f.get(newUser));
        }
        /** 反序列化成List,Map等对象,要先构建反序列化的泛型类型 */
        //objMapper.getTypeFactory().constructCollectionType(List.class, User.class);
        JavaType type = objMapper.getTypeFactory().constructParametricType(HashMap.class, String.class, User.class);
        Map<String, User> newUserMap = objMapper.readValue(jsonMapStr, type);
        System.out.println("---------------Json对象转换为泛型---------------");
        User u = newUserMap.get("zzxia");
        // 使用反射遍历输出对象的所有属性的值
        for(Field f : u.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            String fName = f.getName();
            System.out.println(fName + "：" + f.get(newUser));
        }

        // JsonNode jNode = objMapper.readTree(jsonMapStr); jackson解析复杂json对象，类似xml的dom4J用法
    }

    @Test
    public void testJsonToMap() throws IOException {
        String jsonStr = "{\"username\":\"zzxia\",\"nickName\":\"風俠\",\"pwd\":null,\"contactPhone\":\"18637262930\",\"email\":\"1234@163.com\",\"birthDay\":\"2016-05-18\"}";
        String jsonStr2 = "{\"zzxia\":{\"username\":\"zzxia\",\"nickName\":\"風俠\",\"pwd\":null,\"contactPhone\":\"18637262930\",\"email\":\"1234@163.com\",\"birthDay\":\"2016-05-18\"},\"zz\":{\"username\":\"zzxia\",\"nickName\":\"風俠\",\"pwd\":null,\"contactPhone\":\"18637262930\",\"email\":\"1234@163.com\",\"birthDay\":\"2016-05-18\"}}";
        ObjectMapper obj = new ObjectMapper();
        JavaType type = obj.getTypeFactory().constructParametricType(HashMap.class, String.class, Object.class);
        Map<String, String> jsonMap = obj.readValue(jsonStr2, type);
        System.out.println("-- Map size：" + jsonMap.size());
        Set<Map.Entry<String, String>> entrySet = jsonMap.entrySet();
        Map subMap = new HashMap();
        for(Map.Entry e : entrySet) {
            System.out.println(e.getKey() + "：" + e.getValue());
            subMap = (Map) e.getValue();
            System.out.println(subMap.size());
        }
    }

    /**
     * jsonlib
     */
    @Test
    public void testJsonLib() throws Exception {
        String jsonStr = "{\"username\":\"zzxia\",\"nickName\":\"風俠\",\"pwd\":null,\"contactPhone\":\"18637262930\",\"email\":\"1234@163.com\",\"birthDay\":\"2016-05-18\"}";
        JSONObject jsonReq = JSONObject.fromObject(jsonStr);
        User reqParam = (User) jsonReq.toBean(jsonReq, User.class);
        // 使用反射遍历输出对象的所有属性的值
        for(Field f : reqParam.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            String fName = f.getName();
            System.out.println(fName + "：" + f.get(reqParam));
        }

        JSONObject json = new JSONObject();
        json.put("name", "Tom");
        json.put("color", "black");
        json.put("user", reqParam);

        System.out.println("jsonlib str:" + json.toString());
    }

    /**
     * 将Json字符串转换为Map
     * @param jsonStr
     * @return
     * @throws Exception
     */
    public static Map<String, Object> parseJsonToMap(String jsonStr) throws Exception {
    	Map<String, Object> result = new HashMap<String, Object>();
        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.getTypeFactory().constructParametricType(HashMap.class, String.class, Object.class);
        result = mapper.readValue(jsonStr, type);

        return result;
    }

    /**
     * 将Json字符串转换为Map
     * @param request
     * @return
     * @throws Exception
     */
    public static Map parseRequestToMap(HttpServletRequest request) throws Exception {
        int reqLen = request.getContentLength();
        int total = 0;
        byte[] rowData = new byte[reqLen];
        while(total != -1 && total < reqLen) {
            int offset = request.getInputStream().read(rowData, total, reqLen - total);
            System.out.println("Received size:" + offset);
            total += offset;
        }
        System.out.println("Total received size:" + total);

        String jsonStr = new String(rowData, "utf-8");
        System.out.println("received json str:" + jsonStr);
        //System.out.println(HexUtil.decodeHexStr(new String(jsonStr.getBytes("utf-8"), "utf-8")));
        return parseJsonToMap(jsonStr);
    }

    /**
     * 将Map对象转换为json字符串
     * @param data
     * @return
     * @throws Exception
     */
    public static String mapToJsonStr(Map data) throws Exception {
        if(StringUtil.isEmpty(data)) {
            throw new Exception("mapToJsonStr：Converted params is invalid.");
        }
        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(data);
    }
}
