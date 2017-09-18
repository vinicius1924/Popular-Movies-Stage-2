package com.example.vinicius.popularmoviesstage2.model.data_manager.base;

import com.example.vinicius.popularmoviesstage2.model.api.base.ApiHelper;
import com.example.vinicius.popularmoviesstage2.model.database.base.DbHelper;
import com.example.vinicius.popularmoviesstage2.model.preferences.base.PreferenceHelper;

/**
 * Created by vinicius on 11/09/17.
 */

public interface DataManager extends DbHelper, ApiHelper, PreferenceHelper
{
}
