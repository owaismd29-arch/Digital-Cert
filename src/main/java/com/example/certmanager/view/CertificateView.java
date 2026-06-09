package com.example.certmanager.view;

import java.util.List;

import com.example.certmanager.model.Certificate;

public interface CertificateView {
	void showAllCertificates(List<Certificate> certificates);
	void certificateAdded(Certificate certificate);
	void certificateRemoved(Certificate certificate);
	void certificateUpdated(Certificate certificate);
	void showError(String message, Certificate certificate);
	void showErrorCertificateNotFound(String message, Certificate certificate);
}
