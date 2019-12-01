public class TestConfiguration {

	private Configuration c;

	@Before
	public void instantiateDefaultConfiguration() {
		c = new Configuration();
	}

	@Test
	public void validArgumentsProvided() {
		String[] args = { "-f", "hello.txt", "-v", "--version" };
		c.processArguments(args);
		assertEquals("hello.txt", c.getFileName());
		assertFalse(c.isDebuggingEnabled());
		assertFalse(c.isWarningsEnabled());
		assertTrue(c.isVerbose());
		assertTrue(c.shouldShowVersion());
	}

	@Test(expected = InvalidArgumentException.class)
	public void missingArgument() {
		c.processArguments(new String[] { "-f" });
	}
}
