package cl.peet.kenneth.app_riendo;

import cl.peet.kenneth.beans.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Ver_mis_arriendos extends Activity{
	ListView listaJson;
	String user;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		 Bundle extras = getIntent().getExtras();
	        //Obtenemos datos enviados en el intent.
			 ////////////////7
			
			 
			 ///////////////////7
			 if (extras != null) {
			  	   user  = extras.getString("user");//usuario
			  
			     }else{
			  	   user="error";
		
			  	   }
		setContentView(R.layout.vista_arriendos_mios);
		listaJson = (ListView)findViewById(R.id.listView1);
		
		Tarea1 tarea1 = new Tarea1();
		tarea1.cargarContenido(getApplicationContext());
		tarea1.cargar_user(user);
		tarea1.execute(listaJson);
		listaJson.setOnItemClickListener(new OnItemClickListener() {
			  @Override
			  public void onItemClick(AdapterView<?> parent, View view,
			    int position, long id) {
			    Toast.makeText(getApplicationContext(),
			      "Click ListItem Number " + position, Toast.LENGTH_LONG)
			      .show();
			  }
			}); 
		
	}
	static class Tarea1 extends AsyncTask<ListView, Void, ArrayAdapter<Arriendo>>{
		Context contexto;
		ListView list;
		InputStream is;
		private String user;
		ArrayList<Arriendo> listaclientes = new ArrayList<Arriendo>();
		 
		
		public void cargarContenido(Context contexto){
			this.contexto=contexto;
		}
		public void cargar_user(String user) {
			// TODO Auto-generated method stub
			this.user=user;
		}
		public String get_user() {
			// TODO Auto-generated method stub
			return this.user;
		}
		@Override
		protected void onPreExecute(){
			
		}
		@Override
		protected ArrayAdapter<Arriendo> doInBackground(ListView... params) {
			list = params[0];
			String user = get_user();
			//Log.e("LISTVIEW GENERATION",user);
			String resultado = "fallo"; 
			Arriendo cli;
			HttpClient cliente = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://kennethpeetd.no-ip.biz/droidlogin/mis_arriendos.php");
			//HttpGet peticionGet = new HttpGet("http://kennethpeetd.no-ip.biz/droidlogin/mis_arriendos.php");
			try{
				//////////////////////
				
				 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
				 	
			        nameValuePairs.add(new BasicNameValuePair("usuario", user)); 
			        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				////////////////////////////7
				HttpResponse response = cliente.execute(httppost);
				HttpEntity contenido = response.getEntity();
				is= contenido.getContent();
			}catch(ClientProtocolException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
			BufferedReader buferlector = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String linea = null;
			try{
				while((linea = buferlector.readLine())!=null){
					sb.append(linea);
				}
			}catch(IOException e){
				e.printStackTrace();
			}
			try{
				is.close();
			}catch(IOException e){
				e.printStackTrace();
			}
			resultado = sb.toString();
			try{
				JSONArray arrayJson = new JSONArray(resultado);
				for(int i = 0; i<arrayJson.length();i++){
					JSONObject objetoJson = arrayJson.getJSONObject(i);
					cli = new Arriendo(objetoJson.getInt("id_arriendo"),objetoJson.getString("descripcion"),objetoJson.getInt("valor_arriendo"));
					listaclientes.add(cli);
					
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
			Log.e("Array Adapter","AQUIIII");
			ArrayAdapter<Arriendo> adaptador = new ArrayAdapter<Arriendo>(contexto,android.R.layout.simple_expandable_list_item_1, listaclientes);
			
			return adaptador;
		}
		@Override
		protected void onPostExecute(ArrayAdapter<Arriendo> result){
			list.setAdapter(result);
			
		}
		@Override
		protected void onProgressUpdate(Void... value){
			
		}
		
	}
}
