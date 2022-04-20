package com.test;

public class EchoTestProxy implements com.test.EchoTest {
  private String _endpoint = null;
  private com.test.EchoTest echoTest = null;
  
  public EchoTestProxy() {
    _initEchoTestProxy();
  }
  
  public EchoTestProxy(String endpoint) {
    _endpoint = endpoint;
    _initEchoTestProxy();
  }
  
  private void _initEchoTestProxy() {
    try {
      echoTest = (new com.test.EchoTestServiceLocator()).getechoTestPort();
      if (echoTest != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)echoTest)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)echoTest)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (echoTest != null)
      ((javax.xml.rpc.Stub)echoTest)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.test.EchoTest getEchoTest() {
    if (echoTest == null)
      _initEchoTestProxy();
    return echoTest;
  }
  
  public java.lang.String echo(java.lang.String arg0) throws java.rmi.RemoteException{
    if (echoTest == null)
      _initEchoTestProxy();
    return echoTest.echo(arg0);
  }
  
  
}