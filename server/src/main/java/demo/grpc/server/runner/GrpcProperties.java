package demo.grpc.server.runner;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "grpc")
public class GrpcProperties {

    private Integer port = 50051;
}
