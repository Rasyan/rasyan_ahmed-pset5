package rasyan_native_app.rasyan_ahmed_pset5;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import java.util.ArrayList;

/**
 *  This is the main activity of the app, it displays the list of TodoLists,
 *  this activity contains a floating action button, when pressed it displays a pop up
 *  where the user can specify the title of the new todolist and then add it to the list.
 *
 *  TODO: make listmanager singleton and update other classes
 *  TODO: fix add/delete items
 *  TODO: clean all classes, remove unneeded stuff
 *  TODO: maybe, have keyboard pop up when pop up pops up
 *  TODO: have a are you sure man?? screen (fragment?) pop up when deleting list.
 */

public class AllLists extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    public ListsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<TodoList> data;
    private ListsManager listManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_lists);

        // setup the recyclerview and read the data from the listmanager.
        listManager = ListsManager.getInstance(AllLists.this);
        data = listManager.getTodoLists();
        setUpRecyclerView();

        // code below is for setting up the floating add button on the bottem right of the screen.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // sets up a dialog fragment that asks you for the title of the new list,
                // when the enter button is pressed. onMakeNewList is called. otherwise nothing happens
                android.app.DialogFragment newFragment = InputFrag.newInstance(true,true);
                newFragment.show(getFragmentManager(), "add all");
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
        mAdapter = new ListsAdapter(data);
        mRecyclerView.setAdapter(mAdapter);
    }

    // creates a new list, is called from the dialog fragment.
    public void onMakeNewList(String inputText) {

        // add the text entered in the edit text as a new todolist in the manager and database
        listManager.addTable(inputText,AllLists.this);
        data = listManager.getTodoLists();

        // this function notifies the adapter that the data has changed
        // and as such the recyclerView needs to be updated
        mAdapter.swap(data);
        Toast.makeText(AllLists.this, "added TODO list", Toast.LENGTH_SHORT).show();
    }

    // we need to refresh the recyclerview when we resume this activity
    // so that it updates the list sizes displayed incase an item was added or deleted from a list.
    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.swap(listManager.getTodoLists());
    }

    public void onDeleteList() {
        Toast.makeText(AllLists.this, "no", Toast.LENGTH_SHORT).show();
    }
}
