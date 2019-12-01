public class TestRuby {
   private Ruby runtime;

   @Before
   public void setUp() throws Exception {
      runtime = Ruby.newInstance();
   }

   @Test
   public void testVarAndMet() throws Exception {
      runtime.getLoadService().init(new ArrayList());
      
      AppendableFile script = withTempFile();
      script.line("a = String.new(\"Hello World\")");
      script.line("b = a.reverse");
      script.line("c = \" \"");
      script.line("d = \"Hello\".reverse");
      script.line("e = a[6, 5].reverse");
      script.line("f = 100 + 35");
      script.line("g = 2 * 10");
      script.line("h = 13 % 5");
      script.line("$a = a");
      script.line("$b = b");
      script.line("$c = c");
      script.line("$d = d");
      script.line("$e = e");
      script.line("$f = f");
      script.line("$g = g");
      script.line("$h = h");
      
      eval("load '" + script.getAbsolutePath() + "'");
      assertEquals("Hello World", eval("puts($a)"));
      assertEquals("dlroW olleH", eval("puts $b"));
      assertEquals("Hello World",
            eval("puts $d.reverse, $c, $e.reverse"));
      assertEquals("135 20 3",
            eval("puts $f, \" \", $g, \" \", $h"));
   }
}