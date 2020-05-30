package sample.android.java.http.request;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Instant requestedAt = Instant.now();
        final TextView responseTimeTextView = findViewById(R.id.responseTime);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://pokeapi.co/api/v2/pokemon/";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject myJson = new JSONObject(response);
                            String nextList = myJson.optString("next");
                            int pokemonTotalCount = myJson.optInt("count");
                            Instant respondedAt = Instant.now();
                            Duration requestDuration = Duration.between(requestedAt, respondedAt);
                            responseTimeTextView.setText(pokemonTotalCount + " pokémons found, API Request duration: " + Duration.of(requestDuration.getSeconds(), ChronoUnit.SECONDS).toMillis() + " Millis");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                responseTimeTextView.setText("That didn't work!");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
