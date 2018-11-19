package cs160_group7.relief;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class BuildingAdapter extends RecyclerView.Adapter<BuildingAdapter.ContactViewHolder> {

    private List<BuildingInfo> buildingList;

    public BuildingAdapter(List<BuildingInfo> contactList) {
        this.buildingList = contactList;
    }

    @Override
    public int getItemCount() {
        return buildingList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        final BuildingInfo ci = buildingList.get(i);
        contactViewHolder.vName.setText(ci.name);
        contactViewHolder.vDistance.setText(ci.distance);
        contactViewHolder.vRating.setText(ci.rating);
        contactViewHolder.vOpenHours.setText(ci.openHours);
        contactViewHolder.vBuildingImg.setImageBitmap(ci.image);

        contactViewHolder.vDetailsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailedView.class);
                intent.putExtra("name", ci.name);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected TextView vDistance;
        protected TextView vRating;
        protected TextView vOpenHours;
        protected ImageView vBuildingImg;
        protected Button vDetailsButton;

        public ContactViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.name);
            vDistance = (TextView) v.findViewById(R.id.distance);
            vRating = (TextView)  v.findViewById(R.id.rating);
            vOpenHours = (TextView) v.findViewById(R.id.openHours);
            vBuildingImg = (ImageView) v.findViewById(R.id.buildingImage);
            vDetailsButton = (Button) v.findViewById(R.id.detailsButton);
        }
    }
}

