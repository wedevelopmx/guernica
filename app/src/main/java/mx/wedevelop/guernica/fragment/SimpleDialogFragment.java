package mx.wedevelop.guernica.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;

import mx.wedevelop.guernica.R;

/**
 * Created by root on 26/07/16.
 */

public class SimpleDialogFragment extends DialogFragment {
    private static final String MESSAGE = "message";
    private static final String POSITIVE_BTN = "positive";
    private static final String NEGATIVE_BTN = "negative";

    public static SimpleDialogFragment newInstance(String message, String positiveButton, String negativeButton) {
        SimpleDialogFragment f = new SimpleDialogFragment();

        Bundle args = new Bundle();
        args.putString(MESSAGE, message);
        args.putString(POSITIVE_BTN, positiveButton);
        args.putString(NEGATIVE_BTN, negativeButton);

        f.setArguments(args);

        return f;
    }

    public static interface SimpleDialogListener {
        public void onPositiveAnswer(DialogInterface dialog, int id);
        public void onNegativeAnswer(DialogInterface dialog, int id);
    }

    private SimpleDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String message;
        String positiveButton;
        String negativeButton;


        message = getArguments().getString(MESSAGE);
        positiveButton = getArguments().getString(POSITIVE_BTN);
        negativeButton = getArguments().getString(NEGATIVE_BTN);

        try {
            //If dialog is invoked from Fragment
            listener = (SimpleDialogListener) getTargetFragment();
            if(listener == null) {
                //Othewise it should be invoked from Activity
                listener = (SimpleDialogListener) getActivity();
            }
        } catch(ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement SimpleDialogListener");
        }

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onPositiveAnswer(dialog, id);
                    }
                })
                .setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onNegativeAnswer(dialog, id);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}