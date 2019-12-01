public class TemplateTest {
   @Test
   public void emptyTemplate() throws Exception {
      assertTemplateRendersAsItself("");
   }

   @Test
   public void plainTextTemplate() throws Exception {
      assertTemplateRendersAsItself("plaintext");
   }

   private void assertTemplateRendersAsItself(String template) {
      assertEquals(template, new Template(template).evaluate());
   }
}