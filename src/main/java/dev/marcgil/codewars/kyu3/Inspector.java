package dev.marcgil.codewars.kyu3;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <strong>Papers, Please</strong> is an indie video game where the player takes on the role of a border crossing
 * immigration officer in the fictional dystopian Eastern Bloc-like country of Arstotzka in the year 1982.
 * As the officer, the player must review each immigrant and returning citizen's passports and other
 * supporting paperwork against a list of ever-increasing rules using a number of tools and guides,
 * allowing in only those with the proper paperwork, rejecting those without all proper forms,
 * and at times detaining those with falsified information.
 * <p>
 * <h1>Objective</h1>
 * Your task is to create a constructor function (or class) and a set of instance methods to perform
 * the tasks of the border checkpoint inspection officer. The methods you will need to create are as follows:
 * <p>
 * <h3>Method: <code>receiveBulletin</code></h3>
 * Each morning you are issued an official bulletin from the Ministry of Admission.
 * This bulletin will provide updates to regulations and procedures and the name of a wanted criminal.
 * <p>
 * The bulletin is provided in the form of a <code>string</code>. It may include one or more of the following:
 * <p>
 * Updates to the list of nations (comma-separated if more than one) whose citizens may enter (begins empty, before the first bulletin):
 * <ul>
 * <li>example 1: Allow citizens of Obristan</li>
 * <li>example 2: Deny citizens of Kolechia, Republia</li>
 * </ul>
 * Updates to required documents
 * <ul>
 * <li>example 1: Foreigners require access permit</li>
 * <li>example 2: Citizens of Arstotzka require ID card</li>
 * <li>example 3: Workers require work pass</li>
 * </ul>
 * Updates to required vaccinations
 * <ul>
 * <li>example 1: Citizens of Antegria, Republia, Obristan require polio vaccination</li>
 * <li>example 2: Entrants no longer require tetanus vaccination</li>
 * </ul>
 * Update to a currently wanted criminal
 * <ul>
 * <li>example 1: Wanted by the State: Hubert Popovic</li>
 * </ul>
 * <h3>Method: <code>inspect</code></h3>
 * Each day, a number of entrants line up outside the checkpoint inspection booth to gain passage into Arstotzka.
 * The inspect method will receive an object representing each entrant's set of identifying documents.
 * This object will contain zero or more properties which represent separate documents.
 * Each property will be a string value. These properties may include the following:
 * <p>
 * Applies to all entrants:
 * passport
 * certificate_of_vaccination
 * Applies only to citizens of Arstotzka
 * ID_card
 * Applies only to foreigners:
 * access_permit
 * work_pass
 * grant_of_asylum
 * diplomatic_authorization
 * The inspect method will return a result based on whether the entrant passes or fails inspection:
 * <p>
 * Conditions for passing inspection
 * <p>
 * All required documents are present
 * There is no conflicting information across the provided documents
 * All documents are current (ie. none have expired) -- a document is considered expired if the
 * expiration date is November 22, 1982 or earlier
 * The entrant is not a wanted criminal
 * If a certificate_of_vaccination is required and provided, it must list the required vaccination
 * A "worker" is a foreigner entrant who has WORK listed as their purpose on their access permit
 * If entrant is a foreigner, a grant_of_asylum or diplomatic_authorization are acceptable in lieu
 * of an access_permit. In the case where a diplomatic_authorization is used, it must include Arstotzka
 * as one of the list of nations that can be accessed.
 * If the entrant passes inspection, the method should return one of the following string values:
 * <p>
 * If the entrant is a citizen of Arstotzka: Glory to Arstotzka.
 * If the entrant is a foreigner: Cause no trouble.
 * If the entrant fails the inspection due to expired or missing documents, or their
 * certificate_of_vaccination does not include the necessary vaccinations, return Entry denied:
 * with the reason for denial appended.
 * <p>
 * Example 1: Entry denied: passport expired.
 * Example 2: Entry denied: missing required vaccination.
 * Example 3: Entry denied: missing required access permit.
 * If the entrant fails the inspection due to mismatching information between documents
 * (causing suspicion of forgery) or if they're a wanted criminal,
 * return Detainment: with the reason for detainment appended.
 * <p>
 * If due to information mismatch, include the mismatched item. e.g.Detainment: ID number mismatch.
 * If the entrant is a wanted criminal: Detainment: Entrant is a wanted criminal.
 * NOTE: One wanted criminal will be specified in each daily bulletin, and must be detained when
 * received for that day only. For example, if an entrant on Day 20 has the same name as a criminal
 * declared on Day 10, they are not to be detained for being a criminal.
 * Also, if any of an entrant's identifying documents include the name of that day's wanted
 * criminal (in case of mismatched names across multiple documents), they are assumed to be the wanted criminal.
 * In some cases, there may be multiple reasons for denying or detaining an entrant.
 * For this exercise, you will only need to provide one reason.
 * <p>
 * If the entrant meets the criteria for both entry denial and detainment, priority goes to detaining.
 * For example, if they are missing a required document and are also a wanted criminal,
 * then they should be detained instead of turned away.
 * In the case where the entrant has mismatching information and is a wanted criminal,
 * detain for being a wanted criminal.
 * <h4>Test Example</h4>
 * <pre>
 * String bulletin = "Entrants require passport\n" +
 *     "Allow citizens of Arstotzka, Obristan";
 *
 * Inspector inspector = new Inspector();
 * inspector.receiveBulletin(bulletin);
 *
 * Map<String, String> entrant1 = new HashMap<>();
 * entrant1.put("passport", "ID#: GC07D-FU8AR\n" +
 *     "NATION: Arstotzka\n" +
 *     "NAME: Guyovich, Russian\n" +
 *     "DOB: 1933.11.28\n" +
 *     "SEX: M\n" +
 *     "ISS: East Grestin\n" +
 *     "EXP: 1983.07.10\n"
 * );
 *
 * inspector.inspect(entrant1); // "Glory to Arstotzka."
 * </pre>
 * <h4>Additional Notes</h4>
 * Inputs will always be valid.
 * There are a total of 7 countries: Arstotzka, Antegria, Impor, Kolechia, Obristan, Republia, and United Federation.
 * Not every single possible case has been listed in this Description;
 * use the test feedback to help you handle all cases.
 * The concept of this kata is derived from the video game of the same name, but it is not meant
 * to be a direct representation of the game.
 */
