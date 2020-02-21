package com.recko.app.Misc;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class CustomJsonObjectRequest extends JsonObjectRequest{
    public CustomJsonObjectRequest(int method, String url, JSONObject jsonRequest,
                                   Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers =  super.getHeaders();
        Map<String, String> headers_copy  = new HashMap<>();
        headers_copy.putAll(headers);
        headers_copy.put("Accept-Encoding", "gzip");
        headers_copy.put("User-Agent", "gzip");
        return headers_copy;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String stringResponse = new String(response.data);
            String encoding = response.headers.get("Content-Encoding");

            if (encoding != null) {
                StringBuilder sb = new StringBuilder();

                if (encoding.equals("gzip")) {

                    final GZIPInputStream gStream = new GZIPInputStream(new ByteArrayInputStream(response.data));
                    final InputStreamReader reader = new InputStreamReader(gStream);
                    final BufferedReader in = new BufferedReader(reader);
                    String read;
                    while ((read = in.readLine()) != null) {
                        sb.append(read);
                    }
                    reader.close();
                    in.close();
                    gStream.close();

                    stringResponse = sb.toString();

                } else if (encoding.equals("deflate")) {

                    Inflater inflater = new Inflater(true);
                    final InflaterInputStream inflaterInputStream = new InflaterInputStream(new ByteArrayInputStream(response.data), inflater);
                    final InputStreamReader reader = new InputStreamReader(inflaterInputStream);
                    final BufferedReader in = new BufferedReader(reader);
                    String read;
                    while ((read = in.readLine()) != null) {
                        sb.append(read);
                    }
                    reader.close();
                    in.close();
                    inflaterInputStream.close();

                    stringResponse = sb.toString();
                }
            }

            JSONObject jsonObject = new JSONObject(stringResponse);

            return Response.success(
                    jsonObject,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException ex) {
            return Response.error(new ParseError(ex));
        } catch (JSONException ex) {
            return Response.error(new ParseError(ex));
        } catch (IOException ex) {
            return Response.error(new ParseError(ex));
        } catch (Exception ex) {
            return Response.error(new ParseError(ex));
        }
    }
}
