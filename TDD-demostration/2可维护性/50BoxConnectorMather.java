public class BoxConnectorMatcher extends BaseMather<BufferedImage>{
	private final Diagram diagram;
	private final Box box1;
	private final Box box2;
	
	BoxConnectorMatcher(Diagram diagram, Box box1, Box box2){
		this.diagram = diagram;
		this.box1 = box1;
		this.box2 = box2;
	}
	
	@Override
	public boolean matches(Object o) {
		BufferedImage image = (BufferedImage)o;
		Point start = findEdgePointFor(box1);
		Point end = findEdgePointFor(box2);
		return new PathAlgorithm(image).areConnected(start, end);
	}
	
	private Point findEdgePointFor(final Box box1) {
		Point a = diagram.positionOf(box1);
		int x = a.x + (box1.width()/2);
		int y = a.y + (BOX1.height()/2);
		return new Point(x,y);
	}
	
	@Override
	public boid describeTo(Description d) {
		d.appendText("connecting line exists between " + box1 + " and " + box2);
	}
}