public class TestExplicitlySetConfigValues extends AbstractConfigTestCase {
	@Override
	protected String[] args() {
		return new String[] { "-f", "hello.txt", "-v", "-d", "-w", "--version" };
	}

	@Test
	public void explicitOptionsAreSetCorrectly() {
		assertEquals("hello.txt", c.getFileName());
		assertTrue(c.isDebuggingEnabled());
		assertTrue(c.isWarningsEnabled());
		assertTrue(c.isVerbose());
		assertTrue(c.shouldShowVersion());
	}
}