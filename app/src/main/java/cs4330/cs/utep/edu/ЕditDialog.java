package cs4330.cs.utep.edu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class ЕditDialog extends DialogFragment {

    EditText itemName;
    EditText itemSource;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View DialogView = inflater.inflate(R.layout.edit_dialog,null);

        itemName = DialogView.findViewById(R.id.editTextName);
        itemSource = DialogView.findViewById(R.id.editTextSource);

        itemName.setText(getArguments().getString("itemName"));
        itemSource.setText(getArguments().getString("itemUrl"));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.editDialogTitle);
        builder.setView(DialogView)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String name = itemName.getText().toString();
                        String source = itemSource.getText().toString();
                        ((MainActivity)getActivity()).editItem(name,source, getArguments().getInt("position"));
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
