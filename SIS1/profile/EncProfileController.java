package profile;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import application.DatabaseManager;
import application.UserSession;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.util.converter.DefaultStringConverter;

public class EncProfileController implements Initializable {

	@FXML
	private Label countLBL;

	@FXML
	private Label fullnameLBL;

	@FXML
	private Label firstnameLBL;

	@FXML
	private Label lastnameLBL;

	@FXML
	private Label usernameLBL;

	@FXML
	private Label idLBL;

	@FXML
	private TextField idTF;

	@FXML
	private TextField firstTF;

	@FXML
	private TextField lastTF;

	@FXML
	private TextField userTF;

	@FXML
	private Button makeBTN;

	@FXML
	private Button saveBTN;

	@FXML
	private TextField passwordTF;

	@FXML
	private TextField newPasswordTF, profileConfirmPass;

	@FXML
	private ImageView profileImageView;

	@FXML
	private Button removeBTN;

	@FXML
	private Button updateBTN, addBTN, deleteBTN;

	private Image image;

	Connection con = DatabaseManager.getConnection();

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
	    try {
	        // Check if the user has a saved image in the database
	        InputStream imageStream = getUserImageFromDatabase();
	        if (imageStream != null) {
	            // Load the user image from the database
	            Image userImage = new Image(imageStream);
	            if (userImage.isError()) {
	                // Handle the case where the image couldn't be loaded
	                System.err.println("Error loading user image.");
	                userImage = getDefaultImage();
	            }
	            profileImageView.setImage(userImage);
	            profileImageView.setClip(createClip(profileImageView));
	            centerImage(profileImageView);
	        } else {
	            // Use the default image
	            Image defaultImage = getDefaultImage();
	            profileImageView.setImage(defaultImage);
	            profileImageView.setClip(createClip(profileImageView));
	            centerImage(profileImageView);
	        }

	        profileImageView.setOnMouseClicked(event -> {
	            if (event.getButton() == MouseButton.PRIMARY) {
	                // Handle primary (left) mouse click
	                insertIMG(null);
	            }
	        });
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    insertLBL();

	    setupTextFormatterWithValidation(firstTF, 15, "[a-zA-Z]+", "Only letters are allowed");
	    setupTextFormatterWithValidation(lastTF, 15, "[a-zA-Z]+", "Only letters are allowed");
	    setupTextFormatterWithValidation(userTF, 15, "[a-zA-Z0-9]+", "Only letters and numbers are allowed");
	    setupTextFormatterWithValidation(passwordTF, 15, "[a-zA-Z0-9]+", "Only letters and numbers are allowed");
	    setupTextFormatterWithValidation(newPasswordTF, 15, "[a-zA-Z0-9]+", "Only letters and numbers are allowed");
	    setupTextFormatterWithValidation(profileConfirmPass, 15, "[a-zA-Z0-9]+", "Only letters and numbers are allowed");
	}

	private Image getDefaultImage() {
	    try {
	        InputStream inputStream = getClass().getResourceAsStream("/img_icons/peep.png");
	        if (inputStream == null) {
	            System.err.println("Default image file not found.");
	            return new Image("/src/img_icons/Insert.png");
	        }
	        return new Image(inputStream);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null; // or return a placeholder image
	    }
	}


	private void setupTextFormatterWithValidation(TextField textField, int maxLength, String regex,
			String alertMessage) {
		TextFormatter<String> textFormatter = new TextFormatter<>(new DefaultStringConverter(), null, change -> {
			String newText = change.getControlNewText();
			if (newText.length() <= maxLength && newText.matches(regex)) {
				return change;
			}
			return null; // reject the change if it exceeds the max length or doesn't match the regex
		});

		textField.setTextFormatter(textFormatter);
		addValidationAlert(textField, alertMessage);
	}

