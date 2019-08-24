package com.example.dds_tp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.GridLayout;
import androidx.cardview.widget.CardView;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;



public class MainActivity extends AppCompatActivity {

    Bundle datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datos = getIntent().getExtras();

        final Integer documento = datos.getInt("EXTRA_DOCCLIENTE");

        final GridLayout mainGrid = findViewById(R.id.mainGrid);

        final CardView btnSaldo = findViewById(R.id.saldo);
        final CardView btnTransaccion = findViewById(R.id.transaccion);
        final CardView btnMovimientos = findViewById(R.id.movimientos);
        final CardView btnBonos = findViewById(R.id.bonos);

        final Button btnVolver = findViewById(R.id.btnVolver);
        final Button btnTransferir = findViewById(R.id.btnTransferir);

        final TableLayout ultimosLayout = findViewById(R.id.ultimosLayout);
        final TableLayout bonosLayout = findViewById(R.id.bonosLayout);

        final RelativeLayout layoutVolver = findViewById(R.id.layoutVolver);
        final RelativeLayout transaccionLayout = findViewById(R.id.transaccionLayout);


        btnSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ConsultarSaldo(documento, view.getContext()).execute();
            }
        });

        btnMovimientos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSaldo.setVisibility(View.GONE);
                btnTransaccion.setVisibility(View.GONE);
                btnMovimientos.setVisibility(View.GONE);
                btnBonos.setVisibility(View.GONE);
                mainGrid.setVisibility(View.GONE);
                layoutVolver.setVisibility(View.VISIBLE);
                ultimosLayout.setVisibility(View.VISIBLE);

                new ConsultarUltimosMovimientos(view.getContext()).execute();

            }
        });

        btnTransaccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSaldo.setVisibility(View.GONE);
                btnTransaccion.setVisibility(View.GONE);
                btnMovimientos.setVisibility(View.GONE);
                btnBonos.setVisibility(View.GONE);
                mainGrid.setVisibility(View.GONE);
                transaccionLayout.setVisibility(View.VISIBLE);
                layoutVolver.setVisibility(View.VISIBLE);

            }
        });

        btnBonos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSaldo.setVisibility(View.GONE);
                btnTransaccion.setVisibility(View.GONE);
                btnMovimientos.setVisibility(View.GONE);
                btnBonos.setVisibility(View.GONE);
                mainGrid.setVisibility(View.GONE);
                bonosLayout.setVisibility(View.VISIBLE);
                layoutVolver.setVisibility(View.VISIBLE);

                new ConsultarBonos(view.getContext()).execute();
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSaldo.setVisibility(View.VISIBLE);
                btnTransaccion.setVisibility(View.VISIBLE);
                btnMovimientos.setVisibility(View.VISIBLE);
                btnBonos.setVisibility(View.VISIBLE);
                mainGrid.setVisibility(View.VISIBLE);
                bonosLayout.setVisibility(View.GONE);
                layoutVolver.setVisibility(View.GONE);
                transaccionLayout.setVisibility(View.GONE);
                ultimosLayout.setVisibility(View.GONE);

            }
        });

        btnTransferir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RealizarTransaccion(documento,view.getContext()).execute();
            }
        });
    }




    private class ConsultarSaldo extends AsyncTask<String, String, String> {

        private Integer doc;
        private Context context;

        private ConsultarSaldo(Integer doc, Context context) {
            this.doc = doc;
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {

            String result = null;

            try {
                // result = RESTService.makeGetRequest("http://localhost:8080/copernicus/rest/cuenta/" + this.doc);
                result = RESTService.makeGetRequest("https://jsonplaceholder.typicode.com/posts/" + this.doc);
            } catch (Exception e) {
                Log.d("INFO", e.toString());
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            if (result != null) {

                try {
                    JSONObject cuenta = new JSONObject(result);

                    //String docu = cuenta.getString("saldo");
                    String saldo = cuenta.getString("id");

                    Toast toastSaldo = Toast.makeText(context, "Su saldo es:  $" + saldo, Toast.LENGTH_LONG);
                    toastSaldo.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toastSaldo.show();

                }catch (JSONException e){
                    Log.d("INFO",e.toString());
                }
            }
        }
    }



    private class ConsultarUltimosMovimientos extends AsyncTask<String,String,String>{

        private Context context;

        private ConsultarUltimosMovimientos(Context context){
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {

            String result = null;

            try {
                //result = RESTService.makeGetRequest("http://localhost:8080/copernicus/rest/movimientos/cuenta/");
                result = RESTService.makeGetRequest("https://jsonplaceholder.typicode.com/posts");
            } catch (Exception e) {
                Log.d("INFO", e.toString());
            }
            Log.d("INFO", result.toString());
            return result;
        }

        @Override
        protected void onPostExecute(String result){

            if (result != null){

                JSONArray movimientos;
                JSONObject movimiento = new JSONObject();

                try{
                    movimientos = new JSONArray(result);
                }catch (JSONException e){
                    Log.d("INFO", e.toString());
                    return;
                }
                Log.d("INFO", movimientos.toString());

                TextView campo;

                //Fila 1

                campo = findViewById(R.id.tipo1);
                try{
                    movimiento = movimientos.getJSONObject(0);
                }catch (JSONException e) {Log.d("INFO", e.toString());}

                try {
                    //if (movimiento.getInt("tipo") == 1) {
                    if (movimiento.getInt("id") == 1) {
                        campo.setText("BONO");
                    } else {
                        campo.setText("TRANSACCIÓN");
                    }
                } catch(JSONException e) {Log.d("INFO", e.toString());}

                campo = findViewById(R.id.cantidad1);
                try {
                    //campo.setText(String.valueOf(movimiento.getInt("importe")));
                    campo.setText(String.valueOf(movimiento.getInt("userId")));
                } catch(JSONException e) {Log.d("INFO", e.toString());}

                campo = findViewById(R.id.fecha1);
                try{
                    //campo.setText(movimiento.getString("fechaMovimiento"));
                    campo.setText(movimiento.getString("title"));
                }catch(JSONException e) {Log.d("INFO", e.toString());}


                //Fila 2

                campo = findViewById(R.id.tipo2);
                try{
                    movimiento = movimientos.getJSONObject(1);
                }catch (JSONException e) {Log.d("INFO", e.toString());}

                try {
                    //if (movimiento.getInt("tipo") == 1) {
                    if (movimiento.getInt("id") == 1) {
                        campo.setText("BONO");
                    } else {
                        campo.setText("TRANSACCIÓN");
                    }
                } catch(JSONException e) {Log.d("INFO", e.toString());}

                campo = findViewById(R.id.cantidad2);
                try {
                    //campo.setText(String.valueOf(movimiento.getInt("importe")));
                    campo.setText(String.valueOf(movimiento.getInt("userId")));
                } catch(JSONException e) {Log.d("INFO", e.toString());}

                campo = findViewById(R.id.fecha2);
                try{
                    //campo.setText(movimiento.getString("fechaMovimiento"));
                    campo.setText(movimiento.getString("title"));
                }catch(JSONException e) {Log.d("INFO", e.toString());}


                //Fila 3

                campo = findViewById(R.id.tipo3);
                try{
                    movimiento = movimientos.getJSONObject(2);
                }catch (JSONException e) {Log.d("INFO", e.toString());}

                try {
                    //if (movimiento.getInt("tipo") == 1) {
                    if (movimiento.getInt("id") == 1) {
                        campo.setText("BONO");
                    } else {
                        campo.setText("TRANSACCIÓN");
                    }
                } catch(JSONException e) {Log.d("INFO", e.toString());}

                campo = findViewById(R.id.cantidad3);
                try {
                    //campo.setText(String.valueOf(movimiento.getInt("importe")));
                    campo.setText(String.valueOf(movimiento.getInt("userId")));
                } catch(JSONException e) {Log.d("INFO", e.toString());}

                campo = findViewById(R.id.fecha3);
                try{
                    //campo.setText(movimiento.getString("fechaMovimiento"));
                    campo.setText(movimiento.getString("title"));
                }catch(JSONException e) {Log.d("INFO", e.toString());}


                //Fila 4

                campo = findViewById(R.id.tipo4);
                try{
                    movimiento = movimientos.getJSONObject(3);
                }catch (JSONException e) {Log.d("INFO", e.toString());}

                try {
                    //if (movimiento.getInt("tipo") == 1) {
                    if (movimiento.getInt("id") == 1) {
                        campo.setText("BONO");
                    } else {
                        campo.setText("TRANSACCIÓN");
                    }
                } catch(JSONException e) {Log.d("INFO", e.toString());}

                campo = findViewById(R.id.cantidad4);
                try {
                    //campo.setText(String.valueOf(movimiento.getInt("importe")));
                    campo.setText(String.valueOf(movimiento.getInt("userId")));
                } catch(JSONException e) {Log.d("INFO", e.toString());}

                campo = findViewById(R.id.fecha4);
                try{
                    //campo.setText(movimiento.getString("fechaMovimiento"));
                    campo.setText(movimiento.getString("title"));
                }catch(JSONException e) {Log.d("INFO", e.toString());}

            }
        }
    }

    private class ConsultarBonos extends AsyncTask<String,String,String>{

        private Context context;

        private ConsultarBonos(Context context){
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {

            String result = null;

            try {
                result = RESTService.makeGetRequest("https://lsi.no-ip.org:8282/esferopolis/api/bono/");
            } catch (Exception e) {
                Log.d("INFO", e.toString());
            }
            Log.d("INFO", result.toString());
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            if (result != null) {

                JSONArray bonos;
                JSONObject bono = new JSONObject();

                try {
                    bonos = new JSONArray(result);
                } catch (JSONException e) {
                    Log.d("INFO", e.toString());
                    return;
                }
                Log.d("INFO", bonos.toString());

                TextView campo;

                //Fila 1

                campo = findViewById(R.id.codigo1);
                try {
                    bono = bonos.getJSONObject(0);
                } catch (JSONException e) {
                    Log.d("INFO", e.toString());
                }

                try {
                    campo.setText(bono.getString("codigo"));
                } catch (JSONException e) {
                    Log.d("INFO", e.toString());
                }

                campo = findViewById(R.id.descripcion1);
                try {
                    campo.setText(bono.getString("descripcion"));
                } catch (JSONException e) {
                    Log.d("INFO", e.toString());
                }

                campo = findViewById(R.id.interes1);
                try {
                    campo.setText(String.valueOf(bono.getInt("interes")));
                } catch (JSONException e) {
                    Log.d("INFO", e.toString());
                }


                //Fila 2

                campo = findViewById(R.id.codigo2);
                try {
                    bono = bonos.getJSONObject(1);
                } catch (JSONException e) {Log.d("INFO", e.toString());}

                try{
                    campo.setText(bono.getString("codigo"));
                }catch (JSONException e) {Log.d("INFO", e.toString());}

                campo = findViewById(R.id.descripcion2);
                try{
                    campo.setText(bono.getString("descripcion"));
                }catch (JSONException e) {Log.d("INFO", e.toString());}

                campo = findViewById(R.id.interes2);
                try{
                    campo.setText(String.valueOf(bono.getInt("interes")));
                }catch (JSONException e) {Log.d("INFO", e.toString());}
            }
        }
    }

    private class RealizarTransaccion extends AsyncTask<String, String,String>{

        private Integer doc;
        private Context context;

        EditText destino = findViewById(R.id.idDestino);
        EditText cantidad = findViewById(R.id.importe);

        String idDestino = destino.getText().toString();
        String importe = cantidad.getText().toString();

        private RealizarTransaccion(Integer doc, Context context){
            this.doc = doc;
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;

            try{

                JSONObject transferencia = new JSONObject();
                transferencia.put("cuentaOrigen",doc);
                transferencia.put("cuentaDestino",idDestino);
                transferencia.put("importe",importe);
                transferencia.put("fechaInicio","");
                transferencia.put("fechaFin", "");
                transferencia.put("estado", "COMPLETA");

                result = RESTService.callREST("http://localhost:8080/esferopolis/api/transferencia","PUT",transferencia);

            }catch (JSONException e){Log.d("INFO", e.toString());}

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast notificacion = Toast.makeText(getApplicationContext(), "Transferencia iniciada", Toast.LENGTH_SHORT);
            notificacion.show();
        }
    }



    private class ConsultarImpuesto extends AsyncTask<String, String,String> {
        private Context context;

        private ConsultarImpuesto(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            try {
                result = RESTService.makeGetRequest("http://lsi.no-ip.org:8282/esferopolis/api/impuesto");
            } catch (Exception e) {
                Log.d("INFO", e.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    Float porcImpuesto = Float.parseFloat((jsonArray.getJSONObject(0)).getString("porcentaje"));
                    Float impuesto = porcImpuesto / 100;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
