package com.amirahmed.podra;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.List;

class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListAdapterViewHolder>{


    private List<ListItem> listItems;
    private Context context;

    private String type;

    ListAdapter(List<ListItem> listItems, Context context) {
        super();
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        TinyDB tinyDB = new TinyDB(context);

        type = tinyDB.getString("That");

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ListAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ListAdapterViewHolder holder, final int position) {

        holder.name.setText(listItems.get(position).getDisName());
        holder.email.setText(listItems.get(position).getEmail());
        holder.pass.setText(listItems.get(position).getPassword());
        holder.balance.setText(listItems.get(position).getCardsNumber());
        holder.total.setText(listItems.get(position).getTotal());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (type) {
                    case "dis": {
                        Intent intent = new Intent(context, UpdateDisActivity.class);
                        intent.putExtra("companyName", listItems.get(position).getCompanyname());
                        intent.putExtra("address", listItems.get(position).getAddress());
                        intent.putExtra("disName", listItems.get(position).getDisName());
                        intent.putExtra("phoneNumber", listItems.get(position).getPhoneNumber());
                        intent.putExtra("emailAddress", listItems.get(position).getEmail());
                        intent.putExtra("website", listItems.get(position).getWebsite());
                        intent.putExtra("cardsNumber", listItems.get(position).getCardsNumber());
                        intent.putExtra("city", listItems.get(position).getCity());
                        intent.putExtra("password", listItems.get(position).getPassword());
                        intent.putExtra("ID", listItems.get(position).getID());
                        context.startActivity(intent);

                        break;
                    }
                    case "seller": {
                        Intent intent = new Intent(context, UpdateSellerActivity.class);
                        intent.putExtra("disName", listItems.get(position).getDisName());
                        intent.putExtra("emailAddress", listItems.get(position).getEmail());
                        intent.putExtra("cardsNumber", listItems.get(position).getCardsNumber());
                        intent.putExtra("password", listItems.get(position).getPassword());
                        intent.putExtra("ID", listItems.get(position).getID());
                        context.startActivity(intent);
                        break;
                    }
                    case "user": {
                        Intent intent = new Intent(context, UpdateUserActivity.class);
                        intent.putExtra("disName", listItems.get(position).getDisName());
                        intent.putExtra("phoneNumber", listItems.get(position).getPhoneNumber());
                        intent.putExtra("cardsNumber", listItems.get(position).getCardsNumber());
                        intent.putExtra("ID", listItems.get(position).getID());
                        context.startActivity(intent);

                        break;
                    }
                    default:
                        // No thing happen
                        break;
                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listItems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,listItems.size());


                switch (type) {
                    case "dis": {

                        String url = "http://podra.compu-base.com/newwebservice.asmx/delete_elment_dis?email=" + listItems.get(position).getEmail() + "&password=" + listItems.get(position).getPassword();

                        //progressDialog.show();

                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        //progressDialog.dismiss();

                                        if (response.equals("True")) {
                                            showMessage("تم الحزف بنجاح");
                                        } else {

                                            showMessage("هناك خطأ ما حاول مره اخرى");
                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                                        //progressDialog.dismiss();

                                    }
                                }
                        );


                        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);


                        break;
                    }
                    case "seller": {


                        String url = "http://podra.compu-base.com/newwebservice.asmx/delete_elment_saler?email=" + listItems.get(position).getEmail() + "&password=" + listItems.get(position).getPassword();

                        //progressDialog.show();

                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        //progressDialog.dismiss();

                                        if (response.equals("True")) {
                                            showMessage("تم الحزف بنجاح");
                                        } else {

                                            showMessage("هناك خطأ ما حاول مره اخرى");
                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                                        //progressDialog.dismiss();

                                    }
                                }
                        );


                        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);


                        break;
                    }
                    default: {

                        String url = "http://podra.compu-base.com/newwebservice.asmx/delete_elment_user?name=" + listItems.get(position).getDisName() + "&phone=" + listItems.get(position).getPhoneNumber() + "&number_card=" + listItems.get(position).getCardsNumber();

                        //progressDialog.show();

                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        //progressDialog.dismiss();

                                        if (response.equals("True")) {
                                            showMessage("تم الحزف بنجاح");
                                        } else {

                                            showMessage("هناك خطأ ما حاول مره اخرى");
                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                                        //progressDialog.dismiss();

                                    }
                                }
                        );


                        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);


                        break;
                    }
                }






            }
        });

    }

    private void showMessage(String _s) {
        Toast.makeText(context, _s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class ListAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView name,email,pass, balance,total;
        ImageView delete;
        ListAdapterViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            pass = itemView.findViewById(R.id.pass);
            balance = itemView.findViewById(R.id.ballance);
            total = itemView.findViewById(R.id.total);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
