@Test
public void outputHasLineNumbers() {
   String content = "1st match on #1\nand\n2nd match on #3";
   String out = grep.grep("match", "test.txt", content);
   assertThat(out.contains("test.txt:1 1st match"), equals(true));
   assertThat(out.contains("test.txt:3 2nd match"), equals(true));
}