public class Inspector {

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy.MM.dd");
  private static final String ALLOW_CITIZENS_BULLETIN_START = "Allow citizens of ";
  private static final String DENY_CITIZENS_BULLETIN_START = "Deny citizens of ";

  private enum Nation {
    ARSTOTZKA, ANTEGRIA, IMPOR, KOLECHIA, OBRISTAN, REPUBLIA, UNITED_FEDERATION;

    public static Nation of(String nation) {
      return Nation.valueOf(nation.trim().replace(" ", "_").toUpperCase());
    }

    public boolean isForeigner() {
      return this != ARSTOTZKA;
    }
  }

  private enum DocumentType {
    PASSPORT, ID_CARD, ACCESS_PERMIT, WORK_PASS, GRANT_OF_ASYLUM, DIPLOMATIC_AUTHORIZATION, CERTIFICATE_OF_VACCINATION;

    public static DocumentType of(String document) {
      return DocumentType.valueOf(document.trim().replace(" ", "_").toUpperCase());
    }

    public String toPersonKey() {
      return switch (this) {
        case ID_CARD -> "ID_card";
        case PASSPORT, ACCESS_PERMIT, WORK_PASS,
             GRANT_OF_ASYLUM, DIPLOMATIC_AUTHORIZATION,
             CERTIFICATE_OF_VACCINATION -> this.name().toLowerCase();
      };
    }

    @Override
    public String toString() {
      return switch (this) {
        case ID_CARD -> "ID card";
        case PASSPORT, ACCESS_PERMIT, WORK_PASS,
             GRANT_OF_ASYLUM, DIPLOMATIC_AUTHORIZATION,
             CERTIFICATE_OF_VACCINATION -> this.name().toLowerCase().replaceAll("_", " ");
      };
    }
  }

  private final Set<Nation> allowedCitizensFrom = new HashSet<>();
  private final Map<Nation, Set<DocumentType>> requiredDocumentsByNation = new HashMap<>();
  private final Map<Nation, Set<String>> requiredVaccinationsByNation = new HashMap<>();
  private String wantedCriminal;

