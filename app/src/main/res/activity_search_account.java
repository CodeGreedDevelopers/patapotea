package com.digitalvisionea.mycar.Reset_Password;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.digitalvisionea.mycar.R;
import com.digitalvisionea.mycar.Utility.PostToServer;
import com.digitalvisionea.mycar.Utility.Util;
import com.google.gson.JsonObject;

import CustomPlugins.TransparentDialog;

public class activity_search_account extends Fragment {
    private View view;
    Button search_btn;
    EditText reset_identity;
    String st_user_identity;
    static SharedPreferences user_resetting_info;
    static SharedPreferences.Editor editor;
    static ViewPager viewPager;
    static TransparentDialog transparentDialog;
    PostToServer postToServer;
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle saveInstanceState){
        view=inflater.inflate(R.layout.fragment_searchaccount,viewGroup,false);
        viewPager=getActivity().findViewById(R.id.viewpager_reset);
        transparentDialog=new TransparentDialog(getContext());
        search_btn=view.findViewById(R.id.reset_search_btn);
        reset_identity=view.findViewById(R.id.reset_user_id);

        user_resetting_info=this.getActivity().getSharedPreferences("user_reset_info",Context.MODE_PRIVATE);
        editor=user_resetting_info.edit();
        editor.clear().commit();
        //initializing post to server class
        postToServer=new PostToServer();

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.check_if_edit_text_isEmpty(reset_identity)){
                    reset_identity.setError("Please Enter Your Email or Phone Number");
                }else{
                    st_user_identity=reset_identity.getText().toString();
                    transparentDialog.show();
                    search_account(st_user_identity);
                }


            }
        });

        return view;
    }
    public void search_account(String user_id){

        //beginning of creating json object for sending online
        JsonObject jsonObject=new JsonObject();
        JsonObject user_info=new JsonObject();
        user_info.addProperty("identity",user_id);
        jsonObject.add("data",user_info);
        // end of the json object creating

        //calling method to sending data online
        postToServer.search_if_account_exists(getContext(),jsonObject,user_id);


    }
     public static void validate_recieved(Context context,String account_response, String user_id ){
         if (account_response.equals("EXISTS")){

             editor.putString("user_identity",user_id);
             editor.apply();
             transparentDialog.dismiss();
             viewPager.setCurrentItem(1);
         }
         else if(account_response.equals("NO_EXIST")){
             Util.DisplayInfoShortMessage(context,"User with the identity "+user_id+" does not exist in the system.","Not Found",null);
             transparentDialog.dismiss();
         }else{
             Util.DisplayErrorShortMessage(context,"There was an Error Communicating With The Server","Server Error",null);
             transparentDialog.dismiss();
         }
     }
}
