package com.example.certmanager.view.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionListener;

import com.example.certmanager.controller.CertificateController;
import com.example.certmanager.model.Certificate;
import com.example.certmanager.view.CertificateView;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.DefaultListCellRenderer;

public class CertificateSwingView extends JFrame implements CertificateView {

	private static final long serialVersionUID = 1L;

	private JTextField txtId;
	private JTextField txtTitle;
	private JTextField txtIssuedTo;
	private JTextField txtIssuedBy;
	private JTextField txtYear;

	private JButton btnAdd;
	private JButton btnUpdate;
	private JButton btnDelete;

	private JLabel lblError;

	private JList<Certificate> listCertificates;
	private DefaultListModel<Certificate> listCertificateModel;

	private transient CertificateController certificateController;

	public DefaultListModel<Certificate> getListCertificateModel() {
		return listCertificateModel;
	}

	public void setCertificateController(CertificateController certificateController) {
		this.certificateController = certificateController;
	}

	public CertificateSwingView() {
		setMinimumSize(new Dimension(900, 600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Digital Certificate Management System");

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{45, 0, 770, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 173, 0, 23, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);

		JLabel lblId = new JLabel("id");
		GridBagConstraints gbc_lblId = new GridBagConstraints();
		gbc_lblId.anchor = GridBagConstraints.EAST;
		gbc_lblId.insets = new Insets(0, 0, 5, 5);
		gbc_lblId.gridx = 1;
		gbc_lblId.gridy = 0;
		getContentPane().add(lblId, gbc_lblId);

		txtId = new JTextField();
		txtId.setName("idTextBox");
		GridBagConstraints gbc_txtId = new GridBagConstraints();
		gbc_txtId.insets = new Insets(0, 0, 5, 5);
		gbc_txtId.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtId.gridx = 2;
		gbc_txtId.gridy = 0;
		getContentPane().add(txtId, gbc_txtId);
		txtId.setColumns(10);

		JLabel lblTitle = new JLabel("Title");
		GridBagConstraints gbc_lblTitle = new GridBagConstraints();
		gbc_lblTitle.anchor = GridBagConstraints.EAST;
		gbc_lblTitle.insets = new Insets(0, 0, 5, 5);
		gbc_lblTitle.gridx = 1;
		gbc_lblTitle.gridy = 1;
		getContentPane().add(lblTitle, gbc_lblTitle);

		txtTitle = new JTextField();
		txtTitle.setName("titleTextBox");
		GridBagConstraints gbc_txtTitle = new GridBagConstraints();
		gbc_txtTitle.insets = new Insets(0, 0, 5, 5);
		gbc_txtTitle.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTitle.gridx = 2;
		gbc_txtTitle.gridy = 1;
		getContentPane().add(txtTitle, gbc_txtTitle);
		txtTitle.setColumns(10);

		JLabel lblIssuedTo = new JLabel("Issued To");
		GridBagConstraints gbc_lblIssuedTo = new GridBagConstraints();
		gbc_lblIssuedTo.anchor = GridBagConstraints.EAST;
		gbc_lblIssuedTo.insets = new Insets(0, 0, 5, 5);
		gbc_lblIssuedTo.gridx = 1;
		gbc_lblIssuedTo.gridy = 2;
		getContentPane().add(lblIssuedTo, gbc_lblIssuedTo);

		txtIssuedTo = new JTextField();
		txtIssuedTo.setName("issuedToTextBox");
		GridBagConstraints gbc_txtIssuedTo = new GridBagConstraints();
		gbc_txtIssuedTo.insets = new Insets(0, 0, 5, 5);
		gbc_txtIssuedTo.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtIssuedTo.gridx = 2;
		gbc_txtIssuedTo.gridy = 2;
		getContentPane().add(txtIssuedTo, gbc_txtIssuedTo);
		txtIssuedTo.setColumns(10);

		JLabel lblIssuedBy = new JLabel("Issued By");
		GridBagConstraints gbc_lblIssuedBy = new GridBagConstraints();
		gbc_lblIssuedBy.anchor = GridBagConstraints.EAST;
		gbc_lblIssuedBy.insets = new Insets(0, 0, 5, 5);
		gbc_lblIssuedBy.gridx = 1;
		gbc_lblIssuedBy.gridy = 3;
		getContentPane().add(lblIssuedBy, gbc_lblIssuedBy);

		txtIssuedBy = new JTextField();
		txtIssuedBy.setName("issuedByTextBox");
		GridBagConstraints gbc_txtIssuedBy = new GridBagConstraints();
		gbc_txtIssuedBy.insets = new Insets(0, 0, 5, 5);
		gbc_txtIssuedBy.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtIssuedBy.gridx = 2;
		gbc_txtIssuedBy.gridy = 3;
		getContentPane().add(txtIssuedBy, gbc_txtIssuedBy);
		txtIssuedBy.setColumns(10);

		JLabel lblYear = new JLabel("Year");
		GridBagConstraints gbc_lblYear = new GridBagConstraints();
		gbc_lblYear.anchor = GridBagConstraints.EAST;
		gbc_lblYear.insets = new Insets(0, 0, 5, 5);
		gbc_lblYear.gridx = 1;
		gbc_lblYear.gridy = 4;
		getContentPane().add(lblYear, gbc_lblYear);

		txtYear = new JTextField();
		txtYear.setName("yearTextBox");
		GridBagConstraints gbc_txtYear = new GridBagConstraints();
		gbc_txtYear.insets = new Insets(0, 0, 5, 5);
		gbc_txtYear.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtYear.gridx = 2;
		gbc_txtYear.gridy = 4;
		getContentPane().add(txtYear, gbc_txtYear);
		txtYear.setColumns(10);

		btnAdd = new JButton("Add Certificate");
		btnAdd.setEnabled(false);
		btnAdd.setName("btnAdd");
		btnAdd.addActionListener(e ->
				new Thread(() ->
						certificateController.newCertificate(
								new Certificate(
										txtId.getText(),
										txtTitle.getText(),
										txtIssuedTo.getText(),
										txtIssuedBy.getText(),
										Integer.parseInt(txtYear.getText())))).start());

		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.insets = new Insets(0, 0, 5, 5);
		gbc_btnAdd.gridx = 2;
		gbc_btnAdd.gridy = 5;
		getContentPane().add(btnAdd, gbc_btnAdd);

		btnUpdate = new JButton("Update Certificate");
		btnUpdate.setEnabled(false);
		btnUpdate.setName("btnUpdate");
		btnUpdate.addActionListener(e ->
				new Thread(() ->
						certificateController.updateCertificate(
								new Certificate(
										txtId.getText(),
										txtTitle.getText(),
										txtIssuedTo.getText(),
										txtIssuedBy.getText(),
										Integer.parseInt(txtYear.getText())))).start());

		GridBagConstraints gbc_btnUpdate = new GridBagConstraints();
		gbc_btnUpdate.insets = new Insets(0, 0, 5, 5);
		gbc_btnUpdate.gridx = 1;
		gbc_btnUpdate.gridy = 7;
		getContentPane().add(btnUpdate, gbc_btnUpdate);

		btnDelete = new JButton("Delete Certificate");
		btnDelete.setEnabled(false);
		btnDelete.setName("btnDelete");
		btnDelete.addActionListener(e ->
				new Thread(() ->
						certificateController.deleteCertificate(
								listCertificates.getSelectedValue())).start());

		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.insets = new Insets(0, 0, 5, 5);
		gbc_btnDelete.gridx = 2;
		gbc_btnDelete.gridy = 7;
		getContentPane().add(btnDelete, gbc_btnDelete);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 6;
		getContentPane().add(scrollPane, gbc_scrollPane);

		listCertificateModel = new DefaultListModel<>();
		listCertificates = new JList<>(listCertificateModel);
		listCertificates.setName("certificateList");
		listCertificates.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		ListSelectionListener listSelectionListener = e -> {
			boolean isCertificateSelected = listCertificates.getSelectedIndex() != -1;
			btnDelete.setEnabled(isCertificateSelected);
			btnUpdate.setEnabled(isCertificateSelected);
			if (isCertificateSelected) {
				Certificate selected = listCertificates.getSelectedValue();
				txtId.setText(selected.getId());
				txtTitle.setText(selected.getTitle());
				txtIssuedTo.setText(selected.getIssuedTo());
				txtIssuedBy.setText(selected.getIssuedBy());
				txtYear.setText(String.valueOf(selected.getYear()));
				txtId.setEnabled(false);
				btnAdd.setEnabled(false);
			} else {
				txtId.setText("");
				txtTitle.setText("");
				txtIssuedTo.setText("");
				txtIssuedBy.setText("");
				txtYear.setText("");
				txtId.setEnabled(true);
			}
		};
		listCertificates.addListSelectionListener(listSelectionListener);

		listCertificates.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value,
					int index, boolean isSelected, boolean cellHasFocus) {
				Certificate cert = (Certificate) value;
				return super.getListCellRendererComponent(list,
						getDisplayString(cert), index, isSelected, cellHasFocus);
			}
		});

