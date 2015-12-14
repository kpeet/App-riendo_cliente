package cl.peet.kenneth.app_riendo;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class App_riendo extends Activity{
	Connection post;
	TextView txt_usr;
	String user;
	boolean result_back;
	String IP_Server="kennethpeetd.no-ip.biz";//IP DE NUESTRO PC
	String URL_connect="http://"+IP_Server+"/droidlogin/mis_arriendos.php";//ruta en donde estan nuestros archivos
	private ProgressDialog pDialog;
	@Override
	public void onCreate(Bundle savedInstanceState){
		 super.onCreate(savedInstanceState);
		 
			
		 
		 
		 
		 
		 setContentView(R.layout.mis_arriendos);
		 txt_usr= (TextView) findViewById(R.id.usr_name);
		 Bundle extras = getIntent().getExtras();
         //Obtenemos datos enviados en el intent.
		 if (extras != null) {
		  	   user  = extras.getString("user");//usuario
		     }else{
		  	   user="error";
		  	   }
		 txt_usr.setText(user);//cambiamos texto al nombre del usuario logueado
		mostrar tarea=new mostrar();
		tarea.execute("hola","chao");
		
		 //Botón agregar arriendo
		 Button btn_agregar_arriendo = (Button)findViewById(R.id.boton_agregar_arriendo);
		 Button btn_buscar_arriendo =(Button)findViewById(R.id.buscar_arriendo_btn);
		 btn_agregar_arriendo.setOnClickListener(new View.OnClickListener(){
	        	public void onClick(View view){
	        		 Intent i=new Intent(App_riendo.this, Agregar_arriendo.class);
	     			i.putExtra("user",user);
	     			startActivity(i); 
	     		
	     		
	     	
	        	}
		 });
		 btn_buscar_arriendo.setOnClickListener(new View.OnClickListener(){
	        	public void onClick(View view){
	        		 Intent i=new Intent(App_riendo.this, Buscar_arriendo.class);
	     			i.putExtra("user",user);
	     			startActivity(i); 
	     		
	     		
	     	
	        	}
		 });
	}
/*
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 //setContentView(R.layout.agregar_arriendo);
		 Intent i=new Intent(App_riendo.this, Agregar_arriendo.class);
			//i.putExtra("user",user);
			startActivity(i); 
		
		
	}*/
	private class mostrar extends AsyncTask< String, Integer, Boolean>{
		boolean resul = true;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			return null;
		}
		
	}
	 class asynclogin extends AsyncTask< String, String, String > {
    	 
	    	String valor_arriendo,pass;
	        protected void onPreExecute() {
	        	//para el progress dialog
	            pDialog = new ProgressDialog(App_riendo.this);
	            pDialog.setMessage("Autenticando....");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(false);
	            pDialog.show();
	        }
	 
			protected String doInBackground(String... params) {
				//obtnemos usr y pass
				valor_arriendo=params[0];
				//pass=params[1];
				Log.e("Valor Arriendo doInBackground", valor_arriendo);	
				return "ok";
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

					Intent i=new Intent(App_riendo.this, App_riendo.class);
					i.putExtra("valor_arriendo",valor_arriendo);
					startActivity(i); 
					
	            }else{
	            	//err_login();
	            }
	            
	                									}
			
	        }
  
	

}
