package com.zz.servlet.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by Francis.zz on 2016/8/11.
 * 描述：将webRoot路径加入上下文
 */
public class WebRootListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletEvent) {
        ServletContext context = servletEvent.getServletContext();
        String path = context.getRealPath("/");
        String param = context.getInitParameter("webAppRootKey");
        param = param != null ? param : "web.root";

        System.setProperty(param, path);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletEvent) {
        ServletContext context = servletEvent.getServletContext();
        String param = context.getInitParameter("webAppRootKey");
        param = param != null ? param : "web.root";

        System.clearProperty(param);
    }
}
