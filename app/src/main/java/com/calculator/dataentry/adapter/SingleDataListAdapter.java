package com.calculator.dataentry.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.calculator.dataentry.R;
import com.calculator.dataentry.model.SingleUserModel;
import com.calculator.dataentry.model.UserModel;

import java.util.List;


public class SingleDataListAdapter extends RecyclerView.Adapter<SingleDataListAdapter.ViewHolder> {
    private List<SingleUserModel.userDetails> listdata;
    private Context context;
    // RecyclerView recyclerView;

    public SingleDataListAdapter(Context context, List<SingleUserModel.userDetails> listdata) {
        this.context = context;
        this.listdata = listdata;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.single_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SingleUserModel.userDetails myListData = listdata.get(position);
        holder.txtDate.setText(myListData.finaldate.replaceAll("T00:00:00.000Z",""));
        holder.txtParticular.setText(myListData.particulars);
        holder.txtType.setText(myListData.type);
        holder.txtInvoice.setText(myListData.invoiceno);
        holder.txtDebit.setText(myListData.debit_amount);
        holder.txtCredit.setText(myListData.credit_amount);
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDate,txtParticular,txtType,txtInvoice,txtDebit,txtCredit;
        public LinearLayout linearId;
        public ViewHolder(View itemView) {
            super(itemView);
            this.txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            this.txtParticular = (TextView) itemView.findViewById(R.id.txtParticular);
            this.txtType = (TextView) itemView.findViewById(R.id.txtType);
            this.txtInvoice = (TextView) itemView.findViewById(R.id.txtInvoice);
            this.txtDebit = (TextView) itemView.findViewById(R.id.txtDebit);
            this.txtCredit = (TextView) itemView.findViewById(R.id.txtCredit);
            this.linearId = (LinearLayout)itemView.findViewById(R.id.linearId);
        }
    }
}
