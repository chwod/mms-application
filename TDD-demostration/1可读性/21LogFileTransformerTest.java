public class LogFileTransformerTest {
   private String expectedOutput;
   private String logFile;

   @Before
   public void setUpBuildLogFile() {
      StringBuilder lines = new StringBuilder();
      appendTo(lines, "[2019-10-23 21:20:33] LAUNCHED");
      appendTo(lines, "[2019-10-23 21:20:33] session-id###SID");
      appendTo(lines, "[2019-10-23 21:20:33] user-id###UID");
      appendTo(lines, "[2019-10-23 21:20:33] presentation-id###PID");
      appendTo(lines, "[2019-10-23 21:20:35] screen1");
      appendTo(lines, "[2019-10-23 21:20:36] screen2");
      appendTo(lines, "[2019-10-23 21:21:36] screen3");
      appendTo(lines, "[2019-10-23 21:21:36] screen4");
      appendTo(lines, "[2019-10-23 21:22:00] screen5");
      appendTo(lines, "[2019-10-23 21:22:48] STOPPED");
      logFile = lines.toString();
   }

   @Before
   public void setUpBuildTransformedFile() {
      StringBuilder file = new StringBuilder();
      appendTo(file, "session-id###SID");
      appendTo(file, "presentation-id###PID");
      appendTo(file, "user-id###UID");
      appendTo(file, "started###2019-10-23 21:20:33");
      appendTo(file, "screen1###1");
      appendTo(file, "screen2###60");
      appendTo(file, "screen3###0");
      appendTo(file, "screen4###24");
      appendTo(file, "screen5###48");
      appendTo(file, "finished###2019-10-23 21:22:48");
      expectedOutput = file.toString();
   }

   @Test
   public void transformationGeneratesRightStuffIntoTheRightFile()
          throws Exception {
      TempFile input = TempFile.withSuffix(".src.log").append(logFile);
      TempFile output = TempFile.withSuffix(".dest.log");
      new LogFileTransformer().transform(input.file(), output.file());
      assertTrue("Destination file was not created", output.exists());
      assertEquals(expectedOutput, output.content());
   }
   // rest omitted for clarity
}