package com.example.certmanager.view.swing;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.swing.launcher.ApplicationLauncher.*;

import java.util.regex.Pattern;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.bson.Document;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.MongoDBContainer;

import com.mongodb.MongoClient;
import com.mongodb.client.model.Filters;

@RunWith(GUITestRunner.class)
public class CertificateSwingAppE2E extends AssertJSwingJUnitTestCase {

	@ClassRule
	public static final MongoDBContainer mongo =
			new MongoDBContainer("mongo:4.4.3");

	private static final String DB_NAME = "test-db";
	private static final String COLLECTION_NAME = "test-collection";

	private static final String CERT_FIXTURE_1_ID = "1";
	private static final String CERT_FIXTURE_1_TITLE = "Java SE";
	private static final String CERT_FIXTURE_1_ISSUED_TO = "Owais";
	private static final String CERT_FIXTURE_1_ISSUED_BY = "Oracle";
	private static final int CERT_FIXTURE_1_YEAR = 2024;

	private static final String CERT_FIXTURE_2_ID = "2";
	private static final String CERT_FIXTURE_2_TITLE = "AWS Cloud";
	private static final String CERT_FIXTURE_2_ISSUED_TO = "Bilal";
	private static final String CERT_FIXTURE_2_ISSUED_BY = "Amazon";
	private static final int CERT_FIXTURE_2_YEAR = 2023;

	private MongoClient mongoClient;
	private FrameFixture window;

	@Override
	protected void onSetUp() throws Exception {
		String containerIpAddress = mongo.getContainerIpAddress();
		Integer mappedPort = mongo.getFirstMappedPort();
		mongoClient = new MongoClient(containerIpAddress, mappedPort);
		mongoClient.getDatabase(DB_NAME).drop();
		addTestCertificateToDatabase(
				CERT_FIXTURE_1_ID, CERT_FIXTURE_1_TITLE,
				CERT_FIXTURE_1_ISSUED_TO, CERT_FIXTURE_1_ISSUED_BY, CERT_FIXTURE_1_YEAR);
		addTestCertificateToDatabase(
				CERT_FIXTURE_2_ID, CERT_FIXTURE_2_TITLE,
				CERT_FIXTURE_2_ISSUED_TO, CERT_FIXTURE_2_ISSUED_BY, CERT_FIXTURE_2_YEAR);
		application("com.example.certmanager.app.swing.CertificateSwingApp")
				.withArgs(
						"--mongo-host=" + containerIpAddress,
						"--mongo-port=" + mappedPort.toString(),
						"--db-name=" + DB_NAME,
						"--db-collection=" + COLLECTION_NAME)
				.start();
		window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Digital Certificate Management System".equals(frame.getTitle())
						&& frame.isShowing();
			}
		}).using(robot());
	}

	@Override
	protected void onTearDown() {
		mongoClient.close();
	}

	@Test @GUITest
	public void testOnStartAllDatabaseElementsAreShown() {
		assertThat(window.list().contents())
				.anySatisfy(e -> assertThat(e).contains(
						CERT_FIXTURE_1_ID, CERT_FIXTURE_1_TITLE,
						CERT_FIXTURE_1_ISSUED_TO, CERT_FIXTURE_1_ISSUED_BY,
						String.valueOf(CERT_FIXTURE_1_YEAR)))
				.anySatisfy(e -> assertThat(e).contains(
						CERT_FIXTURE_2_ID, CERT_FIXTURE_2_TITLE,
						CERT_FIXTURE_2_ISSUED_TO, CERT_FIXTURE_2_ISSUED_BY,
						String.valueOf(CERT_FIXTURE_2_YEAR)));
	}

	@Test @GUITest
	public void testAddCertificateSuccess() {
		window.textBox("idTextBox").enterText("10");
		window.textBox("titleTextBox").enterText("Docker Certified");
		window.textBox("issuedToTextBox").enterText("Owais");
		window.textBox("issuedByTextBox").enterText("Docker Inc");
		window.textBox("yearTextBox").enterText("2025");
		window.button(JButtonMatcher.withText("Add Certificate")).click();
		assertThat(window.list().contents())
				.anySatisfy(e -> assertThat(e).contains(
						"10", "Docker Certified", "Owais", "Docker Inc", "2025"));
	}

	@Test @GUITest
	public void testAddCertificateError() {
		window.textBox("idTextBox").enterText(CERT_FIXTURE_1_ID);
		window.textBox("titleTextBox").enterText("Duplicate");
		window.textBox("issuedToTextBox").enterText("Owais");
		window.textBox("issuedByTextBox").enterText("Oracle");
		window.textBox("yearTextBox").enterText("2024");
		window.button(JButtonMatcher.withText("Add Certificate")).click();
		assertThat(window.label("errorLabel").text())
				.contains(CERT_FIXTURE_1_ID, CERT_FIXTURE_1_TITLE,
						CERT_FIXTURE_1_ISSUED_TO, CERT_FIXTURE_1_ISSUED_BY,
						String.valueOf(CERT_FIXTURE_1_YEAR));
	}

	@Test @GUITest
	public void testDeleteCertificateSuccess() {
		window.list("certificateList")
				.selectItem(Pattern.compile(".*" + CERT_FIXTURE_1_TITLE + ".*"));
		window.button(JButtonMatcher.withText("Delete Certificate")).click();
		assertThat(window.list().contents())
				.noneMatch(e -> e.contains(CERT_FIXTURE_1_TITLE));
	}

	@Test @GUITest
	public void testDeleteCertificateError() {
		window.list("certificateList")
				.selectItem(Pattern.compile(".*" + CERT_FIXTURE_1_TITLE + ".*"));
		removeTestCertificateFromDatabase(CERT_FIXTURE_1_ID);
		window.button(JButtonMatcher.withText("Delete Certificate")).click();
		assertThat(window.label("errorLabel").text())
				.contains(CERT_FIXTURE_1_ID, CERT_FIXTURE_1_TITLE);
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

	private void removeTestCertificateFromDatabase(String id) {
		mongoClient.getDatabase(DB_NAME).getCollection(COLLECTION_NAME)
				.deleteOne(Filters.eq("id", id));
	}
}
