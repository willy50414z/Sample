/**
 * EchoTestService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.test;

public interface EchoTestService extends javax.xml.rpc.Service {
    public java.lang.String getechoTestPortAddress();

    public com.test.EchoTest getechoTestPort() throws javax.xml.rpc.ServiceException;

    public com.test.EchoTest getechoTestPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
