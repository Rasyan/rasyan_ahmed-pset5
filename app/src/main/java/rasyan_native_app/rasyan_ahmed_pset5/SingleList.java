package rasyan_native_app.rasyan_ahmed_pset5;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import java.util.ArrayList;

/**
 *  This is the second activity of the app, it displays the list of TodoItems,
 *  this activity contains a floating action button, when pressed it displays a pop up
 *  where the user can specify the text of the new todoItem and then adds it to the list.
 */

public class SingleList extends AppCompatActivity {
    private long tableID;
    private RecyclerView mRecyclerView;
    public ItemsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DataBaseHelper dbhelper;
    private ArrayList<TodoItem> data;
    private TodoList list;
    private String tableName;
    private ListsManager listsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_lists);

        // get data from intent, and setup the helper.
        Intent intent = getIntent();
        list = (TodoList) intent.getSerializableExtra("TodoLists");
        data = list.getTodoItems();
        String titel = list.getTitle();
        TextView textView = (TextView) findViewById(R.id.title);
        textView.setText(titel);
        tableID = list.getTableID();
        tableName = "TABLE" + tableID;
        dbhelper = new DataBaseHelper(this, tableName);
        listsManager = ListsManager.getInstance(SingleList.this);

        setUpRecyclerView();

        // code below is for setting up the floating add button on the bottem right of the screen.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // start the dialog fragment asking for the text of the TodoItem.
                // calls onMakeNewItem when confirm is pressed, otherwise does nothing.
                android.app.DialogFragment newFragment = InputFrag.newInstance(false,true);
                newFragment.show(getFragmentManager(), "add single");
            }
        });
    }

    // sets up the recyclerview and connects the layoutmanager and adapter to it.
    private void setUpRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        // this line below adds a divider between the recyclerView items,
        // requires a dependency.
        // source : https://github.com/yqritc/RecyclerView-FlexibleDivider
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());


        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // use the adapter
        mAdapter = new ItemsAdapter(data,tableID);
        mRecyclerView.setAdapter(mAdapter);
    }

    // adds a new item
    public void onMakeNewItem(String inputText) {

        // adds it to the database and the internal data of the listmanager
        TodoItem newItem = listsManager.addItem(tableID,inputText,SingleList.this);

        // adds it to the data stored locally so that it can notify the recyclerview that it has to update
        data.add(newItem);
        mAdapter.notifyItemInserted(data.size()-1);
        Toast.makeText(SingleList.this, "added to TODO list", Toast.LENGTH_SHORT).show();
    }
}
