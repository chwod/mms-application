
...
@Test
public void shouldRefuseNegativeEntries() {
	int total = record.total;
	try {
		record.add(-1);
	} catch (illegalArgumentException excepted) {
		assertEquals(total, record.total());
	}
}
...