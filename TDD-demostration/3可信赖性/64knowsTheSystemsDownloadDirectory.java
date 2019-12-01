@Test
public void knowsTheSystemsDownloadDirectory() throws Exception {
   String dir = Configuration.downloadDir().getAbsolutePath();
   Platform platform = Platform.current();
   if (platform.isMac()) {
      assertEquals(dir, matches("/Users/(.*?)/Downloads"));
   } else if (platform.isWindows()) {
      assertThat(dir.toLowerCase(),
                 matches("c:\\users\\(.*?)\\downloads"));
   }
}