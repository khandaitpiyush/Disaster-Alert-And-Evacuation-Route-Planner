package com.example.disaster_management.service;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;

@Component
public class ServerStartedListener implements ApplicationListener<ServletWebServerInitializedEvent> {

    @Override
    public void onApplicationEvent(ServletWebServerInitializedEvent event) {
        int port = event.getWebServer().getPort();
        String url = String.format("http://localhost:%d/login", port);

        System.out.println("\n*****************************************************************");
        System.out.println("🚀 APPLICATION IS LIVE AND READY FOR EXHIBITION!");
        System.out.println("🔑 ADMIN/USER LOGIN PAGE: " + url);
        System.out.println("*****************************************************************\n");

        // OPTIONAL: If you want to stop the application quickly, you can use the following:
        // System.out.println("Application Context ID: " + event.getApplicationContext().getId());
    }
}