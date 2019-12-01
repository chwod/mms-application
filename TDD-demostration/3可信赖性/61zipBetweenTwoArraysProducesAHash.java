@Test
public void zipBetweenTwoArraysProducesAHash() throws Exception {
   Arraykeys = new Array("a", "b", "c");
   Arrayvalues = new Array(1, 2,3);
   Arrayzipped = keys.zip(values);
   assertNotNull("We have a hashback", zipped.flatten());
}