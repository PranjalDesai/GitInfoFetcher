package com.pranjaldesai.gitinfofetcher;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.button.MaterialButton;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ProgressBar;

import com.pranjaldesai.gitinfofetcher.Adaptor.OwnerAdaptor;
import com.pranjaldesai.gitinfofetcher.Data.Owner;
import com.pranjaldesai.gitinfofetcher.Data.Repo;
import com.pranjaldesai.gitinfofetcher.Utilities.GithubClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements OwnerAdaptor.OwnerItemClickListener{

    @BindView(R.id.username_inputlayout)
    TextInputLayout usernameInputLayout;
    @BindView(R.id.username_edittext)
    TextInputEditText usernameEditText;
    @BindView(R.id.search_username_button)
    MaterialButton mSearchButton;
    @BindView(R.id.repoTypeChipGroup)
    ChipGroup repoTypeChipGroup;
    @BindView(R.id.user_chip)
    Chip userChip;
    @BindView(R.id.org_chip)
    Chip orgChip;
    @BindView(R.id.searchProgressBar)
    ProgressBar searchProgressBar;
    @BindView(R.id.owner_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.offline)
    ConstraintLayout offlineLayout;

    Subscription subscription;
    private Realm realm;
    boolean isOrg, isUser;
    int repoType= -1;
    Snackbar snackbar;

    RealmResults<Owner> distinctOwnerQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Realm.init(this);
        realm = Realm.getDefaultInstance();

        // Search button to validate and navigate
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateFields();
            }
        });

        repoTypeChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                repoType= group.getCheckedChipId();
            }
        });

        userChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isUser= b;
            }
        });

        orgChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isOrg= b;
            }
        });
    }

    // Checks database, finds distinct users and populates recyclerview with that data
    private void offlineData(){
        distinctOwnerQuery= realm.where(Owner.class).distinct(getResources().getString(R.string.login)).findAll();
        if(distinctOwnerQuery != null && distinctOwnerQuery.size() != 0){
            offlineLayout.setVisibility(View.VISIBLE);
            OwnerAdaptor ownerAdaptor = new OwnerAdaptor(this, distinctOwnerQuery);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setAdapter(ownerAdaptor);
        }else{
            offlineLayout.setVisibility(View.GONE);
        }
    }

    private void dismissKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(usernameEditText.getWindowToken(), 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        usernameEditText.setFocusable(true);
        usernameEditText.setFocusableInTouchMode(true);
        usernameEditText.setText("");
        mSearchButton.setEnabled(true);
        if(snackbar!=null) {
            snackbar.dismiss();
        }
        offlineData();
    }

    // Validate Fields and make api call
    private void validateFields(){
        dismissKeyboard();
        if(usernameEditText.getText()==null || usernameEditText.getText().toString().equals("")){
            usernameInputLayout.setErrorEnabled(true);
            usernameInputLayout.setError(getResources().getString(R.string.required_text));
        }else if (repoType == -1){
            usernameInputLayout.setErrorEnabled(false);
            Snackbar.make(findViewById(R.id.MainActivity),getResources().getString(R.string.type_error), Snackbar.LENGTH_LONG).show();
        } else {
            usernameInputLayout.setErrorEnabled(false);
            usernameEditText.setFocusable(false);
            searchProgressBar.setVisibility(View.VISIBLE);
            if(isUser){
                fetchAllUserData(usernameEditText.getText().toString());
            }else{
                fetchAllOrgsData(usernameEditText.getText().toString());
            }
            mSearchButton.setEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }

    // Fetch user repos
    private void fetchAllUserData(String userName){
        subscription = GithubClient.getInstance()
                .getRepositoriesUserName(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Repo>>() {

                    @Override
                    public void onNext(List<Repo> repos) {
                        loadRepoData(repos);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        snackbar= Snackbar.make(findViewById(R.id.MainActivity),e.getMessage(), Snackbar.LENGTH_INDEFINITE);
                        snackbar.show();
                        searchProgressBar.setVisibility(View.GONE);
                        usernameEditText.setFocusable(true);
                        usernameEditText.setFocusableInTouchMode(true);
                        mSearchButton.setEnabled(true);
                    }

                });
    }

    // Fetch organization repos
    private void fetchAllOrgsData(String userName){
        subscription = GithubClient.getInstance()
                .getRepositoriesOrganization(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Repo>>() {

                    @Override
                    public void onNext(List<Repo> repos) {
                        loadRepoData(repos);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        snackbar= Snackbar.make(findViewById(R.id.MainActivity),e.getMessage(), Snackbar.LENGTH_INDEFINITE);
                        snackbar.show();
                        searchProgressBar.setVisibility(View.GONE);
                        mSearchButton.setEnabled(true);
                        usernameEditText.setFocusable(true);
                        usernameEditText.setFocusableInTouchMode(true);
                    }

                });
    }

    private void loadRepoData(List<Repo> repos){
        if(snackbar!=null) {
            snackbar.dismiss();
        }
        mSearchButton.setEnabled(true);
        usernameEditText.setFocusable(true);
        usernameEditText.setFocusableInTouchMode(true);

        if(repos!=null && repos.size()!= 0){
            // insert or update database
            realm.beginTransaction();
            realm.insertOrUpdate(repos);
            realm.commitTransaction();
            Intent intent= new Intent(this, RepoActivity.class);
            intent.putExtra(getResources().getString(R.string.username), usernameEditText.getText().toString());
            startActivity(intent);
        }else {
            snackbar = Snackbar.make(findViewById(R.id.MainActivity), getResources().getString(R.string.empty_repo), Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        searchProgressBar.setVisibility(View.GONE);
    }

    // OnClick of a recylerview item it will navigate to repo list activity
    @Override
    public void onListItemClick(int clickedItemIndex) {
        if(distinctOwnerQuery.get(clickedItemIndex)!= null) {
            Intent intent = new Intent(this, RepoActivity.class);
            intent.putExtra(getResources().getString(R.string.username), distinctOwnerQuery.get(clickedItemIndex).getLogin());
            startActivity(intent);
        }
    }
}