  public void receiveBulletin(String bulletin) {
    System.out.println("bulletin: " + bulletin);
    String[] updateStatements = bulletin.split("\n");
    for (String updateStatement : updateStatements) {
      if (updateStatement.startsWith(ALLOW_CITIZENS_BULLETIN_START) ||
          updateStatement.startsWith(DENY_CITIZENS_BULLETIN_START)) {
        updateCitizens(updateStatement);
      } else if (updateStatement.endsWith("vaccination")) {
        updateVaccinations(updateStatement);
      } else if (updateStatement.startsWith("Wanted")) {
        updateWantedCriminal(updateStatement);
      } else {
        updateRequiredDocuments(updateStatement);
      }
    }
  }

  private void updateCitizens(String updateStatement) {
    if (updateStatement.startsWith(ALLOW_CITIZENS_BULLETIN_START)) {
      String[] allowedNations = updateStatement.replaceFirst(ALLOW_CITIZENS_BULLETIN_START, "")
          .split(",");
      Arrays.stream(allowedNations).map(Nation::of)
          .forEach(allowedCitizensFrom::add);
    } else if (updateStatement.startsWith(DENY_CITIZENS_BULLETIN_START)) {
      String[] deniedNations = updateStatement.replaceFirst(DENY_CITIZENS_BULLETIN_START, "")
          .split(",");
      Arrays.stream(deniedNations).map(Nation::of)
          .forEach(allowedCitizensFrom::remove);
    }
  }

  private void updateVaccinations(String updateStatement) {
    Set<Nation> nations = extractNationsToUpdate(updateStatement);
    String vaccination = extractVaccinationToUpdate(updateStatement);
    if (updateStatement.contains("no longer require")) {
      nations.forEach(nation -> removeRequiredVaccination(nation, vaccination));
    } else if (updateStatement.contains("require")) {
      nations.forEach(nation -> addRequiredVaccination(nation, vaccination));
    }
  }

  private Set<Nation> extractNationsToUpdate(String updateStatement) {
    String initialWord = updateStatement.substring(0, updateStatement.indexOf(" "));
    return switch (initialWord) {
      case "Citizens" -> {
        String delimiter = updateStatement.contains("no longer") ? "no longer" : "require";
        String nations = updateStatement.replaceFirst("Citizens of", "").split(delimiter)[0];

        yield Arrays.stream(nations.split(","))
            .map(Nation::of)
            .collect(Collectors.toSet());
      }
      case "Foreigners", "Workers" ->
          Arrays.stream(Nation.values()).filter(Nation::isForeigner).collect(Collectors.toSet());
      case "Entrants" -> new HashSet<>(Arrays.asList(Nation.values()));
      default -> throw new IllegalStateException("Unexpected value: " + initialWord);
    };
  }

  private String extractVaccinationToUpdate(String updateStatement) {
    String wordBeforeVaccination = "require ";
    return updateStatement.substring(
        updateStatement.indexOf(wordBeforeVaccination) + wordBeforeVaccination.length(),
        updateStatement.indexOf(" vaccination"));
  }

  private void removeRequiredVaccination(Nation nation, String vaccination) {
    this.requiredVaccinationsByNation.merge(nation, new HashSet<>(List.of(vaccination)),
        (requiredVaccinations, nonRequiredVaccination) -> {
          requiredVaccinations.removeAll(nonRequiredVaccination);
          return requiredVaccinations;
        });
  }

  private void addRequiredVaccination(Nation nation, String vaccination) {
    this.requiredVaccinationsByNation.merge(nation, new HashSet<>(List.of(vaccination)),
        (requiredVaccinations, requiredVaccination) -> {
          requiredVaccinations.addAll(requiredVaccination);
          return requiredVaccinations;
        });
  }

  private void updateWantedCriminal(String update) {
    this.wantedCriminal = update.split(":")[1].trim();
  }

  private void updateRequiredDocuments(String updateStatement) {
    Set<Nation> nations = extractNationsToUpdate(updateStatement);
    DocumentType document = extractDocument(updateStatement);
    if (updateStatement.contains("no longer require")) {
      nations.forEach(nation -> removeRequiredDocument(nation, document));
    } else if (updateStatement.contains("require")) {
      nations.forEach(nation -> addRequiredDocument(nation, document));
    }
  }

  private DocumentType extractDocument(String update) {
    String wordBeforeDocument = "require ";
    return DocumentType.of(
        update.substring(update.indexOf(wordBeforeDocument) + wordBeforeDocument.length()));
  }

