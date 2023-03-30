package com.willy.grpc;

import com.google.protobuf.ByteString;
import com.willy.grpc.proto.HelloRequest;
import com.willy.grpc.proto.HelloResponse;
import com.willy.grpc.proto.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NegotiationType;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;


public class GrpcClient
{
	public static void main(String[] args)
	{
		//通過netty創建通道
		ManagedChannel channel = NettyChannelBuilder.forAddress("127.0.0.1", 8090)
				.negotiationType(NegotiationType.PLAINTEXT)
				.build();
		//獲取用戶端存根對象
		HelloServiceGrpc.HelloServiceBlockingStub blockingStub = HelloServiceGrpc.newBlockingStub(channel);
		//創建入參
		HelloRequest helloRequest = HelloRequest.newBuilder().setRequest1("req1").setRequestBytes(ByteString.copyFromUtf8("hello grpc")).build();
		//調用服務端
		HelloResponse helloResponse = blockingStub.helloWorld(helloRequest);
		//列印回應
		System.out.println(helloResponse.getResponse());
	}
}

