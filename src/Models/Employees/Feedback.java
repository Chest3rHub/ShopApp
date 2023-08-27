package Models.Employees;

import java.time.LocalDate;

public class Feedback {
    Rating rating;
    String comment;
    LocalDate date;

    public Feedback(Rating rating, String comment, LocalDate date) {
        this.rating = rating;
        this.comment = comment;
        this.date=date;
    }
}
