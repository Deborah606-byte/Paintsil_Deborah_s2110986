package com.example.paintsil_deborah_s2110986.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paintsil_deborah_s2110986.FAQItem;
import com.example.paintsil_deborah_s2110986.R;
import java.util.List;

public class FAQListAdapter extends RecyclerView.Adapter<FAQListAdapter.FAQViewHolder> {

    private List<FAQItem> faqItems;

    public FAQListAdapter(List<FAQItem> faqItems) {
        this.faqItems = faqItems;
    }

    @NonNull
    @Override
    public FAQViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_item, parent, false);
        return new FAQViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQViewHolder holder, int position) {
        FAQItem faqItem = faqItems.get(position);
        holder.questionTextView.setText(faqItem.getQuestion());
        holder.answerTextView.setText(faqItem.getAnswer());
    }

    @Override
    public int getItemCount() {
        return faqItems.size();
    }

    public static class FAQViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView;
        TextView answerTextView;

        public FAQViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.questionTextView);
            answerTextView = itemView.findViewById(R.id.answerTextView);

            questionTextView.setOnClickListener(v -> {
                if (answerTextView.getVisibility() == View.GONE) {
                    answerTextView.setVisibility(View.VISIBLE);
                } else {
                    answerTextView.setVisibility(View.GONE);
                }
            });
        }
    }
}
