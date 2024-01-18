package demo.grpc.server;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import demo.grpc.server.api.DemoServiceImpl;
import demo.grpc.server.interceptor.HeaderServerInterceptor;
import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.ServerInterceptors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GrpcDemoServer {

    private final int port;
    private Server    server;

    public GrpcDemoServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        /* The port on which the server should run */
        server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
            // 有多个service时就多次 .addService()
            .addService(ServerInterceptors.intercept(new DemoServiceImpl(), new HeaderServerInterceptor()))
            .build()
            .start();
        log.info("Server started, listening on {}", port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            log.warn("*** shutting down gRPC server since JVM is shutting down");
            try {
                GrpcDemoServer.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            log.warn("*** server shut down");
        }));
    }

    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    /**
     * Main launches the server from the command line.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tH:%1$tM:%1$tS %4$s %2$s: %5$s%6$s%n");

        final GrpcDemoServer server = new GrpcDemoServer(50051);
        server.start();
        server.blockUntilShutdown();
    }
}
