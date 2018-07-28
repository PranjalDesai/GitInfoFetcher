package com.pranjaldesai.gitinfofetcher.Adaptor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pranjaldesai.gitinfofetcher.Data.Repo;
import com.pranjaldesai.gitinfofetcher.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/*
    This adaptor is used to populate and display repositories in recyclerview. It shows all the
    repositories sorted by language and descending star count.
 */

public class RepoAdaptor extends RecyclerView.Adapter<RepoAdaptor.RepoViewHolder> {

    final private ItemClickListener mOnClickListener;
    private RealmResults<Repo> results;

    public RepoAdaptor(ItemClickListener mOnClickListener, RealmResults<Repo> results) {
        this.mOnClickListener = mOnClickListener;
        this.results= results;
    }

    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutID = R.layout.repo_list_item;
        LayoutInflater inflater= LayoutInflater.from(context);

        View view= inflater.inflate(layoutID, viewGroup, false);
        return new RepoViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RepoViewHolder repoViewHolder, int i) {
        repoViewHolder.bind(results.get(i));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public interface ItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    class RepoViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.repoLanguage)
        TextView language;
        @BindView(R.id.repoName)
        TextView name;
        @BindView(R.id.repoStars)
        TextView stars;

        public RepoViewHolder(View itemView, Context context){
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position= getAdapterPosition();
            mOnClickListener.onListItemClick(position);
        }

        public void bind(Repo repo) {
            language.setText(repo.getLanguage());
            name.setText(repo.getName());
            stars.setText(String.valueOf(repo.getStargazers_count()));
        }
    }
}
