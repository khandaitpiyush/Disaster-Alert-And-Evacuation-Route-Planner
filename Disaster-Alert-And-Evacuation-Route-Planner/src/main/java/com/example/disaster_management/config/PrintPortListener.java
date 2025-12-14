package com.example.disaster_management.config;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Listener class that prints the dynamically assigned server port
 * when using server.port=0 or any custom port configuration.
 *
 * This is extremely useful during development because modern Spring Boot versions
 * (3.x+) no longer print the full localhost URL by default when the port is randomized.
 *
 * Example Output:
 * --------------------------------------------------------
 * 🚀 RapidResQ Server Started Successfully
 * 🌐 Local URL: http://localhost:51423
 * --------------------------------------------------------
 *
 * The port is extracted from the WebServerInitializedEvent,
 * which guarantees the server is fully started before printing.
 */
@Component
public class PrintPortListener implements ApplicationListener<WebServerInitializedEvent> {

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {

        int port = event.getWebServer().getPort();
        String localUrl = "http://localhost:" + port;

        System.out.println("\n\n" +
                "========================================================\n" +
                " 🚀 RapidResQ Server Started Successfully\n" +
                "--------------------------------------------------------\n" +
                " 🌐 Local URL: " + localUrl + "\n" +
                " 📌 API Base:   " + localUrl + "/api\n" +
                " 📌 User Panel: " + localUrl + "/user-dashboard\n" +
                "--------------------------------------------------------\n" +
                " (Tip: Add this bookmark for quick access)\n" +
                "========================================================\n"
        );
    }
}
