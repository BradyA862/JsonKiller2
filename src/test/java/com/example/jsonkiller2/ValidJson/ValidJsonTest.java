package com.example.jsonkiller2.ValidJson;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ValidJsonTest {

    @Test
    void ItShouldOutputTRUEIfJsonIsValid() {
        ValidJson validJson = new ValidJson();
        HashMap valid = new HashMap<>();
        valid.put("validate", true);
        assertEquals(valid, validJson.isValidJson("{\"t\":\"t\"}"));
    }

    @Test
    void ItShouldOutputFALSEIfJsonIsINValid() {
        ValidJson validJson = new ValidJson();
        HashMap error = new HashMap<>();
        error.put("validate", false);
        error.put("error", "org.json.JSONException: A JSONObject text must begin with '{' at 1 [character 2 line 1]");
        assertEquals(error, validJson.isValidJson("hi"));
    }

}







//        {
//        "object_or_array": "object",
//        "empty": false,
//        "parse_time_nanoseconds": 19608,
//        "validate": true,
//        "size": 1
//        }

//        {
//        "error": "Expected a ',' or '}' at 15 [character 16 line 1]",
//        "object_or_array": "object",
//        "error_info": "This error came from the org.json reference parser.",
//        "validate": false
//        }