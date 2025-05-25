import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Main extends JPanel {
    BufferedImage original, grayWorldImg, whitePatchImg, shadeOfGrayImg, retinexImg;

    public Main(String imagePath) throws IOException {
        original = ImageIO.read(new File(imagePath));
        grayWorldImg = GrayWorld.apply(original);
        whitePatchImg = WhitePatch.apply(original);
        shadeOfGrayImg = ShadeOfGray.apply(original, 6);
        retinexImg = RetinexSimple.apply(original);

        // Pakai GridLayout 2 baris 3 kolom
        setLayout(new GridLayout(2, 3, 10, 10)); // jarak antar gambar 10px

        add(createLabeledImage("Original", original));
        add(createLabeledImage("Gray World", grayWorldImg));
        add(createLabeledImage("White Patch", whitePatchImg));
        add(createLabeledImage("Shade of Gray", shadeOfGrayImg));
        add(createLabeledImage("Simple Retinex", retinexImg));
        add(new JLabel("")); // Kosong, supaya grid balance
    }

    private JPanel createLabeledImage(String label, BufferedImage image) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel imgLabel = new JLabel(new ImageIcon(image));
        JLabel textLabel = new JLabel(label, SwingConstants.CENTER);
        panel.add(imgLabel, BorderLayout.CENTER);
        panel.add(textLabel, BorderLayout.SOUTH);
        return panel;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Color Constancy Algorithms Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            String imagePath = "C:\\Users\\LENOVO\\OneDrive\\Pictures\\Screenshots\\Screenshot 2025-05-25 154528.png"; // sesuaikan path gambarmu
            Main panel = new Main(imagePath);
            JScrollPane scrollPane = new JScrollPane(panel);
            frame.add(scrollPane);
            frame.setSize(1200, 800);  // ukuran window, bisa digulir
            frame.setLocationRelativeTo(null); // posisi tengah layar
            frame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
