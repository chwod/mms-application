public class CarTest{
	@Test
	public void engineIsStartedWhenCarStarts() {
		TestEngine engine = new TestEngine();
		new Car(engine).start();
		assertTrue(engine.isRunning());
	}
}

public class TestEngine extends Engine{
	private boolean isRunning;
	
	public void start() {
		this.isRunning = true;
	}
	
	public boolean isStart() {
		return this.isRunning;
	}
}