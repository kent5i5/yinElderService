package com.yinkin.yinelderservice.adapter;

import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yinkin.yinelderservice.R;
import com.yinkin.yinelderservice.dummy.DummyContent.DummyItem;
import com.yinkin.yinelderservice.ui.job.jobFragment;
import com.yinkin.yinelderservice.model.Job;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyjobRecyclerViewAdapter extends RecyclerView.Adapter<MyjobRecyclerViewAdapter.ViewHolder> {

    private final List<DummyItem> mValues;
    private final jobFragment mParentFragment;
    private final List<Job> mJobs;
    private final boolean mTwoPane;

    public MyjobRecyclerViewAdapter(List<DummyItem> items,List<Job> Jobs, boolean TwoPane) {
        mParentFragment = null;
        mValues = items;
        mJobs = Jobs;
        mTwoPane = TwoPane;

    }

    public MyjobRecyclerViewAdapter(List<DummyItem> items){
        mValues = items;
        mParentFragment = null;
        mJobs = null;
        mTwoPane = false;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_job, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mJob = mJobs.get(position);
        holder.mIdView.setText(mJobs.get(position).jobid);
        holder.mDateView.setText(mJobs.get(position).created_date.toString());
        holder.mContentView.setText(mJobs.get(position).title
                + " created by " + mJobs.get(position).creator );
    }

    @Override
    public int getItemCount() {
        return mJobs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final TextView mDateView;
        public DummyItem mItem;
        public Job mJob;
        public FloatingActionButton floatingActionButton;
        public View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EditText amountTv = (EditText) getView().findViewById(R.id.editTextAmount);
                String selectedJobId = mIdView.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("amount", selectedJobId);
                Navigation.findNavController(v).navigate(R.id.jobDetailFragment, bundle);         }
        };

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mDateView = (TextView) view.findViewById(R.id.created_date_text2);
            mContentView = (TextView) view.findViewById(R.id.content);

            floatingActionButton = view.findViewById(R.id.goToJobDetailActionButton);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String selectedJobId = mIdView.getText().toString();
                    Bundle bundle = new Bundle();
                    bundle.putString("selectedJobId", selectedJobId);
                    Navigation.findNavController(v).navigate(R.id.jobDetailFragment, bundle);
                }
            });

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}