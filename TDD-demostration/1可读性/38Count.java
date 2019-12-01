@Test
public void count() {
   Data data = project.getData();
   assertNotNull(data);
   assertEquals(4, data.count());
   //assertEquals(5, data.getSummy().getCount());
}