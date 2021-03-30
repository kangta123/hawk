package com.oc.hawk.trace_logging;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.14.0)",
    comments = "Source: trace.proto")
public final class LoggingServiceGrpc {

  private LoggingServiceGrpc() {}

  public static final String SERVICE_NAME = "com.oc.hawk.trace_logging.LoggingService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.oc.hawk.trace_logging.Trace.WriteLogRequest,
      com.oc.hawk.trace_logging.Trace.WriteLogResponse> getWriteLogMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "WriteLog",
      requestType = com.oc.hawk.trace_logging.Trace.WriteLogRequest.class,
      responseType = com.oc.hawk.trace_logging.Trace.WriteLogResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.oc.hawk.trace_logging.Trace.WriteLogRequest,
      com.oc.hawk.trace_logging.Trace.WriteLogResponse> getWriteLogMethod() {
    io.grpc.MethodDescriptor<com.oc.hawk.trace_logging.Trace.WriteLogRequest, com.oc.hawk.trace_logging.Trace.WriteLogResponse> getWriteLogMethod;
    if ((getWriteLogMethod = LoggingServiceGrpc.getWriteLogMethod) == null) {
      synchronized (LoggingServiceGrpc.class) {
        if ((getWriteLogMethod = LoggingServiceGrpc.getWriteLogMethod) == null) {
          LoggingServiceGrpc.getWriteLogMethod = getWriteLogMethod = 
              io.grpc.MethodDescriptor.<com.oc.hawk.trace_logging.Trace.WriteLogRequest, com.oc.hawk.trace_logging.Trace.WriteLogResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.oc.hawk.trace_logging.LoggingService", "WriteLog"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.oc.hawk.trace_logging.Trace.WriteLogRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.oc.hawk.trace_logging.Trace.WriteLogResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new LoggingServiceMethodDescriptorSupplier("WriteLog"))
                  .build();
          }
        }
     }
     return getWriteLogMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static LoggingServiceStub newStub(io.grpc.Channel channel) {
    return new LoggingServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static LoggingServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new LoggingServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static LoggingServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new LoggingServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class LoggingServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void writeLog(com.oc.hawk.trace_logging.Trace.WriteLogRequest request,
        io.grpc.stub.StreamObserver<com.oc.hawk.trace_logging.Trace.WriteLogResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getWriteLogMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getWriteLogMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.oc.hawk.trace_logging.Trace.WriteLogRequest,
                com.oc.hawk.trace_logging.Trace.WriteLogResponse>(
                  this, METHODID_WRITE_LOG)))
          .build();
    }
  }

  /**
   */
  public static final class LoggingServiceStub extends io.grpc.stub.AbstractStub<LoggingServiceStub> {
    private LoggingServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private LoggingServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected LoggingServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new LoggingServiceStub(channel, callOptions);
    }

    /**
     */
    public void writeLog(com.oc.hawk.trace_logging.Trace.WriteLogRequest request,
        io.grpc.stub.StreamObserver<com.oc.hawk.trace_logging.Trace.WriteLogResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getWriteLogMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class LoggingServiceBlockingStub extends io.grpc.stub.AbstractStub<LoggingServiceBlockingStub> {
    private LoggingServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private LoggingServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected LoggingServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new LoggingServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.oc.hawk.trace_logging.Trace.WriteLogResponse writeLog(com.oc.hawk.trace_logging.Trace.WriteLogRequest request) {
      return blockingUnaryCall(
          getChannel(), getWriteLogMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class LoggingServiceFutureStub extends io.grpc.stub.AbstractStub<LoggingServiceFutureStub> {
    private LoggingServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private LoggingServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected LoggingServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new LoggingServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.oc.hawk.trace_logging.Trace.WriteLogResponse> writeLog(
        com.oc.hawk.trace_logging.Trace.WriteLogRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getWriteLogMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_WRITE_LOG = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final LoggingServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(LoggingServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_WRITE_LOG:
          serviceImpl.writeLog((com.oc.hawk.trace_logging.Trace.WriteLogRequest) request,
              (io.grpc.stub.StreamObserver<com.oc.hawk.trace_logging.Trace.WriteLogResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class LoggingServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    LoggingServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.oc.hawk.trace_logging.Trace.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("LoggingService");
    }
  }

  private static final class LoggingServiceFileDescriptorSupplier
      extends LoggingServiceBaseDescriptorSupplier {
    LoggingServiceFileDescriptorSupplier() {}
  }

  private static final class LoggingServiceMethodDescriptorSupplier
      extends LoggingServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    LoggingServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (LoggingServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new LoggingServiceFileDescriptorSupplier())
              .addMethod(getWriteLogMethod())
              .build();
        }
      }
    }
    return result;
  }
}
