@Test
public void multipleArgumentsAreSentToShell() throws Exception {
   File dir = createTempDirWithChild("hello.txt");

   String[] cmd = new String[] { "ls", "-1", dir.getAbsolutePath() };
   Process process = new Process(cmd).runAndWait();

   assertEquals(0, process.exitCode());
   assertEquals("hello.txt", process.output().trim());
}

@Test
public void returnsNonZeroExitCodeForFailedCommands() {
   String[] cmd = new String[] { "this", "will", "fail" };
   Process process = new Process(cmd).runAndWait();
   assertThat(process.exitCode(), is(greaterThan(0)));
}