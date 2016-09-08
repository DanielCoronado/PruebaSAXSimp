package com.example.danie.pruebasaxsimp;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;
/**
 * Created by danie on 27-08-2016.
 */
public class RssParserSax2 {
    private URL rssUrl;
    private Noticia noticiaActual;

    //recibe como parametro la url del documento xml
    public RssParserSax2(String url){
        try{
            this.rssUrl = new URL(url);
        }
        catch (MalformedURLException e){
            throw new RuntimeException(e);
        }
    }

    public List<Noticia> parse(){
        final List<Noticia> noticias = new ArrayList<Noticia>();

        RootElement root = new RootElement("rss"); //construimos un elemento raiz lamado rss
        Element channel = root.getChild("channel");//creamos variable channel para para buscar las etiquetas hijas de channel
        Element item = channel.getChild("item");//variable para buscar las etiquetas hijas de item

        //creamos una nueva variable de clase noticia
        item.setStartElementListener(new StartElementListener(){
            public void start(Attributes attrs) {
                noticiaActual = new Noticia();
            }
        });
        //metodo que agrega un elemento a la lista de Noticia
        item.setEndElementListener(new EndElementListener(){
            public void end() {
                noticias.add(noticiaActual);
            }
        });
        //buscamos el hijo de la etiqueta item llamado title
        //usamos el setEnd para agregarlo a la lista
        item.getChild("title").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        noticiaActual.setTitulo(body);
                    }
                });
        //buscamos el hijo de la etiqueta item llamado pubDate
        //usamos el setEnd para agregarlo a la lista
        item.getChild("pubDate").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        noticiaActual.setFecha(body);
                    }
                });

        try{
            //parseamos lo obtenido por el stram y codificamos el documento xml y el sax parse
            Xml.parse(this.getInputStream(),
                    Xml.Encoding.UTF_8,
                    root.getContentHandler());
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }

        return noticias;
    }

    //se conecta con la url especifica
    private InputStream getInputStream(){
        try{
            return rssUrl.openConnection().getInputStream(); // abre coneccion y obtinene el stream
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