  private void removeRequiredDocument(Nation nation, DocumentType document) {
    this.requiredDocumentsByNation.merge(nation, new HashSet<>(List.of(document)),
        (requiredDocuments, nonRequiredDocument) -> {
          requiredDocuments.removeAll(nonRequiredDocument);
          return requiredDocuments;
        });
  }

  private void addRequiredDocument(Nation nation, DocumentType document) {
    this.requiredDocumentsByNation.merge(nation, new HashSet<>(List.of(document)),
        (requiredDocuments, requiredDocument) -> {
          requiredDocuments.addAll(requiredDocument);
          return requiredDocuments;
        });
  }

  public String inspect(Map<String, String> person) {
    System.out.println("person:");
    for (Entry<String, String> entry : person.entrySet()) {
      System.out.println(entry.getKey() + ": " + entry.getValue());
    }

    Passport passport = null;
    if (person.containsKey("passport")) {
      passport = Passport.of(person.get("passport"));
    }

    AccessPermit accessPermit = null;
    if (person.containsKey("access_permit")) {
      accessPermit = AccessPermit.of(person.get("access_permit"));
    }

    IDCard idCard = null;
    if (person.containsKey("ID_card")) {
      idCard = IDCard.of(person.get("ID_card"));
    }

    GrantOfAsylum grantOfAsylum = null;
    if (person.containsKey("grant_of_asylum")) {
      grantOfAsylum = GrantOfAsylum.of(person.get("grant_of_asylum"));
    }

    CertificateOfVaccination certificateOfVaccination = null;
    if (person.containsKey("certificate_of_vaccination")) {
      certificateOfVaccination = CertificateOfVaccination.of(
          person.get("certificate_of_vaccination"));
    }

    WorkPass workPass = null;
    if (person.containsKey("work_pass")) {
      if (accessPermit != null && accessPermit.getPurpose().equals("WORK")) {
        workPass = WorkPass.of(person.get("work_pass"));
      }
    }

    DiplomaticAuthorization diplomaticAuthorization = null;
    if (person.containsKey("diplomatic_authorization")) {
      diplomaticAuthorization = DiplomaticAuthorization.of(person.get("diplomatic_authorization"));
    }

    String validationMessage = validateInformationAcrossDocuments(passport, idCard, accessPermit,
        workPass, grantOfAsylum, diplomaticAuthorization, certificateOfVaccination);

    Nation nation = extractNation(passport, accessPermit, grantOfAsylum,
        diplomaticAuthorization);
    String name = extractName(passport, idCard, accessPermit, workPass, grantOfAsylum,
        diplomaticAuthorization, certificateOfVaccination);

    if (name != null && name.equalsIgnoreCase(wantedCriminal)) {
      return "Detainment: Entrant is a wanted criminal.";
    }

    if (validationMessage != null) {
      return validationMessage;
    }

    String expirationDateValidationMessage = validateExpirationDates(passport, accessPermit,
        workPass, grantOfAsylum);

    if (expirationDateValidationMessage != null) {
      return expirationDateValidationMessage;
    }

    if (nation == null && passport == null) {
      return "Entry denied: missing required passport.";
    }

    Set<DocumentType> presentDocuments = Arrays.stream(DocumentType.values())
        .filter(document -> person.containsKey(document.toPersonKey()))
        .collect(Collectors.toSet());
    for (DocumentType requiredDocument : requiredDocumentsByNation.getOrDefault(nation, Set.of())) {
      if (nation != null && nation.isForeigner()) {
        if (requiredDocument == DocumentType.ACCESS_PERMIT) {
          if (grantOfAsylum != null || diplomaticAuthorization != null) {
            continue;
          }
        } else if (requiredDocument == DocumentType.WORK_PASS) {
          if (accessPermit == null || !accessPermit.getPurpose().equals("WORK")) {
            continue;
          }
        }
      }

      if (!presentDocuments.contains(requiredDocument)) {
        return "Entry denied: missing required " + requiredDocument.toString() + ".";
      }
    }

    if (!allowedCitizensFrom.contains(nation)) {
      return "Entry denied: citizen of banned nation.";
    }

    Set<String> requiredVaccinationsByNation = this.requiredVaccinationsByNation.getOrDefault(
        nation, Set.of());
    if (!requiredVaccinationsByNation.isEmpty()) {
      if (certificateOfVaccination == null) {
        return "Entry denied: missing required certificate of vaccination.";
      } else if (!new HashSet<>(certificateOfVaccination.vaccines()).containsAll(
          requiredVaccinationsByNation)) {
        return "Entry denied: missing required vaccination.";
      }
    }

    if (nation.isForeigner() && diplomaticAuthorization != null) {
      if (!diplomaticAuthorization.getAccess().contains(Nation.ARSTOTZKA)) {
        return "Entry denied: invalid diplomatic authorization.";
      }
    }

    return nation.isForeigner() ? "Cause no trouble." : "Glory to Arstotzka.";
  }

