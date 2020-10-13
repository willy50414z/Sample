package com.willy.ct.junit;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TestMathCaculator {
	private final MathCaculator mathCacu = new MathCaculator();
	@Test
    void addTest() {
        assertEquals(2, mathCacu.add(1, 1));
    }
}
