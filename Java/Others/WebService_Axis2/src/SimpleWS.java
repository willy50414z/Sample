
public class SimpleWS {
	public String getGreeting(String name)
    {
        return "§A¦n " + name;
    }    
    public int getPrice()
    {
        return new java.util.Random().nextInt(1000);
    }    
}
