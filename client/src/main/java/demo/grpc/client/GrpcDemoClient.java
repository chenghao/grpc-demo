package demo.grpc.client;

import java.util.logging.Logger;

import demo.grpc.api.DemoCreateRequest;
import demo.grpc.api.DemoCreateResponse;
import demo.grpc.api.DemoGetRequest;
import demo.grpc.api.DemoGetResponse;
import demo.grpc.sdk.ApiWrapper;

public class GrpcDemoClient {
    private static final Logger logger = Logger.getLogger(GrpcDemoClient.class.getName());

    public static void main(String[] args) throws InterruptedException {
        ApiWrapper wrapper = ApiWrapper.ofMainnet("123456");

        DemoGetRequest demoGetRequest = DemoGetRequest.newBuilder()
            .setId(10)
            .build();
        logger.info("demoGetRequest: " + demoGetRequest);
        DemoGetResponse demoGetResponse = wrapper.get(demoGetRequest);
        logger.info("demoGetResponse: " + demoGetResponse);

        DemoCreateRequest demoCreateRequest = DemoCreateRequest.newBuilder()
            .setName("hao")
            .setGender(2)
            .build();
        logger.info("demoCreateRequest: " + demoCreateRequest);
        DemoCreateResponse demoCreateResponse = wrapper.create(demoCreateRequest);
        logger.info("demoCreateResponse: " + demoCreateResponse);

        wrapper.shutdown();
    }
}
