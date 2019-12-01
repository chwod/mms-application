public class PathAlgorithm {
   private final BufferedImage img;
   private Set<Point> visited;
   private int lineColor;

   public PathAlgorithm(BufferedImage image) {
      this.img = image;
   }

   public boolean areConnected(Point start, Point end) {
      visited = new HashSet<Point>();
      lineColor = img.getRGB(start.x, start.y);
      return areSomehowConnected(start, end);
   }

   private boolean areSomehowConnected(Point start, Point end) {
      visited.add(start);
      if (areDirectlyConnected(start, end)) return true;
      for (Point next : unvisitedNeighborsOf(start)) {
         if (areSomehowConnected(next, end)) return true;
      }
      return false;
   }

   private List<Point> unvisitedNeighborsOf(Point p) {
      List<Point> neighbors = new ArrayList<Point>();
      for (int xDiff = -1; xDiff <= 1; xDiff++) {
         for (int yDiff = -1; yDiff <= 1; yDiff++) {
            Point neighbor = new Point(p.x + xDiff, p.y + yDiff);
            if (!isWithinImageBoundary(neighbor)) continue;
            int pixel = img.getRGB(neighbor.x, neighbor.y);
            if (pixel == lineColor && !visited.contains(neighbor)) {
               neighbors.add(neighbor);
            }
         }
      }
      return neighbors;
   }

   private boolean isWithinImageBoundary(Point p) {
      if (p.x < 0 || p.y < 0) return false;
      if (p.x >= img.getWidth()) return false;
      if (p.y >= img.getHeight()) return false;
      return true;
   }

   private boolean areDirectlyConnected(Point start, Point end) {
      int xDistance = abs(start.x - end.x);
      int yDistance = abs(start.y - end.y);
      return xDistance <= 1 && yDistance <= 1;
   }
}