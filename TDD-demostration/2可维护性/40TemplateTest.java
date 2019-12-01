public class TemplateTest {
   @Test
   public void emptyTemplate() throws Exception {
      String template = "";
      assertEquals(template, new Template(template).evaluate());
   }

   @Test
   public void plainTextTemplate() throws Exception {
      String template = "plaintext";
      assertEquals(template, new Template(template).evaluate());
   }
}