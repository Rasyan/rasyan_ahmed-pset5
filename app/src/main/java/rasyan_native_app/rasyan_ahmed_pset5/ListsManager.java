package rasyan_native_app.rasyan_ahmed_pset5;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Rasyan on 8-10-2016.
 *
 * A singleton that manages the list of TodoLists, it can add and delete Todolists
 */

public class ListsManager {
    private ArrayList<TodoList> todoLists;
    private DataBaseHelper allListHelper;
    private static final String TABLE = "TABLE";
    private static final String TABLENAMES = "TableNames";

    private static ListsManager instance = null;

    private ListsManager(Context context) {
        todoLists = makeTodoLists(context);
    }

    public static ListsManager getInstance(Context context) {
        if(instance == null) {
            instance = new ListsManager(context);
        }
        return instance;
    }

    // besides all the Todolists themselfs, we also need to store a list of all todolists,
    // this is also done in the same database.
    // there is a special table in it that has the name "TableNames",
    // in this table the Database table names of all todolists are stored.
    // Each TodoList is stored as a normal todoItem, where its title is equal to the the items text field,
    // and its Table name is equal to "TABLE" + its item id.
    // the function below reads this "tableNames" table and then constructs the list of TodoLists from them.
    private ArrayList<TodoList> makeTodoLists(Context context) {
        // read the "tableNames" table
        ArrayList<TodoList> allLists = new ArrayList<>();
        allListHelper = new DataBaseHelper(context,TABLENAMES);

        // read each individual table and store them as a TodoList class
        ArrayList<TodoItem> tableNames = allListHelper.read();
        for (TodoItem table: tableNames) {
            TodoList currentTableList = new TodoList(table.getText(),table.getId());
            DataBaseHelper singleListHelper = new DataBaseHelper(context,TABLE + table.getId());
            currentTableList.setTodoItems(singleListHelper.read());
            allLists.add(currentTableList);
        }
        return allLists;
    }

    // deletes the item specified by its position in the arraylist of todolists.
    public void deleteTable(int position,Context context) {
        // delete it from the database table that contain all the table names
        Long allListID = todoLists.get(position).getTableID();
        allListHelper.delete(allListID);

        // delete it from the local list privately stored here,
        todoLists.remove(position);

        // completely deletes the table from the database.
        DataBaseHelper singleListHelper = new DataBaseHelper(context,
                TABLE + allListID);
        singleListHelper.deleteTable();
    }

    // adds a new entry into the database table that contains all the tables
    // it then recreates the arraylist of todolists stored privately here.
    // this is also where the database table itself is made
    // (when it is read for the first time in makeTodoLists();)
    public void addTable(String title,Context context) {
        allListHelper.create(title);
        todoLists = makeTodoLists(context);
    }

    // finds the item that needs to be added in the todolists
    // and then adds it to the todolists and the database
    public TodoItem addItem(long tableID, String inputText, Context context) {
        int size = todoLists.size();
        for (int i = 0; i < size; i++) {
            TodoList list = todoLists.get(i);
            if (list.getTableID() == tableID) {

                // add to database
                DataBaseHelper singleListHelper = new DataBaseHelper(context,
                        TABLE + tableID);
                singleListHelper.create(inputText);

                // add to internal list
                ArrayList<TodoItem> newList = singleListHelper.read();
                todoLists.get(i).setTodoItems(newList);
                return todoLists.get(i).getTodoItems().get(newList.size()-1);
            }
        }
        return null;
    }
    // finds the item that needs to be deleted in the todolists
    // and then deletes it from the todolists and the database
    public void deleteItem(long tableID, int position, Context context) {
        int size = todoLists.size();
        ArrayList<TodoItem> items = null;
        for (int i = 0; i < size; i++) {
            TodoList list = todoLists.get(i);
            if (list.getTableID() == tableID) {
                items = list.getTodoItems();

                // delete from db
                DataBaseHelper singleListHelper = new DataBaseHelper(context,
                        TABLE + tableID);
                singleListHelper.delete(items.get(position).getId());

                // delete from internal list
                items.remove(position);
                list.setTodoItems(items);
                break;
            }
        }
    }

    // finds the item that needs it done status changed
    // does this for both the db and the internal list
    public void changeDone(long tableID, int position, Context context) {
        int size = todoLists.size();
        for (int i = 0; i < size; i++) {
            TodoList list = todoLists.get(i);
            if (list.getTableID() == tableID) {
                TodoItem item = list.getTodoItems().get(position);

                // change in db
                DataBaseHelper singleListHelper = new DataBaseHelper(context,
                        TABLE + tableID);
                singleListHelper.update(item.getId(),item.getText(),!item.getDone());

                // change internally
                item.setDone(!item.getDone());
                break;
            }
        }
    }
    // getter for the todolists
    public ArrayList<TodoList> getTodoLists() {
        return todoLists;
    }
}
