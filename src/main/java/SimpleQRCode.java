import java.io.File;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class SimpleQRCode{

    public static void main(String[] args) {
        generateQR("https://www.google.com", 300, "qr.png");
    }

    public static void generateQR(String data, int size, String filePath) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(data, BarcodeFormat.QR_CODE, size, size);

            int padding = 40;
            int totalSize = size + padding * 2;

            BufferedImage image = new BufferedImage(totalSize, totalSize, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = image.createGraphics();

            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 🌈 Gradient Background
            GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(255, 255, 255),
                    totalSize, totalSize, new Color(240, 240, 240)
            );
            g.setPaint(gradient);
            g.fillRect(0, 0, totalSize, totalSize);

            // 🟦 Strong Border (Rounded)
            g.setColor(new Color(0, 102, 255)); // bright blue
            g.fillRoundRect(5, 5, totalSize - 10, totalSize - 10, 40, 40);

            // ⬜ Inner white area
            g.setColor(Color.WHITE);
            g.fillRoundRect(padding, padding, size, size, 30, 30);

            int moduleSize = size / matrix.getWidth();

            // 🎨 Gradient QR Color (top to bottom)
            for (int x = 0; x < matrix.getWidth(); x++) {
                for (int y = 0; y < matrix.getHeight(); y++) {

                    if (matrix.get(x, y)) {

                        float ratio = (float) y / matrix.getHeight();

                        // gradient color
                        int r = (int) (0 + ratio * 0);
                        int gColor = (int) (0 + ratio * 120);
                        int b = (int) (0 + ratio * 255);

                        g.setColor(new Color(r, gColor, b));

                        int px = x * moduleSize + padding;
                        int py = y * moduleSize + padding;

                        g.fill(new RoundRectangle2D.Float(
                                px, py,
                                moduleSize,
                                moduleSize,
                                8, 8
                        ));
                    }
                }
            }

            g.dispose();
            ImageIO.write(image, "png", new File(filePath));

            System.out.println("🔥 Attractive QR Generated!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}