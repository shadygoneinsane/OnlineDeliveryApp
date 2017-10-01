package hezra.wingsnsides.ChickenWings;

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

public class ChickenFlavourAdapter extends RecyclerView.Adapter<ChickenFlavourAdapter.PackageViewHolder> {
    List<ChickenFlavour> mPackageList;
    OnPackageClickListener mListener;

    public ChickenFlavourAdapter(List<ChickenFlavour> packageList, OnPackageClickListener listener) {
        mPackageList = packageList;
        mListener = listener;
    }

    @Override
    public PackageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chicken_flavour_package_card, parent, false);
        return new PackageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PackageViewHolder holder, int position) {
        ChickenFlavour packageItem = mPackageList.get(position);
        holder.name.setText(packageItem.getPNAME());
        holder.detail.setText(packageItem.getPDETAIL());
    }

    @Override
    public int getItemCount() {
        return mPackageList.size();
    }

    public class PackageViewHolder extends RecyclerView.ViewHolder {
        Context context;
        TextView name;
        TextView detail;

        public PackageViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            name = (TextView) itemView.findViewById(R.id.name);
            detail = (TextView) itemView.findViewById(R.id.detail);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*name.getText().toString();
                    detail.getText().toString();*/
                    ChickenFlavourAdapter.this.mListener.onClick(mPackageList.get(getAdapterPosition()));
                }
            });
        }
    }
}
