package com.example.certmanager.bdd.steps;

import java.util.List;

import org.bson.Document;

import com.example.certmanager.bdd.CertificateSwingAppBDD;
import com.mongodb.MongoClient;
import com.mongodb.client.model.Filters;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;

public class DatabaseSteps {

	static final String DB_NAME = "test-db";
	static final String COLLECTION_NAME = "test-collection";

	static final String CERT_FIXTURE_1_ID = "1";
	static final String CERT_FIXTURE_1_TITLE = "Java SE";
	static final String CERT_FIXTURE_1_ISSUED_TO = "Owais";
	static final String CERT_FIXTURE_1_ISSUED_BY = "Oracle";
	static final int CERT_FIXTURE_1_YEAR = 2024;

	static final String CERT_FIXTURE_2_ID = "2";
	static final String CERT_FIXTURE_2_TITLE = "AWS Cloud";
	static final String CERT_FIXTURE_2_ISSUED_TO = "Bilal";
	static final String CERT_FIXTURE_2_ISSUED_BY = "Amazon";
	static final int CERT_FIXTURE_2_YEAR = 2023;

	private MongoClient mongoClient;

	@Before
	public void setUp() {
		mongoClient = new MongoClient("localhost", CertificateSwingAppBDD.mongoPort);
		mongoClient.getDatabase(DB_NAME).drop();
	}

	@After
	public void tearDown() {
		mongoClient.close();
	}

	@Given("The database contains the certificates with the following values")
	public void the_database_contains_the_certificates_with_the_following_values(
			List<List<String>> values) {
		values.stream().skip(1).forEach(v ->
				addTestCertificateToDatabase(
						v.get(0), v.get(1), v.get(2), v.get(3),
						Integer.parseInt(v.get(4))));
	}

	@Given("The database contains a few certificates")
	public void the_database_contains_a_few_certificates() {
		addTestCertificateToDatabase(
				CERT_FIXTURE_1_ID, CERT_FIXTURE_1_TITLE,
				CERT_FIXTURE_1_ISSUED_TO, CERT_FIXTURE_1_ISSUED_BY, CERT_FIXTURE_1_YEAR);
		addTestCertificateToDatabase(
				CERT_FIXTURE_2_ID, CERT_FIXTURE_2_TITLE,
				CERT_FIXTURE_2_ISSUED_TO, CERT_FIXTURE_2_ISSUED_BY, CERT_FIXTURE_2_YEAR);
	}

	@Given("The certificate is in the meantime removed from the database")
	public void the_certificate_is_in_the_meantime_removed_from_the_database() {
		mongoClient.getDatabase(DB_NAME).getCollection(COLLECTION_NAME)
				.deleteOne(Filters.eq("id", CERT_FIXTURE_1_ID));
	}

	private void addTestCertificateToDatabase(String id, String title,
			String issuedTo, String issuedBy, int year) {
		mongoClient.getDatabase(DB_NAME).getCollection(COLLECTION_NAME)
				.insertOne(new Document()
						.append("id", id)
						.append("title", title)
						.append("issuedTo", issuedTo)
						.append("issuedBy", issuedBy)
						.append("year", year));
	}
}
