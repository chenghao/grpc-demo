package demo.grpc.server.interceptor;

import java.util.logging.Logger;

import io.grpc.*;

public class HeaderServerInterceptor implements ServerInterceptor {

    private static final Logger       logger            = Logger.getLogger(HeaderServerInterceptor.class.getName());

    static final Metadata.Key<String> CUSTOM_HEADER_KEY = Metadata.Key.of("server_header_key", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
                                                                 Metadata requestHeaders,
                                                                 ServerCallHandler<ReqT, RespT> next) {
        logger.info("header received from client:" + requestHeaders);

        // 获取客户端发送的请求头中的特定键值
        String headerValue = requestHeaders.get(Metadata.Key.of("client_header_key", Metadata.ASCII_STRING_MARSHALLER));

        // 当前客户端上传的client_header_key header值不是123456时就返回请求拒绝
        if (headerValue != null && headerValue.equals("123456")) {
            // 如果键值匹配，正常处理请求
            return next.startCall(new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(serverCall) {
                @Override
                public void sendHeaders(Metadata responseHeaders) {
                    // 发送给客户端的header
                    responseHeaders.put(CUSTOM_HEADER_KEY, headerValue);
                    super.sendHeaders(responseHeaders);
                }
            }, requestHeaders);
        } else {
            // 如果键值不匹配，可以拒绝请求或进行其他处理
            serverCall.close(Status.PERMISSION_DENIED.withDescription("Invalid header value for key: client_header_key"), requestHeaders);
            return new ServerCall.Listener<ReqT>() {}; // 返回一个空监听器，因为请求已被关闭
        }

    }
}
