package dev.marcgil.codewars.kyu3;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.*;
import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InspectorTest {

  // Person
  private static String passports = "passport";
  private static String certificate_of_vacciation = "certificate_of_vaccination";
  private static String work_pass = "work_pass";
  private static String access_permit = "access_permit";
  private static String ID_card = "ID_card";
  private static String diplomatic_authorization = "diplomatic_authorization";
  private static String grant_of_asylum = "grant_of_asylum";

  // Passport
  private static String ID = "ID#";
  private static String NATION = "NATION";
  private static String NAME = "NAME";
  private static String DOB = "DOB";
  private static String SEX = "SEX";
  private static String ISS = "ISS";
  private static String EXP = "EXP";
  private static String HEIGHT = "HEIGHT";
  private static String WEIGHT = "WEIGHT";

  // Access permit
  private static String PURPOSE = "PURPOSE";
  private static String DURATION = "DURATION";

  // Diplomatic Auth
  private static String ACCESS = "ACCESS";

  // Certificate of vacc
  private static String VACCINES = "VACCINES";

  // Work pass
  private static String FIELD = "FIELD";

  private Random rand = new Random();

  /***************
   * SOLUTION
   **************/
  private boolean workPassReq = false;
  private boolean accessPermitRequired = false;
  private boolean idCardRequired = false;
  private String criminal = "";
  private List<String> allowed = new ArrayList<>();
  private Map<String, String> vaccinations = new HashMap<>();
  private Predicate<String> expired = exp -> LocalDate.parse(exp.replaceAll("\\.", "-"))
      .isBefore(LocalDate.of(1982, 11, 23));
  private String homeNation = "Arstotzka";
  private List<String> foreignStates = Arrays.asList("Antegria", "Impor", "Kolechia", "Obristan",
      "Republia", "United Federation");
  private List<String> allStates = Stream.concat(Stream.of(homeNation), foreignStates.stream())
      .collect(Collectors.toList());
  private Map<String, BiConsumer<String, String>> criteria = new LinkedHashMap<>();

  {
    criteria.put("Entrants require passport", (z, x) -> {
    });
    criteria.put("Allow citizens of (.+)", processCitizens(true));
    criteria.put("Deny citizens of (.+)", processCitizens(false));
    criteria.put("Wanted by the State: (.+)",
        (disease, z) -> criminal = disease.replaceAll("(\\w+) (\\w+)", "$2, $1"));
    criteria.put("Foreigners require access permit", (x, z) -> accessPermitRequired = true);
    criteria.put("Citizens of Arstotzka require ID card", (x, z) -> idCardRequired = true);
    criteria.put("Foreigners require (.+?) vaccination",
        (disease, z) -> foreignStates.forEach(x -> vaccinations.put(x, disease)));
    criteria.put("Foreigners no longer require (.+?) vaccination",
        (disease, z) -> foreignStates.forEach(x -> vaccinations.put(x, "")));
    criteria.put("Entrants require (.+?) vaccination",
        (disease, z) -> allStates.forEach(x -> vaccinations.put(x, disease)));
    criteria.put("Entrants no longer require (.+?) vaccination",
        (disease, z) -> allStates.forEach(x -> vaccinations.put(x, "")));
    criteria.put("Citizens of (.+?) no longer require (.+?) vaccination",
        (states, disease) -> Stream.of(states.split(", ")).forEach(x -> vaccinations.put(x, "")));
    criteria.put("Citizens of (.+?) require (.+?) vaccination",
        (states, disease) -> Stream.of(states.split(", "))
            .forEach(x -> vaccinations.put(x, disease)));
    criteria.put("Workers require work pass", (x, z) -> workPassReq = true);
  }

  private BiConsumer<String, String> processCitizens(boolean allow) {
    return (txt, z) -> {
      List<String> states = Stream.of(txt.split(", ")).collect(Collectors.toList());
      if (allow) {
        allowed.addAll(states);
      } else {
        allowed = allowed.stream().filter(x -> !states.contains(x)).collect(Collectors.toList());
      }
    };
  }

  private boolean fieldsDontMatch(String... values) {
    return Stream.of(values).filter(Objects::nonNull).distinct().count() > 1;
  }

  private void receiveBulletin(String bulletin) {
    Stream.of(bulletin.split("\n"))
        .forEach(x -> {
          String key = criteria.keySet().stream().filter(x::matches).findFirst().get();
          Matcher m = Pattern.compile(key).matcher(x);
          while (m.find()) {
            criteria.get(key).accept(m.groupCount() >= 1 ? m.group(1) : "",
                m.groupCount() >= 2 ? m.group(2) : "");
          }
        });
  }

  private Map<String, Map<String, String>> processDocuments(Map<String, String> person) {
    Map<String, Map<String, String>> newPerson = new HashMap<>();

    person.forEach((a, b) -> {
      Map<String, String> value = new HashMap<>();
      Arrays.asList(b.split("\n")).forEach(x -> value.put(x.substring(0, x.indexOf(':')),
          x.substring(x.indexOf(':') + 2, x.length())));
      newPerson.put(a, value);
    });

    return newPerson;
  }

  private String[] inspect(Map<String, String> person, String userSol) {
    Map<String, Map<String, String>> p = processDocuments(person);
    Map<String, String> passport = p.get(passports);
    Map<String, String> vaccCert = p.get(certificate_of_vacciation);
    Map<String, String> workPass = p.get(work_pass);
    Map<String, String> accessPermit = p.get(access_permit);
    Map<String, String> idCard = p.get(ID_card);
    Map<String, String> dipAuth = p.get(diplomatic_authorization);
    Map<String, String> grantOfAsylum = p.get(grant_of_asylum);

    List<String> detainment = new ArrayList<>();
    List<String> denied = new ArrayList<>();

    if (Stream.of(passport == null ? null : passport.get(NAME),
        vaccCert == null ? null : vaccCert.get(NAME), workPass == null ? null : workPass.get(NAME),
        accessPermit == null ? null : accessPermit.get(NAME),
        idCard == null ? null : idCard.get(NAME), dipAuth == null ? null : dipAuth.get(NAME),
        grantOfAsylum == null ? null : grantOfAsylum.get(NAME)).anyMatch(criminal::equals)) {
      return userSol.equals("Detainment: Entrant is a wanted criminal.") ? new String[]{userSol,
          userSol} : new String[]{"Detainment: Entrant is a wanted criminal.", userSol};
    }

    // Mismatched ID
    if (fieldsDontMatch(passport == null ? null : passport.get(ID),
        vaccCert == null ? null : vaccCert.get(ID),
        accessPermit == null ? null : accessPermit.get(ID),
        dipAuth == null ? null : dipAuth.get(ID),
        grantOfAsylum == null ? null : grantOfAsylum.get(ID))) {
      detainment.add("Detainment: ID number mismatch.");
    }

    // Mismatched Name
    if (fieldsDontMatch(passport == null ? null : passport.get(NAME),
        vaccCert == null ? null : vaccCert.get(NAME),
        workPass == null ? null : workPass.get(NAME),
        accessPermit == null ? null : accessPermit.get(NAME),
        idCard == null ? null : idCard.get(NAME),
        dipAuth == null ? null : dipAuth.get(NAME),
        grantOfAsylum == null ? null : grantOfAsylum.get(NAME))) {
      detainment.add("Detainment: name mismatch.");
    }

    // Mismatched Nationality
    if (fieldsDontMatch(passport == null ? null : passport.get(NATION),
        accessPermit == null ? null : accessPermit.get(NATION),
        dipAuth == null ? null : dipAuth.get(NATION),
        grantOfAsylum == null ? null : grantOfAsylum.get(NATION))) {
      detainment.add("Detainment: nationality mismatch.");
    }

    if (detainment.size() > 0) {
      return detainment.contains(userSol) ? new String[]{userSol, userSol}
          : new String[]{detainment.get(rand.nextInt(detainment.size())), userSol};
    }

    // Missing passport
    if (passport == null) {
      denied.add("Entry denied: missing required passport.");
    }

    // Passport expired
    if (passport != null && expired.test(passport.get(EXP))) {
      denied.add("Entry denied: passport expired.");
    }

    // Banned nation
    if (passport != null && !allowed.contains(passport.get(NATION))) {
      denied.add("Entry denied: citizen of banned nation.");
    }

    // Missing access permit
    if (accessPermitRequired && passport != null && !passport.get(NATION).equals(homeNation)
        && (accessPermit == null && grantOfAsylum == null && dipAuth == null)) {
      denied.add("Entry denied: missing required access permit.");
    }

    // Access permit expired
    if (accessPermit != null && expired.test(accessPermit.get(EXP))) {
      denied.add("Entry denied: access permit expired.");
    }

    // Grant_of_asylum expired
    if (grantOfAsylum != null && expired.test(grantOfAsylum.get(EXP))) {
      denied.add("Entry denied: grant of asylum expired.");
    }

    // Invalid diplomatic_authorization
    if (dipAuth != null && !dipAuth.get(ACCESS).contains(homeNation)) {
      denied.add("Entry denied: invalid diplomatic authorization.");
    }

    // Missing ID_card
    if (idCardRequired && passport != null && passport.get(NATION).equals(homeNation)
        && idCard == null) {
      denied.add("Entry denied: missing required ID card.");
    }

    if (accessPermit != null && accessPermit.get(PURPOSE).equals("WORK"))
    // Missing work pass
    {
      if (workPassReq && passport != null && !passport.get(NATION).equals(homeNation)
          && workPass == null) {
        denied.add("Entry denied: missing required work pass.");
      }
    }
    // Expired work pass
    if (workPass != null && expired.test(workPass.get(EXP))) {
      denied.add("Entry denied: work pass expired.");
    }

    // Missing required vaccinations
    if (passport != null) {
      // Missing required vaccinations
      String vaccine = vaccinations.entrySet().stream()
          .filter(x -> x.getKey().equals(passport.get(NATION))).map(Map.Entry::getValue).findFirst()
          .orElse("");
      if (!vaccine.isEmpty() && vaccCert == null) {
        denied.add("Entry denied: missing required certificate of vaccination.");
      }
      if (!vaccine.isEmpty() && vaccCert != null && !vaccCert.get(VACCINES).contains(vaccine)) {
        denied.add("Entry denied: missing required vaccination.");
      }
    }

    if (denied.size() > 0) {
      return denied.contains(userSol) ? new String[]{userSol, userSol}
          : new String[]{denied.get(rand.nextInt(denied.size())), userSol};
    }

    return new String[]{passport.get(NATION).equals(homeNation)
        ? "Glory to " + homeNation + "."
        : "Cause no trouble.", userSol};
  }

  /***************
   * END SOLUTION
   **************/

  private List<String> randomPick(List<String> arr, int maxAmount) {
    return arr
        .stream()
        .sorted((a, b) -> rand.nextInt(3) - 1)
        .limit(maxAmount)
        .collect(Collectors.toList());
  }

  private int rand01(double n) {
    return (int) Math.min(1, Math.round((rand.nextDouble() * n)));
  }

  private int rand01() {
    return rand01(1);
  }

  private String lastNameGen() {
    String[] names = {"Aji", "Anderson", "Andrevska", "Atreides", "Babayev", "Baryshnikova",
        "Bennet", "Bergman", "Blanco", "Borg", "Borshiki", "Bosch", "Bullock", "Burke", "Carlstrom",
        "Chernovski", "Conrad", "Costa", "Costanzo", "Crechiolo", "Czekowicz", "Dahl", "David",
        "DeGraff", "Diaz", "Dimanishki", "Dimitrov", "Dvorkin", "Evans", "Feyd", "Fischer", "Fisk",
        "Fonseca", "Frank", "Frederikson", "Graham", "Grech", "Gregorovich", "Gruben", "Guillot",
        "Hammacher", "Hammerstein", "Hansson", "Harkonnen", "Haas", "Hassad", "Heintz",
        "Henriksson", "Hertzog", "Ibrahimovic", "Jacobs", "Jager", "Jensen", "Johannson", "Jokav",
        "Jordan", "Jovanovic", "Jung", "Kaczynska", "Karlsson", "Karnov", "Kerr", "Khan",
        "Kierkgaard", "Kirsch", "Klass", "Klaus", "Kleiner", "Knapik", "Kostovetsky", "Kovacs",
        "Kowalska", "Kravitz", "Kreczmanski", "Kremenliev", "Krug", "Lang", "Larsen", "Latva",
        "Leonov", "Levine", "Lewandowski", "Li", "Lima", "Lindberg", "Lovska", "Lukowski",
        "Lundberg", "Maars", "Macek", "Malkova", "Mateo", "Medici", "Michaelson", "Mikkelson",
        "Moldavich", "Muller", "Murphy", "Newman", "Nilsson", "Nityev", "Novak", "Odom", "Olah",
        "Ortiz", "Owsianka", "Pai", "Pearl", "Pejic", "Peterson", "Petrova", "Popovic", "Praskovic",
        "Quinn", "Rabban", "Radic", "Ramos", "Rasmussen", "Reed", "Reichenbach", "Reyes", "Roberts",
        "Romanoff", "Romanov", "Romanowski", "Rosebrova", "Rosenfeld", "Sajarvi", "Savelle",
        "Schneider", "Schroder", "Schulz", "Schumer", "Seczek", "Shaw", "Smirnov", "Sorenson",
        "Sousa", "Spektor", "Stanislov", "Steinberg", "Steiner", "Stolichnaya", "Stoyakovich",
        "Strauss", "Thunstrom", "Tjell", "Tolaj", "Tsarnaeva", "Vazquez", "Vaughn", "Vincenza",
        "Vyas", "Wagner", "Watson", "Weiss", "Weisz", "Wojcik", "Wolfe", "Xavier", "Yankov",
        "Young", "Zajak", "Zeitsoff", "Zhang", "Zitna"};
    return names[rand.nextInt(names.length)];
  }

  private String firstNameGen(int rn) {
    String[] male = {"Aaron", "Abdullah", "Adam", "Adamik", "Ahmad", "Aidan", "Alek", "Aleksander",
        "Aleksandr", "Aleksi", "Alfred", "Andre", "Andrej", "Andrew", "Anton", "Aron", "Artour",
        "Attila", "Azeem", "Benito", "Bernard", "Borek", "Boris", "Bruno", "Calum", "Cesar",
        "Christoph", "Claude", "Cosmo", "Damian", "Danil", "David", "Dimitry", "Dominik", "Eduardo",
        "Emil", "Erik", "Evgeny", "Felipe", "Frederic", "Fyodor", "Gaston", "Giovanni", "Gregor",
        "Gregory", "Gunther", "Gustav", "Guy", "Hayden", "Hector", "Henrik", "Hubert", "Hugo",
        "Ibrahim", "Igor", "Isaak", "Ivan", "Jakob", "James", "Jan", "Javier", "Joachim", "Johann",
        "Jonathan", "Jorge", "Josef", "Joseph", "Julio", "Karl", "Khalid", "Konstantine", "Kristof",
        "Kristofer", "Lars", "Lazlo", "Leonid", "Luis", "Lukas", "Maciej", "Marcel", "Marco",
        "Martin", "Mathias", "Matthew", "Michael", "Mikhail", "Mikkel", "Mohammed", "Nikolai",
        "Nico", "Nicolai", "Niel", "Nikolas", "Olec", "Omid", "Otto", "Pablo", "Patrik", "Pavel",
        "Peter", "Petr", "Petros", "Piotr", "Pyotr", "Rafal", "Rasmus", "Rikardo", "Robert",
        "Roman", "Romeo", "Samuel", "Sasha", "Sebastian", "Sergei", "Sergey", "Simon", "Stanislav",
        "Stefan", "Sven", "Tomas", "Tomasz", "Vadim", "Vanya", "Vasily", "Viktor", "Vilhelm",
        "Vincent", "Vlad", "Vladimir", "Werner", "William", "Yuri", "Yosef", "Zachary"};
    String[] female = {"Ada", "Adriana", "Agnes", "Alberta", "Aleksandra", "Alexa", "Alexis",
        "Amalie", "Ana", "Anastasia", "Anita", "Anna", "Antonia", "Anya", "Augustine", "Ava",
        "Beatrix", "Brenna", "Cameron", "Carmen", "Cassandra", "Cecelia", "Cheyenne", "Christina",
        "Daniela", "Danika", "Edine", "Ekaterina", "Eleanor", "Elena", "Elizabeth", "Emily", "Emma",
        "Erika", "Eva", "Felicia", "Freja", "Gabriela", "Gabrielle", "Galina", "Georgia", "Gloria",
        "Greta", "Hanna", "Heidi", "Helga", "Ilya", "Ingrid", "Isabella", "Ivana", "Ivanka",
        "Jennifer", "Jessica", "Joanna", "Josefina", "Josephine", "Julia", "Juliette", "Kamala",
        "Karin", "Karina", "Kascha", "Katarina", "Katherine", "Katrina", "Kristen", "Kristina",
        "Laura", "Lena", "Liliana", "Lisa", "Lorena", "Lydia", "Malva", "Maria", "Marina", "Martha",
        "Martina", "Michaela", "Michelle", "Mikaela", "Mila", "Misha", "Nada", "Nadia", "Naomi",
        "Natalia", "Natalya", "Natasha", "Nicole", "Nikola", "Nina", "Olga", "Paulina", "Petra",
        "Rachel", "Rebeka", "Renee", "Roberta", "Rozsa", "Samantha", "Sara", "Sarah", "Sharona",
        "Simone", "Sofia", "Sonja", "Sophia", "Stefani", "Svetlana", "Tatiana", "Tatyana", "Teresa",
        "Valentina", "Vanessa", "Victoria", "Viktoria", "Wilma", "Yelena", "Yulia", "Yvonna",
        "Zera", "Zoe"};

    return new String[][]{male, female}[rn][rand.nextInt(
        new int[]{male.length, female.length}[rn])];
  }

  private String[] nationGen() {
    String[] nations = {"Antegria", "Arstotzka", "Impor", "Kolechia", "Obristan", "Republia",
        "United Federation"};
    String[] antegria = {"St. Marmero", "Glorian", "Outer Grouse"};
    String[] arstotzka = {"Orvech Vonor", "East Grestin", "Paradizna"};
    String[] impor = {"Enkyo", "Haihan", "Tsunkeido"};
    String[] kolechia = {"Yurko City", "Vedor", "West Grestin"};
    String[] obristan = {"Skal", "Lorndaz", "Mergerous"};
    String[] republia = {"True Glorian", "Lesrenadi", "Bostan"};
    String[] unitedFederation = {"Great Rapid", "Shingleton", "Korista City"};

    String nation = nations[rand.nextInt(nations.length)];
    String[] iss = new String[]{};
    String[] returnVal = new String[2];
    returnVal[0] = nation;

    switch (nation) {
      case "Antegria":
        iss = antegria;
        break;
      case "Arstotzka":
        iss = arstotzka;
        break;
      case "Impor":
        iss = impor;
        break;
      case "Kolechia":
        iss = kolechia;
        break;
      case "Obristan":
        iss = obristan;
        break;
      case "Republia":
        iss = republia;
        break;
      case "United Federation":
        iss = unitedFederation;
        break;
    }

    returnVal[1] = iss[rand.nextInt(iss.length)];

    return returnVal;
  }

  private String idNumGen() {
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    Supplier<String> seqGen = () -> IntStream.range(0, 5)
        .mapToObj(x -> Character.toString(chars.charAt(rand.nextInt(x == 0 ? 26 : chars.length()))))
        .collect(Collectors.joining());
    return seqGen.get() + "-" + seqGen.get();
  }

  private DateTimeFormatter df = DateTimeFormatter.ofPattern("YYYY.MM.dd");

  private String genDate(int minYear, int minMonth, int minDayInMonth, int maxYear, int maxMonth,
      int maxDayInMonth) {
    LocalDate start = LocalDate.of(minYear, minMonth, minDayInMonth);
    LocalDate end = LocalDate.of(maxYear, maxMonth, maxDayInMonth);
    long days = ChronoUnit.DAYS.between(start, end);
    return start.plusDays(rand.nextInt((int) days + 1)).format(df);
  }

  private String dobGen() {
    return genDate(1914, 1, 1, 1914 + 48, 12, 31);
  }

  private String expireGen() {
    return genDate(1982, 11, 23, 1985, 12, 31);
  }

  private String invExpireGen() {
    return genDate(1981, 1, 1, 1982, 11, 22);
  }

  private Map<String, String> passportGen() {
    String[] nation = nationGen();
    int pct = 1 + rand.nextInt(100);
    int n = rand01();

    Map<String, String> passport = new HashMap<>();
    passport.put(ID, idNumGen());
    passport.put(NAME, lastNameGen() + ", " + firstNameGen(n));
    passport.put(DOB, dobGen());
    passport.put(SEX, Character.toString("MF".charAt(n)));
    passport.put(ISS, nation[1]);
    passport.put(EXP, expireGen());
    passport.put(NATION, nation[0]);
    passport.put(WEIGHT, "" + (40 + Math.floor(65 * pct / 100)) + "kg");
    passport.put(HEIGHT, "" + (145 + Math.floor(45 * pct / 100)) + "cm");
    return passport;
  }

  private String passportStr(Map<String, String> passport) {
    List<String> strPass = new ArrayList<>();
    passport.entrySet().stream()
        .filter(x -> !x.getKey().equals(HEIGHT) && !x.getKey().equals(WEIGHT)).forEach(x -> {
          strPass.add(x.getKey());
          strPass.add(x.getValue());
        });
    return makeDocumentString(strPass.toArray(new String[0]));
  }

  private String makeDocumentString(String... values) {
    List<String> document = new ArrayList<>();

    for (int i = 0; i < values.length; i += 2) {
      document.add(values[i] + ": " + values[i + 1]);
    }

    return document.stream().collect(Collectors.joining("\n"));
  }

  private String certVaccGen(Map<String, String> passport, String vx) {
    Stream<String> vaccines = Stream.of("polio", "HPV", "cowpox", "tetanus", "typhus",
        "yellow fever", "cholera", "rubella", "hepatitis B", "measles", "tuberculosis");
    int valid = rand01(0.72);

    return makeDocumentString(
        NAME, passport.get(NAME),
        ID, passport.get(ID),
        VACCINES, Stream.concat((valid == 0 ? Stream.of(vx) : Stream.empty()),
            randomPick(vaccines.filter(x -> !x.equals(vx)).collect(Collectors.toList()),
                2 + valid).stream()).collect(Collectors.joining(", "))
    );
  }

  private String idCardGen(Map<String, String> passport) {
    return makeDocumentString(
        NAME, passport.get(NAME),
        DOB, passport.get(DOB),
        HEIGHT, passport.get(HEIGHT),
        WEIGHT, passport.get(WEIGHT)
    );
  }

  private String workPassGen(Map<String, String> passport) {
    String[] vocations = {"Accounting", "Agriculture", "Architecture", "Aviation", "Construction",
        "Dentistry", "Drafting", "Engineering", "Fine arts", "Fishing", "Food service",
        "General labor", "Healthcare", "Manufacturing", "Research", "Sports", "Statistics",
        "Surveying"};

    return makeDocumentString(
        NAME, passport.get(NAME),
        FIELD, vocations[rand.nextInt(vocations.length)],
        EXP, (rand01(0.68) == 0 ? expireGen() : invExpireGen())
    );
  }

  private String[] entryDataGen() {
    String[] purposes = {"TRANSIT", "VISIT", "WORK", "IMMIGRATE"};
    List<String> timeVars = Arrays.asList("2 DAYS", "14 DAYS", "1 MONTH", "2 MONTHS", "3 MONTHS",
        "6 MONTHS", "1 YEAR", "FOREVER");

    String purpose = purposes[rand.nextInt(purposes.length)];
    List<String> time = new ArrayList<>();

    switch (purpose) {
      case "TRANSIT":
        time = timeVars.subList(0, 2);
        break;
      case "VISIT":
        time = timeVars.subList(1, 5);
        break;
      case "WORK":
        time = timeVars.subList(2, 7);
        break;
      case "IMMIGRATE":
        time = timeVars.subList(7, 8);
        break;
    }

    return new String[]{purpose, time.get(rand.nextInt(time.size()))};
  }

  private String accessPermitGen(Map<String, String> passport) {
    Stream<String> nations = Stream.of("Antegria", "Impor", "Kolechia", "Obristan", "Republia",
        "United Federation");
    String[] entryData = entryDataGen();
    int rn = rand.nextInt(3);
    int addInvalidData = rand01(0.70);

    return makeDocumentString(
        NAME, passport.get(NAME),
        NATION, addInvalidData > 0 && rn == 0 ? nations.filter(x -> !x.equals(passport.get(NATION)))
            .collect(Collectors.toList()).get(rand.nextInt(5)) : passport.get(NATION),
        ID, addInvalidData > 0 && rn == 1 ? idNumGen() : passport.get(ID),
        PURPOSE, entryData[0],
        DURATION, entryData[1],
        HEIGHT, passport.get(HEIGHT),
        WEIGHT, passport.get(WEIGHT),
        EXP, addInvalidData > 0 && rn == 2 ? invExpireGen() : expireGen()
    );
  }

  private String diplomaticAuthGen(Map<String, String> passport) {
    List<String> nations = Arrays.asList("Antegria", "Impor", "Kolechia", "Obristan", "Republia",
        "United Federation");
    int rn01 = rand01(0.72);
    int rn = rand.nextInt(3) + 1;

    return makeDocumentString(
        NATION, passport.get(NATION),
        NAME, passport.get(NAME),
        ID, passport.get(ID),
        ACCESS, Stream.concat(Stream.of(rn01 == 0 ? "Arstotzka" : "").filter(x -> !x.isEmpty()),
            randomPick(nations, rn).stream()).collect(Collectors.joining(", "))
    );
  }

  private String asylumGrantGen(Map<String, String> passport) {
    return makeDocumentString(
        NAME, passport.get(NAME),
        NATION, passport.get(NATION),
        ID, passport.get(ID),
        DOB, passport.get(DOB),
        HEIGHT, passport.get(HEIGHT),
        WEIGHT, passport.get(WEIGHT),
        EXP, rand01(0.72) == 0 ? expireGen() : invExpireGen()
    );
  }

  private Map<String, String> equipEntrant(Map<String, String> passport, int wantedCriminal) {
    Map<String, String> person = new HashMap<>();

    if (rand.nextInt(20) > 0 || wantedCriminal > 0) {
      if (rand.nextInt(7) == 0) {
        passport.put(EXP, invExpireGen());
      }
      person.put(passports, passportStr(passport));
    }
    if (passport.get(NATION).equals(homeNation)) {
      if (idCardRequired && rand.nextInt(7) > 0) {
        if (rand.nextInt(9) > 0) {
          person.put(ID_card, idCardGen(passport));
        } else {
          passport.put(NAME,
              (lastNameGen() + ", " + firstNameGen("MF".indexOf(passport.get(SEX)))));
          person.put(ID_card, idCardGen(passport));
        }
      }
    } else if (accessPermitRequired && rand.nextInt(14) > 0) {
      if (rand.nextInt(7) > 0) {
        person.put(access_permit, accessPermitGen(passport));
        if (person.get(access_permit).contains("WORK") && rand.nextInt(10) > 0) {
          person.put(work_pass, workPassGen(passport));
        }
      } else {
        if (rand01() == 0) {
          person.put(diplomatic_authorization, diplomaticAuthGen(passport));
        } else {
          person.put(grant_of_asylum, asylumGrantGen(passport));
        }
      }
    }

    String vaccine = vaccinations.entrySet().stream()
        .filter(x -> x.getKey().equals(passport.get(NATION))).map(Map.Entry::getValue).findFirst()
        .orElse("");

    if (!vaccine.isEmpty() && rand.nextInt(10) > 0) {
      person.put(certificate_of_vacciation, certVaccGen(passport, vaccine));
    }

    return person;
  }

  @Test
  public void thirtyOneDaysOfService() {
    List<String> foreignNations = Stream.of("Antegria", "Impor", "Kolechia", "Obristan", "Republia",
        "United Federation").collect(Collectors.toList());

    Stack<Map<String, String>> wantedEntrants = new Stack<>();
    while (wantedEntrants.size() < 32) {
      Map<String, String> wc = passportGen();
      while (wantedEntrants.contains(wc)) {
        wc = passportGen();
      }
      wantedEntrants.push(wc);
    }
    Predicate<Map<String, String>> isWanted = x -> wantedEntrants.stream()
        .anyMatch(y -> y.get(NAME).equals(x.get(NAME)));
    Supplier<Map<String, String>> getWanted = wantedEntrants::pop;

    List<List<String>> entrantsNatList = new ArrayList<>();
    String bannedNat = "";
    for (int i = 0; i < 28; i++) {
      List<String> allowBan =
          i > 0 ? Stream.of("Allow citizens of " + bannedNat).collect(Collectors.toList())
              : new ArrayList<>();
      List<String> tempNat = new ArrayList<>(foreignNations);
      tempNat.remove(bannedNat);
      bannedNat = tempNat.get(rand.nextInt(tempNat.size()));
      allowBan.add("Deny citizens of " + bannedNat);
      entrantsNatList.add(allowBan);
    }

    List<List<String>> bulletinSchedule = new ArrayList<>();
    bulletinSchedule.add(Stream.of("Entrants require passport", "Allow citizens of Arstotzka")
        .collect(Collectors.toList()));
    bulletinSchedule.add(
        Stream.of("Allow citizens of " + foreignNations.stream().collect(Collectors.joining(", ")))
            .collect(Collectors.toList()));
    bulletinSchedule.add(
        Stream.of("Foreigners require access permit").collect(Collectors.toList()));
    bulletinSchedule.add(
        Stream.of("Citizens of Arstotzka require ID card", entrantsNatList.get(0).get(0))
            .collect(Collectors.toList()));
    bulletinSchedule.addAll(entrantsNatList);

    List<String> vaccList = randomPick(
        Arrays.asList("polio", "HPV", "cowpox", "tetanus", "typhus", "yellow fever", "cholera",
            "rubella", "hepatitis B", "measles", "tuberculosis"), 7);
    List<String> entrantsVac = new ArrayList<>();

    for (int i = 0; i < vaccList.size(); i++) {
      int selDay = 3 + (i * 4 + rand.nextInt(3));
      if (i > 0) {
        bulletinSchedule.get(selDay).add(
            format("%s no longer require %s vaccination", entrantsVac.get(0), entrantsVac.get(1)));
      }
      entrantsVac = Arrays.asList(
          new String[]{"Entrants", "Foreigners",
              "Citizens of " + randomPick(foreignNations, rand.nextInt(4) + 1).stream()
                  .collect(Collectors.joining(", "))}[rand.nextInt(3)],
          vaccList.get(i));
      bulletinSchedule.get(selDay)
          .add(format("%s require %s vaccination", entrantsVac.get(0), entrantsVac.get(1)));
    }

    bulletinSchedule.get(rand.nextInt(7) + 4).add("Workers require work pass");

    Iterator<List<String>> bulletin = bulletinSchedule.iterator();
    Supplier<List<String>> getBulletin = bulletin::next;

    // Actual 31 days of service
    Function<String, String> dayStr = n -> n + (n.matches("^(1|21)$") ? "st"
        : n.matches("^(2|22)$") ? "nd" : n.matches("^(3|23)$") ? "rd" : "th");
    LocalDate day = LocalDate.of(1982, 11, 23);
    int validPass = 0;

    Inspector inspector = new Inspector();

    for (int i = 0; i < 31; i++) {
      System.out.println(
          format("%s %s, %d", day.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH),
              dayStr.apply(Integer.toString(day.getDayOfMonth())), 1982));

      List<String> newBulletin = getBulletin.get();
      Map<String, String> wantedEntrant = getWanted.get();

      newBulletin.add("Wanted by the State: " + wantedEntrant.get(NAME)
          .replaceAll("^(\\w+), (\\w+)$", "$2 $1"));

      String bulletinStr = newBulletin.stream().collect(Collectors.joining("\n"));
      receiveBulletin(bulletinStr);

      List<Map<String, String>> entrantList = new ArrayList<>();

      // 11 non-criminals
      for (int j = 0; j < 11; j++) {
        Map<String, String> entrantPass = passportGen();
        Map<String, String> equippedEntrant;
        boolean validEntrant = false;

        while (isWanted.test(entrantPass)) {
          entrantPass = passportGen();
        }

        equippedEntrant = equipEntrant(entrantPass, 0);
        while (!validEntrant) {
          if (equippedEntrant.entrySet().stream()
              .allMatch(x -> x.getValue().contains(wantedEntrant.get(NAME)))) {
            equippedEntrant = equipEntrant(entrantPass, 0);
          } else {
            validEntrant = true;
          }
        }
        entrantList.add(equippedEntrant);
      }
      entrantList.add(rand.nextInt(11), equipEntrant(wantedEntrant, 1));

      inspector.receiveBulletin(bulletinStr);

      for (Map<String, String> person : entrantList) {
        String userSol = inspector.inspect(new HashMap<>(person));
        String[] trueSol = inspect(person, userSol);

        assertEquals(trueSol[0], trueSol[1]);

        if (trueSol[0].equals(trueSol[1])) {
          validPass++;
        }
      }

      if (validPass < 180 && i > 14) {
        break;
      }
      day = day.plusDays(1);
    }

    if (validPass < 372) {
      System.out.println(
          "You are under arrest for ineptitude in performing your duties. The penalty is forced labor. Glory to Arstotzka.");
    } else {
      System.out.println(
          "Well done, inspector. The Ministry of Admission has awarded you a plaque in recognition for Sufficience. Glory to Arstotzka.");
    }
  }
}
