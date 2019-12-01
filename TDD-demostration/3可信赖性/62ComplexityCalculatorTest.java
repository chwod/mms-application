public class ComplexityCalculatorTest {
   private ComplexityCalculator complexity;

   ...

   @Test
   public void complexityIsZeroForNonExistentFiles() {
      assertEquals(0.0, complexity.of(new Source("NoSuchFile.java")));
   }

   @Test
   public void complexityForSourceFile() {
      double sample1 = complexity.of(new Source("test/Sample1.java"));
      double sample2 = complexity.of(new Source("test/Sample2.java"));
      assertThat(sample1, is(greaterThan(0.0)));
      assertThat(sample2, is(greaterThan(0.0)));
      assertTrue(sample1 != sample2);
   }
}