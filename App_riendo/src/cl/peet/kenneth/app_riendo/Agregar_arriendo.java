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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Agregar_arriendo extends Activity implements OnItemSelectedListener{

	Button btn_agregar_arriendo;
	EditText valor_arriendo;
	EditText descripcion_arriendo;
	EditText pass;
	String user;
	Spinner spinner;
	Connection post;
	boolean result_back;
	String IP_Server="kennethpeetd.no-ip.biz";//IP DE NUESTRO PC
	String URL_connect="http://"+IP_Server+"/droidlogin/add_arriendo.php";//ruta en donde estan nuestros archivos
	private ProgressDialog pDialog;
	 
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.agregar_arriendo);
		 Bundle extras = getIntent().getExtras();
	        //Obtenemos datos enviados en el intent.
			 if (extras != null) {
			  	   user  = extras.getString("user");//usuario
			     }else{
			  	   user="error";
			  	   }
		
		 valor_arriendo= (EditText) findViewById(R.id.valor_arriendo);
		 descripcion_arriendo= (EditText)findViewById(R.id.descripcion_arriendo);
		 btn_agregar_arriendo = (Button) findViewById(R.id.boton_agregar_arriendo); 
	     final Spinner spinner = (Spinner)findViewById(R.id.spinner1);
	     ArrayAdapter<CharSequence> adapter_cuidades = ArrayAdapter.createFromResource(this, R.array.cuidades_direccion, android.R.layout.simple_spinner_item);
	     adapter_cuidades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     spinner.setAdapter(adapter_cuidades);
	     
	     
	     //AGREGAR ARRIENDO button action
		 btn_agregar_arriendo.setOnClickListener(new View.OnClickListener(){
	        	public void onClick(View view){
	        		 
	        		//Extreamos datos de los EditText
	        		String valor_arriendo_input=valor_arriendo.getText().toString();
	        		String descripcion_arriendo_input=descripcion_arriendo.getText().toString();
	        		String ciudades_input=spinner.getSelectedItem().toString();
	        		//verificamos si estan en blanco
	        		if( checklogindata( valor_arriendo_input , descripcion_arriendo_input )==true){

	        			//si pasamos esa validacion ejecutamos el asynctask pasando el usuario y clave como parametros
	        		Log.e("Valor Arriendo", valor_arriendo_input);	
	        		Log.e("Descripcion Arriendo", descripcion_arriendo_input);
	        		Log.e("Ciudad Arriendo", ciudades_input);
	        		new asynclogin().execute(valor_arriendo_input,descripcion_arriendo_input,ciudades_input);        		               
	        			      		
	        		
	        		}else{
	        			//si detecto un error en la primera validacion vibrar y mostrar un Toast con un mensaje de error.
	        			err_login();
	        		}
	        	
	        		
	        	}
		 });
		 }
	
	 public boolean checklogindata(String username ,String password ){
	    	
		    if 	(username.equals("") || password.equals("")){
		    	Log.e("Login ui", "checklogindata user or pass error");
		    return false;
		    
		    }else{
		    	
		    	return true;
		    }
	 }
	  //vibra y muestra un Toast
	    public void err_login(){
	    	Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		    vibrator.vibrate(200);
		    Toast toast1 = Toast.makeText(getApplicationContext(),"Error:Nombre de usuario o password incorrectos", Toast.LENGTH_SHORT);
	 	    toast1.show();    	
	    }
	    public boolean ingreso_arriendo(String valor_arriendo2 ,String descripcion_arriendo ,String ciudad_arriendo) {
	    	int logstatus=-1;
	    	Connection post = new Connection();
	    	/*Creamos un ArrayList del tipo nombre valor para agregar los datos recibidos por los parametros anteriores
	    	 * y enviarlo mediante POST a nuestro sistema para relizar la validacion*/ 
	    	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
	    	
	    	Log.e("Ingreso Arriendo Valor_arriendo2", valor_arriendo2);
	    	Log.e("Ingreso Arriendo descripcion_arriendo", descripcion_arriendo);
	    				postparameters2send.add(new BasicNameValuePair("usuario",user));
			    		postparameters2send.add(new BasicNameValuePair("valor_arriendo",valor_arriendo2));
			    		postparameters2send.add(new BasicNameValuePair("descripcion_arriendo",descripcion_arriendo));
			    		postparameters2send.add(new BasicNameValuePair("ciudad_arriendo",ciudad_arriendo));
			    
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
								 logstatus=json_data.getInt("logstatus");//accedemos al valor 
								 Log.e("loginstatus","logstatus= "+logstatus);//muestro por log que obtuvimos
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}	            
			             
						//validamos el valor obtenido
			    		 if (logstatus==0){// [{"logstatus":"0"}] 
			    			 Log.e("loginstatus ", "invalido");
			    			 return false;
			    		 }
			    		 else{// [{"logstatus":"1"}]
			    			 Log.e("loginstatus ", "valido");
			    			 return true;
			    		 }
			    		 
				  }else{	//json obtenido invalido verificar parte WEB.
			    			 Log.e("JSON  ", "ERROR");
				    		return false;
				  }
				  
	    	
	    }
		
		 class asynclogin extends AsyncTask< String, String, String > {
	    	 
		    	String valor_arriendo;
		    	String descripcion_arriendo;
		        protected void onPreExecute() {
		        	//para el progress dialog
		            pDialog = new ProgressDialog(Agregar_arriendo.this);
		            pDialog.setMessage("Autenticando....");
		            pDialog.setIndeterminate(false);
		            pDialog.setCancelable(false);
		            pDialog.show();
		        }
		 
				protected String doInBackground(String... params) {
					//obtnemos usr y pass
					valor_arriendo=params[0];
					descripcion_arriendo=params[1];
					String ciudades = params[2];
					Log.e("Valor Arriendo doInBackground", valor_arriendo);	
		            
					//enviamos y recibimos y analizamos los datos en segundo plano.
		    		if (ingreso_arriendo(valor_arriendo,descripcion_arriendo,ciudades)==true){ 
		    			Log.e("ingreso_arriendo true", valor_arriendo);	
		    			return "ok"; //login valido
		    		}else{    		
		    			Log.e("ingreso_arriendo err", valor_arriendo);	
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

						Intent i=new Intent(Agregar_arriendo.this, Ver_mis_arriendos.class);
						i.putExtra("valor_arriendo",valor_arriendo);
						startActivity(i); 
						
		            }else{
		            	err_login();
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

}
