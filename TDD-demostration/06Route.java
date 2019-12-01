public class Route{
	private Clock clock = new Clock();
	private ShortestPath algorithm = new ShortestPath();
	
	public Collection<Directions> directions(){
		if(clock.isRushHour()) {
			return algorithm.avoidBusyIntersections();
		}
		return algorithm.calculateRouteBetween(...);
	}
}