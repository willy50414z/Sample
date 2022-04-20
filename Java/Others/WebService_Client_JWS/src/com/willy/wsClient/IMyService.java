/**
 * IMyService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.willy.wsClient;

import javax.jws.WebService;

@WebService(name = "IMyService", targetNamespace = "http://server.willy.com/", serviceName = "IMyService")
public interface IMyService extends java.rmi.Remote {
    public int minus(int a, int b) throws java.rmi.RemoteException;
    public User login(java.lang.String username, java.lang.String password) throws java.rmi.RemoteException;
    public int add(int a, int b) throws java.rmi.RemoteException;
}
