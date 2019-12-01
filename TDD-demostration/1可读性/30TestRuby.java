public class TestRuby {
   private Ruby runtime;

   @Before
   public void setUp() throws Exception {
      runtime = Ruby.newInstance();
   }

   @Test
   public void testVarAndMet() throws Exception {
      runtime.getLoadService().init(new ArrayList());
      eval("load 'test/testVariableAndMethod.rb'");
      assertEquals("Hello World", eval("puts($a)"));
      assertEquals("dlroW olleH", eval("puts $b"));
      assertEquals("Hello World",
            eval("puts $d.reverse, $c, $e.reverse"));
      assertEquals("135 20 3",
            eval("puts $f, \" \", $g, \" \", $h"));
   }
}