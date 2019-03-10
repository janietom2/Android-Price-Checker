package cs4330.cs.utep.edu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ЕditDialog extends DialogFragment {

    EditText itemName;
    EditText itemSource;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.editDialogTitle);
        builder.setView(R.layout.edit_dialog)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getContext(),"YES", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getContext(), "NO", Toast.LENGTH_SHORT).show();
                 }});

        return builder.create();
    }


    //TODO - set default text depenfding on the values saved in item
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        itemName = (EditText) getView().findViewById(R.id.editTextName);
        itemSource = (EditText) getView().findViewById(R.id.editTextSource);
    }

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
        }
    }*/

}
