package ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.acsi_project.R;

import org.w3c.dom.Text;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    public TextView prodName , prodDesc ,   quantityTxt , priceTxt  , expTxt;
    public CardView editBtn , deleteBtn  ;
    public ConstraintLayout container ;

    public ImageView prodImg  ;
    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        prodName = itemView.findViewById(R.id.prodName);
        prodDesc = itemView.findViewById(R.id.prodDesc);
        quantityTxt = itemView.findViewById(R.id.quantityTxt);
        priceTxt = itemView.findViewById(R.id.priceTxt);
        editBtn = itemView.findViewById(R.id.editBtn);
        deleteBtn = itemView.findViewById(R.id.deleteBtn);
        container = itemView.findViewById(R.id.container);
        expTxt = itemView.findViewById(R.id.expTxt);
        prodImg = itemView.findViewById(R.id.prodImg);
    }
}
