package hezra.wingsnsides.OrderHere;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import hezra.wingsnsides.R;

/**
 * Created by vikesh.
 */

public class FinalOrderAdapter extends RecyclerView.Adapter<FinalOrderAdapter.PackageViewHolder> {
    List<FinalOrderPackage> mPackageList;

    public FinalOrderAdapter(List<FinalOrderPackage> packageList) {
        mPackageList = packageList;
    }

    @Override
    public PackageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.final_order_package_card, parent, false);
        return new PackageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PackageViewHolder holder, final int position) {
        FinalOrderPackage packageItem = mPackageList.get(position);
        holder.name.setText(packageItem.getONAME());
        holder.amount.setText(packageItem.getOAMOUNT());

    }

    @Override
    public int getItemCount() {
        return mPackageList.size();
    }

    public class PackageViewHolder extends RecyclerView.ViewHolder {
        Context context;
        TextView name;
        TextView amount;

        public PackageViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            name = (TextView) itemView.findViewById(R.id.itemName);
            amount = (TextView) itemView.findViewById(R.id.itemPrice);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*name.getText().toString();
                    amount.getText().toString();*/
                    //FinalOrderAdapter.this.mListener.onClick(mPackageList.get(getAdapterPosition()));
                }
            });
        }
    }

}
