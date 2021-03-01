package com.iti.gov.mashawery.home.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.gov.mashawery.R;
import com.iti.gov.mashawery.databinding.TripItemRowBinding;
import com.iti.gov.mashawery.model.Trip;

import java.time.Month;
import java.util.List;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.TripsViewHolder> {

    private Context context;
    private OnTripListener onTripListener;
    private List<Trip> tripList;


    public void setOnTripListener(OnTripListener onTripListener) {
        this.onTripListener = onTripListener;
    }

    public void setTripList(List<Trip> tripList) {
        this.tripList = tripList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TripsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new TripsViewHolder(LayoutInflater.from(context).inflate(R.layout.trip_item_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TripsViewHolder holder, int position) {

        Trip currentTrip = tripList.get(position);
        holder.binding.tvTripName.setText(currentTrip.getName());
        holder.binding.tvStartPoint.setText(currentTrip.getStartPoint());
        holder.binding.tvEndPoint.setText(currentTrip.getEndPoint());

        if(currentTrip.getDate() != null && currentTrip.getTime() != null) {

            Month month = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                month = Month.of(Integer.parseInt(currentTrip.getDate().split("[/]")[1]));
            }

            holder.binding.tvDay.setText(currentTrip.getDate().split("[/]")[0]);
            holder.binding.tvMonth.setText(month.toString().substring(0, 3));
            holder.binding.tvTime.setText(currentTrip.getTime());
        } else {

            holder.binding.tvDay.setText(R.string.empty);
            holder.binding.tvMonth.setText(R.string.empty);
            holder.binding.tvTime.setText(R.string.empty);

        }

        holder.binding.constraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTripListener.onTripClick(currentTrip);
            }
        });

        holder.binding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onTripListener.onTripStart(currentTrip);

            }
        });

        holder.binding.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTripListener.onTripDelete(currentTrip);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (tripList == null)? 0 : tripList.size();
    }

    public class TripsViewHolder extends RecyclerView.ViewHolder{

        public TripItemRowBinding binding;
        public TripsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = TripItemRowBinding.bind(itemView);
        }
    }




}
