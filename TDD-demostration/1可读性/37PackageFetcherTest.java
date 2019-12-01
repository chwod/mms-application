public class PackageFetcherTest {
   private PackageFetcher fetcher;
   private Map downloads;
   private File tempDir;

   @Before
   public void setUp() throws Exception {
      fetcher = new PackageFetcher();
      tempDir = createTempDir("downloads");
      downloads = extractMissingDownloadsFrom("/manifest.xml");
   }

   @After
   public void tearDown() throws Exception {
      IO.delete(tempDir);
   }

   @Test
   public void downloadsAllResources() {
      fetcher.download(downloads, tempDir, new MockConnector());
      assertEquals(4, tempDir.list().length);
   }

   private File createTempDir(String name) {
      String systemTempDir = System.getProperty("java.io.tmpdir");
      File dir = new File(systemTempDir, name);
      dir.mkdirs();
      return dir;
   }

   private Map extractMissingDownloadsFrom(String path) {
      PresentationStorage db = new PresentationStorage();
      PresentationList list = createPresentationListFrom(path);
      List downloads = list.getResourcesMissingFrom(null, db);
      return fetcher.extractDownloads(downloads);
   }

   private PresentationList createPresentationListFrom(String path)
         throws Exception {
      PresentationList list = new PresentationList();
      list.parse(readManifestFrom(path).getRootElement());
      return list;
   }

   private Document readManifestFrom(String path) throws Exception {
      InputStream xml = getClass().getResourceAsStream(path);
      return XOM.parse(IO.streamAsString(xml));
   }
}