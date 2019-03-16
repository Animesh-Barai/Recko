package com.example.neelanshsethi.recko.Misc;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonWrapper extends JSONObject {

    private String default_value = "";

    @Override
    public String getString(String name) throws JSONException {
        return super.has("name") ? super.getString(name) : default_value;
    }

    public void setDefault_value(String default_value) {
        this.default_value = default_value;
    }
}
