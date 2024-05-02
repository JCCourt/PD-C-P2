// Group 10 (2 People) however 100% of code in project written by (Jack Courtenay - SID: 22179753) because team member wouldn't communicate
package bankingapplication;

import java.util.Map;
import java.util.Scanner;

public class CurrencyExchange {

    private FileIO fileIO;
    private Map<String, Double> conversionRates;

    // Constructor to initialise FileIO and read conversion rates from file.
    public CurrencyExchange(String fileName) {
        this.fileIO = new FileIO(fileName);
        this.conversionRates = fileIO.readConversionRates();
    }

    public void calculateExchange() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Available currencies: AUD, JPY, EUR, GBP, USD \nEnter wanted currency code: ");

        String selectedCurrency = scanner.nextLine().toUpperCase();

        double exchangeRate = conversionRates.get(selectedCurrency);
        // Calculate the reverse exchange rate for converting back to the base currency strictly for information purposes for teller
        double reverseExchangeRate = 1 / exchangeRate;

        // Prompt the user to enter the amount to be converted
        System.out.print("Enter amount in NZD: ");
        double originalAmount = scanner.nextDouble();

        double convertedAmountBeforeFee = originalAmount * exchangeRate;
        // 5% bank fee which the bank would take if conversion were to take place.
        double bankFee = convertedAmountBeforeFee * 0.05;
        // Final amount after bank fee 
        double finalConvertedAmountAfterFee = convertedAmountBeforeFee - bankFee;

        // String and float conversions to display output, currencies use 4dp for common practice.
        System.out.printf("Exchange rate to %s: %.4f\n", selectedCurrency, exchangeRate);
        System.out.printf("Exchange rate back to NZD: %.4f\n", reverseExchangeRate);
        System.out.printf("Amount in %s before fees: %.2f\n", selectedCurrency, convertedAmountBeforeFee);
        System.out.printf("Amount received in %s after 5%% bank fee: %.2f\n", selectedCurrency, finalConvertedAmountAfterFee);
    }
}
