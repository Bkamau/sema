package io.vaxly.sema.data.repos.session;

import android.support.annotation.NonNull;

import com.badoo.chateau.data.models.BaseUser;
import io.vaxly.sema.Broadcaster;
import io.vaxly.sema.data.util.ParseHelper;
import io.vaxly.sema.data.util.ParseUtils;
import io.vaxly.sema.data.util.ParseUtils.UsersTable;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.schedulers.Schedulers;

public class ParseSessionDataSource implements SessionDataSource {

    @NonNull
    private Broadcaster mBroadcaster;
    private ParseHelper mParseHelper;

    public ParseSessionDataSource(@NonNull Broadcaster broadcaster, @NonNull ParseHelper parseHelper) {
        mBroadcaster = broadcaster;
        mParseHelper = parseHelper;
    }

    @NonNull
    @Override
    public Observable<BaseUser> signIn(@NonNull SessionQuery.SignIn query) {
        return mParseHelper.signIn(query.getUserName(), query.getPassword())
            .map(ParseUtils::fromParseUser)
            .map(user -> ((BaseUser) user))
            .subscribeOn(Schedulers.io())
            .doOnCompleted(() -> mBroadcaster.userSignedIn());
    }

    @Override
    public Observable<Void> signOut() {
        return mParseHelper.signOut()
            .subscribeOn(Schedulers.io())
            .doOnCompleted(() -> mBroadcaster.userSignedOut());
    }

    @NonNull
    @Override
    public Observable<BaseUser> register(@NonNull SessionQuery.Register query) {
        final Map<String, Object> additionalParams = new HashMap<>();
        additionalParams.put(UsersTable.Fields.DISPLAY_NAME, query.getDisplayName());

        return mParseHelper.signUp(query.getUserName(), query.getPassword(), additionalParams)
            .map(ParseUtils::fromParseUser)
            .map(user -> ((BaseUser) user))
            .subscribeOn(Schedulers.io())
            .doOnCompleted(() -> mBroadcaster.userSignedIn());
    }
}
