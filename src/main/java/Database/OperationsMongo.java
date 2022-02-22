package Database;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import com.mongodb.client.MongoDatabase;

public class OperationsMongo implements IOperations {
	
	private List<Departamentos> departamentos;
	private MongoDatabase mongoDb;
	
	public OperationsMongo() {
		departamentos = new ArrayList<Departamentos>();
		mongoDb = Conection.getMongoConnection();
	}
	
	/**
	 * Crea el departamento facilitado
	 * @param departamento - a crear
	 * @return true si se completa la operacion, false si hay algun problema
	 */
	public boolean addDepartamento(Departamentos departamento) {
		Document document = new Document();
		document.append("depno", departamento.getDeptno());
		document.append("dnombre", departamento.getDnombre());
		document.append("loc", departamento.getLoc());
		
		if (mongoDb.getCollection("departamentos").insertOne(document).getInsertedId() != null) {
			return true;
		}
		return false;
	}
	
	/**
	 * Elimina el departamento con el id correspondiente
	 * @param dptoNum - id del departamento a eliminar
	 * @return true si se completa la operacion y false si falla
	 */
	public boolean removeDepartamento(int dptoNum) {
		Document filter = new Document();
		filter.append("depno", dptoNum);
		
		if (mongoDb.getCollection("departamentos").deleteOne(filter).getDeletedCount() == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * Modifica el departamento facilitado
	 * @param departamento - a modificar
	 */
	public boolean modifyDepartamento(Departamentos departamento) {
		Document filter = new Document();
		filter.append("depno", departamento.getDeptno());
		
		Document update = new Document();
		update.append("depno", departamento.getDeptno());
		update.append("dnombre", departamento.getDnombre());
		update.append("loc", departamento.getLoc());
		
		if (mongoDb.getCollection("departamentos").replaceOne(filter, update).getModifiedCount() == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * Devuelve una lista de todos los departamentos con opcion de filtrar
	 * @param filtroId - id para filtrar o 0 para ignorar filtro
	 * @param filtroNombre - nombre para filtrar o "" para ignorar filtro
	 * @param filtroLocalidad - localidad para filtrar o "" para ignorar filtro
	 * @return lista de departamentos encontrados
	 */
	public List<Departamentos> listDepartamento(int filtroId, String filtroNombre, String filtroLocalidad) {
		departamentos.clear();
		Document filter = new Document();
		if (filtroId != 0) {
			filter.append("depno", filtroId);
		}
		if (!filtroNombre.equals("")) {
			filter.append("dnombre", filtroNombre);
		}
		if (!filtroLocalidad.equals("")) {
			filter.append("loc", filtroLocalidad);
		}
		
		for (Document dep : mongoDb.getCollection("departamentos").find(filter)) {
			departamentos.add(new Departamentos(
					Byte.parseByte(dep.get("depno").toString()), 
					dep.get("dnombre").toString(), 
					dep.get("loc").toString(), 
					null));
		}
		return departamentos;
	}
	
	/**
	 * Cierra la sesion
	 */
	public void closeSession() {
		
	}
}