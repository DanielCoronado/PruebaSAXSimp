package com.example.danie.pruebasaxsimp;

import android.app.Activity;
import android.os.Bundle;

import android.os.AsyncTask;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity {

    private TextView txtResultado;

    private List<Noticia> noticias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtResultado = (TextView)findViewById(R.id.txtResultado);
        //le asignamos la rss que deseamos ejecutar a la clase CargarXmlTask
                CargarXmlTask tarea = new CargarXmlTask();
                tarea.execute("http://www.latercera.com/feed/manager?type=rss&sc=TEFURVJDRVJB");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //Tarea Asíncrona para cargar un XML en segundo plano
    private class CargarXmlTask extends AsyncTask<String,Integer,Boolean> {
        //recibe como parametro un vargars, en este caso un arreglo
        protected Boolean doInBackground(String... params) {

            RssParserSax2 saxparser =
                    new RssParserSax2(params[0]);//recibe el primer parametro

            noticias = saxparser.parse();

            return true;
        }

        protected void onPostExecute(Boolean result) {
            //Tratamos la lista de noticias
            //escribimos los títulos y fecha en pantalla
            for(int i=0; i<noticias.size(); i++)
            {
                txtResultado.setText(
                        txtResultado.getText().toString() +"\n-------------------------------------------------------------------------------"+
                                System.getProperty("line.separator") +
                                "TITULAR:\n"+noticias.get(i).getTitulo() +"\nFECHA:\n"+ noticias.get(i).getFecha());
            }
        }
    }
}
