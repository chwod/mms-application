@Test
public void includeForMissingResourceFails() {
   try {
      new Environment().include("somethingthatdoesnotexist");
   } catch (IOException e) {
      assertThat(e.getMessage(),
                 contains("somethingthatdoesnotexist"));
   }
}