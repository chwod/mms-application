public class DLogTest{
	
	@Test
	public void writeEachMessageToAllTargets() throws Exception{
		SpyTarget spy1 = new SpyTarget();
		SpyTarget spy2 = new SpyTarget();
		DLog log = new DLog(spy1, spy2);
		log.write(Level.INFO, "message");
		assertTrue(spy1.received(Level.INFO, "message"));
		assertTrue(spy2.received(Level.INFO, "message"));
	}
	
	private class SpyTarget implements DLogTarget{
		private List<String> log = new ArrayList<>();
		
		@Override
		public void write(Level level, String message) {
			this.log.add(concatenated(level, message));
		}
		
		public boolean received(Level level, String message) {
			return this.log.contains(concatenated(level, message));
		}
		
		private String concatenated(Level level, String message) {
			return level.getName() + ": " + message;
		}
	}
}