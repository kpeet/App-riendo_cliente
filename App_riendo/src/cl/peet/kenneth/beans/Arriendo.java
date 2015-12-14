package cl.peet.kenneth.beans;

public class Arriendo {
	int id_arriendo;
	int valor_arriendo;
	int id_categoria;
	int id_usuario;
	int estado_arriendo;
	String descripcion;
	
	public Arriendo(int id_arriendo, String descripcion, int valor_arriendo) {
		// TODO Auto-generated constructor stub
		super();
		this.id_arriendo = id_arriendo;
		this.descripcion = descripcion;
		this.valor_arriendo = valor_arriendo;
		
	}
	public int getId_arriendo() {
		return id_arriendo;
	}
	public void setId_arriendo(int id_arriendo) {
		this.id_arriendo = id_arriendo;
	}
	public int getValor_arriendo() {
		return valor_arriendo;
	}
	public void setValor_arriendo(int valor_arriendo) {
		this.valor_arriendo = valor_arriendo;
	}
	public int getId_categoria() {
		return id_categoria;
	}
	public void setId_categoria(int id_categoria) {
		this.id_categoria = id_categoria;
	}
	public int getId_usuario() {
		return id_usuario;
	}
	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}
	public int getEstado_arriendo() {
		return estado_arriendo;
	}
	public void setEstado_arriendo(int estado_arriendo) {
		this.estado_arriendo = estado_arriendo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	@Override
	public String toString(){
		return this.descripcion;
	}

}
