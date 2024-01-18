package demo.grpc.server.runner;

import javax.annotation.Resource;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import demo.grpc.server.GrpcDemoServer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(value = 10)
public class GrpcRunner implements ApplicationRunner {
    @Resource
    private GrpcProperties grpcProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        final GrpcDemoServer server = new GrpcDemoServer(grpcProperties.getPort());
        server.start();
        server.blockUntilShutdown();
    }
}
