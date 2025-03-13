import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;


public class SubroutineHierarchy extends JPanel {
    public static void main(String[] args) {
        JFrame window = new JFrame("Subroutine Hierarchy");
        window.setContentPane(new SubroutineHierarchy());
        window.pack();
        window.setLocation(100, 60);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    private final static int WIDTH = 800;   
    private final static int HEIGHT = 600;

    private final static double X_LEFT = -4;    
    private final static double X_RIGHT = 4;
    private final static double Y_BOTTOM = -3;
    private final static double Y_TOP = 3;

    private final static Color BACKGROUND = Color.WHITE;

    private float pixelSize;

    private int frameNumber = 0;

    private void drawWorld(Graphics2D g2) {
        rotatingShape(g2, 100, -1.02, -0.05);
        rotatingShape(g2, 100, 1.04, -0.98);
        rotatingShape(g2, 80, -1.379, 1.40);
        rotatingShape(g2, 80, -3.13, 2.23);
        rotatingShape(g2, 60, 0.9, 2.05);
        rotatingShape(g2, 60, 2.12, 1.45);

        Bar(g2, 1, 1.05, 0, -0.5);
        Bar(g2, 0.85, 0.95, -2.65, 1.90);
        Bar(g2, 0.6, 0.70, 2.5, 2.5);

        
        Triangle(g2, 0.5, 0.5, 0, -2, Color.BLUE); 
        Triangle(g2, 0.35, 0.35, -2.25, 0.75, new Color(128, 0, 128)); 
        Triangle(g2, 0.25, 0.25, 1.5, 1, Color.GREEN); 
    }

    private void Bar(Graphics2D g2, double x, double y, double offsetX, double offsetY) {
        AffineTransform saveTransform = g2.getTransform();
        g2.scale(x, y);

        g2.setColor(Color.RED);
        g2.translate(offsetX, offsetY);
        g2.rotate(-Math.PI / 8);
        g2.scale(2.3, 0.15);
        filledRect(g2);

        g2.setTransform(saveTransform);
    }

    private void Triangle(Graphics2D g2, double scaleX, double scaleY, double offsetX, double offsetY, Color color) {
        AffineTransform saveTransform = g2.getTransform();
        g2.setColor(color);
        g2.translate(offsetX, offsetY);
        g2.scale(scaleX, scaleY);
        g2.fillPolygon(new int[]{0, 1, -1}, new int[]{3, 0, 0}, 3);
        g2.setTransform(saveTransform);
    }

    private void updateFrame() {
        frameNumber++;
    }

    private void rotatingShape(Graphics2D g2, double radius, double offsetX, double offsetY) {
        AffineTransform saveTransform = g2.getTransform();
        Color saveColor = g2.getColor();
        g2.setStroke(new BasicStroke(2));

        int numOfVertices = 5;  
        double angle = (Math.PI * 2) / numOfVertices;

        int[] xPoints = new int[numOfVertices];
        int[] yPoints = new int[numOfVertices];

        Polygon polygon = new Polygon();

        for (int i = 0; i < numOfVertices; i++) {
            xPoints[i] = (int) (radius * Math.sin(i * angle));
            yPoints[i] = (int) (radius * Math.cos(i * angle));

            if (i != 0)
                polygon.addPoint(xPoints[i - 1], yPoints[i - 1]);

            polygon.addPoint(xPoints[i], yPoints[i]);
            polygon.addPoint(0, 0);
        }

        polygon.addPoint(xPoints[0], yPoints[0]);
        polygon.addPoint(xPoints[numOfVertices - 1], yPoints[numOfVertices - 1]);

        g2.translate(offsetX, offsetY);
        g2.setColor(Color.black);
        g2.rotate(Math.toRadians(frameNumber));
        g2.scale(0.005, 0.005);

        g2.draw(polygon);
        g2.setColor(saveColor);
        g2.setTransform(saveTransform);
    }

    private static void filledRect(Graphics2D g2) { 
        g2.fill(new Rectangle2D.Double(-0.5, -0.5, 1, 1));
    }

    

    private JPanel display;  

    public SubroutineHierarchy() {
        display = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                applyLimits(g2, X_LEFT, X_RIGHT, Y_TOP, Y_BOTTOM, false);
                g2.setStroke(new BasicStroke(pixelSize)); 
                drawWorld(g2);  
            }
        };
        display.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        display.setBackground(BACKGROUND);
        final Timer timer = new Timer(17, new ActionListener() { 
            public void actionPerformed(ActionEvent evt) {
                updateFrame();
                repaint();
            }
        });
        final JCheckBox animationCheck = new JCheckBox("Run Animation");
        animationCheck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (animationCheck.isSelected()) {
                    if (!timer.isRunning())
                        timer.start();
                } else {
                    if (timer.isRunning())
                        timer.stop();
                }
            }
        });
        JPanel top = new JPanel();
        top.add(animationCheck);
        setLayout(new BorderLayout(5, 5));
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 4));
        add(top, BorderLayout.NORTH);
        add(display, BorderLayout.CENTER);
    }

    /**
     
     *
     * @param g2             
     * @param xleft          
     * @param xright         
     * @param ytop           
     * @param ybottom        
     *                       
     * @param preserveAspect 
     *                                      
     */
    private void applyLimits(Graphics2D g2, double xleft, double xright,
                             double ytop, double ybottom, boolean preserveAspect) {
        int width = display.getWidth();   
        int height = display.getHeight(); 
        if (preserveAspect) {
            
            double displayAspect = Math.abs((double) height / width);
            double requestedAspect = Math.abs((ybottom - ytop) / (xright - xleft));
            if (displayAspect > requestedAspect) {
                double excess = (ybottom - ytop) * (displayAspect / requestedAspect - 1);
                ybottom += excess / 2;
                ytop -= excess / 2;
            } else if (displayAspect < requestedAspect) {
                double excess = (xright - xleft) * (requestedAspect / displayAspect - 1);
                xright += excess / 2;
                xleft -= excess / 2;
            }
        }
        double pixelWidth = Math.abs((xright - xleft) / width);
        double pixelHeight = Math.abs((ybottom - ytop) / height);
        pixelSize = (float) Math.min(pixelWidth, pixelHeight);
        g2.scale(width / (xright - xleft), height / (ybottom - ytop));
        g2.translate(-xleft, -ytop);
    }
}