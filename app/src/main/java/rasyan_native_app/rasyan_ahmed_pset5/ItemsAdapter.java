package rasyan_native_app.rasyan_ahmed_pset5;

/**
 * Created by Rasyan on 1-10-2016.
 *
 * The adapter which regulates the views in the recyclerView for the Singlelist class.
 * functionality for clicking them, both long and short clicks
 * this is a sister class of ListsAdapter, they are very similar but their input and listeners are different
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;


public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    public ArrayList<TodoItem> data;
    private View.OnClickListener listener;
    private View.OnLongClickListener longlistener;
    private Context context;
    private int deletePosition;
    private ListsManager listsManager;
    private long tableID;


    // constructor, mainly houses the two different listeners.+
    public ItemsAdapter(ArrayList<TodoItem> tempData,long id){
        data = tempData;
        tableID = id;
        listsManager = ListsManager.getInstance(context);

        // changes an items checked status when it is short clicked
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View view){
                int position = ((ViewGroup) view.getParent()).indexOfChild(view);

                // sets the checked status to the opposite of its current status in the database. and in the listmanager
                listsManager.changeDone(tableID,position,context);

                // also does this for the arraylist data that this adapter uses
                // so that it can notify the recyclerview that this item has changed and needs to be reloaded.
                TodoItem currentViewData = data.get(position);
                currentViewData.setDone(!currentViewData.getDone());
                data.set(position,currentViewData) ;
                notifyItemChanged(position);
            }
        };

        // delete an item when it is long clicked
        longlistener = new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view){
                deletePosition = ((ViewGroup) view.getParent()).indexOfChild(view);

                // starts up a dialog fragment asking for a confirmation.
                // if confirm button is pressed the function onDeleteItem below is called,
                // otherwise nothing happens.
                android.app.DialogFragment newFragment = InputFrag.newInstance(false,false);
                newFragment.show(((Activity) context).getFragmentManager(), "delete single");
                return true;
            }
        };
    }

    // deletes an item from the listmanager, the database and from the data stored here,
    // afterwards refreshes the recyclerview
    public void onDeleteItem() {
        listsManager.deleteItem(tableID,deletePosition,context);
        data.remove(deletePosition);
        Toast.makeText(context, "deleted from TODO list", Toast.LENGTH_SHORT).show();
        notifyItemRemoved(deletePosition);
    }

    // make the viewholder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView todo;

        public ViewHolder(View itemView) {
            super(itemView);
            todo = (TextView) itemView.findViewById(R.id.todo);
        }
    }
    // choose singleview as viewholder
    @Override
    public ItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleview, parent, false);
        ViewHolder vh = new ViewHolder(view);
        context = view.getContext();
        return vh;
    }

    // sets the text and color of the items in the recyclerList based on the data in the database.
    @Override
    public void onBindViewHolder(ItemsAdapter.ViewHolder vh, int i) {
        TodoItem currentViewData = data.get(i);
        vh.todo.setText(currentViewData.getText());

        // sets the text color based on its checked status.
        if (!currentViewData.getDone()) {
            vh.todo.setTextColor(Color.BLACK);
        } else {
            vh.todo.setTextColor(Color.GRAY);
        }

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