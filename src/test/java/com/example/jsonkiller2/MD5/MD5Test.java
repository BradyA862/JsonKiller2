package com.example.jsonkiller2.MD5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class MD5Test {


    @Test
        // also tests Md5 class
    void itShouldReturnMD5CalculationResult() throws NoSuchAlgorithmException {
        String md5 = "fa4c6baa0812e5b5c80ed8885e55a8a6";
        String original = "example_text";
        MD5 md5Class = new MD5(original);
        Assertions.assertEquals(md5, md5Class.md5);
        Assertions.assertEquals(original, md5Class.original);
    }
}