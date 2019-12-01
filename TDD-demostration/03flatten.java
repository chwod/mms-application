@Test
public void flatten() throws Exception {
	Env e = Env.getInstance();
	Structure k = e.newStructure();
	Structore v = e.newStructure();
	//int n = 10;
	int n = 10000;
	for(int i = 0; i< n; ++i) {
		k.append(e.newFixnum(i));
		v.append(e.newFixnum(i));
	}
	Structure t = (Structure)k.zip(e.getCurrentContext(), new IObject[] {v}, Block.NULL_BLOCK);
	v = (Structure)t.flatten(e.getCurrentContext());
	
	assertNotNull(v);
}