package controlador;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Database.Departamentos;
import Database.IOperations;
import vista.FrmPrincipal;

public class Controller {
	
	FrmPrincipal vista;
	IOperations operaciones;
	
	public Controller(FrmPrincipal vista, IOperations operaciones) {
		this.vista = vista;
		this.operaciones = operaciones;
		setActions();
		openWindow();
	}
	
	private void setActions() {
		MenuActionListener menuActionListener = new MenuActionListener();
		RadioActionListener radioActionListener = new RadioActionListener();
		
		vista.mnDptoC.addActionListener(menuActionListener);
		vista.mnDptoR.addActionListener(menuActionListener);
		vista.mnDptoU.addActionListener(menuActionListener);
		vista.mnDptoD.addActionListener(menuActionListener);
		
		vista.mnEmplC.addActionListener(menuActionListener);
		vista.mnEmplR.addActionListener(menuActionListener);
		vista.mnEmplU.addActionListener(menuActionListener);
		vista.mnEmplD.addActionListener(menuActionListener);
		
		vista.btnCancelar.addActionListener(menuActionListener);
		vista.resultados.addListSelectionListener(menuActionListener);
		
		vista.rdbtnNumero.addActionListener(radioActionListener);
		vista.rdbtnNombre.addActionListener(radioActionListener);
		vista.rdbtnLocalidad.addActionListener(radioActionListener);
		
		vista.btnGuardar.addActionListener(e -> {
			if (vista.pnlDpto.isVisible()) {
				try {
					Departamentos departamento = new Departamentos(
							Byte.parseByte(vista.txtNumero.getText(	)), 
							vista.txtNombre.getText(), 
							vista.txtLocalidad.getText(), 
							null);
					if (operaciones.addDepartamento(departamento)) {
						clearFields();
						JOptionPane.showMessageDialog(null, "Departamento creado correctamente");
						listAll();
					} else {
						JOptionPane.showMessageDialog(null, "Error en el guardado");						
					}
				} catch (Exception e2) {
					e2.printStackTrace();
					JOptionPane.showMessageDialog(vista, "Error: " + e2.getMessage());
				}
			}
		});
		vista.btnListarTodos.addActionListener(e -> {
			if (vista.pnlDpto.isVisible()) {
				listAll();
			}
		});
		vista.btnBuscar.addActionListener(e -> {
			if (vista.pnlDpto.isVisible()) {
				try {
					vista.modeloLista.clear();
					int numero = vista.rdbtnNumero.isSelected() ? (byte) vista.cmbNumero.getSelectedItem() : 0;
					String nombre = vista.rdbtnNombre.isSelected() ? (String) vista.cmbNombre.getSelectedItem() : "";
					String localidad = vista.rdbtnLocalidad.isSelected() ? (String) vista.cmbLocalidad.getSelectedItem() : "";
					for (Departamentos departamento : operaciones.listDepartamento(numero, nombre, localidad))
						vista.modeloLista.addElement(departamento);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		vista.btnModificar.addActionListener(e -> {
			if (vista.pnlDpto.isVisible()) {
				Departamentos departamento = new Departamentos(
						Byte.parseByte(vista.txtNumero.getText()), 
						vista.txtNombre.getText(), 
						vista.txtLocalidad.getText(), null);
				if (operaciones.modifyDepartamento(departamento)) {
					JOptionPane.showMessageDialog(vista, "Departamento modificado correctamente");
					listAll();
				} else {
					JOptionPane.showMessageDialog(vista, "Ha habido un error");
				}
			}
		});
		vista.btnEliminar.addActionListener(e -> {
			if (vista.pnlDpto.isVisible()) {
				int departamento = vista.resultados.getSelectedValue().getDeptno();
				if (JOptionPane.showConfirmDialog(vista, "Esta seguro de que desea eliminar el departamento seleccionado?") == JOptionPane.YES_OPTION) {
					if (operaciones.removeDepartamento(departamento)) {
						JOptionPane.showMessageDialog(vista, "Departamento eliminado correctamente");
						listAll();
					} else {
						JOptionPane.showMessageDialog(vista, "Ha habido un error");	
					}
				}
			}
		});
	}
	
	private void openWindow() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					vista.setTitle("Prueba programa hibernate");
					vista.setLocationRelativeTo(null);
					vista.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void listAll() {
		try {
			vista.resultados.setSelectedValue(null, false);
			vista.modeloLista.clear();
			for (Departamentos departamento : operaciones.listDepartamento(0, "", ""))
				vista.modeloLista.addElement(departamento);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}
	
	private void clearFields() {
		vista.txtNumero.setText("");
		vista.txtNombre.setText(""); 
		vista.txtLocalidad.setText("");
	}
	
	class MenuActionListener implements ActionListener, ListSelectionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			vista.resultados.setSelectedValue(null, false);
			switch(e.getActionCommand()) {
				case "DptoC":
					listAll();
					clearFields();
					setPanel(true);
					enableComponents(false, true, false);
					enableButtons(true, false, false, false, false, true);
					break;
				case "DptoR":
					addData();
					setPanel(true);
					enableComponents(true, false, true);
					enableButtons(false, true, true, false, false, false);
					break;
				case "DptoU":
					listAll();
					setPanel(true);
					enableComponents(false, false, false);
					enableButtons(false, false, false, true, false, true);
					break;
				case "DptoD":
					listAll();
					setPanel(true);
					enableComponents(false, false, false);
					enableButtons(false, false, false, false, true, true);
					break;
				case "EmplC":
					setPanel(false);
					enableButtons(true, false, false, false, false, true);
					break;
				case "EmplR":
					setPanel(false);
					enableButtons(false, true, false, false, false, true);
					break;
				case "EmplU":
					setPanel(false);
					enableButtons(false, false, true, true, false, true);
					break;
				case "EmplD":
					setPanel(false);
					enableButtons(false, false, true, false, true, true);
					break;
				default:
					setPanel(true);
					enableComponents(true, false, true);
					enableButtons(false, true, true, false, false, false);
					break;
			}
		}
		
		@Override
		public void valueChanged(ListSelectionEvent e) {
			
			if (vista.resultados.getSelectedValue() != null) {
				setPanel(true);
				enableComponents(false, true, false);
				enableButtons(false, false, false, true, true, true);

				Departamentos departamento = vista.resultados.getSelectedValue();
				vista.txtNumero.setText(String.valueOf(departamento.getDeptno()));
				vista.txtNumero.setEditable(false);
				vista.txtNombre.setText(departamento.getDnombre());
				vista.txtLocalidad.setText(departamento.getLoc());
			} else {
				setPanel(true);
				enableComponents(true, false, true);
				vista.txtNumero.setEditable(true);
				enableButtons(false, true, true, false, false, false);
			}
		}
		private void setPanel(boolean dep) {
			vista.pnlDpto.setVisible(dep);
			vista.pnlEmpl.setVisible(!dep);
		}
		private void enableButtons(boolean btnGuardar, boolean btnListar, boolean btnBuscar, boolean btnModificar, boolean btnEliminar, boolean btnCancelar) {
			vista.btnGuardar.setEnabled(btnGuardar);
			vista.btnListarTodos.setEnabled(btnListar);
			vista.btnBuscar.setEnabled(btnBuscar);
			vista.btnModificar.setEnabled(btnModificar);
			vista.btnEliminar.setEnabled(btnEliminar);
			vista.btnCancelar.setEnabled(btnCancelar);
		}
		private void enableComponents(boolean comboBox, boolean textField, boolean radioButton) {
			for (Component component : vista.pnlDpto.getComponents()) {
				if (component instanceof JComboBox) component.setVisible(comboBox);
				if (component instanceof JTextField) component.setVisible(textField);
				if (component instanceof JRadioButton) component.setVisible(radioButton);
			}
		}
		private void addData() {
			vista.cmbNumero.removeAllItems();
			vista.cmbNombre.removeAllItems();
			vista.cmbLocalidad.removeAllItems();
			for (Departamentos departamento : operaciones.listDepartamento(0, "", "")) {
				vista.cmbNumero.addItem(departamento.getDeptno());
				if (((DefaultComboBoxModel<String>)vista.cmbNombre.getModel()).getIndexOf(departamento.getDnombre()) == -1)
					vista.cmbNombre.addItem(departamento.getDnombre());					
				if (((DefaultComboBoxModel<String>)vista.cmbLocalidad.getModel()).getIndexOf(departamento.getLoc()) == -1)
					vista.cmbLocalidad.addItem(departamento.getLoc());					
			}
		}
	}
	
	class RadioActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			switch(e.getActionCommand()) {
				case "Numero":
					enableComboBox(true, false, false);
					break;
				case "Nombre":
					enableComboBox(false, true, false);
					break;
				case "Localizacion":
					enableComboBox(false, false, true);
					break;
			}
		}
		
		private void enableComboBox(boolean comboNumero, boolean comboNombre, boolean comboLoc) {
			vista.cmbNumero.setEnabled(comboNumero);
			vista.cmbNombre.setEnabled(comboNombre);
			vista.cmbLocalidad.setEnabled(comboLoc);
		}
		
	}
}

