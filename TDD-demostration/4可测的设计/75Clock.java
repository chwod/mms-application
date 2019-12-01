public class Clock {
   private static final Clock singletonInstance = new Clock();

   // private constructor prevents instantiation from other classes
   private Clock() { }

   public static Clock getInstance() {
      return singletonInstance;
   }
}