# WWaffle User Guide

// Update the title above to match the actual product name

// Product screenshot goes here

// Product intro goes here

## Adding deadlines

// Describe the action and its outcome.

// Give examples of usage

Example: `keyword (optional arguments)`

// A description of the expected outcome goes here

```
expected output
```

## Feature ABC

// Feature details


## Feature XYZ

// Feature details

## Sorting tasks

Use `sort name` to sort descriptions alphabetically, ignoring case, or
`sort status` to put unfinished tasks before completed tasks. Tasks with equal
sort values keep their previous relative order. Both commands display the
entire reordered list and save that order for the next launch.

**Task numbers change after sorting.** Use the numbers in the newly displayed
list for `mark`, `unmark`, and `delete`.

For example, if your todos are `zebra`, `apple`, and `Apple` (the last completed),
`sort name` displays:

```text
[ TASKS ]
1. [T][ ] apple
2. [T][X] Apple
3. [T][ ] zebra
```

Sorting an empty list displays `[i] Your task list is empty.` A single task
stays in place. Sorting is a one-time action: new tasks are still appended,
and marking tasks does not automatically sort them again.

Commands and sort keys are lowercase. Missing or unsupported keys and extra
arguments (for example, `sort`, `sort date`, or `sort name extra`) display
`[!] Use: sort name or sort status.` without changing task order.
The existing save-file format is unchanged; only the order of records changes.
