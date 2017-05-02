package net.quikkly.android.testingapp;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


public class PopupChoiceView extends android.support.v7.widget.AppCompatTextView {

    public interface SelectedChangedListener {
        void onSelectedChanged(int position);
    }

    public PopupChoiceView(Context context) {
        super(context);
        init();
    }

    public PopupChoiceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PopupChoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private String title = null;
    private ArrayList<String> choices = new ArrayList<>();
    private int selectedPosition = 0;
    private SelectedChangedListener listener;

    public void setChoices(List<String> choices) {
        if (choices == null || choices.isEmpty())
            throw new IllegalArgumentException("Choices must be non-empty.");
        if (new HashSet<>(choices).size() != choices.size())
            throw new IllegalArgumentException("Choices must be unique.");
        for (String c : choices)
            if (c == null || c.isEmpty())
                throw new IllegalArgumentException("Each choice must be non-empty");
        this.choices = new ArrayList<>(choices);

        if (0 <= selectedPosition && selectedPosition < choices.size())
            setSelected(selectedPosition);
        else
            setSelected(0);
    }

    public void setChoices(String[] choices) {
        if (choices == null || choices.length == 0)
            throw new IllegalArgumentException("Choices must be non-empty.");
        setChoices(Arrays.asList(choices));
    }

    public void setSelected(int position) {
        if (position < 0 || position >= choices.size())
            throw new IllegalArgumentException("Position out of range.");
        setText(choices.get(position));
        if (selectedPosition != position) {
            selectedPosition = position;
            if (listener != null) {
                listener.onSelectedChanged(position);
            }
        }
    }

    public int getSelected() {
        return selectedPosition;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSelectedChangedListener(SelectedChangedListener listener) {
        this.listener = listener;
    }

    private void init() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setSingleChoiceItems(choices.toArray(new CharSequence[0]), getSelected(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                setSelected(which);
                                getHandler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                    }
                                }, 500);
                            }
                        })
                        .setCancelable(true);
                if (!TextUtils.isEmpty(title))
                    builder.setTitle(title);
                builder.show();
            }
        });
    }
}
