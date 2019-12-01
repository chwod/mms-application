public class PlatformTest {
   @Test
   public void platformBitLength() {
      assertTrue(Platform.IS_32_BIT ^ Platform.IS_64_BIT);
   }
}