package fitu.npi.clientapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
import org.w3c.dom.Text;

import java.io.IOException;

public class TSCMainActivity extends AppCompatActivity {
    private String URL = Settings.URL;

    String login;
    Wallet wallet;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tsc_main);

        Intent intent = getIntent();
        login = intent.getStringExtra("login");

        try {
            wallet = _getBalanceTSC();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView textView = (TextView)findViewById(R.id.textViewToken);
        textView.setText(wallet.getValue().toString());
    }

    public void buttonAddBallsClick(View view){
        TextView phone = (TextView)findViewById(R.id.editTextPhone);
        TextView value = (TextView)findViewById(R.id.editTextSumPay);

        TokenChangeModel model = new TokenChangeModel(
                phone.getText().toString(),
                Double.parseDouble(value.getText().toString()),
                1
        );
        try {
            _addToken(model);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void buttonDestBallsClick(View view){
        TextView phone = (TextView)findViewById(R.id.editTextPhone);
        TextView value = (TextView)findViewById(R.id.editTextSumPay);

        TokenChangeModel model = new TokenChangeModel(
                phone.getText().toString(),
                Double.parseDouble(value.getText().toString()),
                1
        );
        try {
            _destToken(model);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //region ТСП
    private void _addToken(TokenChangeModel model) throws JsonProcessingException, JSONException {
        MessageResult messageResult;

        final ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(model);
        JSONObject jsonObj = new JSONObject(jsonInString);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String addString = "/add_token_client";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL.concat(addString),
                jsonObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            MessageResult message = mapper.readValue(response.toString(), MessageResult.class);
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
    }

    private void _destToken(TokenChangeModel model) throws JsonProcessingException, JSONException {
        MessageResult messageResult;

        final ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(model);
        JSONObject jsonObj = new JSONObject(jsonInString);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String addString = "/dest_token";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL.concat(addString),
                jsonObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            MessageResult message = mapper.readValue(response.toString(), MessageResult.class);
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
    }

    private Wallet _getBalanceTSC() throws JsonProcessingException, JSONException {
        final Wallet[] messageResult = new Wallet[1];
        final ObjectMapper mapper = new ObjectMapper();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String addString = "/get_balance_tsc";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Settings.URL.concat(addString),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            messageResult[0] = mapper.readValue(response.toString(), Wallet.class);
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
    //endregion

}
