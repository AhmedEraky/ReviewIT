package com.example.eraky.reviewit.admin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.eraky.reviewit.R;

import java.util.ArrayList;

/**
 * Created by Eraky on 2/12/2018.
 */

public class CommentAdabter extends RecyclerView.Adapter<CommentAdabter.CommentViewHolder> {
    private final ArrayList<Comment> comments;
    private final Context context;
    private final SetOncLickListener listener;
    int type;
    AcceptComment acceptCommentListner;
    RejectComment rejectCommentListner;

    public CommentAdabter(ArrayList<Comment> comments, Context context, SetOncLickListener listener, AcceptComment acceptComment, RejectComment rejectComment, int type) {
        this.comments = comments;
        this.context = context;
        this.listener = listener;
        acceptCommentListner = acceptComment;
        rejectCommentListner = rejectComment;
        this.type = type;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(root);
    }

    @Override
    public void onBindViewHolder(final CommentViewHolder holder, final int position) {
        holder.userComment.setText(comments.get(position).getBody());
        holder.userName.setText(comments.get(position).getCname());
        if (type == 1) {
            holder.productName.setVisibility(View.VISIBLE);
            holder.categoryName.setVisibility(View.VISIBLE);
            holder.reject.setVisibility(View.VISIBLE);
            holder.accept.setVisibility(View.VISIBLE);

            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    acceptCommentListner.Accept(position);
                }
            });
            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rejectCommentListner.Reject(position);
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public interface SetOncLickListener {
        void SetOnclick(Comment comment);
    }

    public interface RejectComment {
        void Reject(int position);
    }

    public interface AcceptComment {
        void Accept(int position);
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView userName;
        TextView userComment;
        Button accept, reject;
        TextView categoryName, productName;

        public CommentViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.comment_user_name);
            userComment = itemView.findViewById(R.id.user_comment);
            accept = itemView.findViewById(R.id.AcceptButton);
            reject = itemView.findViewById(R.id.RejectComment);
            categoryName = itemView.findViewById(R.id.CommentCatregory);
            productName = itemView.findViewById(R.id.CommentProduct);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.SetOnclick(comments.get(getAdapterPosition()));
        }
    }
}
