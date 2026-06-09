package com.example.certmanager.bdd.steps;

import static com.example.certmanager.bdd.steps.DatabaseSteps.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.swing.launcher.ApplicationLauncher.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.awaitility.Awaitility;

import com.example.certmanager.bdd.CertificateSwingAppBDD;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CertificateSwingAppSteps {

	private FrameFixture window;

	@After
	public void tearDown() {
		if (window != null)
			window.cleanUp();
	}

	@When("The Certificate View is shown")
	public void the_Certificate_View_is_shown() {
		application("com.example.certmanager.app.swing.CertificateSwingApp")
				.withArgs(
						"--mongo-port=" + CertificateSwingAppBDD.mongoPort,
						"--db-name=" + DB_NAME,
						"--db-collection=" + COLLECTION_NAME)
				.start();
		window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Digital Certificate Management System".equals(frame.getTitle())
						&& frame.isShowing();
			}
		}).using(BasicRobot.robotWithCurrentAwtHierarchy());
	}

	@Then("The list contains elements with the following values")
	public void the_list_contains_elements_with_the_following_values(
			List<List<String>> values) {
		values.forEach(v ->
				assertThat(window.list().contents())
						.anySatisfy(e -> assertThat(e)
								.contains(v.get(0), v.get(1), v.get(2), v.get(3), v.get(4))));
	}

	@When("The user enters the following values in the text fields")
	public void the_user_enters_the_following_values_in_the_text_fields(
			List<Map<String, String>> values) {
		values.stream()
				.flatMap(m -> m.entrySet().stream())
				.forEach(e -> window.textBox(e.getKey() + "TextBox").enterText(e.getValue()));
	}

	@When("The user clicks the {string} button")
	public void the_user_clicks_the_button(String buttonText) {
		window.button(JButtonMatcher.withText(buttonText)).click();
	}

	@Then("An error is shown containing the following values")
	public void an_error_is_shown_containing_the_following_values(
			List<List<String>> values) {
		assertThat(window.label("errorLabel").text())
				.contains(values.get(0));
	}

	@Given("The user provides certificate data in the text fields")
	public void the_user_provides_certificate_data_in_the_text_fields() {
		window.textBox("idTextBox").enterText(CERT_FIXTURE_1_ID);
		window.textBox("titleTextBox").enterText(CERT_FIXTURE_1_TITLE);
		window.textBox("issuedToTextBox").enterText(CERT_FIXTURE_1_ISSUED_TO);
		window.textBox("issuedByTextBox").enterText(CERT_FIXTURE_1_ISSUED_BY);
		window.textBox("yearTextBox").enterText(String.valueOf(CERT_FIXTURE_1_YEAR));
	}

	@Given("The user provides certificate data in the text fields, specifying an existing id")
	public void the_user_provides_certificate_data_specifying_existing_id() {
		window.textBox("idTextBox").enterText(CERT_FIXTURE_1_ID);
		window.textBox("titleTextBox").enterText("Duplicate Title");
		window.textBox("issuedToTextBox").enterText("Owais");
		window.textBox("issuedByTextBox").enterText("Some Org");
		window.textBox("yearTextBox").enterText("2025");
	}

	@Then("The list contains the new certificate")
	public void the_list_contains_the_new_certificate() {
		Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
				assertThat(window.list().contents())
						.anySatisfy(e -> assertThat(e).contains(
								CERT_FIXTURE_1_ID, CERT_FIXTURE_1_TITLE,
								CERT_FIXTURE_1_ISSUED_TO, CERT_FIXTURE_1_ISSUED_BY,
								String.valueOf(CERT_FIXTURE_1_YEAR))));
	}

	@Then("An error is shown containing the title of the existing certificate")
	public void an_error_is_shown_containing_the_title_of_the_existing_certificate() {
		assertThat(window.label("errorLabel").text())
				.contains(CERT_FIXTURE_1_TITLE);
	}

	@Given("The user selects a certificate from the list")
	public void the_user_selects_a_certificate_from_the_list() {
		window.list("certificateList")
				.selectItem(Pattern.compile(".*" + CERT_FIXTURE_1_TITLE + ".*"));
	}

	@Then("The certificate is removed from the list")
	public void the_certificate_is_removed_from_the_list() {
		Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
				assertThat(window.list().contents())
						.noneMatch(e -> e.contains(CERT_FIXTURE_1_TITLE)));
	}

	@Then("An error is shown containing the title of the selected certificate")
	public void an_error_is_shown_containing_the_title_of_the_selected_certificate() {
		assertThat(window.label("errorLabel").text())
				.contains(CERT_FIXTURE_1_TITLE);
	}
}
