public class TestConfiguration {
	private String dir;
	private Platform platform;
	
	@BeforeClass
	public void setup() {
		dir = Configuration.downloadDir().getAbsolutePath();
		platform = Platform.current();
	}
	
	@Test
	public void knowsTheSystemsDownloadDirectoryOnMac() throws Exception {
		assumeTrue(platform.isMac);
		assertEquals(dir, matches("/Users/(.*?)/Downloads"));
	}
	
	@Test
	public void knowsTheSystemsDownloadDirectoryOnWindow() throws Exception {
		assumeTrue(platform.isWindow);
		assertEquals(dir, matches("c:\\users\\(.*?)\\downloads"));
	}
}