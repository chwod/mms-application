public class TemplateTest {
   @Test
   public void emptyTemplate() throws Exception {
      assertEquals("", new Template("").evaluate());
   }

   @Test
   public void plainTextTemplate() throws Exception {
      assertEquals("plaintext", new Template("plaintext").evaluate());
   }
}