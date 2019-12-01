public class RenderTest {
   @Test
   public void boxesAreConnectedWithALine() throws Exception {
      Box box1 = new Box(20, 20);
      Box box2 = new Box(20, 20);
      box1.connectTo(box2);

      Diagram diagram = new Diagram();
      diagram.add(box1, new Point(10, 10));
      diagram.add(box2, new Point(40, 20));

      BufferedImage image = render(diagram);
      assertThat(colorAt(image, 19, 12), is(WHITE));
      assertThat(colorAt(image, 19, 13), is(WHITE));
      assertThat(colorAt(image, 20, 13), is(BLACK));
      assertThat(colorAt(image, 21, 13), is(BLACK));
      assertThat(colorAt(image, 22, 14), is(BLACK));
      assertThat(colorAt(image, 23, 14), is(BLACK));
      assertThat(colorAt(image, 24, 15), is(BLACK));
      assertThat(colorAt(image, 25, 15), is(BLACK));
      assertThat(colorAt(image, 26, 15), is(BLACK));
      assertThat(colorAt(image, 27, 16), is(BLACK));
      assertThat(colorAt(image, 28, 16), is(BLACK));
      assertThat(colorAt(image, 29, 17), is(BLACK));
      assertThat(colorAt(image, 30, 17), is(BLACK));
      assertThat(colorAt(image, 31, 17), is(WHITE));
      assertThat(colorAt(image, 31, 18), is(WHITE));
   }
}