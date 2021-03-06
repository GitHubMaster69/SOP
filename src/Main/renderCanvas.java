package Main;


import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Objects;
import java.awt.image.BufferedImage;

public class renderCanvas extends JPanel {

    Color red = new Color(255, 0, 0);
    Color green = new Color(0, 255, 0);
    Color blue = new Color(0, 0, 255);

    int width = 600;
    int height = 600;
    int x;
    int y;

    Color color = new Color(255,255,255);

    int eyeFOV = 1;
    int z_pos = 1;
    double[] cameraPos = {0, 0, 0};

    private BufferedImage canvas;


    public renderCanvas(int width, int height) {
         canvas = new BufferedImage(width,height, BufferedImage.TYPE_INT_ARGB);
         render();
    }

    public void paintComponent (Graphics g) {
        System.out.println("paint");
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(canvas,null,null);
    }

    // ======================================================================
    //  render(): Kører gennem alle pixels på skærmen og finder farven ved brug af funktionerne
    // ======================================================================

    public void render() {
        for (int i = -width/2; i < width/2; i++) {
            for (int j = -height/2; j < height/2; j++) {
                double[] d = {i, j};
                double[] direction = CanvasToPos(d);
                color = traceRay(cameraPos, direction, 1, 99999);
                getPixel(i,j,color);
            }
        }
        repaint();
    }

    // ======================================================================
    //  getPixel(): tager værdierne fra render(x,y,farve) og printer det til canvas
    // ======================================================================
    public void getPixel (int x, int y, Color color){
        this.x = width/2 + x;
        this.y = height/2 - y;
        this.color = color;
        int rgb = this.color.getRGB();
       // System.out.println(Objects.toString(color));
        if (this.x < 0 || this.x >= width || this.y <= 0 || this.y >= height) {
            return;
        }
        System.out.println(this.x + "x" + "     " + this.y + "y" + "    " + "farvet:" + "    " + this.color);
        canvas.setRGB(this.x,this.y,(rgb));
    }

    // ======================================================================
    //  Vektor regning med prikprodukt og subration
    // ======================================================================

    private double DotProduct(double[] v1, double[] v2) {
        double[] Dot_P = new double[3];
        Dot_P[0] = v1[0] * v2[0];
        Dot_P[1] = v1[1] * v2[1];
        Dot_P[2] = v1[2] * v2[2];
        return Dot_P[0] + Dot_P[1] + Dot_P[2];
    }

    private double[] Sub(double[] v1, double[] v2) {
        double[] Sub = new double[3];
        Sub[0] = v1[0] - v2[0];
        Sub[1] = v1[1] - v2[1];
        Sub[2] = v1[2] - v2[2];
        return Sub;
    }

    // ======================================================================
    //  koordinatsystem
    // ======================================================================

    public double[] CanvasToPos(double[] canvasPos)  {
       // System.out.println(Arrays.toString(p2b));
        double[] temp = new double[3];
                temp[0] = canvasPos[0] * eyeFOV / width;
                temp[1] = canvasPos[1] * eyeFOV / height;
                temp[2] = z_pos;
             //   System.out.println(Arrays.toString(temp3));
        return temp;
    }

// ======================================================================
    //  IntersectRaySphere udregner kvadratformlen og traceRay udregner om strålen rammer en cirkel
    // ======================================================================

    public double[] IntersectRaySphere(double[] origo, double[] direction, sphere sp) {
        double[] oc = Sub(origo,sp.center);
        double temp1 = DotProduct(direction,  direction);
        double temp2 = 2*DotProduct(oc, direction);
        double temp3 = DotProduct(oc, oc) - sp.radius * sp.radius;

        double discriminant = temp2*temp2 - 4*temp1*temp3;
        if (discriminant < 0) {
            return new double[] {999999,99999};
        }

        double[] tempArray = new double[2];
        tempArray[0] = (-temp2 + Math.sqrt(discriminant)) / (2*temp1);
        tempArray[1] = (-temp2 - Math.sqrt(discriminant)) / (2*temp1);
        return tempArray;
    }


    public Color traceRay(double[] origin, double[]direction, int min_t, int max_t)  {
        sphere[] spheres = new sphere[3];

                                // Du kan ændrer koordinaterne eller farverne på kuglerne som du vil
        spheres[0] = new sphere(new double[]{0, -1, 10},1,Color.ORANGE);
        spheres[1] = new sphere(new double[]{2,0,5},1,Color.YELLOW);
        spheres[2] = new sphere(new double[]{-2,0,12},1,Color.RED);

        double closest_t = 99999;
        sphere closest_sphere = null;


        for (int i = 0; i < spheres.length; i++) {
            double[] ts = IntersectRaySphere(origin, direction, spheres[i]);
            if (ts[0] < closest_t && min_t < ts[0] && ts[0] < max_t) {
                closest_t = ts[0];
                closest_sphere = spheres[i];
                //System.out.println(("a"));

            }
            if (ts[1] < closest_t && min_t < ts[1] && ts[1] < max_t) {
                closest_t = ts[1];
                closest_sphere = spheres[i];
                //System.out.println(("b"));

            }
        }

        if (closest_sphere == null) {
            //System.out.println(("c"));
            return Color.white;
        }
        //System.out.println(("d"));
        return closest_sphere.color;
    }


}


