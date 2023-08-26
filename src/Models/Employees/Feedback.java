package Models.Employees;

import java.util.Date;

public class Feedback {
    Rating rating;
    String comment;
    Date date;

    public Feedback(Rating rating, String comment, Date date) {
        this.rating = rating;
        this.comment = comment;
        this.date=date;
    }
}
