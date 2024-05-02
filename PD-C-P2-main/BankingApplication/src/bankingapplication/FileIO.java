// Group 10 (2 People) however 100% of code in project written by (Jack Courtenay - SID: 22179753) because team member wouldn't communicate
package bankingapplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileIO {

    private String fileName;
    private String regularAccounts = "accounts.txt";
    private String foreignAccounts = "foreignaccounts.txt";
    private String currencies = "currencies.txt";

    public FileIO(String fileName) {
        this.fileName = fileName;
    }

    // *******************************************************************************
    // Using reader superclass that bufferedreader extends from to read text from .txt
    // ********************************************************************************
    // Read regular accounts file (Read #1)
    public Map<String, Double> readRegularAccounts() {
        Map<String, Double> accountBalances = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(regularAccounts))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    accountBalances.put(parts[0], Double.valueOf(parts[1]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return accountBalances;
    }

    // Read foreign accounts file (Read #2)
    public Map<String, Map<String, Double>> readForeignAccounts() {
        Map<String, Map<String, Double>> accountBalances = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(foreignAccounts))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 3 && parts.length % 2 == 1) {
                    String name = parts[0];
                    Map<String, Double> currencies = new HashMap<>();
                    for (int i = 1; i < parts.length; i += 2) {
                        currencies.put(parts[i], Double.parseDouble(parts[i + 1]));
                    }
                    accountBalances.put(name, currencies);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing balance to number: " + e.getMessage());
        }
        return accountBalances;
    }

    // Read currency conversion rates file (Read #3)
    public Map<String, Double> readConversionRates() {
        Map<String, Double> conversionRates = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(currencies))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                // Expects 2 parts, currency code and conversion rate
                if (parts.length == 2) {
                    String currencyCode = parts[0];
                    double rate = Double.parseDouble(parts[1]);
                    conversionRates.put(currencyCode, rate);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading conversion rates: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing conversion rate: " + e.getMessage());
        }
        return conversionRates;
    }

    // ******************************************************************************
    // Using writer that bufferedwriter extends from to write text to a output stream
    // *******************************************************************************
    // Write to regular accounts file (Write #1)
    public void addToRegularAccounts(String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(regularAccounts, true))) {
            writer.write(text);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error while writing to file: " + e.getMessage());
        }
    }

    // Overwrite data in regular accounts file (Write #2)
    public void overwriteRegularAccounts(Map<String, Double> accountBalances) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(regularAccounts, false))) {
            for (Map.Entry<String, Double> entry : accountBalances.entrySet()) {
                writer.write(entry.getKey() + " " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error while writing to file: " + e.getMessage());
        }
    }

    // Write to foreign accounts file (Write #3)
    public void addToForeignAccounts(String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(foreignAccounts, true))) {
            writer.write(text);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error while writing to file: " + e.getMessage());
        }
    }

    // Overwrite data in foreign accounts file (Write #4)
    public void overwriteForeignAccounts(Map<String, Account> accounts) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(foreignAccounts, false))) {
            for (Account account : accounts.values()) {
                if (account instanceof ForeignAccount) {
                    StringBuilder line = new StringBuilder(account.getName());
                    Map<String, Double> balances = ((ForeignAccount) account).getCurrencyBalances();
                    for (Map.Entry<String, Double> entry : balances.entrySet()) {
                        line.append(" ").append(entry.getKey()).append(" ").append(entry.getValue());
                    }
                    writer.write(line.toString());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error while writing to file: " + e.getMessage());
        }
    }
}
