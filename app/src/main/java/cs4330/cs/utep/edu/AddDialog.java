package cs4330.cs.utep.edu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class AddDialog extends DialogFragment {

    EditText itemName;
    EditText itemSource;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View DialogView = inflater.inflate(R.layout.edit_dialog,null);
        itemName = DialogView.findViewById(R.id.editTextName);
        itemSource = DialogView.findViewById(R.id.editTextSource);
        String url = getArguments().getString("url");
        itemSource.setText(url);



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Item");
        builder.setView(DialogView)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String name = itemName.getText().toString();
                        String url = itemSource.getText().toString();
                        ((MainActivity)getActivity()).addItem(name,url);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                 }});
        return builder.create();
    }
}
