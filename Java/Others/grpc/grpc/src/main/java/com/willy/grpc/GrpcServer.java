package com.willy.grpc;

import com.willy.grpc.service.HelloServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;


public class GrpcServer
{
	public static void main(String[] args)
	{
		try
		{
			CountDownLatch countDownLatch = new CountDownLatch(1);
			Server server = ServerBuilder.forPort(8090)
					.addService(new HelloServiceImpl()).build().start();

			countDownLatch.await();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
