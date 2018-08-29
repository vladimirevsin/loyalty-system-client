package fitu.npi.clientapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import java.io.IOException;

public class RegActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void registerClick(View view) {
        TextView FIO = (TextView)findViewById(R.id.editTextFIO);
        TextView phone = (TextView)findViewById(R.id.editTextPhone);
        TextView password = (TextView)findViewById(R.id.editTextPassword);
        TextView passwordConf = (TextView)findViewById(R.id.editTextConfirmPassword);

        if(password.getText().toString().equals(passwordConf.getText().toString()))
        {
            Abonent abonent = new Abonent(
                FIO.getText().toString(),
                 phone.getText().toString(),
                    password.getText().toString()
            );

            try {
                MessageResult result = _regClient(abonent);

                if(result != null && result.getCode() == 0)
                {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra("regStatus", "true");
                    startActivity(intent);
                }
                else
                {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                    dlgAlert.setMessage("Регистрация не удалась!");
                    dlgAlert.setTitle("Ошибка");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //String name = intent.getStringExtra("login");
    }

    private MessageResult _regClient(Abonent abonent) throws JsonProcessingException, JSONException {
        final MessageResult[] messageResult = new MessageResult[1];

        final ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(abonent);
        JSONObject jsonObj = new JSONObject(jsonInString);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String addString = "/reg_client";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                Settings.URL.concat(addString),
                jsonObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            messageResult[0] = mapper.readValue(response.toString(), MessageResult.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        messageResult[0] = new MessageResult(-1, error.toString());
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
