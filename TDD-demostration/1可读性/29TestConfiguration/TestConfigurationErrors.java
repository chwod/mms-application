public class TestConfigurationErrors extends AbstractConfigTestCase {
	@Override
	protected String[] args() {
		return new String[] { "-f" };
	}

	@Test(expected = InvalidArgumentException.class)
	public void missingArgumentRaisesAnError() {
	}
}
