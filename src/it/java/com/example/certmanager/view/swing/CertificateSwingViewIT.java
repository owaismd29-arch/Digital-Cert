package com.example.certmanager.view.swing;

import static org.assertj.core.api.Assertions.*;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.awaitility.Awaitility;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.certmanager.controller.CertificateController;
import com.example.certmanager.model.Certificate;
import com.example.certmanager.repository.mongo.CertificateMongoRepository;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

@RunWith(GUITestRunner.class)
public class CertificateSwingViewIT extends AssertJSwingJUnitTestCase {

	private static MongoServer server;
	private static InetSocketAddress serverAddress;

	private MongoClient client;
	private FrameFixture window;
	private CertificateSwingView certificateSwingView;
	private CertificateController certificateController;
	private CertificateMongoRepository certificateRepository;

	private static final String CERT_DB_NAME = "certdb";
	private static final String CERT_COLLECTION_NAME = "certificate";

	@BeforeClass
	public static void setupServer() {
		server = new MongoServer(new MemoryBackend());
		serverAddress = server.bind();
	}

	@AfterClass
	public static void shutdownServer() {
		server.shutdown();
	}

	@Override
	protected void onSetUp() {
		client = new MongoClient(new ServerAddress(serverAddress));
		certificateRepository = new CertificateMongoRepository(
				client, CERT_DB_NAME, CERT_COLLECTION_NAME);
		for (Certificate c : certificateRepository.findAll()) {
			certificateRepository.delete(c.getId());
		}
		GuiActionRunner.execute(() -> {
			certificateSwingView = new CertificateSwingView();
			certificateController = new CertificateController(
					certificateRepository, certificateSwingView);
			certificateSwingView.setCertificateController(certificateController);
			return certificateSwingView;
		});
		window = new FrameFixture(robot(), certificateSwingView);
		window.show();
	}

	@Override
	protected void onTearDown() {
		client.close();
	}

	@Test @GUITest
	public void testAllCertificates() {
		Certificate cert1 = new Certificate("1", "Java SE", "Owais", "Oracle", 2024);
		Certificate cert2 = new Certificate("2", "AWS Cloud", "Bilal", "Amazon", 2023);
		certificateRepository.save(cert1);
		certificateRepository.save(cert2);
		GuiActionRunner.execute(() -> certificateController.allCertificates());
		assertThat(window.list().contents())
				.containsExactly(cert1.toString(), cert2.toString());
	}

	@Test @GUITest
	public void testAddCertificateSuccess() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("titleTextBox").enterText("Java SE");
		window.textBox("issuedToTextBox").enterText("Owais");
		window.textBox("issuedByTextBox").enterText("Oracle");
		window.textBox("yearTextBox").enterText("2024");
		window.button(JButtonMatcher.withText("Add Certificate")).click();
		Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
				assertThat(window.list().contents())
						.containsExactly(
								new Certificate("1", "Java SE", "Owais", "Oracle", 2024).toString()));
	}

	@Test @GUITest
	public void testDeleteCertificateSuccess() {
		certificateController.newCertificate(
				new Certificate("1", "Java SE", "Owais", "Oracle", 2024));
		window.list().selectItem(0);
		window.button(JButtonMatcher.withText("Delete Certificate")).click();
		Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
				assertThat(window.list().contents()).isEmpty());
	}

	@Test @GUITest
	public void testDeleteCertificateError() {
		Certificate certificate = new Certificate("1", "Java SE", "Owais", "Oracle", 2024);
		GuiActionRunner.execute(() ->
				certificateSwingView.getListCertificateModel().addElement(certificate));
		window.list().selectItem(0);
		window.button(JButtonMatcher.withText("Delete Certificate")).click();
		Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(window.list().contents()).isEmpty();
			window.label("errorLabel").requireText(
					"No existing certificate with id 1: " + certificate);
		});
	}
}
