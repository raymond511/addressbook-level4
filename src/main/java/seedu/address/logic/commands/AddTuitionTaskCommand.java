package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.model.person.Person;
import seedu.address.model.person.TuitionSchedule;
import seedu.address.model.person.TuitionTask;

import seedu.address.logic.commands.exceptions.CommandException;

import java.util.List;

/**
 * Adds a tuition (task) into the schedule.
 */
public class AddTuitionTaskCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "addTuition";

    private static final String INDEX_TARGET_PERSON =
            "last shown index of person that is associated with the task (space)";
    private static final String SPACE = "(space) ";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a tuition (task) into the schedule.\n"
            + "Parameters: "
            + INDEX_TARGET_PERSON + " "
            + "Date(dd/mm/yyyy) " + SPACE
            + "Start time(hh:mm) " + SPACE
            + "Duration(XXhXXm) " + SPACE
            + "Description.\n"
            + "Example: " + COMMAND_WORD + " "
            + "1 "
            + "10/12/2018 "
            + "12:30 "
            + "1h30m"
            + "Calculus homework page 24.";

    public static final String MESSAGE_SUCCESS = "New tuition task added.";

    private final TuitionTask toAdd;
    private final Index targetIndex;

    private TuitionSchedule tuitionSchedule;
    private Person associatedPerson;

    /**
     * Creates an AddTuition to add the specified {@code Task} which is associated to {@code Person}.
     */
    public AddTuitionTaskCommand(TuitionTask task, Index indexOfAssociatedPerson) {
        requireNonNull(task);
        toAdd = task;
        targetIndex = indexOfAssociatedPerson;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(tuitionSchedule);
        tuitionSchedule.addTask(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        findAssociatedPerson();
        assert(associatedPerson.getTuitionSchedule() != null);
        tuitionSchedule = associatedPerson.getTuitionSchedule();
    }

    /**
     * Locates {@code Person} that will be associated with the tuition task.
     *
     * @throws commandException if invalid person index was given.
     */
    private void findAssociatedPerson() throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        associatedPerson = lastShownList.get(targetIndex.getZeroBased());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddTuitionTaskCommand // instanceof handles nulls
                && toAdd.equals(((AddTuitionTaskCommand) other).toAdd));
    }
}
