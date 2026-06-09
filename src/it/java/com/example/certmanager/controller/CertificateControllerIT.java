package com.example.certmanager.controller;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.certmanager.model.Certificate;
import com.example.certmanager.repository.CertificateRepository;
import com.example.certmanager.repository.mongo.CertificateMongoRepository;
import com.example.certmanager.view.CertificateView;
import com.mongodb.MongoClient;

public class CertificateControllerIT {

	@Mock
	private CertificateView certificateView;

	private CertificateRepository certificateRepository;
	private CertificateController certificateController;

	private AutoCloseable closeable;

	private static final String CERT_DB_NAME = "certdb";
	private static final String CERT_COLLECTION_NAME = "certificate";

	private static int mongoPort =
			Integer.parseInt(System.getProperty("mongo.port", "27017"));

	@Before
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
		certificateRepository = new CertificateMongoRepository(
				new MongoClient("localhost", mongoPort),
				CERT_DB_NAME, CERT_COLLECTION_NAME);
		for (Certificate c : certificateRepository.findAll()) {
			certificateRepository.delete(c.getId());
		}
		certificateController = new CertificateController(certificateRepository, certificateView);
	}

	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}

	@Test
	public void testAllCertificates() {
		Certificate certificate = new Certificate("1", "Java SE", "Owais", "Oracle", 2024);
		certificateRepository.save(certificate);
		certificateController.allCertificates();
		verify(certificateView).showAllCertificates(asList(certificate));
	}

	@Test
	public void testNewCertificate() {
		Certificate certificate = new Certificate("1", "Java SE", "Owais", "Oracle", 2024);
		certificateController.newCertificate(certificate);
		verify(certificateView).certificateAdded(certificate);
	}

	@Test
	public void testDeleteCertificate() {
		Certificate certificate = new Certificate("1", "Java SE", "Owais", "Oracle", 2024);
		certificateRepository.save(certificate);
		certificateController.deleteCertificate(certificate);
		verify(certificateView).certificateRemoved(certificate);
	}

	@Test
	public void testUpdateCertificate() {
		Certificate original = new Certificate("1", "Java SE", "Owais", "Oracle", 2024);
		certificateRepository.save(original);
		Certificate updated = new Certificate("1", "Java SE 17", "Owais", "Oracle", 2025);
		certificateController.updateCertificate(updated);
		verify(certificateView).certificateUpdated(updated);
	}
}
