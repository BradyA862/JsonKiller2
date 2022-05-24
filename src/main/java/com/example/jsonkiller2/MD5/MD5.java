package com.example.jsonkiller2.MD5;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    @JsonProperty
    public String md5;

    @JsonProperty
    public String original;

    public MD5(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input.getBytes());
        byte[] digest = md.digest();
        this.md5 = DatatypeConverter
                .printHexBinary(digest).toLowerCase();
        this.original = input;
    }
}


















