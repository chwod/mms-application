public class TestConfiguration {
	@Test
	public void knowsTheSystemsDownloadDirectoryOnMacOsX()
	         throws Exception {
	   String downloadsDir = new MacOsX().downloadDir();
	   assertEquals(downloadsDir,
	                matches("/Users/(.*?)/Downloads"));
	}

	@Test
	public void knowsTheSystemsDownloadDirectoryOnWindows()
	         throws Exception {
	   String downloadsDir = new Windows().downloadDir();
	   assertThat(downloadsDir.toLowerCase(),
	              matches("c:\\users\\(.*?)\\downloads"));
	}
}