package com.zz.servlet;

import com.zz.bean.Constants;
import com.zz.dailytest.TestJson;
import com.zz.utils.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Francis.zz on 2016-05-18.
 * 描述：使用json传输信息； <br/>
 */
public class MyServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("-- Begin to process doGet ...");
        String path = request.getContextPath();
        System.out.println("Path:" + path);
        response.setContentType("text/html;charset=utf-8");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().print("doGet 请求成功!");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("-- Begin to process doPost ...");
        try {
            // String path = MyServlet.class.getClassLoader().getResource("/").getPath();
            String path = request.getSession().getServletContext().getRealPath("/");
            System.out.println("Path2:" + path);
            System.out.println("root:" + System.getProperty("web.root"));
            Map params = TestJson.parseRequestToMap(request);
            if (StringUtil.isEmpty(params)) {
                throw new Exception("Request params is invalid.");
            }
            int opType = (Integer) params.get(Constants.COMMAND_ID);

            if (Constants.OP_TYPE_BUILD_SESSION == opType) {
                System.out.println("-- Begin to build session!");
                HttpSession session = request.getSession(true);
                String sid = session.getId();
                System.out.println("success to build session：" + request.getRemoteHost() + "&" + sid);

                // save info to session
                String reqName = (String) params.get("name");
                String identity = (String) params.get("id");
                session.setAttribute("reqName", reqName);
                session.setAttribute("identity", identity);
                //end
                Map<String, Object> result = new HashMap<String, Object>();
                result.put(Constants.COMMAND_ID, opType);
                result.put("sessionID", sid);
                outPutResp(response, result);
            }else if(Constants.COMMAND_ID_DEMO == opType) {
                System.out.println("Begin to process Demo...");
                HttpSession session = request.getSession(false);
                if(session == null) {
                    throw new Exception("Session is invalid.");
                }
                String reqName = (String) session.getAttribute("reqName");
                String id = (String) session.getAttribute("identity");
                System.out.println("session中保存的信息：reqName[" + reqName + "],id[" + id + "].");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void outPutResp(HttpServletResponse response, Map data) throws Exception {
        String jsonStr = TestJson.mapToJsonStr(data);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.println(jsonStr);
        writer.flush();
        writer.close();
    }
    
    public static Map<String, Object> parseReq(HttpServletRequest request) throws Exception {
        int reqLen = request.getContentLength();
        byte[] reqData = new byte[reqLen];
        int total = 0;
        while(total < reqLen) {
            int realLen = request.getInputStream().readLine(reqData, total, reqLen - total);
            total += realLen;
        }
        // 解决中文乱码问题
        String jsonStr = new String(reqData, "utf-8");
        System.out.println("total received :"+ total);
        System.out.println("JSON req string:" + jsonStr);

        Map<String, Object> jsonReq = TestJson.parseJsonToMap(jsonStr);
        return jsonReq;
    }
}