		scrollPane.setViewportView(listCertificates);

		lblError = new JLabel(" ");
		lblError.setName("errorLabel");
		GridBagConstraints gbc_lblError = new GridBagConstraints();
		gbc_lblError.gridwidth = 3;
		gbc_lblError.insets = new Insets(0, 0, 0, 5);
		gbc_lblError.gridx = 0;
		gbc_lblError.gridy = 8;
		getContentPane().add(lblError, gbc_lblError);

		KeyAdapter btnAddEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				btnAdd.setEnabled(
						listCertificates.getSelectedIndex() == -1
						&& !txtId.getText().trim().isEmpty()
						&& !txtTitle.getText().trim().isEmpty()
						&& !txtIssuedTo.getText().trim().isEmpty()
						&& !txtIssuedBy.getText().trim().isEmpty()
						&& !txtYear.getText().trim().isEmpty());
			}
		};

		txtId.addKeyListener(btnAddEnabler);
		txtTitle.addKeyListener(btnAddEnabler);
		txtIssuedTo.addKeyListener(btnAddEnabler);
		txtIssuedBy.addKeyListener(btnAddEnabler);
		txtYear.addKeyListener(btnAddEnabler);
	}

	@Override
	public void showAllCertificates(List<Certificate> certificates) {
		certificates.forEach(listCertificateModel::addElement);
	}

	@Override
	public void certificateAdded(Certificate certificate) {
		SwingUtilities.invokeLater(() -> {
			listCertificateModel.addElement(certificate);
			resetErrorLabel();
		});
	}

	@Override
	public void certificateRemoved(Certificate certificate) {
		SwingUtilities.invokeLater(() -> {
			listCertificateModel.removeElement(certificate);
			resetErrorLabel();
		});
	}

	@Override
	public void certificateUpdated(Certificate certificate) {
		SwingUtilities.invokeLater(() -> {
			for (int i = 0; i < listCertificateModel.size(); i++) {
				if (listCertificateModel.get(i).getId().equals(certificate.getId())) {
					listCertificateModel.set(i, certificate);
					break;
				}
			}
			resetErrorLabel();
		});
	}

	@Override
	public void showError(String message, Certificate certificate) {
		SwingUtilities.invokeLater(() ->
				lblError.setText(message + ": " + getDisplayString(certificate)));
	}

	@Override
	public void showErrorCertificateNotFound(String message, Certificate certificate) {
		SwingUtilities.invokeLater(() -> {
			lblError.setText(message + ": " + getDisplayString(certificate));
			listCertificateModel.removeElement(certificate);
		});
	}

	private void resetErrorLabel() {
		lblError.setText(" ");
	}

	private String getDisplayString(Certificate certificate) {
		return certificate.getId() + " | " + certificate.getTitle() + " | "
				+ certificate.getIssuedTo() + " | " + certificate.getIssuedBy()
				+ " | " + certificate.getYear();
	}
}
