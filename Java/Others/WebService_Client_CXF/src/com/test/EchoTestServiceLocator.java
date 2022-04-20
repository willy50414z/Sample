/**
 * EchoTestServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.test;

public class EchoTestServiceLocator extends org.apache.axis.client.Service implements com.test.EchoTestService {

    public EchoTestServiceLocator() {
    }


    public EchoTestServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public EchoTestServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for echoTestPort
    private java.lang.String echoTestPort_address = "http://localhost:8080/WebService_Server_CXF/services/echotest";

    public java.lang.String getechoTestPortAddress() {
        return echoTestPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String echoTestPortWSDDServiceName = "echoTestPort";

    public java.lang.String getechoTestPortWSDDServiceName() {
        return echoTestPortWSDDServiceName;
    }

    public void setechoTestPortWSDDServiceName(java.lang.String name) {
        echoTestPortWSDDServiceName = name;
    }

    public com.test.EchoTest getechoTestPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(echoTestPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getechoTestPort(endpoint);
    }

    public com.test.EchoTest getechoTestPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.test.EchoTestServiceSoapBindingStub _stub = new com.test.EchoTestServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getechoTestPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setechoTestPortEndpointAddress(java.lang.String address) {
        echoTestPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.test.EchoTest.class.isAssignableFrom(serviceEndpointInterface)) {
                com.test.EchoTestServiceSoapBindingStub _stub = new com.test.EchoTestServiceSoapBindingStub(new java.net.URL(echoTestPort_address), this);
                _stub.setPortName(getechoTestPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("echoTestPort".equals(inputPortName)) {
            return getechoTestPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://test.com/", "echoTestService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://test.com/", "echoTestPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("echoTestPort".equals(portName)) {
            setechoTestPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
