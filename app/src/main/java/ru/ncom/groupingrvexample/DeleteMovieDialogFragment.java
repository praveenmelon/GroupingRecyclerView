package ru.ncom.groupingrvexample;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import ru.ncom.groupingrvexample.model.Movie;

/**
 * Created by gerg on 10.10.2016.
 */

public class DeleteMovieDialogFragment extends DialogFragment {

    public interface YesNoListener {
        void onDeleteYes(Movie m);

        void onDeleteDismiss();
    }

    private static final String MSG ="MSG";
    private static final String POS ="POS";

    /**
     * Creates AlertDoalog to confirm deletion. Activity must implement {@link YesNoListener}
     * @param message message in AlertDialog
     * @param position when deletion is confirmed  {@link YesNoListener#onDeleteYes(int position)} is fired.
     * @return
     */
    public static DeleteMovieDialogFragment createInstance(Context ctx, Movie m){
        String message = String.format(ctx.getString(R.string.delete_movie_dialog_message),
                m.getTitle(), m.getGenre(), m.getYear());

        DeleteMovieDialogFragment f = new DeleteMovieDialogFragment();
        Bundle b = new Bundle();
        b.putString(MSG, message);
        b.putSerializable(POS, m);
        f.setArguments(b);
        return f;
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof YesNoListener)) {
            throw new ClassCastException(activity.toString() + " must implement YesNoListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, 0);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle b = getArguments();
        final Movie m = (Movie)b.getSerializable(POS);
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_movie_dialog_title)
                .setMessage(b.getString(MSG))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((YesNoListener) getActivity()).onDeleteYes(m);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteMovieDialogFragment.this.getDialog().cancel();
                    }
                })
                .create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        // No actvity when config change
        if (getActivity()!= null)
            ((YesNoListener) getActivity()).onDeleteDismiss();
    }
}
