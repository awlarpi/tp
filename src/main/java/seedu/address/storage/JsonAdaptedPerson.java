package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.AppointmentList;
import seedu.address.model.person.Address;
import seedu.address.model.person.BirthDate;
import seedu.address.model.person.Email;
import seedu.address.model.person.MedicalReport;
import seedu.address.model.person.Name;
import seedu.address.model.person.Nric;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String nric;
    private final String birthDate;
    private final String address;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final JsonAdaptedMedicalReport medicalReport;
    private final JsonAdaptedAppointmentList appointmentList;
    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
            @JsonProperty("email") String email, @JsonProperty("nric") String nric,
            @JsonProperty("birthDate") String birthDate, @JsonProperty("address") String address,
            @JsonProperty("tags") List<JsonAdaptedTag> tags,
            @JsonProperty("medicalReport") JsonAdaptedMedicalReport medicalReport,
            @JsonProperty("appointmentList") JsonAdaptedAppointmentList appointmentList) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.nric = nric;
        this.birthDate = birthDate;
        this.address = address;
        this.medicalReport = medicalReport;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        this.appointmentList = appointmentList;
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        nric = source.getNric().value;
        birthDate = source.getBirthDate().toString();
        address = source.getAddress().value;
        tags.addAll(
                source.getTags().stream().map(JsonAdaptedTag::new).collect(Collectors.toList()));
        medicalReport = new JsonAdaptedMedicalReport(source.getMedicalReport());
        appointmentList = JsonAdaptedAppointmentList.fromModelType(source.getAppointmentList());
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        final List<Appointment> personAppointmentList = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (nric == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Nric.class.getSimpleName()));
        }
        if (!Nric.isValidNric(nric)) {
            throw new IllegalValueException(Nric.MESSAGE_CONSTRAINTS);
        }
        final Nric modelNric = new Nric(nric);

        if (birthDate == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, BirthDate.class.getSimpleName()));
        }
        if (!BirthDate.isValidBirthDate(birthDate)) {
            throw new IllegalValueException(BirthDate.MESSAGE_CONSTRAINTS);
        }
        final BirthDate modelBirthDate = new BirthDate(birthDate);

        if (address == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        final Set<Tag> modelTags = new HashSet<>(personTags);

        final MedicalReport modelMedicalReport = (medicalReport != null)
                ? medicalReport.toModelType()
                : new MedicalReport("None", "None", "None", "None");
        final AppointmentList modelAppointmentList = (appointmentList != null)
                ? appointmentList.toModelType()
                : new AppointmentList();
        return new Person(modelName, modelPhone, modelEmail, modelNric, modelBirthDate,
                modelAddress, modelTags, modelMedicalReport, modelAppointmentList);
    }
}