	private void addValidationAlert(TextField textField, String message) {
		textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				validateAndShowAlert(textField, message);
			}
		});
	}

	private void validateAndShowAlert(TextField textField, String message) {
		if (!textField.getText().matches("[a-zA-Z0-9]+")) {
			showAlert(Alert.AlertType.ERROR, "Invalid Input", message);
			textField.clear();
		}
	}

	private void showAlert(Alert.AlertType alertType, String title, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

	private InputStream getUserImageFromDatabase() throws SQLException {
		try (PreparedStatement stmt = con.prepareStatement("SELECT userIMG FROM users WHERE id = ?")) {
			stmt.setInt(1, UserSession.getInstance().getId());
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				// Return the input stream containing the user image
				return resultSet.getBinaryStream("userIMG");
			}
		}
		return null; // Return null if no image is found
	}

	@FXML
	private void insertIMG(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters()
				.addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

		File selectedFile = fileChooser.showOpenDialog(null);

		if (selectedFile != null) {
			try {
				Image selectedImage = new Image(selectedFile.toURI().toString());
				profileImageView.setImage(selectedImage);
				profileImageView.setClip(createClip(profileImageView));
				centerImage(profileImageView);

				// Save the image to the database
				saveImageToDatabase(selectedFile);
			} catch (Exception e) {
				e.printStackTrace();
				// Handle the exception as needed
			}
		}
	}

	private void saveImageToDatabase(File imageFile) {
		try (Connection con = DatabaseManager.getConnection()) {
			con.setAutoCommit(false);

			try (PreparedStatement stmt = con.prepareStatement("UPDATE users SET userIMG = ? WHERE id = ?")) {
				BufferedImage bufferedImage = SwingFXUtils.fromFXImage(profileImageView.getImage(), null);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(bufferedImage, "png", baos);
				byte[] imageData = baos.toByteArray();
				stmt.setBytes(1, imageData);
				stmt.setInt(2, UserSession.getInstance().getId()); // Use the user ID from UserSession
				stmt.executeUpdate();
				con.commit();
			} catch (SQLException | IOException e) {
				e.printStackTrace();
				con.rollback();
			} finally {
				con.setAutoCommit(true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private InputStream convertImageToInputStream(Image image) throws IOException {
		if (image != null && image.getPixelReader() != null) {
			BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "png", outputStream);
			return new ByteArrayInputStream(outputStream.toByteArray());
		} else {
			InputStream defaultImageStream = getClass().getResourceAsStream("/img_icons/NoPeep.png");
			return defaultImageStream;
		}
	}

	private Circle createClip(ImageView imageView) {
		Circle clip = new Circle();
		double radius = Math.min(imageView.getFitWidth() / 2, imageView.getFitHeight() / 2);
		clip.setRadius(radius);
		clip.setCenterX(radius);
		clip.setCenterY(radius);
		return clip;
	}

	private void centerImage(ImageView imageView) {
		double imageViewWidth = imageView.getFitWidth();
		double imageViewHeight = imageView.getFitHeight();

		Image image = imageView.getImage();

		if (image != null) {
			double imageWidth = image.getWidth();
			double imageHeight = image.getHeight();

			double offsetX = 0;
			double offsetY = 0;

			if (imageWidth > imageViewWidth) {
				offsetX = (imageWidth - imageViewWidth) / 2;
			} else {
				offsetX = (imageViewWidth - imageWidth) / 2;
			}

			if (imageHeight > imageViewHeight) {
				offsetY = (imageHeight - imageViewHeight) / 2;
			} else {
				offsetY = (imageViewHeight - imageHeight) / 2;
			}

			imageView.setViewport(new Rectangle2D(offsetX, offsetY, imageViewWidth, imageViewHeight));
		}
	}

	private void insertLBL() {
		UserSession session = UserSession.getInstance();

		// Assuming that you have appropriate getters in UserSession
		String firstName = session.getFirstname();
		String lastName = session.getLastname();
		String username = session.getUsername();
		String password = session.getPassword();

		// Set the labels
		firstnameLBL.setText(firstName);
		lastnameLBL.setText(lastName);
		usernameLBL.setText(username);
		firstTF.setText(firstName);
		lastTF.setText(lastName);
		userTF.setText(username);
		passwordTF.setText(password);
	}

	@FXML
	private void saveProfile(ActionEvent event) {
		try {
			// Get the values from the text fields
			String newFirstName = firstTF.getText();
			String newLastName = lastTF.getText();
			String newUsername = userTF.getText();

			// Update the database with the new values
			updateProfileInDatabase(newFirstName, newLastName, newUsername);

			// Update the labels
			firstnameLBL.setText(newFirstName);
			lastnameLBL.setText(newLastName);
			usernameLBL.setText(newUsername);

			// Optionally, you may want to update the UserSession with the new values
			UserSession.getInstance().setFirstname(newFirstName);
			UserSession.getInstance().setLastname(newLastName);
			UserSession.getInstance().setUsername(newUsername);

			// Optionally, display a success message or perform other actions
			System.out.println("Profile saved successfully!");
		} catch (SQLException e) {
			e.printStackTrace();
			// Handle the exception as needed
		}
	}

	private void updateProfileInDatabase(String newFirstName, String newLastName, String newUsername)
			throws SQLException {
		try (PreparedStatement stmt = con
				.prepareStatement("UPDATE users SET fname = ?, lname = ?, username = ? WHERE id = ?")) {
			stmt.setString(1, newFirstName);
			stmt.setString(2, newLastName);
			stmt.setString(3, newUsername);
			stmt.setInt(4, UserSession.getInstance().getId()); // Use the user ID from UserSession

			stmt.executeUpdate();
		}
	}

	@FXML
	private void updatePassword(ActionEvent event) {
		try {
			// Get the values from the text fields
			String currentPass = passwordTF.getText();
			String newPass = newPasswordTF.getText();
			String confirmPass = profileConfirmPass.getText();

			// Update the database with the new values
			updatePasswordDatabase(confirmPass);

			// Update the labels
			passwordTF.clear();
			newPasswordTF.clear();
			profileConfirmPass.clear();

			// Optionally, you may want to update the UserSession with the new values
			UserSession.getInstance().setPassword(confirmPass);

			// Display a success message
			showSuccessAlert("Password changed successfully!");
		} catch (SQLException e) {
			e.printStackTrace();
			// Handle the exception as needed
		}
	}

	private void updatePasswordDatabase(String confirmPass) throws SQLException {
		try (PreparedStatement stmt = con.prepareStatement("UPDATE users SET password = ? WHERE id = ?")) {
			stmt.setString(1, confirmPass);
			stmt.setInt(2, UserSession.getInstance().getId()); // Use the user ID from UserSession

			stmt.executeUpdate();
		}
	}

	private void showSuccessAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	



}