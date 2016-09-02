package io.vaxly.sema.data.repos.user;

import android.support.annotation.NonNull;

import com.badoo.chateau.core.repos.users.UserDataSource;
import com.badoo.chateau.core.repos.users.UserQueries;
import io.vaxly.sema.data.model.ExampleUser;
import io.vaxly.sema.data.util.ParseHelper;
import io.vaxly.sema.data.util.ParseUtils;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

public class ParseUserDataSource implements UserDataSource<ExampleUser> {

    private ParseHelper mParseHelper;

    public ParseUserDataSource(@NonNull ParseHelper parseHelper) {
        mParseHelper = parseHelper;
    }

    @NonNull
    @Override
    public Observable<List<ExampleUser>> getAllUsers(UserQueries.GetAllUsersQuery query) {
        final ParseQuery<ParseUser> parseQuery = new ParseQuery<>("_User");
        parseQuery.whereNotEqualTo("objectId", mParseHelper.getCurrentUser().getObjectId());
        parseQuery.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ONLY);

        return mParseHelper.find(parseQuery)
            .flatMap(Observable::from)
            .map(ParseUtils::fromParseUser)
            .toList()
            .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Observable<ExampleUser> getSingleUser(UserQueries.GetUserQuery query) {
        ParseQuery<ParseUser> parseQuery = new ParseQuery<>("_User");
        parseQuery.whereEqualTo("objectId", query.getUserId());
        parseQuery.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ONLY);

        return mParseHelper.find(parseQuery)
            .flatMap(Observable::from)
            .map(ParseUtils::fromParseUser)
            .subscribeOn(Schedulers.io());
    }

}
