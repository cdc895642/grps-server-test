package com.test;

import io.grpc.ForwardingServerCall.SimpleForwardingServerCall;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

public class MeObservver implements ServerInterceptor {

  @Override
  public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
      Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
    String value = metadata.get(Metadata.Key.of("key", Metadata.ASCII_STRING_MARSHALLER));
    System.out.println(value);
    return serverCallHandler.startCall(
        new SimpleForwardingServerCall<ReqT, RespT>(serverCall) {
          @Override
          public void sendHeaders(Metadata responseHeaders) {
            responseHeaders
                .put(Metadata.Key.of("my-new-mather-fucker-key", Metadata.ASCII_STRING_MARSHALLER),
                    "customRespondValue");
            super.sendHeaders(responseHeaders);
          }
        }
        , metadata);
  }
}
