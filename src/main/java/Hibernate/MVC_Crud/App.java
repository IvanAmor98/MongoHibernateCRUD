package Hibernate.MVC_Crud;

import java.awt.EventQueue;

import Database.Departamentos;
import Database.IOperations;
import Database.Operations;
import Database.OperationsMongo;
import controlador.Controller;
import vista.FrmPrincipal;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	IOperations operations = null;
    	try {
    		FrmPrincipal vista = new FrmPrincipal();
        	//operations = new Operations();
    		operations = new OperationsMongo();
        	Controller controller = new Controller(vista, operations);
		} finally {
			//operations.closeSession();
		}
    }
}
