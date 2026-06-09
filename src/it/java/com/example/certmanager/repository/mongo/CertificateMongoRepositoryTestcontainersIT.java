package com.example.certmanager.repository.mongo;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.MongoDBContainer;

import com.example.certmanager.model.Certificate;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class CertificateMongoRepositoryTestcontainersIT {

	@ClassRule
	public static final MongoDBContainer mongo =
			new MongoDBContainer("mongo:4.4.3");

	private MongoClient client;
	private CertificateMongoRepository certificateRepository;
	private MongoCollection<Document> certificateCollection;

	private static final String CERT_DB_NAME = "certdb";
	private static final String CERT_COLLECTION_NAME = "certificate";

	@Before
	public void setup() {
		client = new MongoClient(new ServerAddress(
				mongo.getHost(), mongo.getFirstMappedPort()));
		certificateRepository = new CertificateMongoRepository(
				client, CERT_DB_NAME, CERT_COLLECTION_NAME);
		MongoDatabase database = client.getDatabase(CERT_DB_NAME);
		database.drop();
		certificateCollection = database.getCollection(CERT_COLLECTION_NAME);
	}

	@After
	public void tearDown() {
		client.close();
	}

	@Test
	public void testFindAll() {
		addTestCertificateToDatabase("1", "Java SE", "Owais", "Oracle", 2024);
		addTestCertificateToDatabase("2", "AWS Cloud", "Bilal", "Amazon", 2023);
		assertThat(certificateRepository.findAll())
				.containsExactly(
						new Certificate("1", "Java SE", "Owais", "Oracle", 2024),
						new Certificate("2", "AWS Cloud", "Bilal", "Amazon", 2023));
	}

	@Test
	public void testFindById() {
		addTestCertificateToDatabase("1", "Java SE", "Owais", "Oracle", 2024);
		addTestCertificateToDatabase("2", "AWS Cloud", "Bilal", "Amazon", 2023);
		assertThat(certificateRepository.findById("2"))
				.isEqualTo(new Certificate("2", "AWS Cloud", "Bilal", "Amazon", 2023));
	}

	@Test
	public void testSave() {
		Certificate certificate = new Certificate("1", "Java SE", "Owais", "Oracle", 2024);
		certificateRepository.save(certificate);
		assertThat(readAllCertificatesFromDatabase()).containsExactly(certificate);
	}

	@Test
	public void testDelete() {
		addTestCertificateToDatabase("1", "Java SE", "Owais", "Oracle", 2024);
		certificateRepository.delete("1");
		assertThat(readAllCertificatesFromDatabase()).isEmpty();
	}

	@Test
	public void testUpdate() {
		addTestCertificateToDatabase("1", "Java SE", "Owais", "Oracle", 2024);
		Certificate updated = new Certificate("1", "Java SE 17", "Owais", "Oracle", 2025);
		certificateRepository.update(updated);
		assertThat(readAllCertificatesFromDatabase()).containsExactly(updated);
	}

	private void addTestCertificateToDatabase(String id, String title,
			String issuedTo, String issuedBy, int year) {
		certificateCollection.insertOne(new Document()
				.append("id", id)
				.append("title", title)
				.append("issuedTo", issuedTo)
				.append("issuedBy", issuedBy)
				.append("year", year));
	}

	private List<Certificate> readAllCertificatesFromDatabase() {
		return StreamSupport
				.stream(certificateCollection.find().spliterator(), false)
				.map(d -> new Certificate(
						"" + d.get("id"),
						"" + d.get("title"),
						"" + d.get("issuedTo"),
						"" + d.get("issuedBy"),
						((Number) d.get("year")).intValue()))
				.collect(Collectors.toList());
	}
}
