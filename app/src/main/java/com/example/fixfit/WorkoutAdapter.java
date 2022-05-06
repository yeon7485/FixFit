package com.example.fixfit;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixfit.item.Workout;

import java.util.ArrayList;


public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder> {

    private ArrayList<Workout> mList = null;

    public WorkoutAdapter(ArrayList<Workout> mList) {
        this.mList = mList;
    }
    public interface WorkoutClickListener{
        void onItemClicked(int position);
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_workout, parent, false);
        WorkoutAdapter.ViewHolder vh = new WorkoutAdapter.ViewHolder(view);
        return vh;
    }

    // position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull WorkoutAdapter.ViewHolder holder, int position) {
        Workout item = mList.get(position);

        holder.item_workout_img.setImageResource(item.getImgResId());   // 사진 없어서 기본 파일로 이미지 띄움
        holder.item_workout_txt.setText(item.getWorkoutName());


    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public Workout getItem(int position){
        return mList != null ? mList.get(position) : null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_workout_img;
        TextView item_workout_txt;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.item_workout_img = (ImageView) itemView.findViewById(R.id.item_workout_img);
            this.item_workout_txt = (TextView) itemView.findViewById(R.id.item_workout_txt);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Log.v("position", String.valueOf(pos));
                    Intent intent = new Intent(v.getContext(), WorkoutActivity.class);
                    intent.putExtra("item", getItem(pos));
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}