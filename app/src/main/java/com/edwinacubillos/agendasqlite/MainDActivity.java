package com.edwinacubillos.agendasqlite;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainDActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText eNombre, eCorreo, eTelefono;
    String nombre, correo, telefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_d);
// Desde aca se esta controlando los elementos en el content XML migrados desde activity main sencillo
        eNombre = (EditText) findViewById(R.id.eNombre);
        eCorreo = (EditText) findViewById(R.id.eCorreo);
        eTelefono = (EditText) findViewById(R.id.eTelefono);
// Hablan mucho de la toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
// Implementacion de WIDGET en activity main XML
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();
//Conversion de lo tomado en eNombre como String a nombre cuando se accione algun boton
        nombre = eNombre.getText().toString();
        correo = eCorreo.getText().toString();
        telefono = eTelefono.getText().toString();
// Segun el id detectado por onClick asociados a botones se ejecuta las funciones correspondientes
        switch(id){
            case R.id.bGuardar:
                addContact();
                limpiar();
                break;
            case R.id.bBuscar:
                showContact();
                break;
            case R.id.bModificar:
                updateContact();
                limpiar();
                break;
            case R.id.bEliminar:
                deleteContact();
                limpiar();
                break;
        }
    }
// INICIO DE LA ESCRITURA DE METODO LA MAYORIA TIENEN ASOCIACION A METODOS HOMOLOGOS EN PHP EN Config.java
    private void showContact() {
        class ShowContact extends AsyncTask<Void, Void, String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainDActivity.this,"Show...","Wait...",false,false);
            }

            @Override
            protected String doInBackground(Void... v) {
                RequestHandler rh = new RequestHandler();
                String res = rh.sendGetRequestParam(Config.URL_GET_CONTACT, nombre);
                return res;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showData(s);
                Toast.makeText(MainDActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        }
        ShowContact ae = new ShowContact();
        ae.execute();
    } // FIN DE SHOWCONTAC()

    private void showData(String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray("result");
            JSONObject c = result.getJSONObject(0);
            String telefonoJ = c.getString("telefono");
            String correo = c.getString("correo");
            eTelefono.setText(telefonoJ);
            eCorreo.setText(correo);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    } // FIN DE SHOWDATA()

    private void deleteContact() {
        class DeleteContact extends AsyncTask<Void, Void, String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainDActivity.this,"Delete...","Wait...",false,false);
            }

            @Override
            protected String doInBackground(Void... v) {
                RequestHandler rh = new RequestHandler();
                String res = rh.sendGetRequestParam(Config.URL_DELETE, nombre);
                return res;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(MainDActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        }
        DeleteContact ae = new DeleteContact();
        ae.execute();
    } // FIN DE DELETECONTACT()

    private void updateContact() {
        class UpdateContact extends AsyncTask<Void, Void, String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainDActivity.this,"Updating...","Wait...",false,false);
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put("nombre",nombre);
                params.put("telefono",telefono);
                params.put("correo",correo);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_UPDATE, params);
                return res;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(MainDActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        }
        UpdateContact ae = new UpdateContact();
        ae.execute();
    } // FIN DE UPDATECONTACT()

    private void limpiar() {
        eNombre.setText("");
        eCorreo.setText("");
        eTelefono.setText("");
    } // FIN DE LIMPIAR()

    private void addContact() {
        class AddContact extends AsyncTask<Void, Void, String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainDActivity.this,"Adding...","Wait...",false,false);
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put("nombre",nombre);
                params.put("telefono",telefono);
                params.put("correo",correo);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_ADD, params);
                return res;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(MainDActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        }
        AddContact ae = new AddContact();
        ae.execute();
    } // FIN DE ADDCONTAC()

    //_______________FIN METODOS DE LA AGENDA

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    } // FIN DE ONBACK VOLVER CUANDO BOTON SE SUELTA()

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_d, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
