package util.pojo;

import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;

public class RandomPOJOGenerator {

    public static List<BankTransaction> generatePOJO(int amountToGenerate){
        Faker faker = new Faker();
        List<BankTransaction> randomTransactions = new ArrayList<>();

        if (amountToGenerate > 0){
            for (int i = 0; i < amountToGenerate; i++){

                String name = faker.name().firstName();
                String lastName = faker.name().lastName();
                int accountNumber = faker.number().numberBetween(1000000,9999999);
                double amount = faker.number().randomDouble(2, 1,10000);
                String email = faker.internet().emailAddress();
                boolean active = faker.bool().bool();
                String country = faker.address().country();
                String telephone = faker.phoneNumber().phoneNumber();

                BankTransaction newTransaction = new BankTransaction(
                        name, lastName, accountNumber, amount, email, active, country, telephone
                );
                randomTransactions.add(newTransaction);
                System.out.println(newTransaction);
                System.out.println(newTransaction.getEmail());
            }
        }
        return randomTransactions;
    }
}
