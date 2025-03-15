import com.github.javafaker.Faker;
import com.storemgmt.Model.Db.DatabaseMaker;
import com.storemgmt.Model.Entity.Seller;
import com.storemgmt.Model.Service.SellerService;
import org.apache.log4j.Logger;

public class DataCreator {
        static Logger logger = Logger.getLogger(DataCreator.class);

    public static void main(String[] args) throws Exception {
//        DatabaseMaker.createDatabase();
        Seller seller = Seller
                .builder()
                .firstname("roozbeh")
                .lastname("yousefi")
                .username("Roozbehyo")
                .password("13790631")
                .build();

       new SellerService().save(seller);

        logger.info("anjam shod");
        logger.error("anjam nashod");

//        MemberService.remove(201);

//        System.out.println(MemberService.findByNameAndFamily("S", ""));

//        Faker faker = new Faker();
//
//
//        for (int i = 0; i < 200; i++) {
//            Member member = Member
//                    .builder()
//                    .name(faker.name().firstName())
//                    .family(faker.name().lastName())
//                    .birthDate(
//                            LocalDate.of(
//                                    faker.number().numberBetween(1970, 2020),
//                                    faker.number().numberBetween(1, 12),
//                                    faker.number().numberBetween(1, 20))
//                    )
//                    .username(faker.name().username())
//                    .password(faker.internet().password())
//                    .active(true)
//                    .build();
//            try{
//                MemberService.save(member);
//            }catch (Exception e){
//                System.out.println(e.getMessage());
//            }
//        }

        Faker faker = new Faker();

        /*List<Person> personList = PersonService.findAll();
        for (Person person : personList) {
//            System.out.println(person);
            person = Person.builder().id(person.getId()).build();

            MilitaryLicense militaryLicense = MilitaryLicense.builder()
                    .startMilitaryDate(
                            LocalDate.of(
                                    faker.number().numberBetween(1980, 2020),
                                    faker.number().numberBetween(1, 12),
                                    faker.number().numberBetween(1, 20))
                    )
                    .endMilitaryDate(
                            LocalDate.of(
                                    faker.number().numberBetween(1970, 2020),
                                    faker.number().numberBetween(1, 12),
                                    faker.number().numberBetween(1, 20))
                    )
                    .type(faker.options().option(MilitaryType.class))
                    .province(faker.options().option(Province.class))
                    .person(person)
                    .build();
            try {
                militaryLicense = militaryLicense.toBuilder().endMilitaryDate(militaryLicense.getStartMilitaryDate().plusYears(2))
                        .build();
                MilitaryLicenseService.save(militaryLicense);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }*/
 /*       for (int i = 0; i < 200; i++) {
            MilitaryLicense militaryLicense = MilitaryLicense.builder()
                    .startMilitaryDate(
                            LocalDate.of(
                                    faker.number().numberBetween(1970, 2020),
                                    faker.number().numberBetween(1, 12),
                                    faker.number().numberBetween(1, 20))
                    )
                    .endMilitaryDate(
                            LocalDate.of(
                                    faker.number().numberBetween(1970, 2020),
                                    faker.number().numberBetween(1, 12),
                                    faker.number().numberBetween(1, 20))
                    )

                    .build();
            try {
//                militaryLicense = MilitaryLicense.toBuild
                MilitaryLicenseService.save(militaryLicense);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }*/
    }
}
