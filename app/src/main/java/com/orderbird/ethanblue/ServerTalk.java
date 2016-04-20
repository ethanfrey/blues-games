package com.orderbird.ethanblue;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ethan on 20/04/16.
 */
public class ServerTalk {
    private static final String baseUrl = "http://10.0.3.2:9000/";

    public static void register(final Context ctx, final String name, final String uuid) {
        final String url = baseUrl + uuid;
        JSONObject data = new JSONObject();
        try {
            data.put("name", name);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.POST, url, data, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(ctx, "POST says: " + response.toString(), Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ctx, "Bad service....", Toast.LENGTH_LONG).show();
                        }
                    });

            // Access the RequestQueue through your singleton class.
            HTTPQueue.getInstance(ctx).addToRequestQueue(jsObjRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
