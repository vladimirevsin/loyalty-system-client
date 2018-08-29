package fitu.npi.clientapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ClientActivity extends AppCompatActivity {

    String login;
    Wallet[] wallets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        login = intent.getStringExtra("login");

        try {
            wallets = _getBalanceClient(login);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Wallet[] _getBalanceClient(String phoneNumber) throws JsonProcessingException, JSONException {
        final Wallet[][] messageResult = new Wallet[1][];
        final ObjectMapper mapper = new ObjectMapper();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String addString = "/get_balance_client?phoneNumber=" + phoneNumber.replace(' ', '+');

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Settings.URL.concat(addString),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            messageResult[0] = mapper.readValue(response.toString(), Wallet[].class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest responce", error.toString());
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 150000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 150000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        jsonObjectRequest.setShouldCache(false);
        requestQueue.add(jsonObjectRequest);

        return messageResult[0];
    }
}
