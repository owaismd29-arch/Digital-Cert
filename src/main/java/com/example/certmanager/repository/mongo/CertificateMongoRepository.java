// MongoDB implementation of CertificateRepository
package com.example.certmanager.repository.mongo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.example.certmanager.model.Certificate;
import com.example.certmanager.repository.CertificateRepository;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class CertificateMongoRepository implements CertificateRepository {

	private MongoCollection<Document> certificateCollection;

	private static final String ID = "id";
	private static final String TITLE = "title";
	private static final String ISSUED_TO = "issuedTo";
	private static final String ISSUED_BY = "issuedBy";
	private static final String YEAR = "year";

	public CertificateMongoRepository(MongoClient client, String databaseName, String collectionName) {
		certificateCollection = client
				.getDatabase(databaseName)
				.getCollection(collectionName);
	}

	@Override
	public void save(Certificate certificate) {
		certificateCollection.insertOne(
				new Document()
						.append(ID, certificate.getId())
						.append(TITLE, certificate.getTitle())
						.append(ISSUED_TO, certificate.getIssuedTo())
						.append(ISSUED_BY, certificate.getIssuedBy())
						.append(YEAR, certificate.getYear()));
	}

	@Override
	public List<Certificate> findAll() {
		return StreamSupport
				.stream(certificateCollection.find().spliterator(), false)
				.map(this::fromDocumentToCertificate)
				.collect(Collectors.toList());
	}

	private Certificate fromDocumentToCertificate(Document d) {
		return new Certificate(
				"" + d.get(ID),
				"" + d.get(TITLE),
				"" + d.get(ISSUED_TO),
				"" + d.get(ISSUED_BY),
				((Number) d.get(YEAR)).intValue());
	}

	@Override
	public Certificate findById(String id) {
		Document d = certificateCollection.find(Filters.eq(ID, id)).first();
		if (d != null)
			return fromDocumentToCertificate(d);
		return null;
	}

	@Override
	public void update(Certificate certificate) {
		certificateCollection.updateOne(
				Filters.eq(ID, certificate.getId()),
				new Document("$set", new Document()
						.append(TITLE, certificate.getTitle())
						.append(ISSUED_TO, certificate.getIssuedTo())
						.append(ISSUED_BY, certificate.getIssuedBy())
						.append(YEAR, certificate.getYear())));
	}

	@Override
	public void delete(String id) {
		certificateCollection.deleteOne(Filters.eq(ID, id));
	}
}