  private String extractName(NameDocument... nameDocuments) {
    return Arrays.stream(nameDocuments).filter(Objects::nonNull)
        .map(NameDocument::name)
        .findFirst()
        .orElse(null);
  }

  private Nation extractNation(NationDocument... nationDocuments) {
    return Arrays.stream(nationDocuments).filter(Objects::nonNull)
        .map(NationDocument::getNation)
        .findFirst()
        .orElse(null);
  }

  private String validateExpirationDates(ExpiringDocument... documents) {
    return Arrays.stream(documents)
        .filter(Objects::nonNull)
        .filter(document -> !document.expirationDate().isAfter(LocalDate.of(1982, 11, 22)))
        .findFirst()
        .map(ExpiringDocument::getDocumentType)
        .map(DocumentType::toString)
        .map(documentName -> String.format("Entry denied: %s expired.", documentName))
        .orElse(null);
  }

  private String validateInformationAcrossDocuments(Document... documents) {
    if (Stream.of(documents).filter(Objects::nonNull)
        .filter(NameDocument.class::isInstance)
        .map(NameDocument.class::cast)
        .map(NameDocument::name)
        .distinct().count() > 1) {
      return "Detainment: name mismatch.";
    } else if (Stream.of(documents).filter(Objects::nonNull)
        .filter(IdDocument.class::isInstance)
        .map(IdDocument.class::cast)
        .map(IdDocument::id)
        .distinct().count() > 1) {
      return "Detainment: ID number mismatch.";
    } else if (Stream.of(documents).filter(Objects::nonNull)
        .filter(NationDocument.class::isInstance)
        .map(NationDocument.class::cast)
        .map(NationDocument::getNation)
        .distinct().count() > 1) {
      return "Detainment: nationality mismatch.";
    }
    return null;
  }

  private static Map<String, String> parseFields(String document) {
    return Arrays.stream(document.split("\n"))
        .map(field -> field.split(": "))
        .collect(Collectors.toMap(
            a -> a[0],  //key
            a -> a[1]   //value
        ));
  }

  private static String parseName(String name) {
    String[] names = name.split(", ");
    return names[1] + " " + names[0];
  }

  private interface Document {

    DocumentType getDocumentType();
  }

  private interface NationDocument extends Document {

    Nation getNation();
  }

  private interface NameDocument extends Document {

    String name();
  }

  private interface IdDocument extends Document {

    String id();
  }

  private interface ExpiringDocument extends Document {

    LocalDate expirationDate();
  }

  private static abstract class NationalDocument implements NationDocument, NameDocument,
      IdDocument, Document {

    protected final Nation nation;
    protected final String name;
    protected final String id;

    public NationalDocument(Nation nation, String name, String id) {
      this.nation = nation;
      this.name = name;
      this.id = id;
    }

    @Override
    public Nation getNation() {
      return nation;
    }

    @Override
    public String name() {
      return name;
    }

    @Override
    public String id() {
      return id;
    }
  }

  private static abstract class ExpiringNationalDocument extends NationalDocument implements
      ExpiringDocument {

    protected final LocalDate expirationDate;

    public ExpiringNationalDocument(Nation nation, String name, String id,
        LocalDate expirationDate) {
      super(nation, name, id);
      this.expirationDate = expirationDate;
    }

    @Override
    public LocalDate expirationDate() {
      return expirationDate;
    }
  }

  private static class Passport extends ExpiringNationalDocument {

    public static Passport of(String passport) {
      Map<String, String> passportFields = parseFields(passport);
      return new Passport(Nation.of(passportFields.get("NATION")),
          parseName(passportFields.get("NAME")), passportFields.get("ID#"),
          LocalDate.parse(passportFields.get("EXP"), DATE_TIME_FORMATTER));
    }

    public Passport(Nation nation, String name, String id, LocalDate expirationDate) {
      super(nation, name, id, expirationDate);
    }

