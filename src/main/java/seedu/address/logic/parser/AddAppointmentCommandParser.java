package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPOINTMENT_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DOCTOR_NRIC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FROM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NRIC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TO;

import java.time.LocalDate;
import java.util.stream.Stream;

import seedu.address.commons.util.DateUtil;
import seedu.address.logic.commands.AddAppointmentCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.person.Nric;

/**
 * Parses input arguments and creates a new AddMedicalReportCommand object
 */
public class AddAppointmentCommandParser implements Parser<AddAppointmentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddMedicineUsageCommand
     * and returns an AddMedicineUsage object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddAppointmentCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NRIC, PREFIX_DOCTOR_NRIC,
                        PREFIX_APPOINTMENT_DESCRIPTION, PREFIX_FROM, PREFIX_TO);

        if (!arePrefixesPresent(argMultimap, PREFIX_NRIC, PREFIX_DOCTOR_NRIC,
                PREFIX_APPOINTMENT_DESCRIPTION, PREFIX_FROM, PREFIX_TO)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddAppointmentCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NRIC, PREFIX_DOCTOR_NRIC,
                PREFIX_APPOINTMENT_DESCRIPTION, PREFIX_FROM, PREFIX_TO);

        Nric nric = ParserUtil.parseNric(argMultimap.getValue(PREFIX_NRIC).get());
        String doctorNric = argMultimap.getValue(PREFIX_DOCTOR_NRIC).get();
        String appointmentDescription = argMultimap.getValue(PREFIX_APPOINTMENT_DESCRIPTION).get();
        LocalDate startDate = LocalDate.parse(argMultimap.getValue(PREFIX_FROM).get(),
                DateUtil.getDateFormatter());
        LocalDate endDate = LocalDate.parse(argMultimap.getValue(PREFIX_TO).get(),
                DateUtil.getDateFormatter());

        Appointment medicineUsage = new Appointment(
                doctorNric, appointmentDescription, startDate, endDate, nric.toString());

        return new AddAppointmentCommand(nric, medicineUsage);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
