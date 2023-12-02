package profile;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class ProfileController implements Initializable {

    @FXML
    private ImageView profileImageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Assuming you have an Image object, replace the path with your actual image file path
        Image image = new Image("file:/path/to/your/image.jpg");

        profileImageView.setImage(image);

        // Ensure the image is clipped to a circle
        profileImageView.setClip(createClip(profileImageView));

        // Center the image inside the ImageView
        centerImage(profileImageView);
    }

    private Circle createClip(ImageView imageView) {
        Circle clip = new Circle();
        clip.setCenterX(imageView.getFitWidth() / 2);
        clip.setCenterY(imageView.getFitHeight() / 2);
        clip.setRadius(imageView.getFitWidth() / 2);
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