public class TestObjectSpace {
   private Ruby runtime;
   private ObjectSpace space;
   private IRubyObject string;
   private List<IRubyObject> fixnums;

   @Before
   public void setUp() throws Exception {
      runtime = Ruby.newInstance();
      space = new ObjectSpace();
      
      string = runtime.newString("hello");
      fixnums = new ArrayList<IRubyObject>() {{
         add(runtime.newFixnum(10));
         add(runtime.newFixnum(20));
         add(runtime.newFixnum(30));
      }};
   }
   
   @Test
   public void testObjectSpace() {
	   addTo(space, string);
	   addTo(space, fixnums);
	   
	   Iterator strings = space.interator(runtime.getString());
	   assertContainsExactly(strings, string);
   }
   
   private void addTo(ObjectSpace space, Object... values) {...}
   private void addTo(ObjectSpace space, List values) {...}
   
   private void assertContainsExactly(Iterator i, Object... values) {}
   private void assertContainsExactly(Iterator i, List values) {}
}