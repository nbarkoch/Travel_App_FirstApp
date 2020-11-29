package com.example.travelapp_part1.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.firebase.database.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Entity
public class Travel {
    @NonNull
    @PrimaryKey
    private String travelId = "id";
    private String clientName; // 1 0102 // not null
	private String clientPhone; // regular exp // not null
    private String clientEmail; // regular exp // not null
	private Integer numPassengers = 0; // >0 // not null
	
	@TypeConverters(UserLocationConverter.class)
	private UserLocation travelLocation; // not null

    private List<UserLocation> destLocations;
	
	@TypeConverters(RequestType.class)
    private RequestType requestType; // not a field

    @TypeConverters(DateConverter.class)
    private Date travelDate; // date >= today // not null
	
    @TypeConverters(DateConverter.class)
    private Date arrivalDate; // not null // date1 < date2
	
		
	private HashMap<String, Boolean> company; // not a field
    
    // with these methods the fire base can place all the members in our class
	public String getId(){
	    return this.travelId;
    }
    public String getClientName() {return this.clientName; }
    public String getClientPhone() {return this.clientPhone; }
    public String getClientEmail() {return this.clientEmail; }
    public Integer getNumPassengers() { return this.numPassengers; }
    public String getTravelLocation() {return new UserLocationConverter().asString(this.travelLocation);}
    public List<UserLocation> getDestLocations() { return destLocations; }
    public Integer getRequestType() { return RequestType.getTypeInt(this.requestType);}
    public String getTravelDate() { return new DateConverter().dateToTimestamp(this.travelDate);}
    public String getArrivalDate() { return new DateConverter().dateToTimestamp(this.arrivalDate);}
    public String getCompany() { return new CompanyConverter().asString(this.company);}


    public Travel() {
    }


    public Travel( String clientName, String clientPhone, String clientEmail, Integer numPassengers,
                  UserLocation travelLocation, List<UserLocation> destLocations, Date travelDate, Date arrivalDate) {
        this.clientName = clientName;
        this.clientPhone =  clientPhone;
        this.clientEmail = clientEmail;
        this.numPassengers = numPassengers;
        this.travelLocation = travelLocation;
        this.destLocations = destLocations;
        this.requestType = RequestType.sent;
        this.travelDate = travelDate;
        this.arrivalDate = arrivalDate;
        this.company = new HashMap<String, Boolean>();
    }

    public void setTravelId(String id) {
	    if (id != null)
	        this.travelId = id;
    }


    public void setClientName(String clientName)  {
        //	    if (clientName == null)
//	        throw new IllegalAccessException("missing field: name");

        this.clientName = clientName;
    }

    public void setClientPhone(String clientPhone)  {


//        if (clientPhone == null)
//            throw new IllegalAccessException("missing field: phone number");
//	    if (!phone_validation(clientPhone))
//	        throw new IllegalAccessException("phone number is illegal");
        this.clientPhone = clientPhone;
    }



    public void setClientEmail(String clientEmail) {
//        if (clientEmail == null)
//            throw new IllegalAccessException("missing field: email address");
//        if (!email_validation(clientEmail))
//            throw new IllegalAccessException("email address is illegal");
        this.clientEmail = clientEmail;
    }

    public void setNumPassengers(Integer numPassengers) {
//        if (numPassengers == null)
//            throw new IllegalAccessException("missing field: number passengers");
//        if (numPassengers <= 0)
//            throw new IllegalAccessException("the number of passengers must be at least 1");
        this.numPassengers = numPassengers;
    }

    public void setTravelLocation(UserLocation travelLocation)  {
//        if (travelLocation == null)
//            throw new IllegalAccessException("missing field: travel source location");
        this.travelLocation = travelLocation;
    }

    public void setDestLocations(List<UserLocation> destLocations) {
//        if (destLocations == null)
//            throw new IllegalAccessException("missing field: travel destination locations");
//        if (destLocations.size() == 0)
//            throw new IllegalAccessException("the number of destination locations must be at least 1");
        this.destLocations = destLocations;
    }

    public void setTravelDate(Date travelDate)  {
//        if (travelDate == null)
//            throw new IllegalAccessException("missing field: travel begin date");
        this.travelDate = travelDate;
    }

    public void setArrivalDate(Date arrivalDate) {
//        if (arrivalDate == null)
//            throw new IllegalAccessException("missing field: travel end date");
//        if (arrivalDate.before(travelDate))
//            throw new IllegalAccessException("the travel end date must be after the travel begin date");
        this.arrivalDate = arrivalDate;
    }

    public static class DateConverter {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        @TypeConverter
        public Date fromTimestamp(String date) throws ParseException {
            return (date == null ? null : format.parse(date));
        }

        @TypeConverter
        public String dateToTimestamp(Date date) {
            return date == null ? null : format.format(date);
        }
    }
	
	
	
	public enum RequestType {
        sent(0), accepted(1), run(2), close(3);
        private final Integer code;
        RequestType(Integer value) {
            this.code = value;
        }
        public Integer getCode() {
            return code;
        }
        @TypeConverter
        public static RequestType getType(Integer numeral) {
            for (RequestType ds : values())
                if (ds.code.equals(numeral))
                    return ds;
            return null;
        }
        @TypeConverter
        public static Integer getTypeInt(RequestType requestType) {
            if (requestType != null)
                return requestType.code;
            return null;
        }
    }
	
	
	
	
	 public static class CompanyConverter {
        @TypeConverter
        public HashMap<String, Boolean> fromString(String value) {
            if (value == null || value.isEmpty())
                return null;
            String[] mapString = value.split(","); //split map into array of (string,boolean) strings
            HashMap<String, Boolean> hashMap = new HashMap<>();
            for (String s1 : mapString) //for all (string,boolean) in the map string
            {
                if (!s1.isEmpty()) {//is empty maybe will needed because the last char in the string is ","
                    String[] s2 = s1.split(":"); //split (string,boolean) to company string and boolean string.
                    Boolean aBoolean = Boolean.parseBoolean(s2[1]);
                    hashMap.put(/*company string:*/s2[0], aBoolean);
                }
            }
            return hashMap;
        }

        @TypeConverter
        public String asString(HashMap<String, Boolean> map) {
            if (map == null)
                return null;
            StringBuilder mapString = new StringBuilder();
            for (Map.Entry<String, Boolean> entry : map.entrySet())
                mapString.append(entry.getKey()).append(":").append(entry.getValue()).append(",");
            return mapString.toString();
        }
    }

    public static class UserLocationConverter {
        @TypeConverter
        public UserLocation fromString(String value) {
            if (value == null || value.equals(""))
                return null;
            double lat = Double.parseDouble(value.split(" ")[0]);
            double lang = Double.parseDouble(value.split(" ")[1]);
            return new UserLocation(lat, lang);
        }

        @TypeConverter
        public String asString(UserLocation warehouseUserLocation) {
            return warehouseUserLocation == null ? "" : warehouseUserLocation.getLat() + " " + warehouseUserLocation.getLon();
        }
    }
}
	
	