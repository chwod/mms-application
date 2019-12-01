public class TestRuby {
	private Ruby runtime;

	private AppendableFile script

   @Before
   public void setUp() throws Exception {
	  runtime = Ruby.newInstance();
      runtime.getLoadService().init(new ArrayList());
      script = withTempFile();
   }

	@Test
	public void variableAssignment() throws Exception {
		script.line("a = String.new('Hello')");
		script.line("b = 'World'");
		script.line("$c = 1 + 2");
		afterEvaluating(script);
		assertEquals("Hello", eval("puts(a)"));
		assertEquals("World", eval("puts b"));
		assertEquals("3", eval("puts $c"));
	}

	@Test
	public void methodInvocation() throws Exception {
		script.line("a = 'Hello'.reverse");
		script.line("b = 'Hello'.length()");
		script.line("c = ' abc '.trim(' ', '_')");
		afterEvaluating(script);
		assertEquals("olleH", eval("puts a"));
		assertEquals("3", eval("puts b"));
		assertEquals("_abc_", eval("puts c"));
	}

	private void afterEvaluating(AppendableFile sourceFile) throws Exception {
		eval("load '" + sourceFile.getAbsolutePath() + "'");
	}
}