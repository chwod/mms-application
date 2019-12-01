
	@Test
	public void multipleArgumentsAreSendToShell() throws Exception {
		Fil dir = createTempDirWithChild("hello.txt");
		
		String[] cmd = new String[] {"is","-1",dir.getAbsolutionPath()};
		Process process = new Process(cmd).runAndWait();
		
		if(process.exitcode == 0) {
			assertEquals("hello.txt", process.output().trim());
		}
	}