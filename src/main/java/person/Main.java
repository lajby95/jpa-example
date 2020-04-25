package person;

import com.github.javafaker.Faker;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.ZoneId;

public class Main {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-example");

    private static Faker faker = new Faker();

    public static Person randomPerson(){
        Person p = new Person();
        Address a = new Address(
                faker.address().country(),
                faker.address().state(),
                faker.address().city(),
                faker.address().streetAddress(),
                faker.address().zipCode()
        );
        p.setAddress(a);
        p.setName(faker.name().fullName());
        p.setEmail(faker.internet().emailAddress());
        p.setProfession(faker.company().profession());

        System.out.println();

        p.setGender(faker.options().option(Person.Gender.values()));

        java.util.Date date = faker.date().birthday(22,45);
        java.time.LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        p.setDob(localDate);

        return p;

    }

    private static void createRandomPeople() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            for (int i = 0; i < 1000; i++) {
                em.persist(randomPerson());
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {
        createRandomPeople();
    }
}
