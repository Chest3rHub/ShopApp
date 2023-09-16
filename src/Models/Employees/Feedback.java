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

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    public String toString(){
        return "DATE: "+date  +", RATING: " + rating  + ", COMMENT: " +comment;
    }
}
