package com.example.certmanager.app.swing;

import java.awt.EventQueue;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.certmanager.controller.CertificateController;
import com.example.certmanager.repository.mongo.CertificateMongoRepository;
import com.example.certmanager.view.swing.CertificateSwingView;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(mixinStandardHelpOptions = true)
public class CertificateSwingApp implements Callable<Void> {

	@Option(names = {"--mongo-host"}, description = "MongoDB host address")
	private String mongoHost = "localhost";

	@Option(names = {"--mongo-port"}, description = "MongoDB host port")
	private int mongoPort = 27017;

	@Option(names = {"--db-name"}, description = "Database name")
	private String databaseName = "certdb";

	@Option(names = {"--db-collection"}, description = "Collection name")
	private String collectionName = "certificate";

	public static void main(String[] args) {
		new CommandLine(new CertificateSwingApp()).execute(args);
	}

	@Override
	public Void call() throws Exception {
		EventQueue.invokeLater(() -> {
			try {
				CertificateMongoRepository certificateRepository =
						new CertificateMongoRepository(
								new MongoClient(new ServerAddress(mongoHost, mongoPort)),
								databaseName,
								collectionName);
				CertificateSwingView certificateView = new CertificateSwingView();
				CertificateController certificateController =
						new CertificateController(certificateRepository, certificateView);
				certificateView.setCertificateController(certificateController);
				certificateView.setVisible(true);
				certificateController.allCertificates();
			} catch (Exception e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Exception", e);
			}
		});
		return null;
	}
}
