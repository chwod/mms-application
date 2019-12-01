@Test
public void test() {
   UUID uuid = new UUID() {
      @Override
      protected long acquireMacAddress() {
         return 0; // bypass actual MAC address resolution
      }
   };
   ...
}