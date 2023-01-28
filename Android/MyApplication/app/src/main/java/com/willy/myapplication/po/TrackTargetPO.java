package com.willy.myapplication.po;

import java.util.Date;

public class TrackTargetPO {
    private int id;
    private String targetName;
    private String targetProc;
    private String targetProcArgs;
    private Date lastCheckDate;
    private double lastCheckIndex;
    private int investAmt;

    public TrackTargetPO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetProc() {
        return targetProc;
    }

    public void setTargetProc(String targetProc) {
        this.targetProc = targetProc;
    }

    public double getLastCheckIndex() {
        return lastCheckIndex;
    }

    public void setLastCheckIndex(double lastCheckIndex) {
        this.lastCheckIndex = lastCheckIndex;
    }

    public Date getLastCheckDate() {
        return lastCheckDate;
    }

    public String getTargetProcArgs() {
        return targetProcArgs;
    }

    public void setTargetProcArgs(String targetProcArgs) {
        this.targetProcArgs = targetProcArgs;
    }

    public void setLastCheckDate(Date lastCheckDate) {
        this.lastCheckDate = lastCheckDate;
    }

    public int getInvestAmt() {
        return investAmt;
    }

    public void setInvestAmt(int investAmt) {
        this.investAmt = investAmt;
    }
}
