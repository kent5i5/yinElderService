package com.yinkin.yinelderservice.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.yinkin.yinelderservice.EmployerDetailActivity;
import com.yinkin.yinelderservice.EmployerDetailFragment;
import com.yinkin.yinelderservice.EmployerListActivity;
import com.yinkin.yinelderservice.R;
import com.yinkin.yinelderservice.model.Employer;

import java.util.List;

public class EmployerRecyclerViewAdapter extends RecyclerView.Adapter<EmployerRecyclerViewAdapter.ViewHolder> {

    private final EmployerListActivity mParentActivity;
    private final List<Employer> mValues;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        EmployerListViewModel employerListViewModel;
        @Override
        public void onClick(View view) {
            //DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
            Employer employer =  (Employer)view.getTag();
            //Employer newEmployer = new Employer("0","ken","adc street");
            //employerListViewModel = new ViewModelProvider().get(EmployerListViewModel.class);
            //mValues.add(emp);
            if (mTwoPane) {
                Bundle arguments = new Bundle();

                arguments.putString(EmployerDetailFragment.ARG_ITEM_ID, ((Employer) view.getTag()).username);
                //arguments.putString(EmployerDetailFragment.ARG_ITEM_ID, view.getTag().toString());
                EmployerDetailFragment fragment = new EmployerDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.employer_detail_container, fragment)
                        .commit();
            } else {
                Log.i("argu",employer.username);
                Context context = view.getContext();
                Intent intent = new Intent(context, EmployerDetailActivity.class);
                intent.putExtra(EmployerDetailFragment.ARG_ITEM_ID, employer.username );

                context.startActivity(intent);
            }
        }
    };

    public EmployerRecyclerViewAdapter(EmployerListActivity parent,
                                       List<Employer> employerList,
                                       boolean twoPane) {
        mValues = employerList;
        mParentActivity = parent;
        mTwoPane = twoPane;
    }

    @Override
    public EmployerRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.employer_list_content, parent, false);
        return new EmployerRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EmployerRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mIdView.setText(mValues.get(position).username);
        holder.mContentView.setText(mValues.get(position).address);

        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void add(int position, Employer item) {
        mValues.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mValues.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mIdView;
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);
            mIdView = (TextView) view.findViewById(R.id.id_text);
            mContentView = (TextView) view.findViewById(R.id.content);
        }
    }
}
