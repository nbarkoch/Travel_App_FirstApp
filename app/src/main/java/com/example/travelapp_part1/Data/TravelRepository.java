package com.example.travelapp_part1.Data;

import androidx.lifecycle.LiveData;

import com.example.travelapp_part1.entities.Travel;

public class TravelRepository {
    private static TravelDataSource travelDataSource;

    //private static MutableLiveData<Boolean> isSuccess= new MutableLiveData<>();
    private static TravelRepository instance;

    public static TravelRepository getInstance(){
        if (instance == null){
            instance = new TravelRepository();
        }
        return instance;
    }

    //static DatabaseReference TravelsRef;

    private TravelRepository(){
        travelDataSource = TravelDataSource.getInstance();
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //TravelsRef = database.getReference("Travels");
    }

    //private static void addTravelToFirebase(final Travel travel) {
    //    String key = travel.getId().toString();
    //    //TravelsRef.child(key).setValue(travel);
    //    TravelsRef.child(key).setValue(travel).addOnSuccessListener(new OnSuccessListener<Void>() {
    //        @Override
    //        public void onSuccess(Void aVoid) {
    //            isSuccess.setValue(Boolean.TRUE);
    //            Log.i("real success", "TravelRepository: ");
    //        }
    //    }).addOnFailureListener(new OnFailureListener() {
    //        @Override
    //        public void onFailure(@NonNull Exception e) {
    //            isSuccess.setValue(Boolean.FALSE);
    //            Log.i("real fail", "TravelRepository: ");
    //        }
    //    });
    //}

    public static void saveTravel(Travel trouble) {
        //addTravelToFirebase(trouble);
        travelDataSource.addTravel(trouble);
    }

    public LiveData<Boolean> getIsSuccess() {
        return travelDataSource.getIsSuccess();
        //return isSuccess;
    }

}
