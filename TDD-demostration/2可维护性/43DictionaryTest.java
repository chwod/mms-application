public class DictionaryTest{
	@Test
	public void returnAnIteratorForContents() throws Exception{
		Dictionary dict = new Dictionary();
		dict.add("A", new Long(3));
		dict.add("B","21");
		for(Iterator e = dict.iterator();e.hasNext();) {
			Map.Entry entry = (Map.Entry)e.next();
			if("A".equals(entry.getKey())) {
				assertEuals("3L", entry.getValue());
			}
			if("B".equals(entry.getKey())) {
				assertEuals("21", entry.getValue());
			}
		}
	}
}