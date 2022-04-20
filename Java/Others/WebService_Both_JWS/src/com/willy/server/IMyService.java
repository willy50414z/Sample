/**
 * IMyService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.willy.server;

import javax.jws.WebService;

@WebService()
public interface IMyService extends java.rmi.Remote {
    public int minus(int a, int b) throws java.rmi.RemoteException;
    public org.zttc.service.User login(java.lang.String username, java.lang.String password) throws java.rmi.RemoteException;
    public int add(int a, int b) throws java.rmi.RemoteException;
}
