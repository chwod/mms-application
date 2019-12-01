public class PackageFetcherTest {
   private PackageFetcher fetcher;
   private Map downloads;
   private File tempDir;

   @Before
   public void setUp() throws Exception {
      String systemTempDir = System.getProperty("java.io.tmpdir");
      tempDir = new File(systemTempDir, "downloads");
      tempDir.mkdirs();
      String filename = "/manifest.xml";
      InputStream xml = getClass().getResourceAsStream(filename);
      Document manifest = XOM.parse(IO.streamAsString(xml));
      PresentationList presentations = new PresentationList();
      presentations.parse(manifest.getRootElement());
      PresentationStorage db = new PresentationStorage();
      List list = presentations.getResourcesMissingFrom(null, db);
      fetcher = new PackageFetcher();
      downloads = fetcher.extractDownloads(list);
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
}