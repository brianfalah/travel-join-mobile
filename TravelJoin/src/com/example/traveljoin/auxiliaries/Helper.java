package com.example.traveljoin.auxiliaries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Helper {
    public static void getListViewSize(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
      //setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
        // print height of adapter on log
        Log.i("height of listItem:", String.valueOf(totalHeight));
    }
    
    public static int getIntFromJson(JSONObject object, String attr) throws JSONException{
    	if (object.has(attr) && !object.isNull(attr)){
    		return object.getInt(attr);    		
    	}
    	else{
    		return 0;
    	}
    }
    
    public static String getStringFromJson(JSONObject object, String attr) throws JSONException{
    	if (object.has(attr) && !object.isNull(attr)){
    		return object.getString(attr);    		
    	}
    	else{
    		return "";
    	}
    }
    
    public static Double getDoubleFromJson(JSONObject object, String attr) throws JSONException{
    	if (object.has(attr) && !object.isNull(attr)){
    		return object.getDouble(attr);    		
    	}
    	else{
    		return 0.0;
    	}
    }
    
    public static Boolean getBooleanFromJson(JSONObject object, String attr) throws JSONException{
    	if (object.has(attr) && !object.isNull(attr)){
    		return object.getBoolean(attr);    		
    	}
    	else{
    		return false;
    	}
    }
    
    public static JSONArray getArrayFromJson(JSONObject object, String attr) throws JSONException{
    	if (object.has(attr) && !object.isNull(attr)){
    		return object.getJSONArray(attr);    		
    	}
    	else{
    		return new JSONArray();
    	}
    }
    
//    if (type.equals("string"))
//		return object.getString(attr);
//	if (type.equals("double"))
//		return object.getDouble(attr);
//	if (type.equals("array"))
//		return object.getJSONArray(attr);
}
