package org.openquantumsafe;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RandTest {

    /**
     * Test that a null algorithm name is rejected
     */
    @Test
    public void testNullRandAlgExpectedException() {
        RuntimeException e = Assertions.assertThrows(RuntimeException.class,
                                () -> Rand.randombytes_switch_algorithm(null));
        Assertions.assertEquals("Cannot switch rand algorithm", e.getMessage());
    }
}
