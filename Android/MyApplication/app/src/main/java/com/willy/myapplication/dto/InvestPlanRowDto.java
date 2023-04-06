package com.willy.myapplication.dto;

import java.math.BigDecimal;

public class InvestPlanRowDto {
    private BigDecimal amt;
    private BigDecimal index;

    public InvestPlanRowDto(BigDecimal amt, BigDecimal index) {
        this.amt = amt;
        this.index = index;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    public BigDecimal getIndex() {
        return index;
    }

    public void setIndex(BigDecimal index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "InvestPlanRowDto{" +
                "amt=" + amt +
                ", index=" + index +
                '}';
    }
}
