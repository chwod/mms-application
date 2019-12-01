public class PlatformTest {
   @Test
   public void platformBitLength() {
      assertTrue("Not 32 or 64-bit platform?",
                 Platform.IS_32_BIT || Platform.IS_64_BIT);
      assertFalse("Can't be 32 and 64-bit at the same time.",
                 Platform.IS_32_BIT && Platform.IS_64_BIT);
   }
}