package Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.acsi_project.EditProdActivity;
import com.example.acsi_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.saadahmedev.popupdialog.PopupDialog;
import com.saadahmedev.popupdialog.listener.StandardDialogActionListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import ViewHolders.ProductViewHolder;
import classes.Article;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {
    ArrayList<Article> articles ;
    Context context ;
    private int lastPosition = -1;
    private final DatabaseReference mRef ;
    public ProductAdapter(ArrayList<Article> articles, Context context) {
        this.articles = articles;
        this.context = context;
            mRef = FirebaseDatabase.getInstance("https://acsi-project-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item , parent , false);
        return new ProductViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
       Article article = articles.get(position);
        holder.prodName.setText(article.getName());
        holder.prodDesc.setText(article.getDesc());
        Picasso.get().load(article.getImg()).placeholder(context.getDrawable(R.drawable.loading)).into(holder.prodImg);
        holder.priceTxt.setText("Price : "+article.getPrice() + " DA");
        if (article.getQuantity()<1){
            holder.quantityTxt.setTextColor(context.getColor(R.color.red));
            holder.quantityTxt.setText("Out of stock ! ");
        } else {
            holder.quantityTxt.setText("Quantity : " + article.getQuantity());
        }
        if (article.isExpired()){
            holder.expTxt.setText("Expired ! ");
            holder.expTxt.setTextColor(context.getColor(R.color.red));
        } else {
            holder.expTxt.setText("Expiration date : "+ article.getExpirationDate());
        }
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {

                PopupDialog.getInstance(context)
                        .standardDialogBuilder()
                        .createIOSDialog()
                        .setHeading("Delete")
                        .setDescription("Are you sure you want to delete "+article.getName() + " ?")
                        .setPositiveButtonTextColor(R.color.red)
                        .build(new StandardDialogActionListener() {
                            @Override
                            public void onPositiveButtonClicked(Dialog dialog) {
                                mRef.child("products").child(article.getId())
                                        .removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(context, "Item deleted successfully ", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                                dialog.dismiss();
                                            }
                                        });
                            }
                            @Override
                            public void onNegativeButtonClicked(Dialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        });
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context , EditProdActivity.class);
                i.putExtra("prodID" , article.getId());
                context.startActivity(i);
            }
        });
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context , EditProdActivity.class);
                i.putExtra("prodID" , article.getId());
                context.startActivity(i);
            }
        });
        setAnimation(holder.container , position);
    }

    //calculate the size of the table
    @Override
    public int getItemCount() {
        return articles.size();
    }


    // setting animation
    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.recycler_anim);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    // resting animation
    public void resetAnimation() {
        lastPosition = -1;
    }


    // use it for the searching
    public void setData(ArrayList<Article> filtredArray) {
        this.articles = filtredArray ;
    }
}
