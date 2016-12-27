package com.feicuiedu.gitdroid.hotRepo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.feicuiedu.gitdroid.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gqq on 2016/12/9.
 */
public class RepoListAdapter extends BaseAdapter {

    private List<Repo> data;

    public void addAll(Collection<Repo> repos) {
        data.addAll(repos);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public RepoListAdapter() {
        data = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_repo, parent, false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        Repo repo = data.get(position);
        viewHolder.mTvRepoInfo.setText(repo.getDescription());
        viewHolder.mTvRepoName.setText(repo.getFullName());
        viewHolder.mTvRepoStars.setText("start："+repo.getStarCount()+"");
        Picasso.with(parent.getContext()).load(repo.getOwner().getAvatar()).into(viewHolder.mIvIcon);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.ivIcon)
        ImageView mIvIcon;
        @BindView(R.id.tvRepoName)
        TextView mTvRepoName;
        @BindView(R.id.tvRepoInfo)
        TextView mTvRepoInfo;
        @BindView(R.id.tvRepoStars)
        TextView mTvRepoStars;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
