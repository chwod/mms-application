public class TestRomanNumerals1 {

	@Test
	public void one() {
		assertEquals("I", format(1));
	}

	@Test
	public void two() {
		assertEquals("II", format(2));
	}

	@Test
	public void four() {
		assertEquals("IV", format(4));
	}

	@Test
	public void five() {
		assertEquals("V", format(5));
	}

	@Test
	public void seven() {
		assertEquals("VII", format(7));
	}

	@Test
	public void nine() {
		assertEquals("IX", format(9));
	}

	@Test
	public void ten() {
		assertEquals("X", format(10));
	}
}
