import java.util.Scanner;

public class PerfectVendingMachine {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Items and their details
        String[] items = {"Cola", "Chips", "Chocolate", "Juice"};
        double[] prices = {25, 15, 20, 30};
        int[] stock = {5, 5, 5, 5};

        // Coins available (denomination, count)
        int[][] coins = {
            {1, 50}, {2, 50}, {5, 30}, {10, 20}, {20, 10}, {50, 5}, {100, 5}
        };

        int revenue = 0;

        // Machine loop
        mainLoop:
        while (true) {
            System.out.println("\n=== VENDING MACHINE ===");
            for (int i = 0; i < items.length; i++) {
                System.out.println((i + 1) + ". " + items[i] + " - Rs." + prices[i] + " [Stock: " + stock[i] + "]");
            }
            System.out.println("0. Exit");
            System.out.print("Choose item: ");
            int choice = sc.nextInt();

            if (choice == 0) {
                System.out.println("Machine off. Revenue: Rs." + revenue);
                break;
            }
            if (choice < 1 || choice > items.length) {
                System.out.println("Invalid option.");
                continue;
            }

            int index = choice - 1;
            if (stock[index] == 0) {
                System.out.println("Out of stock.");
                continue;
            }

            // Coupon check (simple)
            double price = prices[index];
            System.out.print("Enter coupon (or NONE): ");
            String coupon = sc.next();
            if (coupon.equals("STUDENT10")) {
                price = price * 0.9;
                System.out.println("10% discount applied. New price: Rs." + price);
            } else {
                System.out.println("No discount.");
            }

            // Payment
            double balance = 0;
            while (balance < price) {
                System.out.println("Due: Rs." + (price - balance) + " | Balance: Rs." + balance);
                System.out.print("Insert coin/note (1,2,5,10,20,50,100) or 0 to cancel: ");
                int pay = sc.nextInt();

                if (pay == 0) {
                    System.out.println("Cancelled. Refunding Rs." + balance);
                    continue mainLoop;
                }

                // Simple validation
                int[] valid = {1, 2, 5, 10, 20, 50, 100};
                boolean ok = false;
                for (int v : valid) {
                    if (pay == v) {
                        ok = true;
                        break;
                    }
                }
                if (!ok) {
                    System.out.println("Invalid coin.");
                    continue;
                }
                balance += pay;
            }

            // Change
            double change = balance - price;
            if (change > 0) {
                if (!canGiveChange(change, coins)) {
                    System.out.println("Cannot give exact change. Refunding Rs." + balance);
                    continue;
                }
            }

            // Update
            stock[index]--;
            revenue += (int) price;

            // Receipt
            System.out.println("\n--- RECEIPT ---");
            System.out.println("Item: " + items[index]);
            System.out.println("Paid: Rs." + balance);
            System.out.println("Change: Rs." + change);
            System.out.println("---------------");

            // Low stock warning
            int total = 0;
            for (int s : stock) total += s;
            if (total < 5) {
                System.out.println("Warning: Low stock!");
            }
        }
        sc.close();
    }

    // Simple change calculation
    public static boolean canGiveChange(double change, int[][] coins) {
        int target = (int) Math.round(change);
        for (int i = coins.length - 1; i >= 0; i--) {
            int denom = coins[i][0];
            int count = coins[i][1];
            while (target >= denom && count > 0) {
                target -= denom;
                count--;
            }
        }
        return target == 0;
    }
}