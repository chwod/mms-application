public class XmlProductCatalogTest {
   private ProductCatalog catalog;

   @Before
   public void createProductCatalog() throws Exception {
      File catalogFile = new File("C:\\workspace\\catalog.xml");
      catalog = new XmlProductCatalog(parseXmlFrom(catalogFile));
   }

   @Test
   public void countsNumberOfProducts() throws Exception {
      assertEquals(2, catalog.numberOfProducts());
   }

   // remaining tests omitted for brevity
}