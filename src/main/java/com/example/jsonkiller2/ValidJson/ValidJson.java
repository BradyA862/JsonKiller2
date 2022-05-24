package com.example.jsonkiller2.ValidJson;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ValidJson {

    public HashMap isValidJson(String input) {
        try {
            JSONObject validate = new JSONObject(input);
            HashMap valid = new HashMap<>();
            valid.put("validate", true);

            return valid;
        } catch (JSONException e) {
            HashMap invalid = new HashMap<>();
            invalid.put("validate", false);
            invalid.put("error", e.toString());

            return invalid;
        }
    }
}
