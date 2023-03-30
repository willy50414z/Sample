package com.willy.grpc.service;

import com.willy.grpc.proto.HelloRequest;
import com.willy.grpc.proto.HelloResponse;
import com.willy.grpc.proto.HelloServiceGrpc;


public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase
{
	@Override
	public void helloWorld(HelloRequest request,
			io.grpc.stub.StreamObserver<HelloResponse>responseObserver) {
		System.out.println("request: " + request.getRequestBytes().toStringUtf8());
		System.out.println("request1: " + request.getRequest1());
		responseObserver.onNext(HelloResponse.newBuilder()
				.setResponse("it is response from grpc server").build());
		responseObserver.onCompleted();
	}




}
