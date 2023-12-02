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

public class ProfileController implements Initializable {

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	Image image = new Image(getClass().getResourceAsStream("..\\src\\imgs_icons\\Insert.png"));
        profileImageView.setImage(image);
        profileImageView.setClip(createClip(profileImageView));
        centerImage(profileImageView);
        
        profileImageView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                // Handle primary (left) mouse click
                insertIMG(null);
            }
        });
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
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement stmt = con.prepareStatement("UPDATE your_table_name SET image_column_name = ? WHERE user_id = ?")) {

            // Convert Image to BufferedImage
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(profileImageView.getImage(), null);

            // Convert BufferedImage to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            byte[] imageData = baos.toByteArray();

            // Set parameters for the PreparedStatement
            stmt.setBytes(1, imageData);
            stmt.setInt(2, UserSession.getInstance().getId());  // Use the user ID from UserSession

            // Execute the update
            stmt.executeUpdate();

            // Commit the transaction
            con.commit();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            // Handle the exception as needed
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