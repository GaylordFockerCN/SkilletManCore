package com.p1nero.smc.util;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageTransparencyComparison {

    /**
     * 比较两图是否相同
     * 对图片缩放处理，提高效率，近似相同即可
     * 本来是打算用来判断新旧图是否相同的，防止新生成世界读取到旧图，后来发现加个存档判断就行了。。
    */
    public static boolean compareImages(File image1, File image2) {

        try {
            BufferedImage img1 = ImageIO.read(image1);
            BufferedImage img2 = ImageIO.read(image2);

            // Resize images
            int newWidth = 100; // New width after resizing
            int newHeight = 100; // New height after resizing
            if (img1.getWidth() > newWidth || img1.getHeight() > newHeight) {
                img1 = resizeImage(img1, newWidth, newHeight);
            }
            if (img2.getWidth() > newWidth || img2.getHeight() > newHeight) {
                img2 = resizeImage(img2, newWidth, newHeight);
            }

            boolean result = compareTransparency(img1, img2);

            if (result) {
                return true;
            }

        } finally {
            return false;
        }
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        resizedImage.getGraphics().drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        return resizedImage;
    }

    private static boolean compareTransparency(BufferedImage img1, BufferedImage img2) {
        if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
            return false; // Images are of different sizes
        }

        for (int y = 0; y < img1.getHeight(); y++) {
            for (int x = 0; x < img1.getWidth(); x++) {
                int pixel1 = img1.getRGB(x, y);
                int pixel2 = img2.getRGB(x, y);

                // Compare alpha values
                int alpha1 = (pixel1 >> 24) & 0xFF;
                int alpha2 = (pixel2 >> 24) & 0xFF;

                if (alpha1<50 && !(alpha2 <50) || alpha1>=50 && !(alpha2>=50)) {
                    return false; // Transparency part is different
                }
            }
        }

        return true;
    }
}
