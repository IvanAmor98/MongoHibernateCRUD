package Database;

import java.util.List;

public interface IOperations {
	public boolean addDepartamento(Departamentos departamento);
	public boolean removeDepartamento(int dptoNum);
	public boolean modifyDepartamento(Departamentos departamento);
	public List<Departamentos> listDepartamento(int filtroId, String filtroNombre, String filtroLocalidad);
}
