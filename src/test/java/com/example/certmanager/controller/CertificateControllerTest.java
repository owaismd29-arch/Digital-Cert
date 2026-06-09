package com.example.certmanager.controller;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.certmanager.model.Certificate;
import com.example.certmanager.repository.CertificateRepository;
import com.example.certmanager.view.CertificateView;

public class CertificateControllerTest {

	@Mock
	private CertificateRepository certificateRepository;

	@Mock
	private CertificateView certificateView;

	@InjectMocks
	private CertificateController certificateController;

	private AutoCloseable closeable;

	@Before
	public void setup() {
		closeable = MockitoAnnotations.openMocks(this);
	}

	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}

	@Test
	public void testAllCertificates() {
		List<Certificate> certificates = asList(new Certificate());
		when(certificateRepository.findAll()).thenReturn(certificates);
		certificateController.allCertificates();
		verify(certificateView).showAllCertificates(certificates);
	}

	@Test
	public void testAllCertificatesWhenEmpty() {
		when(certificateRepository.findAll()).thenReturn(null);
		certificateController.allCertificates();
		verify(certificateView).showAllCertificates(null);
	}

	@Test
	public void testNewCertificateWhenCertificateDoesNotAlreadyExist() {
		Certificate certificate = new Certificate("1", "Java SE", "Owais", "Oracle", 2024);
		when(certificateRepository.findById("1")).thenReturn(null);
		certificateController.newCertificate(certificate);
		InOrder inOrder = inOrder(certificateRepository, certificateView);
		inOrder.verify(certificateRepository).save(certificate);
		inOrder.verify(certificateView).certificateAdded(certificate);
	}

	@Test
	public void testNewCertificateWhenCertificateAlreadyExists() {
		Certificate existing = new Certificate("1", "Java SE", "Owais", "Oracle", 2024);
		Certificate toAdd = new Certificate("1", "Java SE", "Owais", "Oracle", 2024);
		when(certificateRepository.findById("1")).thenReturn(existing);
		certificateController.newCertificate(toAdd);
		verify(certificateView).showError("Already existing certificate with id 1", existing);
		verifyNoMoreInteractions(ignoreStubs(certificateRepository));
	}

	@Test
	public void testUpdateCertificateWhenCertificateExists() {
		Certificate certificate = new Certificate("1", "Java SE Updated", "Owais", "Oracle", 2025);
		when(certificateRepository.findById("1")).thenReturn(certificate);
		certificateController.updateCertificate(certificate);
		InOrder inOrder = inOrder(certificateRepository, certificateView);
		inOrder.verify(certificateRepository).update(certificate);
		inOrder.verify(certificateView).certificateUpdated(certificate);
	}

	@Test
	public void testUpdateCertificateWhenCertificateDoesNotExist() {
		Certificate certificate = new Certificate("1", "Java SE", "Owais", "Oracle", 2024);
		when(certificateRepository.findById("1")).thenReturn(null);
		certificateController.updateCertificate(certificate);
		verify(certificateView).showErrorCertificateNotFound(
				"No existing certificate with id 1", certificate);
		verifyNoMoreInteractions(ignoreStubs(certificateRepository));
	}

	@Test
	public void testDeleteCertificateWhenCertificateExists() {
		Certificate certificate = new Certificate("1", "Java SE", "Owais", "Oracle", 2024);
		when(certificateRepository.findById("1")).thenReturn(certificate);
		certificateController.deleteCertificate(certificate);
		InOrder inOrder = inOrder(certificateRepository, certificateView);
		inOrder.verify(certificateRepository).delete("1");
		inOrder.verify(certificateView).certificateRemoved(certificate);
	}

	@Test
	public void testDeleteCertificateWhenCertificateDoesNotExist() {
		Certificate certificate = new Certificate("1", "Java SE", "Owais", "Oracle", 2024);
		when(certificateRepository.findById("1")).thenReturn(null);
		certificateController.deleteCertificate(certificate);
		verify(certificateView).showErrorCertificateNotFound(
				"No existing certificate with id 1", certificate);
		verifyNoMoreInteractions(ignoreStubs(certificateRepository));
	}
}
