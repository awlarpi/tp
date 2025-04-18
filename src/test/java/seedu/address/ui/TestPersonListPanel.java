package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.commons.util.DateUtil.DATE_FORMATTER;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.AppointmentList;
import seedu.address.model.medicineusage.Dosage;
import seedu.address.model.medicineusage.MedicineName;
import seedu.address.model.medicineusage.MedicineUsage;
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
 * Contains tests for the {@code PersonListPanel} class.
 */
public class TestPersonListPanel {

    private PersonListPanel personListPanel;
    private ResultDisplay resultDisplay;

    @BeforeEach
    public void setUp() {
    }

    /**
     * Tests the {@code parsePersonData} method in {@code PersonListPanel} class.
     */
    @Test
    public void parsePersonData_validPerson_correctString() {
        MedicalReport medicalReport = new MedicalReport("None", "None", "None", "None");
        AppointmentList appointmentList = new AppointmentList();
        Set<Tag> tags = new HashSet<>();
        Person person = new Person(
                new Name("John Doe"),
                new Phone("12345678"),
                new Email("johndoe@example.com"),
                new Nric("S1234567A"),
                new BirthDate("05-05-1985"),
                new Address("123 Main St"),
                tags,
                medicalReport,
                appointmentList
        );

        String expected = "Name: John Doe\n"
                + "NRIC: S1234567A\n"
                + "Birthday: 05-05-1985\n"
                + "Age: 39\n\n"
                + "Phone: 12345678\n"
                + "Email: johndoe@example.com\n"
                + "Address: 123 Main St\n\n"
                + "Drug Allergies: None\n"
                + "Illnesses: None\n"
                + "Surgeries: None\n"
                + "Immunizations: None\n\n"
                + "Medicine Usages: \n"
                + "No medicine usages found\n"
                + "\n"
                + "Appointments: \n"
                + "No appointments found"
                + "\n";

        assertEquals(expected, PersonListPanel.parsePersonData(person));
    }

    /**
     * Tests the {@code parsePersonData} method in {@code PersonListPanel} class with non-empty MedicineUsage.
     */
    @Test
    public void parsePersonData_personWithMedicineUsages_correctString() {
        Set<Tag> tags = new HashSet<>();
        DateTimeFormatter formatter = DATE_FORMATTER;
        LocalDate startdate = LocalDate.parse("01-01-2020", formatter);
        LocalDate enddate = LocalDate.parse("31-12-2020", formatter);
        MedicalReport medicalReport = new MedicalReport("Peanuts", "Asthma", "Appendectomy", "Flu");

        // Use MedicineName and Dosage objects instead of raw strings
        ObservableList<MedicineUsage> medicineUsages = FXCollections.observableArrayList(
                new MedicineUsage(new MedicineName("Medicine1"), new Dosage("Dosage1"), startdate, enddate),
                new MedicineUsage(new MedicineName("Medicine2"), new Dosage("Dosage2"), startdate, enddate)
        );

        AppointmentList appointmentList = new AppointmentList();
        appointmentList.add(new Appointment("Injection",
                LocalDateTime.of(2025, 10, 1, 10, 30),
                LocalDateTime.of(2025, 10, 1, 11, 0),
                "S9876543G"));

        Person person = new Person(
                new Name("Jane Doe"),
                new Phone("87654321"),
                new Email("janedoe@example.com"),
                new Nric("S7654321B"),
                new BirthDate("05-05-1985"),
                new Address("456 Another St"),
                tags,
                medicalReport,
                appointmentList
        );

        // Add MedicineUsage to the MedicalReport
        medicalReport.add(medicineUsages.get(0));
        medicalReport.add(medicineUsages.get(1));

        String expected = "Name: Jane Doe\n"
                + "NRIC: S7654321B\n"
                + "Birthday: 05-05-1985\n"
                + "Age: 39\n\n"
                + "Phone: 87654321\n"
                + "Email: janedoe@example.com\n"
                + "Address: 456 Another St\n\n"
                + "Drug Allergies: Peanuts\n"
                + "Illnesses: Asthma\n"
                + "Surgeries: Appendectomy\n"
                + "Immunizations: Flu\n\n"
                + "Medicine Usages: \n"
                + "1: Medicine1, Dosage1, from 01-01-2020 to 31-12-2020\n"
                + "2: Medicine2, Dosage2, from 01-01-2020 to 31-12-2020\n"
                + "\n"
                + "Appointments: \n"
                + "1: Injection FROM 01-10-2025 10:30 TO 01-10-2025 11:00 [Not visited]"
                + "\n";

        assertEquals(expected, PersonListPanel.parsePersonData(person));
    }
}
