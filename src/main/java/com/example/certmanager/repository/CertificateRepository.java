// Repository interface for CRUD operations
package com.example.certmanager.repository;

import java.util.List;

import com.example.certmanager.model.Certificate;

public interface CertificateRepository {
	void save(Certificate certificate);
	List<Certificate> findAll();
	Certificate findById(String id);
	void update(Certificate certificate);
	void delete(String id);
}
