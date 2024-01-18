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

        return next.startCall(new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(serverCall) {
            @Override
            public void sendHeaders(Metadata responseHeaders) {
                // 发送给客户端的header
                // responseHeaders.put(CUSTOM_HEADER_KEY, "customRespondValue");
                super.sendHeaders(responseHeaders);
            }
        }, requestHeaders);
    }
}
