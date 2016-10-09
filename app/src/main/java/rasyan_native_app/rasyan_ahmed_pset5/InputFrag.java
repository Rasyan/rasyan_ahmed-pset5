package rasyan_native_app.rasyan_ahmed_pset5;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;


/**
 * This fragment shows a dialog when called.
 * It has four possible configurations, specified by two different booleans that are given to it as arguments.
 * The first boolean is there to specify which activity you are from, true for AllLists and false for SingleList.
 * This changes the title of the dialog and which classes functions are called when the confirm button is pressed.
 * The second boolean whether the dialog needs an EditText or not,
 * if it does not this means that the dialog is an confirmation and as such needs to call delete functions instead of add.
 */
public class InputFrag extends android.app.DialogFragment {
    private static final String ARG_PARAM1 = "class";
    private static final String ARG_PARAM2 = "input";

    private static final String ALL_TITLE = "Enter Title";
    private static final String SINGLE_TITLE = "Enter Todo item";
    private static final String ALL_CONFIRMATION = "Are you sure you wish to delete this List?";
    private static final String SINGLE_CONFIRMATION = "Are you sure you wish to delete this item?";

    private String title;

    // the two booleans which type of dialog this is.
    private boolean input;
    private boolean classType;

    // and empty constructor is needed.
    public InputFrag() {

    }

    // makes a new instence of the class, stores the two booleans provided (which specify the type of dialog)
    // as arguments.
    public static InputFrag newInstance(boolean param1, boolean param2) {
        InputFrag fragment = new InputFrag();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        args.putBoolean(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    // initilizes some internal variables
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            // gets the two booleans from the arguments
            classType = getArguments().getBoolean(ARG_PARAM1);
            input = getArguments().getBoolean(ARG_PARAM2);

            // sets the title of the dialog based on the two booleans
            if (input) {
                if (classType) {
                    title = ALL_TITLE;
                } else {
                    title = SINGLE_TITLE;
                }
            } else {
                if (classType) {
                    title = ALL_CONFIRMATION;
                } else {
                    title = SINGLE_CONFIRMATION;
                }
            }
        }

    }

    // the main method which builds the dialog and sets the listeners
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // constructs the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // adds the EditText inside fragment_input.xml if necessary
        if (input) {
            builder.setView(inflater.inflate(R.layout.fragment_input, null));
        }

        // adds the enter button and sets a listener
        builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                // if there is an edit text and the text entered is not nothing,
                // then call the make method of the right class
                if (input) {
                    EditText edit = (EditText) getDialog().findViewById(R.id.edit);
                    String inputText = String.valueOf(edit.getText());
                    if (!inputText.equals("")) {
                        if (classType) {
                            ((AllLists) getActivity()).onMakeNewList(inputText);
                        } else {
                            ((SingleList) getActivity()).onMakeNewItem(inputText);
                        }
                    } else {
                        Toast.makeText(getActivity(), "No text entered", Toast.LENGTH_SHORT).show();
                    }

                // else call the delete method of the right class
                } else {
                    if (classType) {
                        ((AllLists)getActivity()).mAdapter.onDeleteList();
                    } else {
                        ((SingleList)getActivity()).mAdapter.onDeleteItem();
                    }
                }
            }
        })

        // setup the cancel button and the listener
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog, dialog gets removed automaticly
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}

