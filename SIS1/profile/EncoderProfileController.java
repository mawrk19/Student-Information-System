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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

public class EncoderProfileController implements Initializable {

    @FXML
    private Label countLBL;

    @FXML
    private Label fullnameLBL;

    @FXML
    private Label idLBL;

    @FXML
    private TextField idTF;

    @FXML
    private Button makeBTN;

    @FXML
    private TextField passwordTF;

    @FXML
    private ImageView profileImageView;

    @FXML
    private Button removeBTN;

    @FXML
    private Button updateBTN;

    @FXML
    private TextField usernameTF;
    
    private Image image;

    Connection con = DatabaseManager.getConnection();
    		
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load the default image
        Image defaultImage = new Image(getClass().getResourceAsStream("..\\src\\imgs_icons\\Insert.png"));
        profileImageView.setImage(defaultImage);
        profileImageView.setClip(createClip(profileImageView));
        centerImage(profileImageView);

        // Check if there is a saved image in the database
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT userImg FROM users WHERE id = ?")) {

            stmt.setInt(1, UserSession.getInstance().getId());
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                // If there is a saved image, load it
                InputStream imageStream = resultSet.getBinaryStream("userImg");
                if (imageStream != null) {
                    Image savedImage = new Image(imageStream);
                    profileImageView.setImage(savedImage);
                    profileImageView.setClip(createClip(profileImageView));
                    centerImage(profileImageView);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }

        // Set up the click event to change the image
        profileImageView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                insertIMG(null);
            }
        });
        
        setFullnameLabel();
    }

    private void setFullnameLabel() {
        UserSession session = UserSession.getInstance();
        String username = session.getUsername();
        String lastname = session.getLastname(); // Assuming there is a getLastname method in your UserSession class
        String fullname = username + " " + lastname;
        String id = String.valueOf(session.getId()); // Convert id to String

        fullnameLBL.setText(fullname);
        idLBL.setText(id);
    }

    
    @FXML
    private void insertIMG(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

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
        Connection con = null;
        try {
            con = DatabaseManager.getConnection();
            con.setAutoCommit(false);

            // Convert Image to BufferedImage
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(profileImageView.getImage(), null);

            // Convert BufferedImage to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            byte[] imageData = baos.toByteArray();

            try (PreparedStatement stmt = con.prepareStatement("UPDATE users SET userImg = ? WHERE id = ?")) {
                // Set parameters for the PreparedStatement
                stmt.setBytes(1, imageData);
                stmt.setInt(2, UserSession.getInstance().getId());  // Use the user ID from UserSession

                // Execute the update
                stmt.executeUpdate();

                // Commit the transaction
                con.commit();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the exception as needed
                con.rollback();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            // Handle the exception as needed
        } finally {
            try {
                if (con != null) {
                    if (!con.getAutoCommit()) {
                        con.setAutoCommit(true); // Reset autocommit to true
                    }
                }
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
                // Handle the rollback exception as needed
            }
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
    
    
}