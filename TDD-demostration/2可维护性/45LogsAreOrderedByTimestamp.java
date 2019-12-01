	@Test
	public void LogsAreOrderedByTimestamp() throws Exception {
		generateLogFile(logDir, "app-2.log","one");
		generateLogFile(logDir, "app-1.log","two");
		generateLogFile(logDir, "app-0.log","three");
		generateLogFile(logDir, "app.log","four");
		
		Log aggregate = AggregateLogs.collectAll(logDir);
		assertEquals(asList("one","two","three","four"),aggregate.asList());
	}
	
	private void generateLogFile(final File dir, final String name, final String[] messages) {
		File file = new File(dir,name);
		for(String message : messages) {
			IO.appendToFile(file,message);
		}
	}