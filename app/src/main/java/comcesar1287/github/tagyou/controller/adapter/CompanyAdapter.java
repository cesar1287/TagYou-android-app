package comcesar1287.github.tagyou.controller.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import comcesar1287.github.tagyou.R;
import comcesar1287.github.tagyou.controller.domain.Company;
import comcesar1287.github.tagyou.controller.interfaces.RecyclerViewOnClickListenerHack;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.MyViewHolder>{

    private List<Company> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private Context c;

    public CompanyAdapter(Context c, List<Company> l){
        this.c = c;
        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public CompanyAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = mLayoutInflater.inflate(R.layout.item_company, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {

        Glide.with(c)
                .load(mList.get((position)).getLogo())
                .centerCrop()
                .into(myViewHolder.bannerCompany);
        myViewHolder.nameCompany.setText(mList.get(position).getName());
        myViewHolder.quantity.setText(String.valueOf(mList.get(position).getQuantity()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        mRecyclerViewOnClickListenerHack = r;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener /*View.OnCreateContextMenuListener*/{
        ImageView bannerCompany;
        TextView nameCompany;
        TextView quantity;

        MyViewHolder(View itemView) {
            super(itemView);
            bannerCompany = (ImageView) itemView.findViewById(R.id.company_banner);
            nameCompany = (TextView) itemView.findViewById(R.id.company_name);
            quantity = (TextView) itemView.findViewById(R.id.number_person);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mRecyclerViewOnClickListenerHack != null){
                mRecyclerViewOnClickListenerHack.onClickListener(v, getAdapterPosition());
            }
        }
    }
}