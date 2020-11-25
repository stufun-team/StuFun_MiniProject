package com.example.stufun.PageAdapter;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stufun.Model.UserChatModel;
import com.example.stufun.Prevalent.Prevalent;
import com.example.stufun.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    public static  final int LEFT = 0;
    public static  final int RIGHT = 1;
    private final Context context;
    private final List<UserChatModel> userChatModels;


    public MessageAdapter(Context mContext,List<UserChatModel> chatModels)
    {
        this.userChatModels = chatModels;
        this.context = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType== RIGHT)
        {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_txt_right_viewholder,parent,false);
            return new ViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_txt_left_viewholder,parent,false);
            return new ViewHolder(view);
        }
    }

    private void downloadFile(Context context, String url) {


        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, "/", "report" + ".jpg");

        assert downloadmanager != null;
        downloadmanager.enqueue(request);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final UserChatModel chat = userChatModels.get(position);

        if(chat.getType().equals("txt")){

            holder.txtLayout.setVisibility(View.VISIBLE);
            holder.textView.setText(chat.getMessage());
            holder.timetxt.setText(chat.getDate());
        }
        else if(chat.getType().equals("img"))
        {
            holder.imageLayout.setVisibility(View.VISIBLE);
            holder.timeimg.setText(chat.getDate());
            Picasso.get().load(chat.getImage()).into(holder.imageView);

            holder.download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downloadFile(context,chat.getImage());
                }
            });

        }

        if(position == userChatModels.size()-1)
        {
            if(chat.isIsseen())
                holder.seentxt.setText("Seen");
            else
                holder.seentxt.setText("Delivered");
        }
        else {
            holder.seentxt.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return userChatModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(Prevalent.currentuser.getUid().equals(userChatModels.get(position).getSender()))
            return RIGHT;
        else return LEFT;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public TextView seentxt;
        public TextView timetxt,timeimg;
        public RelativeLayout txtLayout,imageLayout;
        public ImageView imageView;
        public ImageView download;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.chat_txt);
            seentxt = itemView.findViewById(R.id.seen_txt);
            timetxt = itemView.findViewById(R.id.chat_time);
            timeimg = itemView.findViewById(R.id.chat_image_time);
            txtLayout = itemView.findViewById(R.id.chat_txt_layout);
            imageLayout = itemView.findViewById(R.id.chat_image_layout);
            imageView = itemView.findViewById(R.id.chat_image);
            download = itemView.findViewById(R.id.doenload_img);

        }
    }
}
