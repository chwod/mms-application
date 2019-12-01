@Test
public void complexityForSourceFile() {
   assertEquals(2, complexity.of(new Source("test/Sample1.java")));
   assertEquals(5, complexity.of(new Source("test/Sample2.java")));
}