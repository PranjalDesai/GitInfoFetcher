package com.pranjaldesai.gitinfofetcher.Adaptor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pranjaldesai.gitinfofetcher.Data.Owner;
import com.pranjaldesai.gitinfofetcher.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.RealmResults;

/*
    This adaptor is used to populate and display offline data in recyclerview. It shows all the
    distinct user data in the database.
 */

public class OwnerAdaptor extends RecyclerView.Adapter<OwnerAdaptor.OwnerViewHolder> {

    final private OwnerAdaptor.OwnerItemClickListener mOnClickListener;
    private RealmResults<Owner> owners;

    public OwnerAdaptor(OwnerItemClickListener mOnClickListener, RealmResults<Owner> owners) {
        this.mOnClickListener = mOnClickListener;
        this.owners = owners;
    }

    @NonNull
    @Override
    public OwnerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutID = R.layout.owner_list_item;
        LayoutInflater inflater= LayoutInflater.from(context);

        View view= inflater.inflate(layoutID, viewGroup, false);
        return new OwnerViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull OwnerViewHolder ownerViewHolder, int i) {
        ownerViewHolder.bind(owners.get(i));
    }

    @Override
    public int getItemCount() {
        return owners.size();
    }

    public interface OwnerItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    class OwnerViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.ownerName)
        TextView ownerName;
        @BindView(R.id.ownerAvatar)
        CircleImageView ownerAvatar;
        Context mContext;

        public OwnerViewHolder(View itemView, Context context){
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            mContext=context;
        }

        @Override
        public void onClick(View view) {
            int position= getAdapterPosition();
            mOnClickListener.onListItemClick(position);
        }

        public void bind(Owner owner) {
            ownerName.setText(owner.getLogin());
            try {
                Glide.with(mContext)
                        .load(owner.getAvatar_url())
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.account_circle)
                                .fitCenter())
                        .into(ownerAvatar);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
