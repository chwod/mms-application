@Test(expected = IOException.class)
public void includingMissingResourceFails() {
   new Environment().include("somethingthatdoesnotexist");
}