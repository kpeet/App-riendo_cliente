package cl.peet.kenneth.app_riendo;


import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class Fijar_filtro_usuario extends Activity implements OnItemSelectedListener{
	Button btn_registrar_filtro;
	Spinner spinner;
	Connection post;
	String user;
	boolean result_back;
	String IP_Server="kennethpeetd.no-ip.biz";//IP DE NUESTRO PC
	String URL_connect="http://"+IP_Server+"/droidlogin/registrar_filtro.php";//ruta en donde estan nuestros archivos
	private ProgressDialog pDialog;
	
	public void onCreate(Bundle savedInstanceState){
		 super.onCreate(savedInstanceState);
		 Bundle extras = getIntent().getExtras();
        //Obtenemos datos enviados en el intent.
		 if (extras != null) {
		  	   user  = extras.getString("user");//usuario
		     }else{
		  	   user="error";
		  	   }
		 
		 setContentView(R.layout.filtro_usuario);
		 btn_registrar_filtro = (Button) findViewById(R.id.boton_generar_filtro);
		  final Spinner spinner = (Spinner)findViewById(R.id.spinner_filtro);
		   ArrayAdapter<CharSequence> adapter_cuidades = ArrayAdapter.createFromResource(this, R.array.cuidades_direccion, android.R.layout.simple_spinner_item);
		   adapter_cuidades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		   spinner.setAdapter(adapter_cuidades);
		   btn_registrar_filtro.setOnClickListener(new View.OnClickListener(){
	        	public void onClick(View view){
	        		 
	        		//Extraccion datos del Spinner
	        		String ciudades_input=spinner.getSelectedItem().toString();
	        		//verificamos si estan en blanco
	        		Log.e("Ciudad Arriendo", ciudades_input);
	        		new async_registrar_filtro().execute(user,ciudades_input);        		               
	        	}
		 });
	}
	 class async_registrar_filtro extends AsyncTask< String, String, String > {
    	 
	    	String valor_arriendo;
	    	String descripcion_arriendo;
	        protected void onPreExecute() {
	        	//para el progress dialog
	            pDialog = new ProgressDialog(Fijar_filtro_usuario.this);
	            pDialog.setMessage("Autenticando....");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(false);
	            pDialog.show();
	        }
	 
			protected String doInBackground(String... params) {
				//obtnemos usr y pass
				Boolean update_filtro;
				user=params[0];
				String ciudad_filtro = params[1];
				Log.e("Filtro doInBackground", ciudad_filtro);	
				update_filtro=ingreso_filtro_usuario(user,ciudad_filtro);
	            if(update_filtro==true){
	            	return "ok";
	            }else{
	            	return "fail";
	            }
				//enviamos y recibimos y analizamos los datos en segundo plano.
	    		/*
				if (ingreso_arriendo(valor_arriendo,descripcion_arriendo,ciudades)==true){ 
	    			Log.e("ingreso_arriendo true", valor_arriendo);	
	    			return "ok"; //login valido
	    		}else{    		
	    			Log.e("ingreso_arriendo err", valor_arriendo);	
	    			return "err"; //login invalido     	          	  
	    		}
	        	*/
			}
	       
			/*Una vez terminado doInBackground segun lo que halla ocurrido 
			pasamos a la sig. activity
			o mostramos error*/
	        protected void onPostExecute(String result) {
	           
	           pDialog.dismiss();//ocultamos progess dialog.
	           Log.e("onPostExecute=",""+result);
	           
	           if (result.equals("ok")){
	        	   Log.e("Validacion Ingreso Filtro","OK");
					Intent i=new Intent(Fijar_filtro_usuario.this, Buscar_arriendo.class);
					i.putExtra("user",user);
					startActivity(i); 
					
	            }else{
	            	Log.e("Validacion Ingreso Filtro","FAIL");
	            	Intent i=new Intent(Fijar_filtro_usuario.this, Buscar_arriendo.class);
					i.putExtra("user",user);
					startActivity(i);
	            	//err_login();
	            }
	            
	                									}
			
	        }

	@Override
	public void onItemSelected(AdapterView<?> parent, View arg1, int pos,
			long id) {
		// TODO Auto-generated method stub
		parent.getItemAtPosition(pos);
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	 public boolean ingreso_filtro_usuario(String user ,String ciudad_filtro ) {
	    	int update_filtro=-1;
	    	Connection post = new Connection();
	    	/*Creamos un ArrayList del tipo nombre valor para agregar los datos recibidos por los parametros anteriores
	    	 * y enviarlo mediante POST a nuestro sistema para relizar la validacion*/ 
	    	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
	    	
	    	Log.e("Ingreso Arriendo user", user);
	    	Log.e("Ingreso Arriendo ciudad_filtro", ciudad_filtro);
			    		postparameters2send.add(new BasicNameValuePair("usuario",user));
			    		postparameters2send.add(new BasicNameValuePair("ciudad_filtro",ciudad_filtro));
			   //realizamos una peticion y como respuesta obtenes un array JSON
			    JSONArray jdata=post.getserverdata(postparameters2send, URL_connect);

			    SystemClock.sleep(950);
			    		
			    //si lo que obtuvimos no es null
			    
			    	if (jdata!=null && jdata.length() > 0){

			    		JSONObject json_data; //creamos un objeto JSON
			    		
			    		 
			    		 try {
								json_data = jdata.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
								update_filtro=json_data.getInt("update_filtro");//accedemos al valor 
								 Log.e("update_filtro","update_filtro= "+update_filtro);//muestro por log que obtuvimos
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}	            
			             
						//validamos el valor obtenido
			    		 if (update_filtro==0){// [{"logstatus":"0"}] 
			    			 Log.e("update_filtro ", "invalido");
			    			 return false;
			    		 }
			    		 else{// [{"logstatus":"1"}]
			    			 Log.e("update_filtro ", "valido");
			    			 return true;
			    		 }
			    		 
				  }else{	//json obtenido invalido verificar parte WEB.
			    			 Log.e("JSON  ", "ERROR");
				    		return false;
				  }
				  
	    	
	    }

}
