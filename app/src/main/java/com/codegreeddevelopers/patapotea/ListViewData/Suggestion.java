package com.codegreeddevelopers.patapotea.ListViewData;

import android.annotation.SuppressLint;
import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by FakeJoker on 10/03/2018.
 */


@SuppressLint("ParcelCreator")
public class Suggestion implements SearchSuggestion {
    public String suggestion ;

    public Suggestion(String s){

        this.suggestion = s ;

    }

    public String getSuggestion(){

        return this.suggestion ;

    }

    @Override
    public String getBody() {
        return this.suggestion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
