package demo.grpc.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;

@SpringBootApplication(scanBasePackages = { "demo.grpc" })
public class GrpcDemoServerApplication implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

    public static void main(String[] args) {
        SpringApplication.run(GrpcDemoServerApplication.class, args);
    }

    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {
        TomcatServletWebServerFactory f = (TomcatServletWebServerFactory) factory;
        f.setProtocol("org.apache.coyote.http11.Http11Nio2Protocol");

        /*
         * f.addConnectorCustomizers(c -> {
         * Http11Nio2Protocol protocol = (Http11Nio2Protocol) c.getProtocolHandler();
         * protocol.setMaxConnections(10000);
         * protocol.setMaxThreads(500);
         * protocol.setMinSpareThreads(50);
         * protocol.setConnectionTimeout(3000);
         * protocol.setAcceptCount(100);
         * });
         */
    }
}
