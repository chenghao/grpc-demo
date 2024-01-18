package demo.grpc.server.api;

import demo.grpc.api.*;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DemoServiceImpl extends DemoServiceGrpc.DemoServiceImplBase {
    @Override
    public void get(DemoGetRequest request, StreamObserver<DemoGetResponse> responseObserver) {
        // 创建响应对象
        DemoGetResponse.Builder builder = DemoGetResponse.newBuilder();

        builder.setId(1);
        builder.setName("hao");
        builder.setGender(0);

        // 返回响应
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void create(DemoCreateRequest request, StreamObserver<DemoCreateResponse> responseObserver) {
        // 创建响应对象
        DemoCreateResponse response = DemoCreateResponse.newBuilder()
            .setId((int) (System.currentTimeMillis() / 1000))
            .build();

        // 返回响应
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
