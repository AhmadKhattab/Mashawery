package com.iti.gov.mashawery.history.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.gov.mashawery.R;
import com.iti.gov.mashawery.databinding.HistoryItemBinding;
import com.iti.gov.mashawery.home.view.OnTripListener;
import com.iti.gov.mashawery.model.Trip;

import java.time.Month;
import java.util.List;

public class TripsHistoryAdapter extends RecyclerView.Adapter<TripsHistoryAdapter.TripsHistoryViewHolder> {

    private Context context;
    private OnHistoryListener onHistoryListener;
    private List<Trip> historyList;


    public void setOnHistoryListener(OnHistoryListener onHistoryListener) {
        this.onHistoryListener= onHistoryListener;
    }

    public void setHistoryList(List<Trip> tripList) {
        this.historyList = tripList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TripsHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new TripsHistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.history_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TripsHistoryViewHolder holder, int position) {

        Trip currentTrip = historyList.get(position);

        Month month = Month.of(Integer.parseInt(currentTrip.getDate().split("[/]")[1]));

        holder.binding.tvTripName.setText(currentTrip.getName());
        holder.binding.tvStartPoint.setText(currentTrip.getStartPoint());
        holder.binding.tvEndPoint.setText(currentTrip.getEndPoint());


        holder.binding.tvDay.setText(currentTrip.getDate().split("[/]")[0]);
        holder.binding.tvMonth.setText(month.toString().substring(0, 3));
        holder.binding.tvTime.setText(currentTrip.getTime());

        if(currentTrip.getStatus() == 2) {
            holder.binding.status.setBackgroundColor(context.getResources().getColor(R.color.orange));
        }

        holder.binding.constraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHistoryListener.onHistoryClick(currentTrip);
            }
        });


        holder.binding.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHistoryListener.onHistoryDelete(currentTrip);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (historyList == null) ? 0 : historyList.size();
    }



    public class TripsHistoryViewHolder extends RecyclerView.ViewHolder {

        public HistoryItemBinding binding;

        public TripsHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = HistoryItemBinding.bind(itemView);
        }
    }
}
