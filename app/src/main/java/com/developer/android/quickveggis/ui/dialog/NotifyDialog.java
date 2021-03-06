package com.developer.android.quickveggis.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.developer.android.quickveggis.R;

import butterknife.Bind;
import butterknife.ButterKnife;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class NotifyDialog extends AppCompatDialogFragment {
    @Bind(R.id.blockItems)
    LinearLayout blockItems;
    @Bind(R.id.btnNext)
    View btnNext;
    @Bind(R.id.chDontShow)
    CheckBox chDontShow;
    int id;
    List<Integer> items;
    ActionListener listener;
    int title;
    @Bind(R.id.txtTitle)
    TextView txtTitle;

    @Bind(R.id.notify_video)
    VideoView notify_video;

    @Bind(R.id.notify_title)
    TextView notify_title;

    @Bind(R.id.notify_descript)
    TextView notify_descript;

    /* renamed from: com.quickveggies.quickveggies.ui.dialog.NotifyDialog.1 */
    class C02761 implements OnClickListener {
        C02761() {
        }

        public void onClick(View v) {
            NotifyDialog.this.setShowDialog(NotifyDialog.this.getContext(), NotifyDialog.this.id, !NotifyDialog.this.chDontShow.isChecked());
            if (NotifyDialog.this.listener != null) {
                NotifyDialog.this.listener.onContinueClicked(NotifyDialog.this.id);
            }
            NotifyDialog.this.dismiss();
        }
    }

    public static boolean isShowDialog(Context context, int id) {
        return context.getSharedPreferences("Dialog", AccessibilityNodeInfoCompat.ACTION_PASTE).getBoolean(String.valueOf(id), true);
    }

    public void setShowDialog(Context context, int id, boolean value) {
        context.getSharedPreferences("Dialog", AccessibilityNodeInfoCompat.ACTION_PASTE).edit().putBoolean(String.valueOf(id), value).apply();
    }

    public static NotifyDialog newInstance(int id, int title, ArrayList<Integer> items,String titleStr,String descriptStr,String uriPath) {
        Bundle args = new Bundle();
        args.putSerializable("items", items);
        args.putInt("title", title);
        args.putInt("id", id);
        args.putString("videoUri",uriPath);
        args.putString("titlestr",titleStr);
        args.putString("descript",descriptStr);
        NotifyDialog fragment = new NotifyDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_notify, container, false);
        ButterKnife.bind((Object) this, view);
        Bundle bundle = getArguments();
        this.items = (ArrayList) bundle.getSerializable("items");
        this.title = bundle.getInt("title");
        this.id = bundle.getInt("id");
        this.txtTitle.setText(this.title);
        fillItems();
        this.chDontShow.setChecked(!isShowDialog(getContext(), this.id));
        this.btnNext.setOnClickListener(new C02761());
        String uriPath=bundle.getString("videoUri");

        if (uriPath=="") this.notify_video.setVisibility(View.GONE);
        else{
            Uri uri2=Uri.parse(uriPath);
            notify_video.setVideoURI(uri2);
            notify_video.requestFocus();
            notify_video.start();
            notify_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });
        }

        notify_title.setText(bundle.getString("titlestr"));
        notify_descript.setText(bundle.getString("descript"));
        return view;
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        setCancelable(false);
        dialog.getWindow().requestFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        return dialog;
    }

    private void fillItems() {
        this.blockItems.removeAllViews();
        for (int i = 0; i < this.items.size(); i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_survey_data, this.blockItems, false);
            TextView txtContent = (TextView) view.findViewById(R.id.txtTitle);
            ((TextView) view.findViewById(R.id.txtNumber)).setText(String.valueOf(i + 1));
            txtContent.setText(((Integer) this.items.get(i)).intValue());
            this.blockItems.addView(view);
        }
    }
}
