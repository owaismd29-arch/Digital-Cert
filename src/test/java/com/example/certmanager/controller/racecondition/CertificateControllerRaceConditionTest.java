package com.example.certmanager.controller.racecondition;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.awaitility.Awaitility;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.certmanager.controller.CertificateController;
import com.example.certmanager.model.Certificate;
import com.example.certmanager.repository.CertificateRepository;
import com.example.certmanager.view.CertificateView;

public class CertificateControllerRaceConditionTest {

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
	public void testNewCertificateConcurrent() {
		List<Certificate> certificates = new ArrayList<>();
		Certificate certificate = new Certificate("1", "Java SE", "Owais", "Oracle", 2024);
		when(certificateRepository.findById(anyString()))
				.thenAnswer(invocation -> certificates.stream().findFirst().orElse(null));
		doAnswer(invocation -> {
			certificates.add(certificate);
			return null;
		}).when(certificateRepository).save(any(Certificate.class));
		List<Thread> threads = IntStream.range(0, 10)
				.mapToObj(i -> new Thread(() -> certificateController.newCertificate(certificate)))
				.peek(Thread::start)
				.collect(Collectors.toList());
		Awaitility.await().atMost(10, TimeUnit.SECONDS)
				.until(() -> threads.stream().noneMatch(Thread::isAlive));
		assertThat(certificates).containsExactly(certificate);
	}

	@Test
	public void testDeleteCertificateConcurrent() {
		List<Certificate> certificates = new ArrayList<>();
		Certificate certificate = new Certificate("1", "Java SE", "Owais", "Oracle", 2024);
		certificates.add(certificate);
		when(certificateRepository.findById(anyString()))
				.thenAnswer(invocation -> certificates.stream().findFirst().orElse(null));
		doAnswer(invocation -> {
			certificates.remove(certificate);
			return null;
		}).when(certificateRepository).delete(anyString());
		List<Thread> threads = IntStream.range(0, 10)
				.mapToObj(i -> new Thread(() -> certificateController.deleteCertificate(certificate)))
				.peek(Thread::start)
				.collect(Collectors.toList());
		Awaitility.await().atMost(10, TimeUnit.SECONDS)
				.until(() -> threads.stream().noneMatch(Thread::isAlive));
		assertThat(certificates).isEmpty();
	}

	@Test
	public void testUpdateCertificateConcurrent() {
		List<Certificate> certificates = new ArrayList<>();
		Certificate original = new Certificate("1", "Java SE", "Owais", "Oracle", 2024);
		Certificate updated = new Certificate("1", "Java SE 17", "Owais", "Oracle", 2025);
		certificates.add(original);
		when(certificateRepository.findById(anyString()))
				.thenAnswer(invocation -> certificates.stream().findFirst().orElse(null));
		doAnswer(invocation -> {
			certificates.clear();
			certificates.add(updated);
			return null;
		}).when(certificateRepository).update(any(Certificate.class));
		List<Thread> threads = IntStream.range(0, 10)
				.mapToObj(i -> new Thread(() -> certificateController.updateCertificate(updated)))
				.peek(Thread::start)
				.collect(Collectors.toList());
		Awaitility.await().atMost(10, TimeUnit.SECONDS)
				.until(() -> threads.stream().noneMatch(Thread::isAlive));
		assertThat(certificates).containsExactly(updated);
	}
}
