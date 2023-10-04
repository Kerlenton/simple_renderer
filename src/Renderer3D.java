import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Renderer3D extends JFrame {
    static ArrayList<Triangle> tris;
    static boolean sameSide(Vertex A, Vertex B, Vertex C, Vertex p){
        Vertex V1V2 = new Vertex(B.x - A.x,B.y - A.y,B.z - A.z);
        Vertex V1V3 = new Vertex(C.x - A.x,C.y - A.y,C.z - A.z);
        Vertex V1P = new Vertex(p.x - A.x,p.y - A.y,p.z - A.z);

        // If the cross product of vector V1V2 and vector V1V3 is the same as the one of vector V1V2 and vector V1p, they are on the same side.
        // We only need to judge the direction of z
        double V1V2CrossV1V3 = V1V2.x * V1V3.y - V1V3.x * V1V2.y;
        double V1V2CrossP = V1V2.x * V1P.y - V1P.x * V1V2.y;

        return V1V2CrossV1V3 * V1V2CrossP >= 0;
    }

    public static Color getShade(Color color, double shade) {
        int red = (int) (color.getRed() * shade);
        int green = (int) (color.getGreen() * shade);
        int blue = (int) (color.getBlue() * shade);
        return new Color(red, green, blue);
    }

    public Renderer3D() {
        // Создаем главное меню
        JMenuBar menuBar = new JMenuBar();

        // Создаем меню "Файл"
        JMenu fileMenu = new JMenu("Shapes");

        // Создаем пункты меню для меню "Файл"
        JMenuItem t = new JMenuItem("Cube");
        JMenuItem c = new JMenuItem("Tetra");

        // Добавляем обработчики событий для пунктов меню "Файл"
        t.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Действия при выборе пункта "Открыть"
                tris = new ArrayList<Triangle>(8);
                tris.add(new Triangle(new Vertex(100, 100, 100),
                        new Vertex(100, -100, 100),
                        new Vertex(-100, 100, 100),
                        Color.WHITE));
                tris.add(new Triangle(new Vertex(-100, -100, 100),
                        new Vertex(100, -100, 100),
                        new Vertex(-100, 100, 100),
                        Color.WHITE));

                tris.add(new Triangle(new Vertex(-100, -100, -100),
                        new Vertex(-100, 100, -100),
                        new Vertex(100, -100, -100),
                        Color.RED));
                tris.add(new Triangle(new Vertex(100, 100, -100),
                        new Vertex(-100, 100, -100),
                        new Vertex(100, -100, -100),
                        Color.RED));

                tris.add(new Triangle(new Vertex(-100, -100, -100),
                        new Vertex(-100, -100, 100),
                        new Vertex(100, -100, -100),
                        Color.GREEN));
                tris.add(new Triangle(new Vertex(100, -100, 100),
                        new Vertex(-100, -100, 100),
                        new Vertex(100, -100, -100),
                        Color.GREEN));

                tris.add(new Triangle(new Vertex(100, 100, 100),
                        new Vertex(100, 100, -100),
                        new Vertex(-100, 100, 100),
                        Color.ORANGE));
                tris.add(new Triangle(new Vertex(-100, 100, -100),
                        new Vertex(100, 100, -100),
                        new Vertex(-100, 100, 100),
                        Color.ORANGE));

                tris.add(new Triangle(new Vertex(100, 100, 100),
                        new Vertex(100, -100, 100),
                        new Vertex(100, 100, -100),
                        Color.PINK));
                tris.add(new Triangle(new Vertex(100, -100, -100),
                        new Vertex(100, -100, 100),
                        new Vertex(100, 100, -100),
                        Color.PINK));

                tris.add(new Triangle(new Vertex(-100, -100, -100),
                        new Vertex(-100, 100, -100),
                        new Vertex(-100, -100, 100),
                        Color.CYAN));
                tris.add(new Triangle(new Vertex(-100, 100, 100),
                        new Vertex(-100, 100, -100),
                        new Vertex(-100, -100, 100),
                        Color.CYAN));
            }
        });

        c.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Действия при выборе пункта "Сохранить"
                tris = new ArrayList<Triangle>(4);
                tris.add(new Triangle(new Vertex(100, 100, 100),
                        new Vertex(-100, -100, 100),
                        new Vertex(-100, 100, -100),
                        Color.WHITE));
                tris.add(new Triangle(new Vertex(100, 100, 100),
                        new Vertex(-100, -100, 100),
                        new Vertex(100, -100, -100),
                        Color.RED));
                tris.add(new Triangle(new Vertex(-100, 100, -100),
                        new Vertex(100, -100, -100),
                        new Vertex(100, 100, 100),
                        Color.GREEN));
                tris.add(new Triangle(new Vertex(-100, 100, -100),
                        new Vertex(100, -100, -100),
                        new Vertex(-100, -100, 100),
                        Color.BLUE));

            }
        });

        // Добавляем пункты меню в меню "Файл"
        fileMenu.add(t);
        fileMenu.add(c);

        // Добавляем меню "Файл" в главное меню
        menuBar.add(fileMenu);

        // Устанавливаем главное меню в окно
        setJMenuBar(menuBar);

        // Настройка окна
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Пример меню");
        setSize(300, 200);
        setLocationRelativeTo(null); // центрирование окна
        setVisible(true);
    }

    public static void main(String[] args) {// Создаем меню
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Renderer3D();
            }
        });

        JFrame frame = new JFrame();
        Container pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());

        double[] x = {0, 0};
        double[] y = {0, 0};

        // panel to display render results
        JPanel renderPanel = new JPanel() {
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // rendering magic will happen here
                /* ArrayList<Triangle> tris = new ArrayList<Triangle>(4);
                tris.add(new Triangle(new Vertex(100, 100, 100),
                        new Vertex(-100, -100, 100),
                        new Vertex(-100, 100, -100),
                        Color.WHITE));
                tris.add(new Triangle(new Vertex(100, 100, 100),
                        new Vertex(-100, -100, 100),
                        new Vertex(100, -100, -100),
                        Color.RED));
                tris.add(new Triangle(new Vertex(-100, 100, -100),
                        new Vertex(100, -100, -100),
                        new Vertex(100, 100, 100),
                        Color.GREEN));
                tris.add(new Triangle(new Vertex(-100, 100, -100),
                        new Vertex(100, -100, -100),
                        new Vertex(-100, -100, 100),
                        Color.BLUE));
                */

                /*tris = new ArrayList<Triangle>(8);
                tris.add(new Triangle(new Vertex(100, 100, 100),
                        new Vertex(100, -100, 100),
                        new Vertex(-100, 100, 100),
                        Color.WHITE));
                tris.add(new Triangle(new Vertex(-100, -100, 100),
                        new Vertex(100, -100, 100),
                        new Vertex(-100, 100, 100),
                        Color.WHITE));

                tris.add(new Triangle(new Vertex(-100, -100, -100),
                        new Vertex(-100, 100, -100),
                        new Vertex(100, -100, -100),
                        Color.RED));
                tris.add(new Triangle(new Vertex(100, 100, -100),
                        new Vertex(-100, 100, -100),
                        new Vertex(100, -100, -100),
                        Color.RED));

                tris.add(new Triangle(new Vertex(-100, -100, -100),
                        new Vertex(-100, -100, 100),
                        new Vertex(100, -100, -100),
                        Color.GREEN));
                tris.add(new Triangle(new Vertex(100, -100, 100),
                        new Vertex(-100, -100, 100),
                        new Vertex(100, -100, -100),
                        Color.GREEN));

                tris.add(new Triangle(new Vertex(100, 100, 100),
                        new Vertex(100, 100, -100),
                        new Vertex(-100, 100, 100),
                        Color.ORANGE));
                tris.add(new Triangle(new Vertex(-100, 100, -100),
                        new Vertex(100, 100, -100),
                        new Vertex(-100, 100, 100),
                        Color.ORANGE));

                tris.add(new Triangle(new Vertex(100, 100, 100),
                        new Vertex(100, -100, 100),
                        new Vertex(100, 100, -100),
                        Color.PINK));
                tris.add(new Triangle(new Vertex(100, -100, -100),
                        new Vertex(100, -100, 100),
                        new Vertex(100, 100, -100),
                        Color.PINK));

                tris.add(new Triangle(new Vertex(-100, -100, -100),
                        new Vertex(-100, 100, -100),
                        new Vertex(-100, -100, 100),
                        Color.CYAN));
                tris.add(new Triangle(new Vertex(-100, 100, 100),
                        new Vertex(-100, 100, -100),
                        new Vertex(-100, -100, 100),
                        Color.CYAN)); */


                // The generated shape is centered on the origin (0, 0, 0), and we will do rotation around the origin later.

                double heading = Math.toRadians(x[0]);
                Matrix3 headingTransform = new Matrix3(new double[]{
                        Math.cos(heading), 0, -Math.sin(heading),
                        0, 1, 0,
                        Math.sin(heading), 0, Math.cos(heading)
                });
                double pitch = Math.toRadians(y[0]);
                Matrix3 pitchTransform = new Matrix3(new double[]{
                        1, 0, 0,
                        0, Math.cos(pitch), Math.sin(pitch),
                        0, -Math.sin(pitch), Math.cos(pitch)
                });
// Merge matrices in advance
                Matrix3 transform = headingTransform.multiply(pitchTransform);
                BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

                //g2.translate(getWidth() / 2, getHeight() / 2);
                //g2.setColor(Color.WHITE);

                double[] zBuffer = new double[img.getWidth() * img.getHeight()];
// initialize array with extremely far away depths
                for (int q = 0; q < zBuffer.length; q++) {
                    zBuffer[q] = Double.NEGATIVE_INFINITY;
                }

                if (tris != null) {
                    for (Triangle t : tris) {
                        Vertex v1 = transform.transform(t.v1);
                        Vertex v2 = transform.transform(t.v2);
                        Vertex v3 = transform.transform(t.v3);

                        // since we are not using Graphics2D anymore,
                        // we have to do translation manually
                        v1.x += getWidth() / 2;
                        v1.y += getHeight() / 2;
                        v2.x += getWidth() / 2;
                        v2.y += getHeight() / 2;
                        v3.x += getWidth() / 2;
                        v3.y += getHeight() / 2;

                        Vertex ab = new Vertex(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
                        Vertex ac = new Vertex(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z);
                        Vertex norm = new Vertex(
                                ab.y * ac.z - ab.z * ac.y,
                                ab.z * ac.x - ab.x * ac.z,
                                ab.x * ac.y - ab.y * ac.x
                        );
                        double normalLength = Math.sqrt(norm.x * norm.x + norm.y * norm.y + norm.z * norm.z);
                        norm.x /= normalLength;
                        norm.y /= normalLength;
                        norm.z /= normalLength;

                        double angleCos = Math.abs(norm.z);

                        // compute rectangular bounds for triangle
                        int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
                        int maxX = (int) Math.min(img.getWidth() - 1,
                                Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
                        int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
                        int maxY = (int) Math.min(img.getHeight() - 1,
                                Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));

                        double triangleArea =
                                (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x);

                        for (int y = minY; y <= maxY; y++) {
                            for (int x = minX; x <= maxX; x++) {
                                double b1 =
                                        ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triangleArea;
                                double b2 =
                                        ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triangleArea;
                                double b3 =
                                        ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triangleArea;
                                if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
                                    double depth = b1 * v1.z + b2 * v2.z + b3 * v3.z;
                                    int zIndex = y * img.getWidth() + x;
                                    if (zBuffer[zIndex] < depth) {
                                        img.setRGB(x, y, getShade(t.color, angleCos).getRGB());
                                        zBuffer[zIndex] = depth;
                                    }
                                }
                            }
                        }

                    }

                    g2.drawImage(img, 0, 0, null);
                }
            }
        };
        renderPanel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                double yi = 180.0 / renderPanel.getHeight();
                double xi = 180.0 / renderPanel.getWidth();
                x[0] = (int) (e.getX() * xi);
                y[0] = -(int) (e.getY() * yi);
                renderPanel.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });

        pane.add(renderPanel, BorderLayout.CENTER);

        frame.setSize(600, 600);
        frame.setVisible(true);
    }
}