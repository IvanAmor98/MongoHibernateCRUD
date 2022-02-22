package Database;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class Operations implements IOperations {
	
	private Session session;
	private List<Departamentos> departamentos;
	
	public Operations() {
		session = Conection.getConnection().openSession();
	}
	
	/**
	 * Crea el departamento facilitado
	 * @param departamento - a crear
	 * @return true si se completa la operacion, false si hay algun problema
	 */
	public boolean addDepartamento(Departamentos departamento) {
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			session.save(departamento);
			transaction.commit();
			return true;
		} catch (Exception e) {
			transaction.rollback();
			return false;
		}
	}
	
	/**
	 * Elimina el departamento con el id correspondiente
	 * @param dptoNum - id del departamento a eliminar
	 * @return true si se completa la operacion y false si falla
	 */
	public boolean removeDepartamento(int dptoNum) {
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			Departamentos departamento = session.load(Departamentos.class, (byte)dptoNum);
			session.delete(departamento);
			
			transaction.commit();
			
			return true;
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Modifica el departamento facilitado
	 * @param departamento - a modificar
	 */
	public boolean modifyDepartamento(Departamentos departamento) {
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			Departamentos modified = null;
			for (Departamentos dep : departamentos) {
				if (dep.getDeptno() == departamento.getDeptno()) {
					dep.setDnombre(departamento.getDnombre());
					dep.setLoc(departamento.getLoc());
					modified = dep;
				}
			}
			
			session.saveOrUpdate(modified);
			
			transaction.commit();
			return true;
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Devuelve una lista de todos los departamentos con opcion de filtrar
	 * @param filtroId - id para filtrar o 0 para ignorar filtro
	 * @param filtroNombre - nombre para filtrar o "" para ignorar filtro
	 * @param filtroLocalidad - localidad para filtrar o "" para ignorar filtro
	 * @return lista de departamentos encontrados
	 */
	public List<Departamentos> listDepartamento(int filtroId, String filtroNombre, String filtroLocalidad) {
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			boolean check = false;
			
			String query = "from Departamentos";
			if (filtroId != 0) {
				query += " WHERE deptno = " + filtroId;
				check = true;
			}
			if (!filtroNombre.equals("")) {
				if (check) {
					query += " AND dnombre = '" + filtroNombre + "'";
				} else {
					query += " WHERE dnombre = '" + filtroNombre + "'";		
					check = true;		
				}
			}
			if (!filtroLocalidad.equals("")) {
				if (check) {
					query += " AND loc = '" + filtroLocalidad + "'";
				} else {
					query += " WHERE loc = '" + filtroLocalidad + "'";				
				}
			}
			departamentos = session.createQuery(query, Departamentos.class).getResultList();

			transaction.commit();
			
			return departamentos;
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Cierra la sesion
	 */
	public void closeSession() {
		session.clear();
	}
}
