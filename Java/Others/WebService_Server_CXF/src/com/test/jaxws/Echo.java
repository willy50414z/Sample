
package com.test.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class was generated by Apache CXF 3.2.4
 * Mon Jun 11 14:23:34 CST 2018
 * Generated source version: 3.2.4
 */

@XmlRootElement(name = "echo", namespace = "http://test.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "echo", namespace = "http://test.com/")

public class Echo {

    @XmlElement(name = "arg0")
    private java.lang.String arg0;

    public java.lang.String getArg0() {
        return this.arg0;
    }

    public void setArg0(java.lang.String newArg0)  {
        this.arg0 = newArg0;
    }

}

