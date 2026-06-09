package com.example.certmanager.view.swing;

import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.TimeUnit;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.awaitility.Awaitility;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.MongoDBContainer;

import com.example.certmanager.controller.CertificateController;
import com.example.certmanager.model.Certificate;
import com.example.certmanager.repository.mongo.CertificateMongoRepository;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

@RunWith(GUITestRunner.class)
public class ModelViewControllerIT extends AssertJSwingJUnitTestCase {

	@ClassRule
	public static final MongoDBContainer mongo =
			new MongoDBContainer("mongo:4.4.3");

	private MongoClient client;
	private FrameFixture window;
	private CertificateMongoRepository certificateRepository;
	private CertificateController certificateController;

	private static final String CERT_DB_NAME = "certdb";
	private static final String CERT_COLLECTION_NAME = "certificate";

	@Override
	protected void onSetUp() {
		client = new MongoClient(new ServerAddress(
				mongo.getHost(), mongo.getFirstMappedPort()));
		certificateRepository = new CertificateMongoRepository(
				client, CERT_DB_NAME, CERT_COLLECTION_NAME);
		for (Certificate c : certificateRepository.findAll()) {
			certificateRepository.delete(c.getId());
		}
		window = new FrameFixture(robot(), GuiActionRunner.execute(() -> {
			CertificateSwingView certificateSwingView = new CertificateSwingView();
			certificateController = new CertificateController(
					certificateRepository, certificateSwingView);
			certificateSwingView.setCertificateController(certificateController);
			return certificateSwingView;
		}));
		window.show();
	}

	@Override
	protected void onTearDown() {
		client.close();
	}

	@Test
	public void testAddCertificate() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("titleTextBox").enterText("Java SE");
		window.textBox("issuedToTextBox").enterText("Owais");
		window.textBox("issuedByTextBox").enterText("Oracle");
		window.textBox("yearTextBox").enterText("2024");
		window.button(JButtonMatcher.withText("Add Certificate")).click();
		Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
				assertThat(certificateRepository.findById("1"))
						.isEqualTo(new Certificate("1", "Java SE", "Owais", "Oracle", 2024)));
	}

	@Test
	public void testDeleteCertificate() {
		certificateRepository.save(
				new Certificate("1", "Java SE", "Owais", "Oracle", 2024));
		GuiActionRunner.execute(() -> certificateController.allCertificates());
		window.list().selectItem(0);
		window.button(JButtonMatcher.withText("Delete Certificate")).click();
		Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
				assertThat(certificateRepository.findById("1")).isNull());
	}

	@Test
	public void testUpdateCertificate() {
		certificateRepository.save(
				new Certificate("1", "Java SE", "Owais", "Oracle", 2024));
		GuiActionRunner.execute(() -> certificateController.allCertificates());
		window.list().selectItem(0);
		window.textBox("titleTextBox").setText("Java SE 17");
		window.textBox("yearTextBox").setText("2025");
		window.button(JButtonMatcher.withText("Update Certificate")).click();
		Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
				assertThat(certificateRepository.findById("1"))
						.isEqualTo(new Certificate("1", "Java SE 17", "Owais", "Oracle", 2025)));
	}
}
