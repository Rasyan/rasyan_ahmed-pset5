package rasyan_native_app.rasyan_ahmed_pset5;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Rasyan on 7-10-2016.
 *
 * Contains all the info of a single TodoList
 */

public class TodoList implements Serializable{
    private String title;
    private ArrayList<TodoItem> items;
    private Long tableID;

    // constructor
    public TodoList(String title,Long tableID) {
        this.title = title;
        items = new ArrayList<>();
        this.tableID = tableID;
    }

    // setters
    public void setTodoItems(ArrayList<TodoItem> items) {
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<TodoItem> getTodoItems() {
        return items;
    }

    public int getSize() {
        return items.size();
    }

    public Long getTableID() {
        return tableID;
    }
}
