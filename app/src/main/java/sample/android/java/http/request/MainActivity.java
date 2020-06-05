package sample.android.java.http.request;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

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
                            JSONObject pokemonApiJsonResponse = new JSONObject(response);
                            Gson gson = new Gson();
                            PokemonRoot pokemonRoot = gson.fromJson(pokemonApiJsonResponse.toString(), PokemonRoot.class);
                            Instant respondedAt = Instant.now();
                            Duration requestDuration = Duration.between(requestedAt, respondedAt);
                            responseTimeTextView.setText(pokemonRoot.getCount() + " pokémons found, API Request duration: " + Duration.of(requestDuration.getSeconds(), ChronoUnit.SECONDS).toMillis() + " Millis");
                            List<String> pokemonsNames = pokemonRoot.getResults().stream().map(PokemonResult::getName).collect(Collectors.toList());
                            ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.activity_pokemon, pokemonsNames);

                            ListView listView = findViewById(R.id.pokemonsListView);
                            listView.setAdapter(adapter);
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
