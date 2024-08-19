package learn.myhouse.ui;

public enum MainMenuOption {
    EXIT(0, "Exit"),
    VIEW_RESERVATIONS_BY_DATE(1, "View Reservations for Host"),
    MAKE_RESERVATION(2,"Make a Reservation"),
    EDIT_RESERVATION(3, "Edit a Reservation"),
    CANCEL_RESERVATION(4, "Cancel a Reservation");


    private int value;
    private String message;

    /**
     * constructor that accepts and sets a value and a message to each MainMenuOption
     * @param value
     * @param message
     */
    private MainMenuOption(int value, String message) {
        this.value = value;
        this.message = message;
    }

    /**
     * Method that accepts an int and returns a MainMenuOption
     * if a corresponding value is found
     * @param value
     * @return the MainMenuOption found
     */
    public static MainMenuOption fromValue(int value) {
        for (MainMenuOption option : MainMenuOption.values()) {
            if (option.getValue() == value) {
                return option;
            }
        }
        return EXIT;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

}
