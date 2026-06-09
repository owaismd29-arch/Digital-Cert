package com.example.certmanager.controller;

import com.example.certmanager.model.Certificate;
import com.example.certmanager.repository.CertificateRepository;
import com.example.certmanager.view.CertificateView;

public class CertificateController {

	private CertificateRepository certificateRepository;
	private CertificateView certificateView;

	public CertificateController(CertificateRepository certificateRepository,
			CertificateView certificateView) {
		this.certificateRepository = certificateRepository;
		this.certificateView = certificateView;
	}

	public void allCertificates() {
		certificateView.showAllCertificates(certificateRepository.findAll());
	}

	public synchronized void newCertificate(Certificate certificate) {
		Certificate existing = certificateRepository.findById(certificate.getId());
		if (existing != null) {
			certificateView.showError(
					"Already existing certificate with id " + certificate.getId(), existing);
			return;
		}
		certificateRepository.save(certificate);
		certificateView.certificateAdded(certificate);
	}

	public synchronized void updateCertificate(Certificate certificate) {
		Certificate existing = certificateRepository.findById(certificate.getId());
		if (existing == null) {
			certificateView.showErrorCertificateNotFound(
					"No existing certificate with id " + certificate.getId(), certificate);
			return;
		}
		certificateRepository.update(certificate);
		certificateView.certificateUpdated(certificate);
	}

	public synchronized void deleteCertificate(Certificate certificate) {
		if (certificateRepository.findById(certificate.getId()) == null) {
			certificateView.showErrorCertificateNotFound(
					"No existing certificate with id " + certificate.getId(), certificate);
			return;
		}
		certificateRepository.delete(certificate.getId());
		certificateView.certificateRemoved(certificate);
	}
}
