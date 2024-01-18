package demo.grpc.sdk.interceptor;

import java.util.logging.Logger;

import io.grpc.*;

public class HeaderClientInterceptor implements ClientInterceptor {
    private static final Logger       logger            = Logger.getLogger(HeaderClientInterceptor.class.getName());

    static final Metadata.Key<String> CUSTOM_HEADER_KEY = Metadata.Key.of("client_header_key", Metadata.ASCII_STRING_MARSHALLER);

    private String                    headerValue;

    public HeaderClientInterceptor(String headerValue) {
        this.headerValue = headerValue;
    }

    public HeaderClientInterceptor() {}

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method,
                                                               CallOptions callOptions,
                                                               Channel next) {
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                // 发送给服务端的header
                headers.put(CUSTOM_HEADER_KEY, headerValue);
                super.start(new ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(responseListener) {
                    @Override
                    public void onHeaders(Metadata headers) {
                        logger.info("header received from server:" + headers);
                        super.onHeaders(headers);
                    }
                }, headers);
            }
        };
    }
}
