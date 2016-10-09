package rasyan_native_app.rasyan_ahmed_pset5;

/**
 * Created by Rasyan on 1-10-2016.
 *
 * The adapter which regulates the views in the recyclerView'for the SingleList actiity and provides
 * functionality for clicking them, both long and short clicks
 * this is a sister class of ListsAdapter, they are very similar but their input and listeners are different
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.ViewHolder> {

    public ArrayList<TodoList> data;
    private View.OnClickListener listener;
    private View.OnLongClickListener longlistener;
    private Context context;
    private int deletePosition;

    // constructor, mainly houses the two different listeners.
    public ListsAdapter(ArrayList<TodoList> tempData){
        data = tempData;

        // produces an intent to go to the SingleList class,
        // passing along the TodoList of the clicked list as an extra
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View view){
                int position = ((ViewGroup) view.getParent()).indexOfChild(view);
                TodoList currentViewData = data.get(position);
                Intent intent = new Intent(context,SingleList.class);
                intent.putExtra("TodoLists",currentViewData);
                context.startActivity(intent);
            }
        };

        // delete an list when it is long clicked
        longlistener = new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view){
                deletePosition = ((ViewGroup) view.getParent()).indexOfChild(view);

                // starts up a dialog fragment asking for a confirmation.
                // if confirm button is pressed the function onDeleteList below is called,
                // otherwise nothing happens.
                android.app.DialogFragment newFragment = InputFrag.newInstance(true,false);
                newFragment.show(((Activity) context).getFragmentManager(), "delete all");
               return true;
            }
        };
    }
    // called when confirm is pressed on the confirmation window
    // asks the listmanager to delete the table from the database and its internal data arraylist
    // afterwards it also removes the list from the data stored in this class and updates the recyclerview
    public void onDeleteList() {
        ListsManager listManager = ListsManager.getInstance(context);
        listManager.deleteTable(deletePosition,context);
        Toast.makeText(context, "deleted TodoList", Toast.LENGTH_SHORT).show();
        data.remove(deletePosition);
        notifyItemRemoved(deletePosition);
    }

    // a method that is called in main Activity when an item is added to the database,
    // it swaps the data in this adapter with the new one provided
    // by deleting the old one and adding all the data from the new one.
    // so that the recyclerlist can be notified of a dataset change.
    public void swap(ArrayList<TodoList> newData){
        data = new ArrayList<>(newData);
        notifyDataSetChanged();
    }
    // make the viewholder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView todo;

        public ViewHolder(View itemView) {
            super(itemView);
            todo = (TextView) itemView.findViewById(R.id.todo);
        }
    }

    // choose singleview as viewholder and inflate it to show in the list.
    @Override
    public ListsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleview, parent, false);
        ViewHolder vh = new ViewHolder(view);
        context = view.getContext();
        return vh;
    }

    // sets the text of the items in the recyclerList based on the data in the titles of the TodoLists
    @Override
    public void onBindViewHolder(ListsAdapter.ViewHolder vh, int i) {
        TodoList currentViewData = data.get(i);
        vh.todo.setText(currentViewData.getTitle() + " (" + currentViewData.getSize() + ")");

        // sets up the listeners within them
        vh.itemView.setOnClickListener(listener);
        vh.itemView.setOnLongClickListener(longlistener);
    }

    // returns the size of the data it contains, which reflects the number of entry's in the recyclerList
    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }
}