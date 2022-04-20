package test.webservice;

import java.rmi.RemoteException;

import teach.axis.AxisWS1Stub;
import teach.axis.AxisWS1Stub.Receive;
import teach.axis.AxisWS1Stub.Send;

public class Test {
	public static void main(String[] args) throws RemoteException {
		// TODO Auto-generated method stub
		AxisWS1Stub stub = new AxisWS1Stub();
		System.out.println(stub.send(new Send()).get_return());
		Receive re=new Receive();
		re.setMsg("cde");
		stub.receive(re);
	}
}
