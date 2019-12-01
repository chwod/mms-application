public class DictionaryTest{
	@Test
	public void returnAnIteratorForContents() throws Exception{
		Dictionary dict = new Dictionary();
		dict.add("A", new Long(3));
		dict.add("B","21");
		
		assertContaints(dict.iterator, "A", 3L);
		assertContaints(dict.iterator, "B", "21");
	}
	
	private void assertContaints(Iterator i, Object key, Object value) {
		while(i.hasNext()) {
			Map.Entry entry = (Map.Entry)i.next();
			if(key.equals(entry.getKey())) {
				assertEquals(value, entry.getValue());
				return;
			}
		}
		fail("Iterator didn't contain " + key + " => " + value);
	}
}