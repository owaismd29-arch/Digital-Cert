package com.example.certmanager.view.swing;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.timeout;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultListModel;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.awaitility.Awaitility;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.certmanager.controller.CertificateController;
import com.example.certmanager.model.Certificate;

@RunWith(GUITestRunner.class)
public class CertificateSwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;
	private CertificateSwingView certificateSwingView;

	@Mock
	private CertificateController certificateController;

	private AutoCloseable closeable;

	@Override
	protected void onSetUp() throws Exception {
		closeable = MockitoAnnotations.openMocks(this);
		GuiActionRunner.execute(() -> {
			certificateSwingView = new CertificateSwingView();
			certificateSwingView.setCertificateController(certificateController);
			return certificateSwingView;
		});
		window = new FrameFixture(robot(), certificateSwingView);
		window.show();
	}

	@Override
	protected void onTearDown() throws Exception {
		closeable.close();
	}

	@Test @GUITest
	public void testInitialState() {
		window.button("btnAdd").requireDisabled();
		window.button("btnUpdate").requireDisabled();
		window.button("btnDelete").requireDisabled();
		window.textBox("idTextBox").requireEmpty();
		window.textBox("titleTextBox").requireEmpty();
		window.textBox("issuedToTextBox").requireEmpty();
		window.textBox("issuedByTextBox").requireEmpty();
		window.textBox("yearTextBox").requireEmpty();
	}

	@Test @GUITest
	public void testAddButtonEnabledWhenAllFieldsAreFilled() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("titleTextBox").enterText("Java SE");
		window.textBox("issuedToTextBox").enterText("Owais");
		window.textBox("issuedByTextBox").enterText("Oracle");
		window.textBox("yearTextBox").enterText("2024");
		window.button("btnAdd").requireEnabled();
	}

	@Test @GUITest
	public void testAddButtonDisabledWhenAnyFieldIsEmpty() {
		JTextComponentFixture idBox = window.textBox("idTextBox");
		JTextComponentFixture titleBox = window.textBox("titleTextBox");
		JTextComponentFixture issuedToBox = window.textBox("issuedToTextBox");
		JTextComponentFixture issuedByBox = window.textBox("issuedByTextBox");
		JTextComponentFixture yearBox = window.textBox("yearTextBox");

		idBox.enterText(" ");
		titleBox.enterText("Java SE");
		issuedToBox.enterText("Owais");
		issuedByBox.enterText("Oracle");
		yearBox.enterText("2024");
		window.button(JButtonMatcher.withText("Add Certificate")).requireDisabled();

		idBox.setText(""); titleBox.setText(""); issuedToBox.setText("");
		issuedByBox.setText(""); yearBox.setText("");

		idBox.enterText("1");
		titleBox.enterText(" ");
		issuedToBox.enterText("Owais");
		issuedByBox.enterText("Oracle");
		yearBox.enterText("2024");
		window.button(JButtonMatcher.withText("Add Certificate")).requireDisabled();
	}

	@Test @GUITest
	public void testDeleteButtonEnabledWhenCertificateIsSelected() {
		GuiActionRunner.execute(() ->
				certificateSwingView.getListCertificateModel()
						.addElement(new Certificate("1", "Java SE", "Owais", "Oracle", 2024)));
		window.list("certificateList").selectItem(0);
		JButtonFixture deleteButton = window.button(JButtonMatcher.withText("Delete Certificate"));
		deleteButton.requireEnabled();
		window.list("certificateList").clearSelection();
		Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(deleteButton::requireDisabled);
	}

	@Test @GUITest
	public void testShowAllCertificates() {
		Certificate cert1 = new Certificate("1", "Java SE", "Owais", "Oracle", 2024);
		Certificate cert2 = new Certificate("2", "AWS Cloud", "Bilal", "Amazon", 2023);
		GuiActionRunner.execute(() ->
				certificateSwingView.showAllCertificates(Arrays.asList(cert1, cert2)));
		Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
				assertThat(window.list().contents())
						.containsExactly(cert1.toString(), cert2.toString()));
	}

	@Test @GUITest
	public void testShowErrorMessage() {
		Certificate certificate = new Certificate("1", "Java SE", "Owais", "Oracle", 2024);
		certificateSwingView.showError("error message", certificate);
		Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
				window.label("errorLabel")
						.requireText("error message: " + certificate));
	}

	@Test @GUITest
	public void testShowErrorCertificateNotFound() {
		Certificate cert1 = new Certificate("1", "Java SE", "Owais", "Oracle", 2024);
		Certificate cert2 = new Certificate("2", "AWS Cloud", "Bilal", "Amazon", 2023);
		GuiActionRunner.execute(() -> {
			certificateSwingView.getListCertificateModel().addElement(cert1);
			certificateSwingView.getListCertificateModel().addElement(cert2);
		});
		certificateSwingView.showErrorCertificateNotFound("error message", cert1);
		Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			window.label("errorLabel").requireText("error message: " + cert1);
			assertThat(window.list().contents()).containsExactly(cert2.toString());
		});
	}

	@Test @GUITest
	public void testCertificateAdded() {
		Certificate certificate = new Certificate("1", "Java SE", "Owais", "Oracle", 2024);
		certificateSwingView.certificateAdded(certificate);
		Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(window.list().contents()).containsExactly(certificate.toString());
			window.label("errorLabel").requireText(" ");
		});
	}

	@Test @GUITest
	public void testCertificateRemoved() {
		Certificate cert1 = new Certificate("1", "Java SE", "Owais", "Oracle", 2024);
		Certificate cert2 = new Certificate("2", "AWS Cloud", "Bilal", "Amazon", 2023);
		GuiActionRunner.execute(() -> {
			DefaultListModel<Certificate> model = certificateSwingView.getListCertificateModel();
			model.addElement(cert1);
			model.addElement(cert2);
		});
		certificateSwingView.certificateRemoved(cert1);
		Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(window.list().contents()).containsExactly(cert2.toString());
			window.label("errorLabel").requireText(" ");
		});
	}

	@Test @GUITest
	public void testCertificateUpdated() {
		Certificate original = new Certificate("1", "Java SE", "Owais", "Oracle", 2024);
		Certificate updated = new Certificate("1", "Java SE 17", "Owais", "Oracle", 2025);
		certificateSwingView.certificateAdded(original);
		certificateSwingView.certificateUpdated(updated);
		Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(window.list().contents()).containsExactly(updated.toString());
			window.label("errorLabel").requireText(" ");
		});
	}

	@Test @GUITest
	public void testAddButtonDelegatesToController() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("titleTextBox").enterText("Java SE");
		window.textBox("issuedToTextBox").enterText("Owais");
		window.textBox("issuedByTextBox").enterText("Oracle");
		window.textBox("yearTextBox").enterText("2024");
		window.button(JButtonMatcher.withText("Add Certificate")).requireEnabled();
		window.button(JButtonMatcher.withText("Add Certificate")).click();
		verify(certificateController, timeout(5000))
				.newCertificate(new Certificate("1", "Java SE", "Owais", "Oracle", 2024));
	}

	@Test @GUITest
	public void testDeleteButtonDelegatesToController() {
		Certificate cert1 = new Certificate("1", "Java SE", "Owais", "Oracle", 2024);
		Certificate cert2 = new Certificate("2", "AWS Cloud", "Bilal", "Amazon", 2023);
		GuiActionRunner.execute(() -> {
			DefaultListModel<Certificate> model = certificateSwingView.getListCertificateModel();
			model.addElement(cert1);
			model.addElement(cert2);
		});
		window.list("certificateList").selectItem(1);
		window.button(JButtonMatcher.withText("Delete Certificate")).click();
		Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
				verify(certificateController).deleteCertificate(cert2));
	}

	@Test @GUITest
	public void testUpdateButtonDelegatesToController() {
		Certificate cert = new Certificate("1", "Java SE", "Owais", "Oracle", 2024);
		GuiActionRunner.execute(() ->
				certificateSwingView.getListCertificateModel().addElement(cert));
		window.list("certificateList").selectItem(0);
		window.textBox("titleTextBox").setText("Java SE 17");
		window.textBox("yearTextBox").setText("2025");
		window.button(JButtonMatcher.withText("Update Certificate")).click();
		Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
				verify(certificateController)
						.updateCertificate(new Certificate("1", "Java SE 17", "Owais", "Oracle", 2025)));
	}

	@Test @GUITest
	public void testWhenCertificateIsSelectedFieldsArePopulated() {
		Certificate cert = new Certificate("1", "Java SE", "Owais", "Oracle", 2024);
		GuiActionRunner.execute(() ->
				certificateSwingView.getListCertificateModel().addElement(cert));
		window.list("certificateList").selectItem(0);
		window.textBox("idTextBox").requireText("1");
		window.textBox("titleTextBox").requireText("Java SE");
		window.textBox("issuedToTextBox").requireText("Owais");
		window.textBox("issuedByTextBox").requireText("Oracle");
		window.textBox("yearTextBox").requireText("2024");
	}
}
