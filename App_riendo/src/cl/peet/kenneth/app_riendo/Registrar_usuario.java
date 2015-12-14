package cl.peet.kenneth.app_riendo;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Registrar_usuario extends Activity{
	Button btn_agregar_usuario;
	EditText username;
	EditText pass;
	Connection post;
	boolean result_back;
	String IP_Server="kennethpeetd.no-ip.biz";//IP DE NUESTRO PC
	String URL_connect="http://"+IP_Server+"/droidlogin/add_usuario.php";//ruta en donde estan nuestros archivos
	String numero_telefono;
	private ProgressDialog pDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.registro_usuario);
		
		 username = (EditText) findViewById(R.id.username_registro_input);
		 pass = (EditText)findViewById(R.id.pass_registro_input);
		 btn_agregar_usuario = (Button) findViewById(R.id.registrar_usuario_btn); 
	     //AGREGAR ARRIENDO button action
		 btn_agregar_usuario.setOnClickListener(new View.OnClickListener(){
	        	public void onClick(View view){
	        		 
	        		//Extreamos datos de los EditText
	        		String username_input =username.getText().toString();
	        		String pass_input =pass.getText().toString();
	        		//verificamos si estan en blanco
	        		if( checkdata( username_input , pass_input )==true){

	        			//si pasamos esa validacion ejecutamos el asynctask pasando el usuario y clave como parametros
	        		Log.e("username_input", username_input);	
	        		Log.e("pass_input", pass_input);
	        		numero_telefono=getPhoneNumber();
	        		Log.e("numero_telefono", numero_telefono);
	        		new async_registro_usuario().execute(username_input,pass_input,numero_telefono);        		               
	        			      		
	        		
	        		}else{
	        			//si detecto un error en la primera validacion vibrar y mostrar un Toast con un mensaje de error.
	        			err_registro();
	        		}
	        	
	        		
	        	}
		 });
		 }
	 public boolean checkdata(String username ,String password ){
	    	
		    if 	(username.equals("") || password.equals("")){
		    	Log.e("Login ui", "checklogindata user or pass error");
		    return false;
		    
		    }else{
		    	
		    	return true;
		    }
	 }
	 private String getPhoneNumber(){
		  TelephonyManager mTelephonyManager;
		  mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE); 
		  return mTelephonyManager.getLine1Number();
		}
	  public void err_registro(){
	    	Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		    vibrator.vibrate(200);
		    Toast toast1 = Toast.makeText(getApplicationContext(),"Error:En Nombre de usuario o password", Toast.LENGTH_SHORT);
	 	    toast1.show();    	
	    }
	  class async_registro_usuario extends AsyncTask< String, String, String > {
	  
	        protected void onPreExecute() {
	        	//para el progress dialog
	            pDialog = new ProgressDialog(Registrar_usuario.this);
	            pDialog.setMessage("Validando Registro....");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(false);
	            pDialog.show();
	        }
	 
			protected String doInBackground(String... params) {
				//obtnemos usr y pass
				String user_input=params[0];
				String pass_input=params[1];
				String numero_input = params[2];
				Log.e("Valor Arriendo doInBackground", user_input);	
	            
				//enviamos y recibimos y analizamos los datos en segundo plano.
	    		if (ingreso_usuario(user_input,pass_input,numero_input)==true){ 
	    			Log.e("ingreso_usuario true", "OK");	
	    			return "ok"; //login valido
	    		}else{    		
	    			Log.e("ingreso_usuario err", "FAIL");	
	    			return "err"; //login invalido     	          	  
	    		}
	        	
			}
	       
			/*Una vez terminado doInBackground segun lo que halla ocurrido 
			pasamos a la sig. activity
			o mostramos error*/
	        protected void onPostExecute(String result) {

	           pDialog.dismiss();//ocultamos progess dialog.
	           Log.e("onPostExecute=",""+result);
	           
	           if (result.equals("ok")){

					Intent i=new Intent(Registrar_usuario.this, Login.class);
					startActivity(i); 
					
	            }else{
	            	err_registro();
	            }
	            
	                									}
			
	        }
	  public boolean ingreso_usuario(String username ,String password ,String numero_telefono) {
	    	int ingreso_status=-1;
	    	Connection post = new Connection();
	    	/*Creamos un ArrayList del tipo nombre valor para agregar los datos recibidos por los parametros anteriores
	    	 * y enviarlo mediante POST a nuestro sistema para relizar la validacion*/ 
	    	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
	    	
	    	Log.e("Ingreso Usuario","username "+username);
	    	Log.e("Ingreso Usuario","password "+password);
	    	Log.e("Ingreso Usuario","numero_telefono "+numero_telefono);
			    		postparameters2send.add(new BasicNameValuePair("username",username));
			    		postparameters2send.add(new BasicNameValuePair("password",password));
			    		postparameters2send.add(new BasicNameValuePair("numero_telefono",numero_telefono));
			    
			   //realizamos una peticion y como respuesta obtenes un array JSON
			    JSONArray jdata=post.getserverdata(postparameters2send, URL_connect);

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
								ingreso_status=json_data.getInt("ingreso_status");//accedemos al valor 
								 Log.e("ingreso_status","ingreso_status= "+ingreso_status);//muestro por log que obtuvimos
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}	            
			             
						//validamos el valor obtenido
			    		 if (ingreso_status==0){// [{"logstatus":"0"}] 
			    			 Log.e("loginstatus ", "invalido");
			    			 return false;
			    		 }
			    		 else if(ingreso_status==2){
			    			 return false;
			    		 }
			    		 else if(ingreso_status==1){
			    			 return true;
			    		 }
			    		 
			    		 else{// [{"logstatus":"1"}]
			    			 Log.e("loginstatus ", "valido");
			    			 return false;
			    		 }
			    		 
				  }else{	//json obtenido invalido verificar parte WEB.
			    			 Log.e("JSON  ", "ERROR");
				    		return false;
				  }
				  
	    	
	    }

}
