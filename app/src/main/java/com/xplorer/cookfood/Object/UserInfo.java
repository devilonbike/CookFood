package com.xplorer.cookfood.Object;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

/**
 * Created by Raghavendra on 25-03-2015.
 */

@ParseClassName("_User")
public class UserInfo extends ParseUser {
    public UserInfo(){}

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public ParseFile getImageFile() {
        return getParseFile("image");
    }

    public void setImageFile(ParseFile image) {
        put("image", image);
    }

}
