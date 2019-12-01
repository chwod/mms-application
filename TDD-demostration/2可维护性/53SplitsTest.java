public class SplitTest{
	Account account;
	Split split;
	BudgetCatagory bc1,bc2,bc3,bc4;
	
	@Test
	public void fromSplits() throws Exception {
		List<TransactionSplit> fromSplits = new ArrayList<>();
		fromSplits.add(createSplit(bc3,1000));
		fromSplits.add(createSplit(bc4,34));
		Transaction t = createTransaction(split,acount);
		t.setFromSplits(fromSplits);
		assertTrue(transaction(t).size() == 1);
	}
	
	@Test
	public void toSplits() throws Exception {
		List<TransactionSplit> toSplits = new ArrayList<>();
		toSplits.add(createSplit(bc1,1000));
		toSplits.add(createSplit(bc2,34));
		Transaction t = createTransaction(split,acount);
		t.setToSplits(toSplits);
		assertTrue(transaction(t).size() == 1);
	}
}