public class LogFileTransformerTest {
	
	private static final String END = "2019-11-30 15:01:00";
	private static final String START = "2019-11-30 15:00:00";
	private LogFile logFile;

	@Before
	public void prepareLogFile() {
		this.logFile = new LogFile(START, END);
	}
	
	@Test
	public void overallFileStructureIsCorrect() throws Exception {
		StringBuilder expected = new StringBuilder();
	      appendTo(file, "session-id###SID");
	      appendTo(file, "presentation-id###PID");
	      appendTo(file, "user-id###UID");
	      appendTo(file, "started###2005-05-23 21:20:33");
	      appendTo(file, "finished###2005-05-23 21:22:48");
	      assertEquals(expected.toString(), transform(this.logFile.toString()));
	}
	
	@Test
	public void screenDurationsGoBetweenStartedAndFinished() throws Exception{
		this.logFile.addContent("[2019-10-23 21:20:35] screen1");
		String out = transform(this.logFile.toString());
		assertTrue(out.indexOf("started") < out.indexOf("screen1"));
		assertTrue(out.indexOf("screen1") < out.indexOf("finished"));
	}
	
	@Test
	public void screenDurationsAreRenderedInSeconds() throws Exception{
		this.logFile.addContent("[2019-10-23 21:20:35] screen1");
		this.logFile.addContent("[2019-10-23 21:20:36] screen2"));
		this.logFile.addContent("[2019-10-23 21:21:36] screen3"));
		String out = transform(this.logFile.toString());
		assertTrue(output.contains("screen1###1"));
		assertTrue(output.contains("screen2###60"));
		assertTrue(output.contains("screen3###0"));
	}
	
	private String transform(String log) {...};
	private void appendTo(StringBuilder builder, String str) {...};
	private class LogFile{
		...
	}
}