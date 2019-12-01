public class Car{
	private Engine engine;
	
	public Car(Engine engine) {
		this.engine = engine;
	}
	
	public void start() {
		this.engine.start();
	}
	
	public void drive(Route route) {
		for(Directions directions : route.directions) {
			directions.follow();
		}
	}
	
	public void stop() {
		this.engine.stop();
	}
}