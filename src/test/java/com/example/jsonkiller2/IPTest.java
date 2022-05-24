package com.example.jsonkiller2;

import com.example.jsonkiller2.ip.IP;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IPTest {

    @Test
    void itShouldReturnAnIP() {
        IP ip = new IP("1.2.3.4");
//        Assertions.assertEquals("1.2.3.4", ip.getIp());
    }


//    @Test
//    void itShouldJsonify() throws JsonProcessingException {
//        final String ip = "1.2.3.4";
//        assertEquals();
//    }
}