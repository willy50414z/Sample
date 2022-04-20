package teach.axis;

public class AxisWS1 {
	public void receive(String msg) {
		System.out.println(msg);
	}
	public String send() {
		return "this is from AxisWS1";
	}
}
