package com.pranjaldesai.gitinfofetcher;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.pranjaldesai.gitinfofetcher.Adaptor.RepoAdaptor;
import com.pranjaldesai.gitinfofetcher.Data.Repo;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class RepoActivity extends AppCompatActivity implements RepoAdaptor.ItemClickListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.repo_list)
    RecyclerView mRecyclerView;

    private Realm realm;
    RealmResults<Repo> sortedQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();

        // Get extra username from intent
        String userName= getIntent().getStringExtra(getResources().getString(R.string.username));

        // Show the Up button and setting actionBar and toolbar with username
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && userName!= null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(userName);
        }

        if(userName!=null){
            toolbar.setTitle(userName);

            // Query that gets result for selected user which is sorted by repo language and star count in descending order.
            sortedQuery = realm.where(Repo.class).equalTo(getResources().getString(R.string.login_name),userName, Case.INSENSITIVE).sort(getResources().getString(R.string.language), Sort.ASCENDING,getResources().getString(R.string.star_count), Sort.DESCENDING).findAll();

            // Sets the recyclerview with the query results
            if(sortedQuery!=null) {
                RepoAdaptor repoAdaptor = new RepoAdaptor(this, sortedQuery);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                mRecyclerView.setAdapter(repoAdaptor);
            }
        }


    }

    // Back arrow in app bar implementation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // OnClick of a recylerview item it will navigate to details activity
    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent= new Intent(this, RepoDetailsActivity.class);
        Repo repo= sortedQuery.get(clickedItemIndex);
        intent.putExtra(getResources().getString(R.string.repoDetails), repo);
        startActivity(intent);
    }
}
