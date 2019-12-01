public class TestDefaultConfigValues extends AbstractConfigTestCase {
	@Test
	public void defaultOptionsAreSetCorrectly() {
		assertFalse(c.isDebuggingEnabled());
		assertFalse(c.isWarningsEnabled());
		assertFalse(c.isVerbose());
		assertFalse(c.shouldShowVersion());
	}
}