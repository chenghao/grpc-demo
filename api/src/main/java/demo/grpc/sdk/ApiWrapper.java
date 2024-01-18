package demo.grpc.sdk;

import java.util.concurrent.TimeUnit;

import demo.grpc.api.*;
import demo.grpc.sdk.interceptor.HeaderClientInterceptor;
import demo.grpc.sdk.util.Constant;
import io.grpc.ClientInterceptor;
import io.grpc.ClientInterceptors;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ApiWrapper {

    public final DemoServiceGrpc.DemoServiceBlockingStub blockingStub;
    public final ManagedChannel                          channel;

    public ApiWrapper(String grpcEndpoint, String apiKey) {
        channel = ManagedChannelBuilder.forTarget(grpcEndpoint).usePlaintext().build();

        ClientInterceptor interceptor = new HeaderClientInterceptor(apiKey);
        blockingStub = DemoServiceGrpc.newBlockingStub(ClientInterceptors.intercept(channel, interceptor));
    }

    public static ApiWrapper ofMainnet(String apiKey) {
        return new ApiWrapper(Constant.MAIN_NET, apiKey);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public DemoGetResponse get(DemoGetRequest request) {
        return blockingStub.get(request);
    }

    public DemoCreateResponse create(DemoCreateRequest request) {
        return blockingStub.create(request);
    }
}
