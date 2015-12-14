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

public class Buscar_arriendo extends Activity {
	
	Connection post;
	String user;
	String filtro_usuario;
	boolean result_back;
	String IP_Server="kennethpeetd.no-ip.biz";//IP DE NUESTRO PC
	String URL_connect="http://"+IP_Server+"/droidlogin/verificar_filtro.php";//ruta en donde estan nuestros archivos
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
		 new asyncfiltro().execute(user);  
		 
		 //setContentView(R.layout.busqueda_arriendo);
	}
	public boolean buscar_filtro_usuario(String usuario){
		Connection post = new Connection();
		int verificacion_filtro = 0;
    	/*Creamos un ArrayList del tipo nombre valor para agregar los datos recibidos por los parametros anteriores
    	 * y enviarlo mediante POST a nuestro sistema para relizar la validacion*/ 
    	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
		    		postparameters2send.add(new BasicNameValuePair("usuario",usuario));
		   //realizamos una peticion y como respuesta obtenes un array JSON
		    JSONArray jdata=post.getserverdata(postparameters2send, URL_connect);
		    Log.e("JDATA FILTRO","AQUI VAMOS");
      		/*como estamos trabajando de manera local el ida y vuelta sera casi inmediato
      		 * para darle un poco realismo decimos que el proceso se pare por unos segundos para poder
      		 * observar el progressdialog
      		 * la podemos eliminar si queremos
      		 */
		    SystemClock.sleep(950);
		    		
		    //si lo que obtuvimos no es null
		    
		    	if (jdata!=null && jdata.length() > 0){

		    		JSONObject json_data; //creamos un objeto JSON
		    		 Log.e("data!=false ","FALSE= ");//muestro por log que obtuvimos
		    		 
		    		 try {
							json_data = jdata.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
						
							 verificacion_filtro=json_data.getInt("verificacion_filtro");//accedemos al valor 
							 Log.e("verificacion_filtro","verificacion_filtro= "+ verificacion_filtro);//muestro por log que obtuvimos
							if(verificacion_filtro==1){
								 filtro_usuario=json_data.getString("filtro_usuario");//accedemos al valor 
								 Log.e("filtro_usuario","filtro_usuario= "+ filtro_usuario);//muestro por log que obtuvimos
							}
							 
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							
							e.printStackTrace();
						}	            
		    		 
					//validamos el valor obtenido
		    		if (verificacion_filtro == 1){// [{"logstatus":"0"}] 
		    			 Log.e("verificacion_filtro ", "invalido");   
		    			 return true;
		    		 }
		    		 else{// [{"logstatus":"1"}]
		    			 Log.e("verificacion_filtro ", "valido");
		    			 return false;
		    		 }
		    		 
			  }else{	//json obtenido invalido verificar parte WEB.
		    			 Log.e("JSON  ", "ERROR");
			    		return false;
			  }
				
		
		
	}
	 class asyncfiltro extends AsyncTask< String, String, String > {
    	 
	    	String valor_arriendo,pass;
	        protected void onPreExecute() {
	        	//para el progress dialog
	            pDialog = new ProgressDialog(Buscar_arriendo.this);
	            pDialog.setMessage("Cargando....");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(false);
	            pDialog.show();
	        }
	 
			protected String doInBackground(String... params) {
				//obtnemos usr y pass
				user=params[0];
				//pass=params[1];
				Boolean resultado_filtro;
				
				//VERIFICA SI EL USUARIO YA TIENE SETEADO UN FILTRO PREVIO
				resultado_filtro=buscar_filtro_usuario(user);
				//ANALISIS DEL RESULTADO DEL FILTRO PARA EL USUARIO
				if(resultado_filtro==true){
					
					return "ok";
				}else{
					return "fail";
				}
				
				//enviamos y recibimos y analizamos los datos en segundo plano.
	    		/*
				if (ingreso_arriendo(valor_arriendo,valor_arriendo)==true){ 
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
	        	   Log.e("onPostExecute","OK, Paso a arriendos encontrados");
	        	   Log.e("onPostExecute filtro_usuario",filtro_usuario);
					Intent i=new Intent(Buscar_arriendo.this, Arriendos_encontrados.class);
					i.putExtra("user",user);
					i.putExtra("filtro_usuario",filtro_usuario);
					startActivity(i); 
					
	            }else{
	            	//Log.e("onPostExecute filtro_usuario",filtro_usuario);
	            	Intent i=new Intent(Buscar_arriendo.this, Fijar_filtro_usuario.class);
					i.putExtra("user",user);
					startActivity(i); 
	            }
	            
	                									}
			
	        }
	 

}
