package testClass;

import com.test.EchoTest;
import com.test.EchoTestProxy;

public class TestWS {
	public static void main(String[] args) throws Exception {

		EchoTest client = new EchoTestProxy();

		System.out.println(client.echo("test echo"));

	}
}
