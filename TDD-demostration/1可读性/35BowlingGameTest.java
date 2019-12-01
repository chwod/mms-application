public class BowlingGameTest {
   @Test
   public void perfectGame() throws Exception {
  //    roll(TEN_PINS, TWELVE_TIMES);
      roll(pins(10), times(12));
      assertThat(game.score(), is(equalTo(300)));
   }

   private int pins(int n) { return n; }
   private int times(int n) { return n; }
}