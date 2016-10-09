package rasyan_native_app.rasyan_ahmed_pset5;

import java.io.Serializable;

/**
 * Created by Rasyan on 7-10-2016.
 *
 * Contains the info of a single thing that has to be done on a TodoList.
 *
 */

public class TodoItem implements Serializable{
    String text;
    boolean done;
    long id;

    // constructor
    public TodoItem(String text) {
        this.text = text;
        done = false;
    }

    // setters
    public void setText(String text) {
        this.text = text;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void setId(long id) {
        this.id = id;
    }


    // getters
    public String getText() {
        return text;
    }

    public boolean getDone() {
        return done;
    }

    public long getId() {
        return id;
    }


}