    @Override
    public DocumentType getDocumentType() {
      return DocumentType.PASSPORT;
    }
  }

  private static class AccessPermit extends ExpiringNationalDocument {

    private final String purpose;

    public static AccessPermit of(String accessPermit) {
      Map<String, String> accessPermitFields = parseFields(accessPermit);
      return new AccessPermit(accessPermitFields, Nation.of(accessPermitFields.get("NATION")),
          parseName(accessPermitFields.get("NAME")), accessPermitFields.get("ID#"),
          LocalDate.parse(accessPermitFields.get("EXP"), DATE_TIME_FORMATTER));
    }

    public AccessPermit(Map<String, String> accessPermitFields, Nation nation, String name,
        String id, LocalDate expirationDate) {
      super(nation, name, id, expirationDate);
      this.purpose = accessPermitFields.get("PURPOSE");
    }

    public String getPurpose() {
      return purpose;
    }

    @Override
    public DocumentType getDocumentType() {
      return DocumentType.ACCESS_PERMIT;
    }
  }

  private static class GrantOfAsylum extends ExpiringNationalDocument {

    public static GrantOfAsylum of(String grantOfAsylum) {
      Map<String, String> grantOfAsylumFields = parseFields(grantOfAsylum);
      return new GrantOfAsylum(Nation.of(grantOfAsylumFields.get("NATION")),
          parseName(grantOfAsylumFields.get("NAME")), grantOfAsylumFields.get("ID#"),
          LocalDate.parse(grantOfAsylumFields.get("EXP"), DATE_TIME_FORMATTER));
    }

    public GrantOfAsylum(Nation nation, String name, String id, LocalDate expirationDate) {
      super(nation, name, id, expirationDate);
    }

    @Override
    public DocumentType getDocumentType() {
      return DocumentType.GRANT_OF_ASYLUM;
    }
  }

  private record IDCard(String name) implements NameDocument {

    public static IDCard of(String idCard) {
      Map<String, String> idCardFields = parseFields(idCard);
      return new IDCard(parseName(idCardFields.get("NAME")));
    }

    @Override
    public DocumentType getDocumentType() {
      return DocumentType.ID_CARD;
    }
  }

  private record WorkPass(String name, LocalDate expirationDate) implements ExpiringDocument,
      NameDocument {

    public static WorkPass of(String workPass) {
      Map<String, String> workPassFields = parseFields(workPass);
      return new WorkPass(parseName(workPassFields.get("NAME")),
          LocalDate.parse(workPassFields.get("EXP"), DATE_TIME_FORMATTER));
    }

    @Override
    public DocumentType getDocumentType() {
      return DocumentType.WORK_PASS;
    }
  }

  private static class DiplomaticAuthorization extends NationalDocument {

    private final List<Nation> access;

    public static DiplomaticAuthorization of(String diplomaticAuthorization) {
      Map<String, String> diplomaticAuthorizationFields = parseFields(diplomaticAuthorization);
      return new DiplomaticAuthorization(
          Nation.of(diplomaticAuthorizationFields.get("NATION")),
          parseName(diplomaticAuthorizationFields.get("NAME")),
          diplomaticAuthorizationFields.get("ID#"),
          Arrays.stream(diplomaticAuthorizationFields.get("ACCESS").split(","))
              .map(Nation::of)
              .toList());
    }

    public DiplomaticAuthorization(Nation nation, String name, String id, List<Nation> access) {
      super(nation, name, id);
      this.access = access;
    }

    public List<Nation> getAccess() {
      return access;
    }

    @Override
    public DocumentType getDocumentType() {
      return DocumentType.DIPLOMATIC_AUTHORIZATION;
    }
  }

  private record CertificateOfVaccination(String name, String id, List<String> vaccines) implements
      NameDocument, IdDocument {

    public static CertificateOfVaccination of(String certificateOfVaccination) {
      Map<String, String> certificateOfVaccinationFields = parseFields(certificateOfVaccination);
      return new CertificateOfVaccination(
          parseName(certificateOfVaccinationFields.get("NAME")),
          certificateOfVaccinationFields.get("ID#"),
          Arrays.stream(certificateOfVaccinationFields.get("VACCINES").split(","))
              .map(String::trim)
              .toList());
    }

    @Override
    public DocumentType getDocumentType() {
      return DocumentType.CERTIFICATE_OF_VACCINATION;
    }
  }

}
