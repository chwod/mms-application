public class BowlingGameTest {
   @Test
   public void perfectGame() throws Exception {
      roll(10, 12);                                 // magic
      assertThat(game.score(), is(equalTo(300)));   // numbers
   }
}