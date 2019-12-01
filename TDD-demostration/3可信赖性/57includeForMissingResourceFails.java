@Test
public void includeForMissingResourceFails() {
   try {
      new Environment().include("somethingthatdoesnotexist");
      fail("some thing reason!");
   } catch (IOException e) {
      assertThat(e.getMessage(),
                 contains("somethingthatdoesnotexist"));
   }
}