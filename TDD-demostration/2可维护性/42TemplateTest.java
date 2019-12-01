@Test
public void groupShouldContainTwoSupervisors() {
   List<Employee> all = group.list();
   List<Employee> employees = new ArrayList<Employee>(all);
   Iterator<Employee> i = employees.iterator();
   while (i.hasNext()) {
      Employee employee = i.next();
      if (!employee.isSupervisor()) {
         i.remove();
      }
   }
   assertEquals(2, employees.size());
}

@Test
public void groupShouldContainFiveNewcomers() {
   List<Employee> newcomers = new ArrayList<Employee>();
   for (Employee employee : group.list()) {
      DateTime oneYearAgo = DateTime.now().minusYears(1);
      if (employee.startingDate().isAfter(oneYearAgo)) {
         newcomers.add(employee);
      }
   }
   assertEquals(5, newcomers.size());
}