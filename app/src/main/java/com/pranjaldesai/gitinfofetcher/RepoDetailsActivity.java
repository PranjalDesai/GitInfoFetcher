package com.pranjaldesai.gitinfofetcher;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.pranjaldesai.gitinfofetcher.Data.Repo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepoDetailsActivity extends AppCompatActivity {

    @BindView(R.id.details_toolbar)
    Toolbar toolbar;
    @BindView(R.id.description_details)
    TextView description;
    @BindView(R.id.language_details)
    TextView language;
    @BindView(R.id.url_details)
    TextView html_url;
    @BindView(R.id.stargazers_count_details)
    TextView stargazers_count;
    @BindView(R.id.watchers_count_details)
    TextView watchers_count;
    @BindView(R.id.forks_details)
    TextView forks;
    @BindView(R.id.size_details)
    TextView size;
    @BindView(R.id.open_issues_details)
    TextView open_issues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_details);
        ButterKnife.bind(this);

        //get extra repo data from intent
        Repo repo= getIntent().getParcelableExtra(getResources().getString(R.string.repoDetails));

        // Show the Up button and setting actionBar and toolbar with repo name
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(repo.getName());
        }

        //display repo details
        if(repo!=null) {
            toolbar.setTitle(repo.getName());
            description.setText(repo.getDescription());
            language.setText(repo.getLanguage());
            html_url.setText(repo.getHtml_url());
            stargazers_count.setText(String.valueOf(repo.getStargazers_count()));
            watchers_count.setText(String.valueOf(repo.getWatchers_count()));
            forks.setText(String.valueOf(repo.getForks()));
            size.setText(String.valueOf(repo.getSize()));
            open_issues.setText(String.valueOf(repo.getOpen_issues()));
        }

    }

    // Back arrow in app bar implementation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
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
}
