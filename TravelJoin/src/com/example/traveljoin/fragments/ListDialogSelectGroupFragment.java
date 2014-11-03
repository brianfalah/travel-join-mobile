package com.example.traveljoin.fragments;

import java.util.ArrayList;

import com.example.traveljoin.adapters.GeneralItemListAdapter;
import com.example.traveljoin.models.GeneralItem;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ListDialogSelectGroupFragment extends DialogFragment {

    private OnListDialogItemSelect listener;
    private String title;
    private ArrayList<GeneralItem> list;

    public ListDialogSelectGroupFragment(OnListDialogItemSelect listener, ArrayList<GeneralItem> list, String title) {
        this.listener=listener;
        this.list=list;
        this.title=title;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	GeneralItemListAdapter adapter = new GeneralItemListAdapter(getActivity(), list);
        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setCancelable(false)
                .setAdapter(adapter,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                        	listener.onListItemSelected(list.get(item));
                        	getDialog().dismiss(); 
                        	ListDialogSelectGroupFragment.this.dismiss();
                        }
                    }).create();
//        .setItems(list, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int item) {
//
//                listener.onListItemSelected(list[item]);
//                getDialog().dismiss(); 
//                ListDialogSelectGroupFragment.this.dismiss();
//
//            }
//        })

    }

    public interface OnListDialogItemSelect{
        public void onListItemSelected(GeneralItem selection);
    }

}
