package com.example.dds_tp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import android.view.View;
import android.os.AsyncTask;
import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Ingreso extends AppCompatActivity {

    public static final String EXTRA_DOCCLIENTE = "com.example.dds_tp.EXTRA_IDCLIENTE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingreso);

        final EditText documento = findViewById(R.id.documento);
        final EditText password = findViewById(R.id.password);
        final Button btnIngresar = findViewById(R.id.btnIngresar);

        documento.setTransformationMethod(null);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new NuevoIngreso(documento.getText().toString(),view.getContext()).execute();
            }
        });
    }

    private class NuevoIngreso extends AsyncTask<String, String, String> {

        private String doc;
        private Context context;

        private NuevoIngreso(String doc, Context context){
            this.doc = doc;
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            try{
                //result = RESTService.makeGetRequest( "http://localhost:8080/copernicus/rest/cuenta/" + this.doc);
                result =  RESTService.makeGetRequest("https://jsonplaceholder.typicode.com/posts/" + this.doc);
            }catch (Exception e){
                Log.d("INFO", e.toString());
            }
            if (result != null)
                Log.d("INFO", result.toString());
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                if (result.length() > 0) {
                    try {
                        JSONObject cliente = new JSONObject(result);
                        //Integer documento = cliente.getInt("idCuenta");
                        Integer documento = cliente.getInt("id");

                        Intent intent = new Intent(Ingreso.this, MainActivity.class);
                        intent.putExtra("EXTRA_DOCCLIENTE", documento);

                        Toast notificacion = Toast.makeText(getApplicationContext(), "Bienvenido!  " + this.doc, Toast.LENGTH_SHORT);
                        notificacion.show();

                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    EditText documento = findViewById(R.id.documento);
                    EditText password = findViewById(R.id.password);
                    documento.setText("");
                    password.setText("");
                }
            }
        }
    }
}