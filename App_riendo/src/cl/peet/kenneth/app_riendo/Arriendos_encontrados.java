package cl.peet.kenneth.app_riendo;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Arriendos_encontrados extends Activity{
	Button modificar_filtro_btn;
	Button llamar_btn;
	Connection post;
	String user;
	String ciudad_filtro;
	TextView txt_filtro;
	boolean result_back;
	String IP_Server="kennethpeetd.no-ip.biz";//IP DE NUESTRO PC
	String URL_connect="http://"+IP_Server+"/droidlogin/verificar_filtro.php";//ruta en donde estan nuestros archivos
	private ProgressDialog pDialog;
	
	public void onCreate(Bundle savedInstanceState){
		 super.onCreate(savedInstanceState);
		 Bundle extras = getIntent().getExtras();
        //Obtenemos datos enviados en el intent.
		 ////////////////7
	
		
		 
		 ///////////////////7
		 if (extras != null) {
		  	   user  = extras.getString("user");//usuario
		  	 ciudad_filtro  = extras.getString("filtro_usuario");//usuario
		  
		     }else{
		  	   user="error";
		  	   ciudad_filtro="error";
		  	   }
		 Log.e("filtro_usuario",ciudad_filtro);
		 setContentView(R.layout.arriendos_encontrados_usuario);
		 modificar_filtro_btn = (Button) findViewById(R.id.modificar_filtro_btn);
		 txt_filtro= (TextView) findViewById(R.id.filtro_actual_usuario);
		 txt_filtro.setText(ciudad_filtro);//cambiamos texto al nombre del usuario logueado
		 llamar_btn = (Button) findViewById(R.id.llamar);
		 modificar_filtro_btn.setOnClickListener(new View.OnClickListener(){
	        	public void onClick(View view){
	        		 
	        		Intent i=new Intent(Arriendos_encontrados.this, Fijar_filtro_usuario.class);
					i.putExtra("user",user);
					startActivity(i); 
	        		
	        	
	        		
	        	}
		 });
		 llamar_btn.setOnClickListener(new View.OnClickListener(){
	        	public void onClick(View view){
	        		 
	        		 Intent i = new Intent(Intent.ACTION_CALL);
					//i.putExtra("user",user);
	        		 i.setData(Uri.parse("tel:+56977010314"));
					startActivity(i); 
	        		
	        	
	        		
	        	}
		 });
		 
	}
}
