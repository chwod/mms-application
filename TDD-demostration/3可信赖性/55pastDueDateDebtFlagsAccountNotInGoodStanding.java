@Test
public void pastDueDateDebtFlagsAccountNotInGoodStanding() {
   // create a basic account
   Customer customer = new Customer();
   DeliquencyPlan deliquencyPlan= DeliquencyPlan.MONTHLY;
   Account account = new CorporateAccount(customer, deliquencyPlan);

   // register a debt that has adue date in the future
   Moneyamount = new Money("EUR", 1000);
   account.add(new Liability(customer, amount, Time.fromNow(1, DAYS)));

   // account should still be ingood standing
   assertTrue(account.inGoodStanding());

   // fast-forward past the due date
   Time.moveForward(1, DAYS);

   // account shouldn't be in good standing anymore
   assertFalse(account.inGoodStanding());
}