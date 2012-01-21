package com.thomcc.nine.level.gen;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class VoronoiNoise {
  public final int pts;
  public ArrayList<Point2D.Double> points;
  private Random _random;
  
  public static final int DISTANCE_NORMAL    = 0;
  public static final int DISTANCE_SQUARE    = 1;
  public static final int DISTANCE_CHEBYCHEV = 2;
  public static final int DISTANCE_MANHATTAN = 3;
  
  private interface Dist {
    public double calc(Point2D.Double a, Point2D.Double b);
  }
  private final Dist dNormal;
  private final Dist dSquare;
  private final Dist dChebychev;
  private final Dist dManhattan;
  
  public int w, h;
  public VoronoiNoise(int width, int height, int pts) {
    this.pts = pts;
    w = width;
    h = height;
    _random = new Random();
    points = new ArrayList<Point2D.Double>();
    for (int i = 0; i < pts; ++i) 
      points.add(new Point2D.Double(_random.nextDouble() * width, _random.nextDouble() * height));
    
    dManhattan = new Dist() {
      public double calc(Point2D.Double a, Point2D.Double b) {
        return dWrap(a.x, b.x, w)+dWrap(a.y, b.y, h); }};
    dChebychev = new Dist() {
      public double calc(Point2D.Double a, Point2D.Double b) {
        return Math.max(dWrap(a.x, b.x, w), dWrap(a.y, b.y, h)); }};
    dSquare = new Dist() {
      public double calc(Point2D.Double a, Point2D.Double b) {
        double x = dWrap(a.x, b.x, w); double y = dWrap(a.y, b.y, h);
        return x*x+y*y; }};
    dNormal = new Dist() {
      public double calc(Point2D.Double a, Point2D.Double b) {
        double x = dWrap(a.x, b.x, w); double y = dWrap(a.y, b.y, h);
        return Math.sqrt(x*x+y*y); }};
  }
  
  public Point2D.Double nearest(Point2D.Double p, Dist d) {
    Point2D.Double min = points.get(0);
    double mindist = d.calc(p, min);
    for (Point2D.Double pt : points) {
      double dist = d.calc(p, pt);
      if (mindist > dist) { min = pt; mindist = dist; }
    }
    return min;
  }
  
  public Point2D.Double[] nearest2(Point2D.Double p, Dist d) {
    Point2D.Double min1 = nearest(p, d);
    Point2D.Double min2 = points.get(0) == min1 ? points.get(1) : points.get(0);
    double mindist = d.calc(p, min2);
    for (Point2D.Double pt : points) {
      if (pt == min1) continue;
      double dist = d.calc(p, pt);
      if (mindist > dist){ min2 = pt; mindist = dist; }
    }
    return new Point2D.Double[] { min1, min2 };
  }
  
  public Point2D.Double[] nearestN(final Point2D.Double p, final Dist d, int n) {
    Collections.sort(points, new Comparator<Point2D.Double>() {
      public int compare(Point2D.Double p1, Point2D.Double p2) {
        return (int) Math.signum(d.calc(p, p1)-d.calc(p, p2)); }});
    
    Point2D.Double[] pts = new Point2D.Double[n];
    
    for (int i = 0; i < n; ++i) pts[i] = points.get(i);
    
    return pts;
  }
  
  public double[][] calculate(int distanceMetric) {
    return calculate(distanceMetric, 2);
  }
  
  public double[][] calculate(int distanceMetric, int neighbors) {
    Dist d;
    switch (distanceMetric) {
    case DISTANCE_NORMAL: d = dNormal; break;
    case DISTANCE_SQUARE: d = dSquare; break;
    case DISTANCE_MANHATTAN: d = dManhattan; break;
    case DISTANCE_CHEBYCHEV: d = dChebychev; break;
    default: throw new IllegalArgumentException("invalid distance metric");
    }
    if (neighbors <= 0) throw new IllegalArgumentException("invalid number of neighbors");
    switch (neighbors) { // optimize the common cases
    case 1: return calc1(d);
    case 2: return calc2(d);
//    case 3: return calc3(d);
//    case 4: return calc4(d);
//    default: return calcN(d,n);
    }
    throw new IllegalArgumentException("unimplemented"); 
  }
  public double[][] calc1(Dist dist) {
    double[][] ary = new double[h][w];
    double maxd = -1;
    for (int y = 0; y < h; ++y) {
      for (int x = 0; x < w; ++x) {
        Point2D.Double p = new Point2D.Double(x, y);
        Point2D.Double n = nearest(p, dist);
        double d = dist.calc(p, n);
        if (d > maxd) maxd = d;
        ary[y][x] = d;
      }
    }
    for (int y = 0; y < h; ++y) {
      for (int x = 0; x < w; ++x) {
        ary[y][x] /= maxd;
      }
    }
    return ary;
  }
  public double[][] calc2(Dist dist) {
    double[][] ary = new double[h][w];
    double maxd = -1;
    for (int y = 0; y < h; ++y) {
      for (int x = 0; x < w; ++x) {
        Point2D.Double p = new Point2D.Double(x, y);
        Point2D.Double[] ns = nearest2(p, dist);
        Point2D.Double n1 = ns[0];
        Point2D.Double n2 = ns[1];
        double d = dist.calc(p, n2)-dist.calc(p, n1);
        if (d > maxd) maxd = d;
        ary[y][x] = d;
      }
    }
    for (int y = 0; y < h; ++y) {
      for (int x = 0; x < w; ++x) {
        ary[y][x] /= maxd;
      }
    }

    return ary;
  }
  
  private static final double dWrap(double a, double b, int clamp) {
    double d = Math.abs(a-b);
    if (d > clamp/2) {
      if (a < b) return a + clamp-b;
      else return b + clamp-a;
    } else return d;
  }
  
  
  public static void main(String args[]) {
    int width = 2000;
    int height = 2000;
    int points = 500;

    long now = System.nanoTime();
    double[][] grid = new VoronoiNoise(width, height, points).calculate(DISTANCE_CHEBYCHEV);
    long later = System.nanoTime();
    long t = later-now;
    long millis = t/1000000;
    System.out.format("Voronoi calculated in %.1f seconds. (%s nanoseconds, %s milliseconds)\n", (double)t/1e9, t, millis);
    System.out.format("\tWidth: %s, Height: %s, Points: %s\n", width, height, points);
    BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    int[] pix = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
    for (int y = 0; y < width; ++y) {
      for (int x = 0; x < height; ++x) {
        double cell = grid[y][x];        
        int v = (int) (255.0 * grid[y][x]);

        if (cell < 0.1) v = 0;
        else if (cell < 0.25) v = 63;
        else if (cell < 0.50) v = 127;
        else if (cell < 0.75) v = 191;
        else v = 255;
         
        int c = v << 16 | v << 8 | v;
        pix[x + y*width] = c;
      }
    }
    
    JOptionPane.showMessageDialog(
        null,
        null,
        "vnoise",
        JOptionPane.YES_NO_OPTION,
        new ImageIcon(img.getScaledInstance(img.getWidth(),
            img.getHeight(), Image.SCALE_AREA_AVERAGING)));

  }
}
